
import java.util.List;

/**
 * @author Kohsuke Kawaguchi
 */
public class Test {
    // example of using Signature and LocalVariableTypeTable
    // RuntimeInvisibleAnnotations, RuntimeInvisibleParameterAnnotations,
    // RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
    @Foo
    @Bar
    public <T> void foo(@Foo @Bar T x) {
        List<T> y=null;
        y.add(x);
        return;
    }
}
