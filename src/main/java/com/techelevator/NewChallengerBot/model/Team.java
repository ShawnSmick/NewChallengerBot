package com.techelevator.NewChallengerBot.model;

import java.util.ArrayList;
import java.util.List;

import com.techelevator.NewChallengerBot.utils.DuplicatePlayerException;
import com.techelevator.NewChallengerBot.utils.util;

public class Team {
	private List<Player> players = new ArrayList<>();
	public boolean containsPlayer(Player player) {
		for(Player play : players) {
			if(play.equals(player)) {
				return true;
			}
		}
		return false;
	}
	public boolean containsPlayer(String player) {
		for(Player play : players) {
			if(play.getAlias().equals(player)) {
				return true;
			}
		}
		return false;
	}
	public void addPlayer(Player player) throws DuplicatePlayerException{
		if(player!=null&&!containsPlayer(player)) {
			players.add(player);
		}else {
			throw new DuplicatePlayerException();
		}
	}
	public List<Player> getPlayers(){
		return players;
	}
	public static int getLargestTeamSize(List<Team> teams) {
		int size = 0;
		for (Team team : teams) {
			if (team.getPlayers().size() > size) {
				size = team.getPlayers().size();
			}
		}
		return size;
	}
	// Returns the largest name size on a given team
	public int[] getLargestImage() {
		int[] size = new int[2];
		for (Player player : this.getPlayers()) {
			int[] nameSize = player.getLogoSize();
			util.largerSize(size, nameSize);
		}
		return size;
	}
	public static int[] getLargestTeamImage(List<Team> teams) {
		int[] size = new int[2];
		for (Team team : teams) {
			int[] teamSize = team.getLargestImage();
			util.largerSize(size, teamSize);
		}
		return size;
	}
}
