package com.floorcorn.tickettoride.communication;

import java.util.HashMap;

public class Results {

	public enum ResultClasses {

	}
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

	public void deserializeMap() {
		//for(Type t : results.keySet()) {
			//TODO put serializer in shared and deserialze each object.
		//}
	}
}
