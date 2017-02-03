package com.floorcorn.tickettoride.communication;

import java.util.ArrayList;

public class Results {

	private boolean success;

	private ArrayList<Object> results;

	public Results(boolean success, ArrayList<Object> results) {
		this.success = success;
		this.results = new ArrayList<Object>(results);
	}

	public Results(boolean success, Object result) {
		this.success = success;
		results = new ArrayList<Object>();
		results.add(result);
	}

	public boolean isSuccess() {
		return success;
	}

	public ArrayList<Object> getResults(){
		return results;
	}
}
