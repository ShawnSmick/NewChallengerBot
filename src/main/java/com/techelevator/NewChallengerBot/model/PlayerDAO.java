package com.techelevator.NewChallengerBot.model;

import java.util.List;

public interface PlayerDAO {
	public long create(Player newPlayer);
	public long update(Player upPlayer);
	public long delete(Player delPlayer);
	public boolean playerExists(long id);
	public List<Player> findAllPlayers();
	public List<Player> findPlayersByMatch(long id);
	public Player findPlayerById(long id);
	List<long[]> getAllWinLoss();
	int[] getWinLoss(long id);
	boolean canPing(long id);

}
