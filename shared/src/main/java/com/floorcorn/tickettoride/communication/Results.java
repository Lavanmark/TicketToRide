package com.floorcorn.tickettoride.communication;

public class Results {
	private boolean success;

	private Object result;

	public Results(boolean success, Object result) {
		this.success = success;
		this.result = result;
	}

	public boolean isSuccess() {
		return success;
	}

	public Object getResult(){
		return result;
	}
}
