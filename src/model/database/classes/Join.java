package model.database.classes;

import model.database.enumerators.JoinMethod;
import model.database.enumerators.LinkMethod;

import java.util.ArrayList;

public class Join {

    private String originTable;
    private ArrayList<String> originColumns;

    private TableAlias destinationTable;
    private ArrayList<String> destinationColumns;

    private LinkMethod linkMethod;
    private JoinMethod joinMethod;

    private String outputVariable;

    public Join(String originTable, TableAlias destinationTable, LinkMethod linkMethod, JoinMethod joinMethod, String outputVariable) {
        this.originTable = originTable;
        this.originColumns = new ArrayList<>();
        this.destinationTable = destinationTable;
        this.destinationColumns = new ArrayList<>();
        this.linkMethod = linkMethod;
        this.joinMethod = joinMethod;
        this.outputVariable = outputVariable;
    }

    public void addJoinColumn(String origin, String destination) {
        this.originColumns.add(origin);
        this.destinationColumns.add(destination);
    }

    public String getOriginTable() {
        return originTable;
    }

    public TableAlias getDestinationTable() {
        return destinationTable;
    }

    public String build() throws Exception {
        return joinMethod.getMethod() + " JOIN `" + destinationTable.getTable() + "` AS `" + destinationTable.build() + "` ON " + this.buildJoins();
    }

    public String getOutputVariable() {
        return this.outputVariable;
    }

    private String buildJoins() throws Exception {
        if (originColumns.size() != destinationColumns.size())
            throw new Exception("Foreign key error: destination table and origin table do not share the same amount of columns!");

        var builder = new StringBuilder();

        for (int i = 0; i < originColumns.size(); i++) {
            builder.append("`").append(originTable).append("`.`").append(originColumns.get(i)).append("`");
            builder.append(" = ");
            builder.append("`").append(destinationTable.build()).append("`.`").append(destinationColumns.get(i)).append("`");
            if (i != originColumns.size() - 1)
                builder.append(" ").append(linkMethod.getMethod()).append(" ");
        }

        return builder.toString();
    }
}
