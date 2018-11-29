package model.database.enumerators;

public enum JoinMethod {
    INNER(""),
    RIGHT("RIGHT"),
    LEFT("LEFT");

    private String method;

    JoinMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
