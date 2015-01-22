package modal.java.evaluators;

import modal.java.cards.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HandEvaluator {

    public static HandType evaluateHandType(Hand hand) {
        if(isStraightFlush(hand))
            return HandType.STRAIT_FLUSH;
        if(isFourOfAKind(hand))
            return HandType.FOUR_OF_A_KIND;
        if(isFullHouse(hand))
            return HandType.FULL_HOUSE;
        if(isFlushFunctional(hand))
            return HandType.FLUSH;;
        if(isStraight(hand))
            return HandType.STRAIT;
        if(isThreeOfAKind(hand))
            return HandType.THREE_OF_A_KIND;
        if(isTwoPair(hand))
            return HandType.TWO_PAIR;
        if (isOnePair(hand))
            return HandType.ONE_PAIR;
        return HandType.HIGH_CARD;
    }

    public static int findWinnerPosition(List<Hand> hands)
    {
        Hand winningHand = findWinningHand(hands);
        int i = 0;
        for(Hand hand: hands)
        {
            if(hand == winningHand) return i;
        }
        return -1;
    }

    //TODO add more logic here!
    public static Hand findWinningHand(List<Hand> hands)
    {

        Collections.sort(hands, new Comparator<Hand>() {
            @Override
            public int compare(Hand o1, Hand o2) {
                return Integer.compare(o1.getHandType().ordinal(),o2.getHandType().ordinal());
            }
        });

        return hands.get(0);
    }
    public static Card highCard(Hand hand)
    {
        Card currentCard = hand.getCards().get(0);
        for(Card c : hand.getCards())
        {
            if(c.getRank().compareTo(currentCard.getRank()) > 1) currentCard = c;
        }
        return currentCard;
    }
    public static boolean isStraitFlushFunctional(Hand hand){
        List<Rank> ranks = hand.getCards().stream().map(c -> c.getRank()).collect(Collectors.toList());
        return hand.getCards().stream().allMatch(c -> c.getSuit() == hand.getCards().get(0).getSuit())
                && (ranks.stream().mapToInt(r -> r.ordinal()).max().getAsInt() - ranks.stream().mapToInt(r -> r.ordinal()).min().getAsInt() == 4)
                && ranks.stream().distinct().count() == 5;
    }


    public static boolean isStraightFlush(Hand hand) {
        Collections.sort(hand.getCards(), new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                return o1.getRank().compareTo(o2.getRank());
            }
        });

        Rank previousRank = null;
        Suit previousSuit = null;
        for (Card card : hand.getCards()) {
            if (previousRank != null && card.getRank().ordinal() != previousRank.ordinal() + 1) {
                return false;
            }

            if (previousSuit != null && card.getSuit() != previousSuit) {
                return false;
            }

            previousRank = card.getRank();
            previousSuit = card.getSuit();
        }

        return true;
    }

    public static boolean isFourOfAKind(Hand hand) {
        Map<Rank,Integer> map = new HashMap<Rank,Integer>();
        for (Card card : hand.getCards())
        {
            map.put(card.getRank(),0);
        }
        for (Card card : hand.getCards())
        {
            map.put(card.getRank(),map.get(card.getRank()) + 1);
        }
        return map.containsValue(4);
    }

    public boolean isFourOfAKindFunctional(Hand hand){
        List<Rank> ranks =  hand.getCards().stream().map(c -> c.getRank()).collect(Collectors.toList());
        Stream<Rank> distinctRank= ranks.stream().distinct();

        Long count = ranks.stream().filter(r -> r == distinctRank.findFirst().get()).count();

        return (distinctRank.count() == 2) && (count == 1L || count == 4L);
    }

    public static boolean isFullHouse(Hand hand) {
        Map<Rank,Integer> map = new HashMap<Rank,Integer>();
        for (Card card : hand.getCards())
        {
            map.put(card.getRank(),0);
        }
        for (Card card : hand.getCards())
        {
            map.put(card.getRank(),map.get(card.getRank()) + 1);
        }
        return map.containsValue(2) && map.containsValue(3);
    }

    public boolean isFullHouseFunctional(Hand hand){
        List<Rank> ranks =  hand.getCards().stream().map(c -> c.getRank()).collect(Collectors.toList());
        Stream<Rank> distinctRank= ranks.stream().distinct();

        Long count = ranks.stream().filter(r -> r == distinctRank.findFirst().get()).count();

        return (distinctRank.count() == 2) && (count == 2L || count == 3L);
    }

    public static boolean isFlush(Hand hand) {
        Card previousCard = null;
        for(Card card: hand.getCards())
        {
            if(previousCard != null)
                if(card.getSuit() != previousCard.getSuit())
                    return false;
        }
        return true;
    }

    public static boolean isFlushFunctional(Hand hand){
        List<Suit> suit =  hand.getCards().stream().map(c -> c.getSuit()).collect(Collectors.toList());
        Stream<Suit> distinctSuit= suit.stream().distinct();

        return (suit.stream().distinct().count() == 1);
    }

    public static boolean isStraight(Hand hand) {
        Collections.sort(hand.getCards(), new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                return o1.getRank().compareTo(o2.getRank());
            }
        });

        Rank previousRank = null;
        for (Card card : hand.getCards()) {
            if (previousRank != null && card.getRank().ordinal() != previousRank.ordinal() + 1) {
                return false;
            }

            previousRank = card.getRank();
        }

        return true;
    }

    public static boolean isStraightFunctional(){
        //TODO
        return false;
    }

    public static boolean isThreeOfAKind(Hand hand) {
        Map<Rank,Integer> map = new HashMap<Rank,Integer>() ;
        for (Card card : hand.getCards())
        {
            map.put(card.getRank(),0);
        }
        for (Card card : hand.getCards())
        {
            map.put(card.getRank(),map.get(card.getRank()) + 1);
        }
        return map.containsValue(3);
    }

    public static boolean isThreeOfAKindFunctional(Hand hand){
        List<Rank> ranks =  hand.getCards().stream().map(c -> c.getRank()).collect(Collectors.toList());
        if( ranks.stream().distinct().count() != 3) return false;
        Map<Rank, Long> map = hand.getCards().stream().collect(Collectors.groupingBy(c -> c.getRank(), Collectors.counting()));
        return map.containsValue(3l);
    }

    public static boolean isTwoPair(Hand hand) {
        Map<Rank,Integer> map = new HashMap<Rank,Integer>() ;
        for (Card card : hand.getCards())
        {
            map.put(card.getRank(),0);
        }
        for (Card card : hand.getCards())
        {
            map.put(card.getRank(),map.get(card.getRank()) + 1);
        }
        int count = 0;
        for (Map.Entry<Rank, Integer> entry : map.entrySet())
        {
           if(entry.getValue() == 2) count ++;
        }
        return count == 2;
    }

    public static boolean isTwoPairFunctional(Hand hand) {

        List<Rank> ranks =  hand.getCards().stream().map(c -> c.getRank()).collect(Collectors.toList());
        if( ranks.stream().distinct().count() != 3) return false;
        Map<Rank, Long> map = hand.getCards().stream().collect(Collectors.groupingBy(c -> c.getRank(), Collectors.counting()));
        int count = 0;
        for (Map.Entry<Rank, Long> entry : map.entrySet())
        {
            if(entry.getValue() == 2) count ++;
        }
        return count == 2;
    }

    public static boolean isOnePair(Hand hand) {
        Map<Rank,Integer> map = new HashMap<Rank,Integer>();
        for (Card card : hand.getCards())
        {
            map.put(card.getRank(),0);
        }
        for (Card card : hand.getCards())
        {
            map.put(card.getRank(),map.get(card.getRank()) + 1);
        }
        return map.containsValue(2);
    }
}
