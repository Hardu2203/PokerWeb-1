package modal.java.cards;

public final class Card {

    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Card(String representation) {
        this(Rank.fromSymbol(representation.substring(0, representation.length() - 1)),
             Suit.fromSymbol(representation.charAt(representation.length() - 1)));
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return rank.toString() + suit.toString();
    }
}
