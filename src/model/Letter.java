package model;

public class Letter {

    //Is een string omdat het soms meer dan 1 char heeft
    private String _letter;

    public Letter(String letter) {
        _letter = letter;
    }

    public String letter() {
        return _letter;
    }

    public void setLetter(String letter) {

    }

}
