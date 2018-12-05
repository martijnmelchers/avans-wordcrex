package model.database.classes;

import java.util.ArrayList;

public class JoinResult {
    private ArrayList<Join> joins;
    private ArrayList<Select> selects;
    private ArrayList<TableAlias> aliases;
    private ArrayList<String> finishedForeignKeys;

    public JoinResult() {
        this.joins = new ArrayList<>();
        this.aliases = new ArrayList<>();
        this.selects = new ArrayList<>();
        this.finishedForeignKeys = new ArrayList<>();
    }

    public void addJoin(Join join) {
        this.joins.add(join);
    }

    public void addJoins(ArrayList<Join> joins) {
        this.joins.addAll(joins);
    }

    public void addFinishedForeignKey(String keyName) {
        this.finishedForeignKeys.add(keyName);
    }

    public void addAlias(TableAlias newAlias) {
        boolean match = false;
        for (TableAlias alias : this.aliases) {
            if (alias.getTable().equals(newAlias.getTable()) && alias.getIdentifier().equals(newAlias.getIdentifier()))
                match = true;
        }

        if (!match)
            this.aliases.add(newAlias);
    }

    public void addAliases(ArrayList<TableAlias> newAliases) {
        for(TableAlias newAlias : newAliases) {
            boolean match = false;
            for (TableAlias alias : this.aliases) {
                if (alias.getTable().equals(newAlias.getTable()) && alias.getIdentifier().equals(newAlias.getIdentifier()))
                    match = true;
            }

            if (!match)
                this.aliases.add(newAlias);
        }
    }

    public void addSelect(Select select) {
        this.selects.add(select);
    }

    public void addSelects(ArrayList<Select> selects) {
        this.selects.addAll(selects);
    }

    public boolean foreignKeyFinished(String keyName) {
        return this.finishedForeignKeys.contains(keyName);
    }

    public ArrayList<Join> getJoins() {
        return this.joins;
    }

    public ArrayList<TableAlias> getAliases() {
        return aliases;
    }

    public ArrayList<Select> getSelects() {
        return this.selects;
    }
}
