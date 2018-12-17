package model.database.classes;

public class TableAlias {
    private String table;
    private Integer identifier;

    public TableAlias(String table, Integer identifier) {
        this.table = table;
        this.identifier = identifier;
    }

    public String build() {
        if (this.identifier == -1)
            return this.table;

        return this.table + "-" + this.identifier;
    }

    public String getTable() {
        return this.table;
    }

    public Integer getIdentifier() {
        return this.identifier;
    }
}
