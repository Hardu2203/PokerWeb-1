package modal.java.cards;

public enum HandType {

    STRAIT_FLUSH        ("Strait flush"),
    FOUR_OF_A_KIND      ("Four of a kind"),
    FULL_HOUSE          ("Full House"),
    FLUSH               ("Flush"),
    STRAIT              ("Strait"),
    THREE_OF_A_KIND     ("Three of a kind"),
    TWO_PAIR            ("Two pair"),
    ONE_PAIR            ("One Pair"),
    HIGH_CARD           ("High card");

    private final String symbol;

    private HandType(String symbol) {
        this.symbol = symbol;
    }

    public static HandType fromSymbol(String symbol) {
        for (HandType rank : values()) {
            if (rank.symbol.equals(symbol)) {
                return rank;
            }
        }

        throw new IllegalArgumentException(symbol + " doesn't exist.");
    }

    @Override
    public String toString() {
        return symbol;
    }
}
