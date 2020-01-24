package com.nwdaf.Analytics;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class analyticsRepository implements RowMapper {
	
	@Autowired
    JdbcTemplate jdbcTemplate;

    public List<analytics> getUser()
    {
    	//return jdbcTemplate.query("SELECT snssais, load_level_info, eventId from t1;", new UserRowMapper());
    	return jdbcTemplate.query("SELECT snssais, load_level_info, eventId from t2",new analyticsRowMapper());
    }


    public analytics findById(Integer id)
    {
        String query = "SELECT * FROM t2 WHERE eventId = ?";

        try
        { return (analytics) this.jdbcTemplate.queryForObject(query, new Object[] { id }, new analyticsRowMapper()); }

        catch(EmptyResultDataAccessException ex)
        { return null; }
    }

    
  
    
    
    
    
    

	@Override
	public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		
	    analytics analytics = new analytics();

        analytics.setEventId(resultSet.getInt("eventId"));
        analytics.setLoad_level_info(resultSet.getInt("load_level_info"));
        analytics.setSnssais(resultSet.getString("snssais"));
       
        return analytics;
	}

	public void saveUser(analytics analytics) {
		// TODO Auto-generated method stub
		
	}

 

    
  

}
	
	


