package model.database.enumerators;

public enum LinkMethod {
    And("AND"),
    Or("OR");

    private String method;

    LinkMethod(String method){
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
