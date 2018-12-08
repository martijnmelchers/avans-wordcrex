package model.database.services;

import model.database.classes.Clause;
import model.database.classes.Join;
import model.database.classes.Select;
import model.database.classes.TableAlias;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class QueryBuilder {
    static String buildClause(List<Clause> clauses) throws Exception {
        var builder = new StringBuilder();

        for (Clause clause : clauses) {
            if (clauses.indexOf(clause) != clauses.size() - 1) {
                builder.append(clause.build(true)).append(" ");
            } else {
                builder.append(clause.build(false));
            }
        }

        return builder.toString();
    }

    static String buildInsert(String table, List<String> columns, List<Object> values) throws Exception {
        var insertString = new StringBuilder();

        for (Object object : values) {
            boolean addComma = true;

            if (values.indexOf(object) == values.size() - 1)
                addComma = false;

            insertString.append(ObjectHelper.objectToSQL(object)).append(addComma ? ", " : "");
        }

        return String.format("INSERT INTO %s (`%s`) VALUES (%s)", table, String.join("`, `", columns), insertString);
    }

    static String buildUpdate(String table, HashMap<String, Object> updatedValues, List<Clause> clauses) throws Exception {
        var updateString = new StringBuilder();
        var iteration = 0;

        for (Map.Entry<String, Object> entry : updatedValues.entrySet()) {
            boolean addComma = true;

            if (iteration == updatedValues.size() - 1)
                addComma = false;

            iteration++;
            updateString.append("`").append(entry.getKey()).append("`").append(" = ").append(ObjectHelper.objectToSQL(entry.getValue())).append(addComma ? ", " : "");
        }

        String clauseString = clauses.size() > 0 ? "WHERE " + QueryBuilder.buildClause(clauses) : "";

        return String.format("UPDATE %s SET %s %s", table, updateString, clauseString);
    }

    public static String buildDelete(String table, List<Clause> clauses) throws Exception {
        String clauseString = clauses.size() > 0 ? " WHERE " + QueryBuilder.buildClause(clauses) : "";

        return String.format("DELETE FROM %s%s", table, clauseString);
    }

    public static String buildSelect(String table, List<Select> columns, List<Clause> clauses, List<Join> joins) throws Exception {

        String selectString = columns.size() > 0 ? QueryBuilder.buildSelectFields(columns) : "*";
        String joinString = joins.size() > 0 ? "\n" + QueryBuilder.buildJoin(joins) : "";
        String clauseString = clauses.size() > 0 ? " WHERE " + QueryBuilder.buildClause(clauses) : "";


        return String.format("SELECT %s FROM %s%s%s", selectString, table, joinString, clauseString);
    }

    private static String buildSelectFields(List<Select> selects) {
        var builder = new StringBuilder();

        for(Select select : selects) {
            builder.append(select.build());
            if(selects.indexOf(select) != selects.size() - 1)
                builder.append(", ");
        }


        return builder.toString();
    }


    public static String buildJoin(List<Join> joins) throws Exception {
        var builder = new StringBuilder();

        for(Join join : joins) {
            builder.append(join.build()).append("\n");
        }


        return builder.toString();
    }
}
