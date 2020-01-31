package com.nwdaf.Analytics.model;

public class analytics {
	
	private int eventId;
	private int load_level_info;
	private String snssais;
	
	
	
	
	
	public analytics() {
		
	}
     
	
	

	public analytics(int eventId, int load_level_info, String snssais) {
		super();
		this.eventId = eventId;
		this.load_level_info = load_level_info;
		this.snssais = snssais;
	}




	public int getEventId() {
		return eventId;
	}




	public void setEventId(int eventId) {
		this.eventId = eventId;
	}




	public int getLoad_level_info() {
		return load_level_info;
	}




	public void setLoad_level_info(int load_level_info) {
		this.load_level_info = load_level_info;
	}




	public String getSnssais() {
		return snssais;
	}




	public void setSnssais(String snssais) {
		this.snssais = snssais;
	}






}
