package com.knaps.dev.Models;

import java.util.Date;

import com.knaps.dev.Contracts.ObservationSubject;
import com.knaps.dev.Contracts.Route;

public class CalculatedRoute extends Route {
	private Date startTime;
	private Date endTime;
	
	public CalculatedRoute(ObservationSubject o) {
		super(o);
		// TODO Auto-generated constructor stub
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public Date getDuration(){
		//TODO Calculate duration of a route!.
		return new Date();
	}
	
	public void sendToCache(){
		//TODO save calculated route to cache (for search results)
	}
	
	public void addToCalendar(){
		//TOD add this calculated route to user's calendar
	}
	
}
