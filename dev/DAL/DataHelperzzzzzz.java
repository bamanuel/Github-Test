package com.knaps.dev.DAL;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.knaps.dev.Contracts.ObservationSubject;
import com.knaps.dev.Contracts.Route;
import com.knaps.dev.Enums.*;
import com.knaps.dev.Models.Alert;
import com.knaps.dev.Models.Line;
import com.knaps.dev.Models.PlannedRoute;
import com.knaps.dev.Models.Station;
import com.knaps.dev.Utilities.AppPrefs;
import com.knaps.dev.Utilities.Constants;

public class DataHelper extends Activity{
	private final String TAG = "myDB";
	private SQLiteDatabase db;
	private final DataAccess dbhelper;
	public DataHelper(Context c){
		dbhelper = new DataAccess(c);
		//dbhelper.openDataBase();
	}
	public void close(){
		db.close();
	}
	public void open() throws SQLiteException
	{
		try{
			db = dbhelper.getWritableDatabase();
			int j = 0;
		}catch (SQLiteException e){
			Log.v("Open database exception caught", e.getMessage());
			db = dbhelper.getReadableDatabase();
		}
	}
	
	public ArrayList<Line> getAllLines(){
		Cursor c = db.rawQuery("SELECT " +
								Constants.DB_LINE_FIELDS +
								"FROM line l", null);
					
		return getLineObjectArray(c);
	}
	
	public ArrayList<Line> getLinesByStation(int stationId) {
		Cursor c = db.rawQuery("SELECT " +
								Constants.DB_LINE_FIELDS +
								"FROM line l"+ 
								"INNER JOIN stationline sl"+
								"INNER JOIN company c ON c._id = l.companyID" +
								"WHERE sl.stationId = ? AND sl.lineID = l._id", new String[] {Integer.toString(stationId)});
					
		return getLineObjectArray(c);
	}
	
	public Line getLine(int lineId){
		Cursor c = db.rawQuery("SELECT " +
								Constants.DB_LINE_FIELDS +
								"FROM line l"+ 
								"INNER JOIN company c ON c._id = l.companyID" +
								"WHERE l._id = ?", new String[] {Integer.toString(lineId)});
					
		
		return getLineObjectArray(c).get(0);
	}
	
	public ArrayList<Alert> getAllCurrentAlerts(){
		
		Cursor c = db.rawQuery("select" + 
							    Constants.DB_ALERT_FIELDS +
								"from alert a", null);
		
		return getAlertObjectArray(c);
	}
	
	public ArrayList<Alert> getAlertsForStation(int stationId){
		
		Cursor c = db.rawQuery("select" + 
							    Constants.DB_ALERT_FIELDS +
								"from alert a " +
								"inner join alertstationline al on a._id = al.alertid" +
								"inner join stationline sl on al.stationlineid = sl._Id" +
								"where sl.stationId =  ?", new String[] {Integer.toString(stationId)});
		
		return getAlertObjectArray(c);
		
	}
	public ArrayList<Alert> getAlertsForLine(int lineId){
		
		Cursor c = db.rawQuery("select" + 
								Constants.DB_ALERT_FIELDS +
								"from alert a " +
								"inner join alertstationline al on a._id = al.alertid" +
								"inner join stationline sl on al.stationlineid = sl._Id" +
								"where sl.lineId =  ?", new String[] {Integer.toString(lineId)});
		
		return getAlertObjectArray(c);
	}
	public ArrayList<Station> getAllStations(){
		
		Cursor c = db.rawQuery("SELECT " +
								Constants.DB_STATION_FIELDS +
								"FROM station s", null);
		
		return getStationObjectArray(c);
	}
	public ArrayList<Station> getStationsByLine(int lineId) {
		
		Cursor c = db.rawQuery("SELECT " +
								Constants.DB_STATION_FIELDS +
								"FROM station s" +
								"INNER JOIN stationline sl ON sl.stationid = s._id", 
								new String[] { Integer.toString(lineId)});
		
		return getStationObjectArray(c);
	}
	public Station getStation(int stationId){
		
		Cursor c = db.rawQuery("SELECT " +
								Constants.DB_STATION_FIELDS +
								"FROM station s" +
								"where s._id = ?", new String[] { Integer.toString(stationId)});
		
		return getStationObjectArray(c).get(0);
	}
	
	public static ArrayList<PlannedRoute> getAllSavedRoutes(){
		// TODO get all saved routes
		return null;
	}
	public static PlannedRoute getSavedRoute(String name){
		// TODO get route by name
		return null;
	}
	
	public static void saveRoute(Route route){
		// TODO Save route to Preferences??
	}
	
	public boolean updateDatabase(){
		// TODO Update the Database from ONLINE (SYNC!)
		return true;
	}

	private ArrayList<Line> getLineObjectArray(Cursor c) {
		// Retrieve the appPrefs 
		startManagingCursor(c);
		ArrayList<Line> lines = new ArrayList<Line>();
		if (c.moveToFirst()){
			do{
				Line temp = new Line(c.getInt(0), 
									c.getString(1), 
									c.getString(2), 
									c.getInt(3), 
									c.getString(4), 
									c.getString(5), 
									c.getString(6), 
									c.getString(7), 
									c.getFloat(8),
									/*(ObservationSubject) new AppPrefs(this).getSerializable(Constants.ALERT_SUBJECT)*/null);
				lines.add(temp);
			}while(c.moveToNext());
		}
		return lines;
	}
	private ArrayList<Alert> getAlertObjectArray(Cursor c) {
		startManagingCursor(c);
		ArrayList<Alert> alerts = new ArrayList<Alert>();
		
		if(c.moveToFirst()){
			do {
				DateFormat df = new SimpleDateFormat("YYYY-MM-DDThh:mm:ss.sTZD");
				Alert a = null;
				try {
					a = new Alert(c.getString(0), df.parse(c.getString(1)), LineStatus.fromString(c.getInt(2)));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					Log.e(TAG, String.format("failed to parse string to date {0}", c.getString(1)));			
					e.printStackTrace();
				};
				alerts.add(a);
			}while (c.moveToNext());
		}
		return alerts;
	}
	private ArrayList<Station> getStationObjectArray(Cursor c) {
		// TODO Auto-generated method stub
		startManagingCursor(c);
		ArrayList<Station> stations = new ArrayList<Station>();
		
		if(c.moveToFirst()){
			do {
				Station s = new Station(c.getInt(0), 
										c.getString(1),
										c.getString(2), 
										intToBool(c.getInt(3)), 
										intToBool(c.getInt(4)), 
									    intToBool(c.getInt(5)), 
									    intToBool(c.getInt(6)), 
									    intToBool(c.getInt(7)), 
									    (ObservationSubject) new AppPrefs(this).getSerializable(Constants.ALERT_SUBJECT));
				stations.add(s);
			}while (c.moveToNext());
		}
		
		return stations;
	}
	private boolean intToBool(int i){
		boolean b = (i != 0);
		return b;
	}
}