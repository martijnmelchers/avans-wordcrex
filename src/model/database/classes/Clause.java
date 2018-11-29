package model.database.classes;

import model.database.enumerators.CompareMethod;
import model.database.enumerators.LinkMethod;
import model.database.services.ObjectHelper;

public class Clause {
<<<<<<< HEAD
=======
    private String table;
>>>>>>> development
    private String field;
    private CompareMethod method;
    private Object value;
    private LinkMethod linkMethod;

<<<<<<< HEAD
    public Clause(String field, CompareMethod method, Object value, LinkMethod linkMethod) {
=======
    public Clause(String table, String field, CompareMethod method, Object value, LinkMethod linkMethod) {
        this.table = table;
>>>>>>> development
        this.field = field;
        this.method = method;
        this.value = value;
        this.linkMethod = linkMethod;
    }

<<<<<<< HEAD
    public Clause(String field, CompareMethod method, Object value) {
=======
    public Clause(String table, String field, CompareMethod method, Object value) {
        this.table = table;
>>>>>>> development
        this.field = field;
        this.method = method;
        this.value = value;
        this.linkMethod = LinkMethod.AND;
    }

    public String build(boolean needsLink) throws Exception {
<<<<<<< HEAD
        return "`" + this.field + "` " + this.method.getMethod() + " " + ObjectHelper.objectToSQL(this.value) +  " " + (needsLink ? linkMethod.getMethod() : "");
=======
        return "`" + this.table + "`.`" + this.field + "` " + this.method.getMethod() + " " + ObjectHelper.objectToSQL(this.value) +  " " + (needsLink ? linkMethod.getMethod() : "");
>>>>>>> development
    }
}
