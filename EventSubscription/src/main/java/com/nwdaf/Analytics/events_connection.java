package com.nwdaf.Analytics;

import java.util.UUID;

public class events_connection {
	
	//LOAD_LEVEL_INFORMATION
	//private int event_id;
	private String snssais;
	private boolean anySlice;
	private String subscriptionID;

	//
	
	private int id;
	//


	public events_connection() {
	}

	public events_connection(String snssais, boolean anySlice, String subscriptionID) {
		super();
		this.snssais = snssais;
		this.anySlice = anySlice;
		this.subscriptionID = subscriptionID;
	}



	public events_connection(String snssais, boolean anySlice, UUID subscriptionID) {
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	


	
	/*public int getEvent_id() {
		return event_id;
	}
	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}*/
	
	public String getSnssais() {
		return snssais;
	}
	public void setSnssais(String snssais) {
		this.snssais = snssais;
	}
	

	public boolean isAnySlice() {
		return anySlice;
	}

	public void setAnySlice(boolean anySlice) {
		this.anySlice = anySlice;
	}

	/*@Override
	public String toString() {
		return "events_connection [snssais=" + snssais + ", anySlice=" + anySlice + ", id="
				+ id + "]";
	}*/

	@Override
	public String toString() {
		return "events_connection{" +
				"snssais='" + snssais + '\'' +
				", anySlice=" + anySlice +
				", subscriptionID='" + subscriptionID + '\'' +
				'}';
	}

	public String getSubscriptionID() {
		return subscriptionID;
	}

	public void setSubscriptionID(String subscriptionID) {
		this.subscriptionID = subscriptionID;
	}


	// END OF LOAD_LEVEL_INFORMATION

	
	
	

}