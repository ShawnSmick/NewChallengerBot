package com.techelevator.NewChallengerBot.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.techelevator.NewChallengerBot.NewChallenger;
import com.techelevator.NewChallengerBot.utils.DuplicatePlayerException;

public class Match {
	private List<Team>teams = new ArrayList<>();
	private long matchID=-1;
	private int winner=-1;
	private byte[] pairingImage;
	private Timestamp timestamp;
	public void setNow() {
		timestamp = new Timestamp(System.currentTimeMillis());
	}
	public Timestamp getTime() {
		return timestamp;
	}
	public void setTime(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public int matchContainsName(String player) {
		for(int i = 0; i < teams.size();i++) {
			if(teams.get(i).containsPlayer(player)) {
				return i;
			}
		}
		return -1;
	}
	public int matchContainsPlayer(Player player) {
		for(int i = 0; i < teams.size();i++) {
			if(teams.get(i).containsPlayer(player)) {
				return i;
			}
		}
		return -1;
	}
	public void addPlayer(Player player, int team) throws DuplicatePlayerException {
		if(player.getId()==-1||matchContainsPlayer(player)<0) {
			if(teams.size()<=team) {
				teams.add(new Team());
			}
			teams.get(team).addPlayer(player);
		}else {
			throw new DuplicatePlayerException();
		}
	}
	public String finishMatch(String player) {
		return finishMatch(matchContainsName(player));
	}
	public String finishMatch(Player player) {
		return finishMatch(matchContainsPlayer(player));
	}
	public String finishMatch(int team) {
		if(winner == -1) {
			winner = team;
			String Winners = "Team";
			for(Player player : teams.get(winner).getPlayers()) {
				if(player.getAlias()!=null) {
					Winners += " "+ player.getAlias();
				}else {
					Winners += " "+ player.getName();
				}
			}
			Winners += " is victorious!";
			NewChallenger.matchdao.create(this);
			return Winners;
		}
		return "Match has already been recorded!";
		
	}
	public int getTeamAmount() {
		return teams.size();
	}
	public List<Team> getTeams() {
		return teams;
	}
	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}
	public long getMatchID() {
		return matchID;
	}
	public void setMatchID(long matchID) {
		this.matchID = matchID;
	}
	public int getWinner() {
		return winner;
	}
	public void setWinner(int winner) {
		this.winner = winner;
	}
	public byte[] getPairingImage() {
		return pairingImage;
	}
	public void setPairingImage(byte[] pairingImage) {
		this.pairingImage = pairingImage;
	}
	
}
