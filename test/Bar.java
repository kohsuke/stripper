

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * @author Kohsuke Kawaguchi
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.PARAMETER,ElementType.METHOD})
public @interface Bar {
}
