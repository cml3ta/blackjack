public class Deck {

	private Card[] deck; // An array of 52 Cards, representing the deck.
	private int cardsUsed; // How many cards have been dealt from the deck.

	public Deck() {
		// Create an unshuffled deck of cards.
		int numCards = 52 * 8;
		deck = new Card[numCards];
		int cardCt = 0; // How many cards have been created so far.

		for (int i = 0; i < 8; i++) {
			for (int suit = 0; suit < 4; suit++) {
				for (int value = 0; value < 13; value++) {
					deck[cardCt] = new Card(value, suit);
					cardCt++;
				}
			}
		}
		cardsUsed = 0;
	}

	public void shuffle() {
		// Put all the used cards back into the deck, and shuffle it into
		// a random order.
		// System.out.println("---------- shuffle ---------");
		int cards = 52 * 8;
		cards -= 1;
		for (int i = cards; i > 0; i--) {
			int rand = (int) (Math.random() * (i + 1));
			Card temp = deck[i];
			deck[i] = deck[rand];
			deck[rand] = temp;
		}
		cardsUsed = 0;
	}

	public int cardsLeft() {
		// As cards are dealt from the deck, the number of cards left
		// decreases. This function returns the number of cards that
		// are still left in the deck.
		return ( 52 * 8 ) - cardsUsed;
	}

	public Card dealCard() {
		// Deals one card from the deck and returns it.
		int cards = ( 52 * 8 );
		if (cardsUsed == cards/4) {
			shuffle();
		}
		cardsUsed++;
		return deck[cardsUsed - 1];
	}
	
	public void print() {
		int cards = 52 * 8;
		for(int i = cardsUsed; i < cards; i++) {
			System.out.println(deck[i].getValue());
		}
	}

}