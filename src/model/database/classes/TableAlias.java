package model.database.classes;

public class TableAlias {
    private String table;
    private Integer identifier;
    private Integer usages;

    public TableAlias(String table, Integer identifier) {
        this.table = table;
        this.identifier = identifier;
        this.usages = 0;
    }

    public String build() {
        if(identifier == -1)
            return this.table;

        return this.table + "-" + this.identifier;
    }

    public String getTable() {
        return table;
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public Integer getUsages() {
        return usages;
    }

    public void addUsage() {
        usages++;
    }
}
