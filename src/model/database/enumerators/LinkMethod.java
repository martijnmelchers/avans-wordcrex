package model.database.enumerators;

public enum LinkMethod {
    AND("AND"),
    OR("OR");

    private String method;

    LinkMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return this.method;
    }
}
