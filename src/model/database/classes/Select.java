package model.database.classes;

public class Select {
    private TableAlias table;
    private String column;

    public Select(TableAlias table, String column) {
        this.table = table;
        this.column = column;
    }

    public String build() {
        return String.format("`%s`.`%s` AS '%s.%s'", table.build(), column, table.build(), column);
    }
}
