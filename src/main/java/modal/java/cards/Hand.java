package modal.java.cards;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

public final class Hand {

    private final List<Card> cards = new ArrayList<>(5);

    public Hand(String ... cardRepresentations) {
        if (cardRepresentations.length != 5) {
            throw new IllegalArgumentException("Exactly 5 cards are required.");
        }

        for (String cardRepresentation : cardRepresentations) {
            cards.add(new Card(cardRepresentation));
        }
    }

    public List<Card> getCards() {
        return cards;
    }

    public static Hand getRandomHand(Deck d)
    {
        d.Shuffle();
        LinkedList<Card> cards = d.getCards();
        Hand hand = new Hand(cards.get(0).toString(),cards.get(1).toString(),cards.get(2).toString(),cards.get(3).toString(),cards.get(4).toString());
        cards.removeFirst();
        cards.removeFirst();
        cards.removeFirst();
        cards.removeFirst();
        cards.removeFirst();
        return hand;
    }
    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(",", "(", ")");
        for (Card card : cards) {
            stringJoiner.add(card.toString());
        }
        return stringJoiner.toString();
    }
}
