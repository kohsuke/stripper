package org.kohsuke.stripper;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Ant task that drives {@link Stripper}.
 *
 * @author Kohsuke Kawaguchi
 */
public class StripperTask extends Task {
    private final Set<FileSet> sources = new HashSet<FileSet>();

    private final InfoSetBuilder skip = new InfoSetBuilder();

    /**
     * Class files to be processed.
     */
    public void addConfiguredSource( FileSet fs ) {
        sources.add(fs);
    }

    public void setRemove(String list) {
        // loads the original class and adapts it
        StringTokenizer tokens = new StringTokenizer(list);
        while(tokens.hasMoreTokens()) {
            String name = tokens.nextToken();
            try {
                skip.add(name);
            } catch (IllegalArgumentException e) {
                throw new BuildException(name+" is not a valid attribute name");
            }
        }
    }

    public void execute() throws BuildException {
        long beforeTotal=0;
        long afterTotal=0;
        Stripper stripper = new Stripper(skip.getResult());
        for( FileSet fs : sources ) {
            DirectoryScanner ds = fs.getDirectoryScanner(getProject());
            File baseDir = ds.getBasedir();

            for( String f : ds.getIncludedFiles() ) {
                File file = new File(baseDir,f);
                long before = file.length();
                try {
                    stripper.process(file);
                } catch (IOException e) {
                    throw new BuildException(e);
                }
                long after = file.length();

                log(String.format(
                    "%s %d->%d (%d%%)\n",file,before,after,(after*100)/before), Project.MSG_VERBOSE);

                beforeTotal += before;
                afterTotal += after;
            }
        }

        log(String.format(
            "Total stripped %d->%d (%d%%)\n",beforeTotal,afterTotal,(afterTotal*100)/beforeTotal), Project.MSG_INFO);
    }
}
