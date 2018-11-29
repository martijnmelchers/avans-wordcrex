package model.database.classes;

import model.database.enumerators.CompareMethod;
import model.database.enumerators.LinkMethod;
import model.database.services.ObjectHelper;

public class Clause {

    private String table;

    private String field;
    private CompareMethod method;
    private Object value;
    private LinkMethod linkMethod;

    public Clause(String table, String field, CompareMethod method, Object value, LinkMethod linkMethod) {
        this.table = table;

        this.field = field;
        this.method = method;
        this.value = value;
        this.linkMethod = linkMethod;
    }


    public Clause(String table, String field, CompareMethod method, Object value) {
        this.table = table;

        this.field = field;
        this.method = method;
        this.value = value;
        this.linkMethod = LinkMethod.AND;
    }

    public String build(boolean needsLink) throws Exception {

        return "`" + this.table + "`.`" + this.field + "` " + this.method.getMethod() + " " + ObjectHelper.objectToSQL(this.value) +  " " + (needsLink ? linkMethod.getMethod() : "");

    }
}
