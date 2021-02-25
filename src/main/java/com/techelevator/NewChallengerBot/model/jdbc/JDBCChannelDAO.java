package com.techelevator.NewChallengerBot.model.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.techelevator.NewChallengerBot.model.ChannelDAO;

public class JDBCChannelDAO implements ChannelDAO{

		Connection conn;
		
		/* Create a Statement object so that we can execute a SQL Query */

		
		public JDBCChannelDAO(DataSource dataSource) throws SQLException{
			conn = dataSource.getConnection();
			
		}
		@Override
		public long create(long id) {
			try {
				String sqlCreateValid = "INSERT INTO validChannels (id) VALUES (?) RETURNING id";
				PreparedStatement validChannel = conn.prepareStatement(sqlCreateValid);
				validChannel.setLong(1,id);
				ResultSet idRow = validChannel.executeQuery();
				id = -1;
				if(idRow.next()) {
					id =idRow.getLong("id");
				}
				return id;
			}catch(SQLException ex) {
				System.err.println(ex.getMessage());
				
				return -1;
				
			}
		}
		@Override
		public boolean isValid(long id) {
			try {
				String sqlcheckValid = "SELECT * FROM validChannels WHERE id = ?";
				PreparedStatement validChannel = conn.prepareStatement(sqlcheckValid);
				validChannel.setLong(1,id);
				ResultSet idRow = validChannel.executeQuery();
				id = -1;
				if(idRow.next()) {
					return true;
				}
				return false;
			}catch(SQLException ex) {
				System.err.println(ex.getMessage());
				return false;
				
			}
		}
		@Override
		public void delete(long id) {
			try {
				String sqlcheckValid = "DELETE FROM validChannels WHERE id = ?";
				PreparedStatement validChannel = conn.prepareStatement(sqlcheckValid);
				validChannel.setLong(1,id);
				validChannel.execute();
			}catch(SQLException ex) {
				System.err.println(ex.getMessage());	
			}
		}
}
