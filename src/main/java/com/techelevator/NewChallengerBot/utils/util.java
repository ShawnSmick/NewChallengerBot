package com.techelevator.NewChallengerBot.utils;

import java.util.ArrayList;
import java.util.List;

public class util {
	/**
	 * Assuming equal sized arrays returns and array of the largest values of each
	 * point for example [2,5] and [4,3] would return [4,5]
	 * 
	 * @param size1 
	 * @param size2 
	 */
	public static int[] largerSize(int[] size1, int[] size2) {
		for (int i = 0; i < size1.length; i++) {
			if (size2[i] > size1[i]) {
				size1[i] = size2[i];
			}
		}
		return size1;
	}

	/**
	 * Generates several teams from message argument and sends an image with the vs
	 * pairing
	 */
	public static String[] splitNoBlanks(String toSplit, String regex) {
		String[] splitS = toSplit.split(regex);
		List<String> cleanSplit = new ArrayList<>();
		for (int i = 0; i < splitS.length; i++) {
			splitS[i] = splitS[i].trim();
			if (splitS[i].length() > 0) {
				cleanSplit.add(splitS[i]);
			}
		}
		String[] cleanArray = cleanSplit.toArray(new String[0]);
		return cleanArray;
		
	}
}
