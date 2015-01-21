package modal.java.cards;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by Eduan on 2015-01-12.
 */
public class Deck {
    private LinkedList<Card> cards;
    public Deck()
    {
        cards = new LinkedList<Card>();
        String[] suits = {"S","H","D","C"};
        String[] ranks = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
        for(String s : suits)
        {
            for(String r : ranks)
            {
                cards.add(new Card(r + s));
            }
        }
        Shuffle();
    }
    public void Shuffle()
    {
        Collections.shuffle(cards);
    }

    public LinkedList<Card> getCards() {
        return cards;
    }
}
