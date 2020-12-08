import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
	public List<Object> playGame(int betAmount, String decisionType, Deck deck) {
		ArrayList<Card> yourHand = new ArrayList<>();
		ArrayList<Card> dealersHand = new ArrayList<>();
		
		
		// lets shuffle then check the deck
		// deck.print();

		// get your first card
		yourHand.add(deck.dealCard());

		// give the dealer their first card
		Card upCard = deck.dealCard();
		dealersHand.add(upCard);

		// give you your second card
		yourHand.add(deck.dealCard());

		// give the dealer their card
		dealersHand.add(deck.dealCard());

		// lets see the resulting hands
		// System.out.print("Your Hand:");
		List<Integer> yourResults = makeDecisions(decisionType, yourHand, upCard, deck);
		// System.out.print("Dealers Hand:");
		int dealersResult = dealerDecisions(dealersHand, deck);

		Integer returnArray[] = {0,0,0,0};
		for (Integer result : yourResults) {
			// new return array:
			// [win, loss, push, blackjack]
			
			String retResult = getResult(result,dealersResult);
			returnArray = addtoReturnArray(returnArray, retResult);
		}
		
		List<Object> finalReturn = Arrays.asList(returnArray,deck);
		
		return finalReturn;

	}


	private Integer[] addtoReturnArray(Integer[] returnArray, String retResult) {
		// [win, loss, push, blackjack]
		if(retResult.equals("Win")) {
			returnArray[0] ++;
		} else if(retResult.equals("Loss")) {
			returnArray[1] ++;
		} else if(retResult.equals("Push")) {
			returnArray[2] ++;
		} else if(retResult.equals("Blackjack")) {
			returnArray[3] ++;
		}
		
		return returnArray;
	}


	private String getResult(Integer result, int dealersResult) {
		// first make sure that both people didnt get blackjack (i.e. 100)
		if (result == 100 && dealersResult == 100 ) {
			return "Push";
		}
		
		// if you got a blackjack and it was not a push, then you get 3:2 (i.e. 1.5)
		// current hack: set all blackjacks to a result of 100
		else if (dealersResult == 100) {
			// this breaks the tie if you got natural 21 and dealer got blackjack
			return "Loss";
		} else if (result == 100) {
			// if you got blackjack, then you get your 1.5
			return "Blackjack";
		} else if (result > 21) {
			return "Loss";
		} else if (dealersResult > 21) {
			return "Win";
		} else if(result == dealersResult){
			return "Push";
		} else if(result > dealersResult) {
			return "Win";
		} else {
			return "Loss";
		}
		
		
	}

	private List<Integer> makeDecisions(String decisionType, ArrayList<Card> yourHand, Card upCard, Deck deck) {
		if(getHandTotal(yourHand) == 16) {
			// System.out.println("got 16");
		}
		
		List<Integer> returnInts = new ArrayList<Integer>();

		if (decisionType.equals("followDealersRules")) {
			returnInts.add(followDealersRules(yourHand,deck));
		} else if (decisionType.equals("noDoubleOrSplit")) {
			returnInts.add(noDoubleOrSplit(yourHand, upCard, deck));
		} else if (decisionType.equals("noDoubleYesSplit")) {
			returnInts = noDoubleYesSplit(yourHand, upCard, deck);
		} else if (decisionType.equals("yesDoubleNoSplit")) {
			returnInts = yesDoubleNoSplit(yourHand, upCard, deck);
		} else if (decisionType.equals("yesDoubleAndSplit")) {
			returnInts = yesDoubleAndSplit(yourHand, upCard, deck);
		}

		return returnInts;

	}

	// dealers decision making
	private int dealerDecisions(ArrayList<Card> dealersHand, Deck deck) {
		int total = getHandTotal(dealersHand);
		while (total < 17) {
			dealersHand.add(deck.dealCard());
			total = getHandTotal(dealersHand);
		}
		for(Card card: dealersHand) {
			// System.out.print("," + card.getValue());
		}
		// System.out.println();
		return total;
	}

	// get the value of your hand
	private int getHandTotal(ArrayList<Card> hand) {
		int total = 0;
		int numAces = 0;

		// lets check for blackjack first
		if (hand.size() == 2 && (hand.get(0).getValue() + hand.get(1).getValue() == 11) && (hand.get(0).isAce() || hand.get(1).isAce() )) {
			return 100;
		}

		for (int i = 0; i < hand.size(); i++) {
			Card newCard = hand.get(i);
			if (newCard.isAce()) {
				// set ace initially to 1 and handle later
				total += newCard.getValue();
				numAces++;
			} else {
				total += newCard.getValue();
			}
		}

		// now lets see the ace total
		for (int i = 0; i < numAces; i++) {
			// if you have the space to add 10, do it
			// otherwise it stays at 1
			if (total < 12) {
				total += 10;
			}
		}

		return total;
	}

	////////////////////////////////////////////////////////////
	//////////////////// Decision Making ////////////////////////
	////////////////////////////////////////////////////////////

	private int followDealersRules(ArrayList<Card> yourHand, Deck deck) {
		int total = getHandTotal(yourHand);
		while (total < 17) {
			yourHand.add(deck.dealCard());
			total = getHandTotal(yourHand);
		}
		for(Card card: yourHand) {
			// System.out.print("," + card.getValue());
		}
		// System.out.println();
		return total;
	}

	private int noDoubleOrSplit(ArrayList<Card> yourHand, Card upCard, Deck deck) {
		String optimalStrategy = getOptimalStrategy(yourHand, upCard);
		while (!optimalStrategy.equals("Stay")) {
			yourHand.add(deck.dealCard());
			optimalStrategy = getOptimalStrategy(yourHand, upCard);
		}
		return getHandTotal(yourHand);
	}

	private ArrayList<Integer> noDoubleYesSplit(ArrayList<Card> yourHand, Card upCard, Deck deck) {
		ArrayList<Integer> returnHands = new ArrayList<Integer>();
		ArrayList<Card> secondHand = new ArrayList<Card>();
		// first check to see if you can split
		if (youShouldSplit(yourHand, upCard)) {
			// if you can split, then you have to generate two different hands and
			// handle/return both
			secondHand.add(yourHand.get(1));
			yourHand.remove(1);

			// add the second card to both hands
			yourHand.add(deck.dealCard());
			secondHand.add(deck.dealCard());

			// TODO re-run the function on both hands (could split again)
			// for now, just finish out those two hands
			returnHands.add(noDoubleOrSplit(yourHand, upCard, deck));
			returnHands.add(noDoubleOrSplit(secondHand, upCard, deck));

			// i wonder how often this double split happens
			// not worth right now for only 0.5 percent of games
			if (youShouldSplit(yourHand, upCard) || youShouldSplit(secondHand, upCard)) {
				// System.out.print("");
			}
		} else {
			returnHands.add(noDoubleOrSplit(yourHand, upCard, deck));
		}

		return returnHands;
	}

	private List<Integer> yesDoubleNoSplit(ArrayList<Card> yourHand, Card upCard, Deck deck) {
		// we treat a Double as two duplicate hands, where you get one hit on each hand
		ArrayList<Integer> returnHands = new ArrayList<Integer>();
		String optimalStrategy = getOptimalStrategy(yourHand, upCard);
		if (optimalStrategy.equals("Double")) {
			// you only get one card
			yourHand.add(deck.dealCard());
			returnHands.add(getHandTotal(yourHand));
			returnHands.add(getHandTotal(yourHand));
		} else {
			returnHands.add(noDoubleOrSplit(yourHand, upCard, deck));
		}

		return returnHands;
	}

	private List<Integer> yesDoubleAndSplit(ArrayList<Card> yourHand, Card upCard, Deck deck) {
		ArrayList<Integer> returnHands = new ArrayList<Integer>();
		ArrayList<Card> secondHand = new ArrayList<Card>();
		// first check to see if you can split
		if (youShouldSplit(yourHand, upCard)) {
			// if you can split, then you have to generate two different hands and
			// handle/return both
			secondHand.add(yourHand.get(1));
			yourHand.remove(1);

			// add the second card to both hands
			yourHand.add(deck.dealCard());
			secondHand.add(deck.dealCard());

			// TODO re-run the function on both hands (could split again)
			// for now, just finish out those two hands
			returnHands.addAll(yesDoubleNoSplit(yourHand, upCard, deck));
			returnHands.addAll(yesDoubleNoSplit(secondHand, upCard, deck));

			// i wonder how often this double split happens
			// not worth right now for only 0.5 percent of games
			if (youShouldSplit(yourHand, upCard) || youShouldSplit(secondHand, upCard)) {
				// System.out.print("");
			}
		} else {
			returnHands.addAll(yesDoubleNoSplit(yourHand, upCard, deck));
		}

		return returnHands;
	}

	////////////////////////////////////////////////////////////
	//////////////////// Optimal Strategy///////////////////////
	////////////////////////////////////////////////////////////
	private String getOptimalStrategy(ArrayList<Card> yourHand, Card upCard) {
		boolean youHaveAce = checkForAce(yourHand);
		int total = getHandTotal(yourHand);
		
		// see if you have two cards in hand and are allowed to double
		String doubleReturn = "Double";
		if(yourHand.size() > 2) {
			doubleReturn = "Hit";
		}

		// make sure you arent over 21
		if (total >= 21) {
			return "Stay";
		}

		// if you have an ace, handle it if you have a soft number
		if (youHaveAce) {
			// now if you do have an ace
			// get the non-ace total
			int nonAceTotal = getNonAceTotal(yourHand);
			if (nonAceTotal == 2 && upCard.getValue() >= 5 && upCard.getValue() <= 6) {
				return doubleReturn;
			} else if (nonAceTotal < 6 && upCard.getValue() >= 4 && upCard.getValue() <= 6) {
				return doubleReturn;
			} else if (nonAceTotal == 6 && upCard.getValue() >= 3 && upCard.getValue() <= 6) {
				return doubleReturn;
			} else if (nonAceTotal == 7 && upCard.getValue() <= 6) {
				return doubleReturn;
			} else if (nonAceTotal == 7 && upCard.getValue() >= 7 && upCard.getValue() <= 8) {
				return "Stay";
			} else if (nonAceTotal == 9) {
				return "Stay";
			} else if (upCard.getValue() == 6) {
				return doubleReturn;
			} else if (upCard.getValue() == 8) {
				return "Stay";
			} else if (nonAceTotal < 11) {
				return "Hit";
			} else {
				// System.out.println();
			}
		}

		// if you did not have an ace or a now hard number, then handle
		if (total >= 17) {
			return "Stay";
		} else if (upCard.isAce() && total != 11) {
			return "Hit";
		} else if (total == 9 && upCard.getValue() < 7) {
			return doubleReturn;
		} else if (total == 10 && upCard.getValue() < 10) {
			return doubleReturn;
		} else if (total == 11) {
			return doubleReturn;
		} else if (total == 12 && upCard.getValue() >= 4 && upCard.getValue() <= 6) {
			return "Stay";
		} else if (total >= 13 && total <= 17 && upCard.getValue() <= 6) {
			return "Stay";
		} else {
			return "Hit";
		}

	}

	private boolean checkForSplit(ArrayList<Card> yourHand) {
		if (yourHand.size() > 2) {
			return false;
		}

		if (yourHand.get(0).getName().equals(yourHand.get(1).getName())) {
			return true;
		} else {
			return false;
		}
	}

	private boolean youShouldSplit(ArrayList<Card> yourHand, Card upCard) {
		boolean youCanSplit = checkForSplit(yourHand);

		// first check to see if you should split
		if (youCanSplit) {
			int splitCardValue = yourHand.get(0).getValue();
			if (splitCardValue == 1 || splitCardValue == 8) {
				return true;
			} else if (splitCardValue == 9 && !(upCard.isAce() || upCard.getValue() == 7 || upCard.getValue() == 10)) {
				return true;
			} else if (splitCardValue == 7 && upCard.getValue() < 9) {
				return true;
			} else if (splitCardValue == 6 && upCard.getValue() < 8) {
				return true;
			} else if (splitCardValue == 4 && upCard.getValue() >= 5 && upCard.getValue() <= 6) {
				return true;
			} else if (splitCardValue <= 3 && upCard.getValue() <= 7) {
				return true;
			}
		}

		// otherwise return false
		return false;
	}

	private boolean checkForAce(ArrayList<Card> yourHand) {
		for (int i = 0; i < yourHand.size(); i++) {
			if (yourHand.get(i).isAce()) {
				return true;
			}
		}
		return false;
	}

	private int getNonAceTotal(ArrayList<Card> yourHand) {
		ArrayList<Card> tempHand = new ArrayList<Card>();
		boolean removedAce = false;

		for (int i = 0; i < yourHand.size(); i++) {
			if (yourHand.get(i).isAce()) {
				removedAce = true;
			} else {
				tempHand.add(yourHand.get(i));
			}
		}

		return getHandTotal(tempHand);
	}

}
