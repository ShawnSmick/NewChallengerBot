package com.techelevator.NewChallengerBot.model.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.sql.DataSource;

import com.techelevator.NewChallengerBot.model.Match;
import com.techelevator.NewChallengerBot.model.MatchDAO;
import com.techelevator.NewChallengerBot.model.Player;
import com.techelevator.NewChallengerBot.model.Team;

public class JDBCMatchDAO implements MatchDAO{
	Connection conn;
	
	/* Create a Statement object so that we can execute a SQL Query */

	
	public JDBCMatchDAO(DataSource dataSource) throws SQLException{
		conn = dataSource.getConnection();
		
	}
	@Override
	public long create(Match match) {
		try {
			String sqlCreateMatch = "INSERT INTO match (winner,time_played,time_ended) VALUES (?,?,?) RETURNING id";
			PreparedStatement createMatch = conn.prepareStatement(sqlCreateMatch);
			createMatch.setInt(1,match.getWinner());
			createMatch.setTimestamp(2, match.getTime());
			createMatch.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			ResultSet idRow = createMatch.executeQuery();
			long id = -1;
			if(idRow.next()) {
				id =idRow.getLong("id");
			}
			for(int i= 0; i<match.getTeams().size();i++) {
				for(Player player : match.getTeams().get(i).getPlayers()) {
					if(player.getDiscord_id()!=-1) {
						String sqlCreateMatchPlayer = "INSERT INTO player_match (player_id,match_id,team) VALUES (?,?,?)";
						PreparedStatement createMatchPlayer = conn.prepareStatement(sqlCreateMatchPlayer);
						createMatchPlayer.setLong(1, player.getId());
						createMatchPlayer.setLong(2, id);
						createMatchPlayer.setInt(3, i);
						createMatchPlayer.execute();
					}
				}
			}
			
			
			return id;	
		}
		catch(Exception ex) {
			
			ex.printStackTrace();
		}
		
		return -1;
	}

	@Override
	public long update(Match match) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long delete(Match match) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Match getMatchByID(Match match) {
		// TODO Auto-generated method stub
		return null;
	}

}
