package org.kohsuke.stripper;

import java.io.File;

/**
 * A command-line driver for {@link Stripper}.
 *
 * @author Kohsuke Kawaguchi
 */
public class Main {

    public static void main(String[] args) throws Exception {
        System.exit(run(args));
    }

    public static int run(String[] args) throws Exception {
        InfoSetBuilder isb = new InfoSetBuilder();
        // loads the original class and adapts it
        for(String name : args) {
            if(name.startsWith("-")) {
                name = name.substring(1);
                try {
                    isb.add(name);
                } catch (IllegalArgumentException e) {
                    System.err.println(name+" is not a valid attribute name");
                    return 1;
                }
            }
        }
        Stripper stripper = new Stripper(isb.getResult());
        for(String name : args) {
            if(name.startsWith("-"))
                continue;

            File f = new File(name);
            long before = f.length();
            stripper.process(f);
            long after = f.length();

            System.out.printf("%s %d->%d (%d%%)\n",name,before,after,(after*100)/before);
        }
        return 0;
    }
}
