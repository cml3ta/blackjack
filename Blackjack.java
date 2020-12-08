import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Blackjack {
	public static void main(String[] args) {
		int pushCount = 0;
		int winCount = 0;
		int blackjackCount = 0;
		int lossCount = 0;
		int totalCount = 0;
		double blackjackPayout = 1.5;
		// basic assumptions
		// 1 deck
		// dealer stays at soft 17
		// double whenever you want
		// split whenever
		// double after splitting is allowed
		// blackjack pays 3:2
		// number of games

		int gamesToPlay = 1000000;
		int nonPushCount = 0;
		int betAmount = 1;
		// Decision Types are: followDealersRules, noDoubleOrSplit, noDoubleYesSplit,
		// yesDoubleNoSplit, yesDoubleAndSplit
		String decisionType = "followDealersRules";

		// start a new game object
		Game game = new Game();
		Deck deck = new Deck();
		deck.shuffle();

		// loop through the number of deals
		System.out.println("-----------------Hand Results--------------------");
		while (nonPushCount < gamesToPlay) {
			// index 0 is games played and index 1 is games won
			List<Object> gameReturn = game.playGame(betAmount, decisionType, deck);
			Integer[] gamesResults = (Integer[]) gameReturn.get(0);
			deck = (Deck) gameReturn.get(1);
			
			// new return array:
			// [win, loss, push, blackjack]
			nonPushCount += (gamesResults[0] + gamesResults[1] + gamesResults[3]);
			pushCount += gamesResults[2]; 
			winCount += gamesResults[0];
			blackjackCount += gamesResults[3];
			lossCount += gamesResults[1];
			// printHandResult(gamesResults);
		}
		System.out.println();

		// get the total hands played
		totalCount = (pushCount + nonPushCount);
		
		// get decimal format
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		decimalFormat.setGroupingUsed(true);
		decimalFormat.setGroupingSize(3);
		
		// print simplified results
		printSimplifiedResult(decimalFormat, totalCount, winCount, lossCount, pushCount, blackjackCount, blackjackPayout);

		// print true final result
		printFinalResult(decimalFormat, nonPushCount, winCount, pushCount, blackjackCount, blackjackPayout);
	}
		
	private static void printHandResult(Integer[] gamesResults) {
		System.out.println(gamesResults[0] + "," + gamesResults[1] + "," + gamesResults[2] + "," + gamesResults[3]);
	}

	public static void printSimplifiedResult(DecimalFormat decimalFormat, int totalCount, int winCount, int lossCount, int pushCount, int blackjackCount, double blackjackPayout) {		
		// print the simplified result
		double firstHandWinPercent = (winCount * 1.0) / totalCount;
		double blackjackWinPercent = (blackjackCount * 1.0) / totalCount;
		System.out.println("-----------------Result Overview--------------------");
		System.out.println("Games Played: " + decimalFormat.format(totalCount));
		System.out.println("Non-Push Games: " + decimalFormat.format(totalCount-pushCount));
		System.out.println("Regular Wins: " + decimalFormat.format(winCount) + " ("
				+ decimalFormat.format(firstHandWinPercent * 100) + "%)");
		System.out.println("Blackjack Wins: " + decimalFormat.format(blackjackCount)+ " ("
				+ decimalFormat.format((blackjackCount * 1.0/totalCount) * 100) + "%)");
		System.out.println("Losses: " + decimalFormat.format(lossCount) + " ("
				+ decimalFormat.format(((lossCount * 1.0) / totalCount) * 100) + "%)");
		System.out.println("Pushes: " + decimalFormat.format(pushCount) + " ("
				+ decimalFormat.format(((pushCount * 1.0) / totalCount) * 100) + "%)");
		System.out.println();
		
		System.out.println("----------Re-Calculating Pushes to get Expected Total--------");
		System.out.println("Total Expected Wins: " 
				+ decimalFormat.format(winCount) + " + " + blackjackPayout + " * " + decimalFormat.format(blackjackCount)
				+ decimalFormat.format(firstHandWinPercent * 100) + "% * " +  decimalFormat.format(pushCount) + " + "
				+ blackjackPayout + " * " + decimalFormat.format(blackjackWinPercent * 100) + "% * " +  decimalFormat.format(pushCount)
				+ " out of " + decimalFormat.format(totalCount));
		System.out.println("Updated Win Count: " + decimalFormat.format(winCount + pushCount * firstHandWinPercent + blackjackPayout * blackjackWinPercent * pushCount)
				+ " out of " + decimalFormat.format(totalCount));
		System.out.println("Updated Odds: "
				+ decimalFormat.format(100 * (winCount + blackjackPayout * blackjackCount + pushCount * firstHandWinPercent + blackjackPayout * blackjackWinPercent * pushCount) / (totalCount))
				+ "%");
		System.out.println();

	}
	
	
	private static void printFinalResult(DecimalFormat decimalFormat, int nonPushCount, int winCount, int pushCount, int blackjackCount, double blackjackPayout) {
		// print the final result
		System.out.println("--------------Ignore Pushes----------------");
		System.out.println("Games Played (Including Pushes): " + decimalFormat.format(nonPushCount + pushCount));
		System.out.println("Games Completed (Removing Pushes): " + decimalFormat.format(nonPushCount));
		System.out.print("Regular Wins: " + decimalFormat.format(winCount));
		System.out.println(" (" + decimalFormat.format(100.0 * winCount / nonPushCount) + "%)");
		System.out.print("Blackjack Wins: " + decimalFormat.format(blackjackCount));
		System.out.println(" (" + decimalFormat.format(100.0 * blackjackCount / nonPushCount) + "%)");
		System.out.print("Losses: " + decimalFormat.format(nonPushCount - winCount - blackjackCount));
		System.out.println(" (" + decimalFormat.format(100.0 * (nonPushCount - winCount - blackjackCount) / nonPushCount) + "%)");
		System.out.println("Total Wins: " + decimalFormat.format(winCount) + " + " + decimalFormat.format(blackjackCount) + " * " + blackjackPayout
		+ " = " + decimalFormat.format(winCount + blackjackCount * blackjackPayout));
		System.out.println("Odds (After Resolving Pushes): " + decimalFormat.format(100 * (winCount + blackjackCount * blackjackPayout)/nonPushCount) + "%");
		System.out.println();

	}


}
