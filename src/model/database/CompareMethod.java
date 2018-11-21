package model.database;

public enum CompareMethod {
    Equal("="),
    NotEqual("!="),
    Greater(">"),
    GreaterEqual(">="),
    Less("<"),
    LessEqual("=<");

    private String method;


    CompareMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
