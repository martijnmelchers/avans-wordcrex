package model.database.enumerators;

public enum CompareMethod {
    EQUAL("="),
    NOT_EQUAL("!="),
    GREATER(">"),
    GREATER_EQUAL(">="),
    LESS("<"),
    LESS_EQUAL("<="),
    LIKE("LIKE");

    private String method;


    CompareMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
