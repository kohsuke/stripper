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
            r.add(Info.valueOf(name));
    }

    void add( Info i ) {
        r.add(i);
    }

    Set<Info> getResult() {
        return r;
    }
}
