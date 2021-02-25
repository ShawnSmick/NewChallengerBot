package com.techelevator.NewChallengerBot.model;

public interface MatchDAO {
	public long create(Match match);
	public long update(Match match);
	public long delete(Match match);
	public Match getMatchByID(Match match);
}
