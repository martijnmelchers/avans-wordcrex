package model;

public class Letter {

    //Is een string omdat het soms meer dan 1 char heeft
    private String _letter;

    private int _points;

    private int _letterId;

    public Letter(String letter) {
        _letter = letter;
    }

    public String getLetter() {
        return _letter;
    }

    public void setPoints(int points){
        _points = points;
    }

    public void setLetter(String letter) {
        _letter = letter;
    }

    public void setId(int letterId) { _letterId = letterId; }

    public int getid() { return _letterId; }

    public static final String[] defaultLetters()
    {
       return new String[]{
            "A", "A", "A", "A", "A", "A", "A",
            "B", "B",
            "C", "C",
            "D", "D", "D", "D", "D",
            "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E",
            "F", "F",
            "G", "G", "G",
            "H", "H",
            "I", "I", "I", "I",
            "J","J",
            "K", "K", "K",
            "L", "L", "L",
            "M", "M", "M",
            "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
            "O", "O", "O", "O", "O", "O",
            "P", "P",
            "Q",
            "R", "R", "R", "R", "R",
            "S", "S", "S", "S", "S",
            "T", "T", "T", "T", "T",
            "U", "U", "U",
            "V", "V",
            "W", "W",
            "X",
            "Y",
            "Z", "Z"
       };
    }
}
