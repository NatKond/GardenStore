package de.telran.gardenStore.serializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) //v klassah
@Retention(RetentionPolicy.RUNTIME) //v runtime dlya obrabotki
public @interface SensitiveData {
    String mask() default "***";
    int visibleChars() default 0;
}
//budu probovat ne dlya vseh a vyborocno