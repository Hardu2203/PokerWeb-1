package Services;

import com.google.inject.Singleton;
import modal.java.cards.Deck;
import modal.java.cards.Hand;
import modal.java.evaluators.HandEvaluator;

/**
 * Created by Eduan on 2015-01-12.
 */
@Singleton
public class PokerService {

    public Hand dealHand(Deck d)
    {
       return Hand.getRandomHand(d);
    }

    public String evaluateHand(Hand h)
    {
        return HandEvaluator.evaluateHandType(h).toString();
    }

}
