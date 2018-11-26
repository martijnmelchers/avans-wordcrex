package model.database.classes;

public class Select {
    private String table;
    private String column;

    public Select(String table, String column) {
        this.table = table;
        this.column = column;
    }

    public String build() {
        return String.format("`%s`.`%s` AS '%s.%s'", table, column, table, column);
    }
}
