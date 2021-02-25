package com.techelevator.NewChallengerBot.model;

public interface ChannelDAO {
	long create(long id);
	boolean isValid(long id);
	void delete(long id);
}
