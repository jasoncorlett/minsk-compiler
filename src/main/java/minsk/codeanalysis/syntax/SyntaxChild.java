package minsk.codeanalysis.syntax;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SyntaxChild {
	int order() default Integer.MAX_VALUE;
}
