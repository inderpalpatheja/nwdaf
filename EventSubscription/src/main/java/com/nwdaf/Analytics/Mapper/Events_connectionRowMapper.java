package com.nwdaf.Analytics.Mapper;

import com.nwdaf.Analytics.events_connection;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Events_connectionRowMapper implements RowMapper  {
    
	//LOAD_LEVEL_INFORMATION
	@Override
	public Object mapRow(ResultSet resultSet, int i) throws SQLException{
		
		  events_connection c = new events_connection();
	       return c;
	}
	
	

}