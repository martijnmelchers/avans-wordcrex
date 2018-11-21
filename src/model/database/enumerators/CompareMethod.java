package model.database.enumerators;

public enum CompareMethod {
    Equal("="),
    NotEqual("!="),
    Greater(">"),
    GreaterEqual(">="),
    Less("<"),
    LessEqual("=<"),
    Like("LIKE");

    private String method;


    CompareMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
