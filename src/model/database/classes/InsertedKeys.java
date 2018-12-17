package model.database.classes;

public class InsertedKeys {
    private String table;
    private String column;
    private Object value;

    public InsertedKeys(String table, String column, Object value) {
        this.table = table;
        this.column = column;
        this.value = value;
    }

    public String getTable() {
        return this.table;
    }

    public String getColumn() {
        return this.column;
    }

    public Object getValue() {
        return this.value;
    }
}
