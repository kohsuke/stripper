package org.kohsuke.stripper;

import java.util.Set;
import java.util.EnumSet;

/**
 * @author Kohsuke Kawaguchi
 */
public class InfoSetBuilder {
    private Set<Info> r = EnumSet.noneOf(Info.class);

    void add( String name ) {
        if(name.equalsIgnoreCase("all"))
            r = EnumSet.allOf(Info.class);
        else
        if(name.equals("basicDebugInfo"))
            r.addAll(BASIC_DEBUG_INFO);
        else
        if(name.equals("allDebugInfo"))
            r.addAll(ALL_DEBUG_INFO);
        else
        if(name.equals("compilerInfo"))
            r.addAll(COMPILER_INFO);
        else
            r.add(Info.valueOf(name));
    }

    void add( Info i ) {
        r.add(i);
    }

    Set<Info> getResult() {
        return r;
    }
    
    private static final Set<Info> BASIC_DEBUG_INFO = EnumSet.of(
            Info.LocalVariableTable,
            Info.LocalVariableTypeTable
    );

    private static final Set<Info> ALL_DEBUG_INFO = EnumSet.of(
            Info.LocalVariableTable,
            Info.LocalVariableTypeTable,
            Info.LineNumberTable,
            Info.SourceFile
    );

    private static final Set<Info> COMPILER_INFO = EnumSet.of(
            Info.Deprecated,
            Info.Synthetic,
            Info.InnerClasses,
            Info.EnclosingMethod,
            Info.RuntimeInvisibleAnnotations,
            Info.RuntimeInvisibleParameterAnnotations,
            Info.Signature,
            Info.SourceDebugExtension
    );
}
