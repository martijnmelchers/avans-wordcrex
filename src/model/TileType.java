package model;

public enum TileType {

    LETTER_TIMES_FOUR("4L"),
    WORD_TIMES_THREE("3W"),
    LETTER_TIMES_TWO("2L"),
    LETTER_TIMES_SIX("6L"),
    WORD_TIMES_FOUR("4W"),
    STARTING_TILE(""),
    STANDARD("");

    private String text;

    TileType(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
