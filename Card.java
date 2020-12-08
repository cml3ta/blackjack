public class Card 
{
	String name;
	String suit;
	String[] names = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
	String[] suits = {"Diamonds", "Hearts", "Spades", "Clubs" };
	
	public Card(int valueIndex, int suitIndex) {
		this.name = names[valueIndex ];
		this.suit = suits[suitIndex ];
	}
	
	public Card(String name) {
		this.name = name;
		this.suit = suits[0];
	}

	public String getName() {
		return name;
	}
	
	public boolean isAce() {
		return name.equals("Ace");
	}
	
	public int getValue() {
		if( isAce() ){
			return 1;
		} else if( name.equals("Jack") || name.equals("Queen") || name.equals("King")){
			return 10;
		} else {
			return Integer.parseInt(name);
		}
	}

}
