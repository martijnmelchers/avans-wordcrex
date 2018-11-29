package model.database.classes;

public class TableAlias {
    private String table;
    private String alias;
    private Integer usages;

    public TableAlias(String table, String alias) {
        this.table = table;
        this.alias = alias;
        this.usages = 0;
    }

    public String build() {
        return this.alias;
    }

    public String getTable() {
        return table;
    }

    public String getAlias() {
        return alias;
    }

    public Integer getUsages() {
        return usages;
    }

    public void addUsage() {
        usages++;
    }
}
