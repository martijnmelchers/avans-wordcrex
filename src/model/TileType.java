package model;

public enum TileType {

    LETTER_TIMES_FOUR("L4"),
    WORD_TIMES_THREE("W3"),
    LETTER_TIMES_TWO("L2"),
    LETTER_TIMES_SIX("L6"),
    WORD_TIMES_FOUR("W4"),
    STANDARD("");

    private String text;

    private TileType(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
