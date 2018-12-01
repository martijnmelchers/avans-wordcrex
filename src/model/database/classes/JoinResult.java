package model.database.classes;

import java.util.ArrayList;

public class JoinResult {
    private ArrayList<Join> joins;
    private ArrayList<TableAlias> aliases;
    private ArrayList<String> finishedForeignKeys;

    public JoinResult() {
        this.joins = new ArrayList<>();
        this.aliases = new ArrayList<>();
        this.finishedForeignKeys = new ArrayList<>();
    }

    public void addJoin(Join join) {
        this.joins.add(join);
    }

    public void addJoins(ArrayList<Join> joins) {
        this.joins.addAll(joins);
    }

    public void addAlias(TableAlias alias) {
        this.aliases.add(alias);
    }

    public void addAliases(ArrayList<TableAlias> aliases) {
        this.aliases.addAll(aliases);
    }

    public void addFinishedForeignKey(String keyName) {
        this.finishedForeignKeys.add(keyName);
    }

    public boolean foreignKeyFinished(String keyName) {
        return this.finishedForeignKeys.contains(keyName);
    }

    public ArrayList<Join> getJoins() {
        return this.joins;
    }

    public ArrayList<TableAlias> getAliases() {
        return this.aliases;
    }

    public ArrayList<String> getFinishedForeignKeys() {
        return this.finishedForeignKeys;
    }
}
