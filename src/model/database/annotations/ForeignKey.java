package model.database.annotations;

import model.database.enumerators.ResultMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ForeignKey {
    Class<?> type();

    String field();

    String output() default "";

    ResultMethod result() default ResultMethod.SINGLE;
}
