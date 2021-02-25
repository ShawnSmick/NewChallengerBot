package com.techelevator.NewChallengerBot.model.jdbc;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.techelevator.NewChallengerBot.model.Player;
import com.techelevator.NewChallengerBot.model.PlayerDAO;

public class JDBCPlayerDAO implements PlayerDAO{
	Connection conn;
	
	/* Create a Statement object so that we can execute a SQL Query */

	
	public JDBCPlayerDAO(DataSource dataSource) throws SQLException{
		conn = dataSource.getConnection();
	
	}
	@Override
	public List<Player> findAllPlayers(){
		return null;
	}
	@Override
	public boolean canPing(long id) {
		try {
			String sqlcanPing = "SELECT opt_in FROM player WHERE discord_id = ?";
			PreparedStatement canPing = conn.prepareStatement(sqlcanPing);
			canPing.setLong(1, id);
			ResultSet pingRow = canPing.executeQuery();
			if(pingRow.next()) {
				boolean pingable = pingRow.getBoolean("opt_in");
				if(pingable) {
					return true;
				}
			}
		}catch(Exception ex) {
			
		}
		return false;
	}
	@Override
	public long create(Player newPlayer) {
		try {
			String sqlCreatePlayer = "INSERT INTO player (discord_id,alias,logo,opt_in) VALUES (?,?,?,?) RETURNING id";
			PreparedStatement createPlayer = conn.prepareStatement(sqlCreatePlayer);
			byte[] imageData =newPlayer.getLogo();
			createPlayer.setLong(1, newPlayer.getDiscord_id());
			createPlayer.setString(2, newPlayer.getAlias());
			createPlayer.setBinaryStream(3, new ByteArrayInputStream(imageData),imageData.length);
			createPlayer.setBoolean(4,newPlayer.isOptin());
			ResultSet idRow = createPlayer.executeQuery();
			if(idRow.next()) {
				return idRow.getLong("id");
			}
					
		}
		catch(Exception ex) {
			
			ex.printStackTrace();
		}
		
		return -1;
	}

	@Override
	public long update(Player upPlayer) {
		try {
			String sqlCreatePlayer = "UPDATE player SET alias = ?, logo = ?, opt_in = ? WHERE discord_id = ? RETURNING id";
			PreparedStatement createPlayer = conn.prepareStatement(sqlCreatePlayer);
			byte[] imageData =upPlayer.getLogo();
			createPlayer.setLong(4, upPlayer.getDiscord_id());
			createPlayer.setString(1, upPlayer.getAlias());
			createPlayer.setBoolean(3,upPlayer.isOptin());
			createPlayer.setBinaryStream(2, new ByteArrayInputStream(imageData),imageData.length);
			ResultSet idRow = createPlayer.executeQuery();
			if(idRow.next()) {
				return idRow.getLong("id");
			}
					
		}
		catch(Exception ex) {
			
			ex.printStackTrace();
		}
		
		return -1;
	}

	@Override
	public long delete(Player delPlayer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Player> findPlayersByMatch(long id) {
		return null;
	}


	@Override
	public Player findPlayerById(long id) {
		try {
			String sqlCreatePlayer = "SELECT * FROM player WHERE discord_id = ?";
			PreparedStatement createPlayer = conn.prepareStatement(sqlCreatePlayer);

			createPlayer.setLong(1, id);

			ResultSet idRow = createPlayer.executeQuery();
			if(idRow.next())
				return mapRowToPlayer(idRow);
		}
		catch(Exception ex) {
			
			ex.printStackTrace();
		}
		return null;
	}


	public Player mapRowToPlayer(ResultSet rows) throws SQLException{
		Player tempPlay = new Player();
		tempPlay.setId(rows.getLong("id"));
		tempPlay.setDiscord_id(rows.getLong("discord_id"));
		tempPlay.setAlias(rows.getString("alias"));
		
		tempPlay.setLogo(rows.getBytes("logo"));
		tempPlay.setOptin(rows.getBoolean("opt_in"));
		return tempPlay;
	}
	@Override
	public boolean playerExists(long id) {
		try {
			String sqlCreatePlayer = "SELECT * FROM player WHERE discord_id = ?";
			PreparedStatement createPlayer = conn.prepareStatement(sqlCreatePlayer);

			createPlayer.setLong(1, id);
			
			ResultSet idRow = createPlayer.executeQuery();
			if(idRow.next()) {
				return true;
			}
					
		}
		catch(Exception ex) {
			
			ex.printStackTrace();
		}
		return false;
	}
	@Override
	public List<long[]> getAllWinLoss() {
		String sqlGetWinLoss = "SELECT p.discord_id,Count(1) as Matches,Count(CASE WHEN pm.team = m.winner THEN 1 ELSE NULL END) AS Wins ,Count(CASE WHEN pm.team = m.winner THEN NULL ELSE 1 END) AS losses"+
								" FROM player p "+
									" INNER JOIN player_match pm "+
									" ON p.id = pm.player_id "+
										" INNER JOIN match m "+
										" ON m.id = pm.match_id "+
								" GROUP BY p.discord_id";
		List<long[]> WinnersAndLosers = new ArrayList<>();
		try {
			PreparedStatement winLoss = conn.prepareStatement(sqlGetWinLoss);
			ResultSet winRow = winLoss.executeQuery();
			while(winRow.next()) {
				long[] winsAndLosses = new long[4];
				winsAndLosses[0] = winRow.getInt("wins");
				winsAndLosses[1] = winRow.getInt("losses");
				winsAndLosses[2] = winRow.getInt("matches");
				winsAndLosses[3] = winRow.getLong("discord_id");
				WinnersAndLosers.add(winsAndLosses);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return WinnersAndLosers;
	}
	@Override
	public int[] getWinLoss(long id) {
		String sqlGetWinLoss = "SELECT p.discord_id,Count(1) as Matches,Count(CASE WHEN pm.team = m.winner THEN 1 ELSE NULL END) AS Wins ,Count(CASE WHEN pm.team = m.winner THEN NULL ELSE 1 END) AS losses"+
								" FROM player p "+
									" INNER JOIN player_match pm "+
									" ON p.id = pm.player_id "+
										" INNER JOIN match m "+
										" ON m.id = pm.match_id "+
								" WHERE p.discord_id = ?  GROUP BY p.discord_id";
		int[] winsAndLosses = new int[3];
		try {
			PreparedStatement winLoss = conn.prepareStatement(sqlGetWinLoss);
			winLoss.setLong(1, id);
			ResultSet winRow = winLoss.executeQuery();
			if(winRow.next()) {
				winsAndLosses[0] = winRow.getInt("wins");
				winsAndLosses[1] = winRow.getInt("losses");
				winsAndLosses[2] = winRow.getInt("matches");
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return winsAndLosses;
	}
}
