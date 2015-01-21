package modal.java.evaluators;

import modal.java.cards.Card;
import modal.java.cards.Hand;
import modal.java.cards.Rank;
import modal.java.cards.Suit;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HandEvaluator {

    public static String evaluateHandType(Hand hand) {
        if(isStraightFlush(hand))
            return "Strait Flush";
        if(isFourOfAKind(hand))
            return "Four of a kind";
        if(isFullHouse(hand))
            return "Full house";
        if(isFlushFunctional(hand))
            return "Flush";
        if(isStraight(hand))
            return "Strait";
        if(isThreeOfAKind(hand))
            return "Three of a kind";
        if(isTwoPair(hand))
            return "Two pair";
        if (isOnePair(hand))
            return "One pair";
        return "High Card:" + highCard(hand).toString();
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
