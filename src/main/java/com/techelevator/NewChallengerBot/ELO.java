package com.techelevator.NewChallengerBot;

import com.techelevator.NewChallengerBot.model.Player;

public class ELO {
	private static final int PLACEMENT_MATCH_NUMBER = 30;
	private static final int PLACEMENT_MATCH_K = 30;
	private static final int SUB_2400_ELO_K = 20;
	private static final int OVER_2400_ELO_K = 10;
	public void calcElo(Player p1, Player p2, int winner) {
//		double nR1 = Math.pow(10d, p1.getElo()/400d); //normalized Rating
//		double nR2 = Math.pow(10d, p2.getElo()/400d);
//		System.out.println(nR1);
//		System.out.println(nR2);
//		double E1 = nR1 / (nR1+nR2); //Expected outcome
//		double E2 = nR2 / (nR2+nR1);
//		System.out.println(E1);
//		System.out.println(E2);
//		double S1 = (winner==1)?1:((winner==0)?.5f:0);
//		double S2 = (winner==2)?1:((winner==0)?.5f:0);
//		int K1 = (p1.getMatchCount()<=PLACEMENT_MATCH_NUMBER)?PLACEMENT_MATCH_K:((p1.getElo()<2400)?SUB_2400_ELO_K:OVER_2400_ELO_K);
//		int K2 = (p2.getMatchCount()<=PLACEMENT_MATCH_NUMBER)?PLACEMENT_MATCH_K:((p2.getElo()<2400)?SUB_2400_ELO_K:OVER_2400_ELO_K);
//		p1.setElo((int)(p1.getElo()+ K1 * (S1-E1)));
//		p2.setElo((int)(p2.getElo()+ K2 * (S2-E2)));
	}
}
