package modal.java.cards;

public enum Rank {

    ACE     ("A"),
    TWO     ("2"),
    THREE   ("3"),
    FOUR    ("4"),
    FIVE    ("5"),
    SIX     ("6"),
    SEVEN   ("7"),
    EIGHT   ("8"),
    NINE    ("9"),
    TEN     ("10"),
    JACK    ("J"),
    QUEEN   ("Q"),
    KING    ("K");

    private final String symbol;

    private Rank(String symbol) {
        this.symbol = symbol;
    }

    public static Rank fromSymbol(String symbol) {
        for (Rank rank : values()) {
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
