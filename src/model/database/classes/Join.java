package model.database.classes;

import model.database.enumerators.JoinMethod;
import model.database.enumerators.LinkMethod;

import java.util.ArrayList;

public class Join {

    private String originTable;
    private ArrayList<String> originColumns;

    private String destinationTable;
    private ArrayList<String> destinationColumns;
    private LinkMethod linkMethod;
    private JoinMethod joinMethod;

    public Join(String originTable, String destinationTable, LinkMethod linkMethod, JoinMethod joinMethod) {
        this.originTable = originTable;
        this.originColumns = new ArrayList<>();
        this.destinationTable = destinationTable;
        this.destinationColumns = new ArrayList<>();
        this.linkMethod = linkMethod;
        this.joinMethod = joinMethod;

    }

    public void addJoinColumn(String origin, String destination) {
        this.originColumns.add(origin);
        this.destinationColumns.add(destination);
    }

    public String getOriginTable() {
        return originTable;
    }

    public String getDestinationTable() {
        return destinationTable;
    }

    public String build() throws Exception {
        return joinMethod.getMethod() + " JOIN " + originTable + " ON " + this.buildJoins();
    }

    private String buildJoins() throws Exception {
        if (originColumns.size() != destinationColumns.size())
            throw new Exception("Foreign key error: destination table and origin table do not share the same amount of columns!");

        var builder = new StringBuilder();

        for (int i = 0; i < originColumns.size(); i++) {
            builder.append("`").append(originTable).append("`.`").append(originColumns.get(i)).append("`");
            builder.append(" = ");
            builder.append("`").append(destinationTable).append("`.`").append(destinationColumns.get(i)).append("`");
            if (i != originColumns.size() - 1)
                builder.append(linkMethod.getMethod());
        }

        return builder.toString();
    }
}
