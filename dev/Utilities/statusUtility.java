package com.knaps.dev.Utilities;

import java.util.Date;

import com.knaps.dev.Contracts.Route;
import com.knaps.dev.Models.Line;
import com.knaps.dev.Models.LineSection;
import com.knaps.dev.Models.Station;

public class statusUtility {
	// TODO IS OPEN LOGIC 
	public static boolean isOpen(Line line) {
		return true;
	}
	
	public static boolean isOpen(LineSection lineSection){
		return true;
	}
	
	public static boolean isOpen(Route route){
		return true;
	}

	public static boolean isOpen(Station station) {
		return true;
	}

	public static boolean isOpen(Line line, Date time) {
		return true;
	}
	
	public static boolean isOpen(LineSection lineSection, Date time){
		return true;
	}
	
	public static boolean isOpen(Route route, Date time){
		return true;
	}

	public static boolean isOpen(Station station, Date time) {
		return true;
	}
}
