package org.kohsuke.stripper;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.CodeVisitor;
import org.objectweb.asm.CodeAdapter;
import org.objectweb.asm.Label;
import org.objectweb.asm.attrs.AnnotationDefaultAttribute;
import org.objectweb.asm.attrs.EnclosingMethodAttribute;
import org.objectweb.asm.attrs.LocalVariableTypeTableAttribute;
import org.objectweb.asm.attrs.RuntimeInvisibleAnnotations;
import org.objectweb.asm.attrs.RuntimeInvisibleParameterAnnotations;
import org.objectweb.asm.attrs.RuntimeVisibleAnnotations;
import org.objectweb.asm.attrs.RuntimeVisibleParameterAnnotations;
import org.objectweb.asm.attrs.SignatureAttribute;
import org.objectweb.asm.attrs.SourceDebugExtensionAttribute;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

/**
 * A tool that removes unnecessary information from class files.
 *
 * @author Kohsuke Kawaguchi
 */
public class Stripper {

    private final Set<Info> skip;

    /**
     * @param skip
     *      list of attributes to skip.
     */
    public Stripper(Set<Info> skip) {
        this.skip = skip;
    }

    /**
     * Strips attributes from the specified file.
     */
    public void process(File f) throws IOException {
        FileInputStream in = new FileInputStream(f);
        ClassReader cr = new ClassReader(in);

        ClassWriter cw = new ClassWriter(false);
        ClassVisitor cv = new ClassFilter(cw);
        cr.accept(cv, atts, false);
        byte[] b = cw.toByteArray();

        in.close();

        // stores the adapted class on disk
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(b);
        fos.close();
    }

    private boolean filtered(Attribute attr) {
        try {
            return skip.contains(Info.valueOf(attr.type));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    class ClassFilter extends ClassAdapter {
        public ClassFilter(ClassVisitor cv) {
            super(cv);
        }

        public void visit(int version, int access, String name, String superName, String[] interfaces, String sourceFile) {
            if(skip.contains(Info.SourceFile))
                sourceFile = null;
            super.visit(version, access, name, superName, interfaces, sourceFile);
        }

        public void visitAttribute(Attribute attr) {
            if(!filtered(attr))
                super.visitAttribute(attr);
        }

        public void visitField(int access, String name, String desc, Object value, Attribute attrs) {
            super.visitField(access, name, desc, value, filter(attrs));
        }

        public CodeVisitor visitMethod(int access, String name, String desc, String[] exceptions, Attribute attrs) {
            return new CodeFilter(super.visitMethod(access, name, desc, exceptions, filter(attrs)));
        }

        private Attribute filter(Attribute attrs) {
            if(attrs==null)
                return null;

            if(filtered(attrs))
                return filter(attrs.next);
            else {
                attrs.next = filter(attrs.next);
                return attrs;
            }
        }
    }

    private class CodeFilter extends CodeAdapter {
        public CodeFilter(CodeVisitor cv) {
            super(cv);
        }

        public void visitLineNumber(int line, Label start) {
            if(!skip.contains(Info.LineNumberTable))
                super.visitLineNumber(line, start);
        }

        public void visitLocalVariable(String name, String desc, Label start, Label end, int index) {
            if(!skip.contains(Info.LocalVariableTable))
                super.visitLocalVariable(name, desc, start, end, index);
        }

        public void visitAttribute(Attribute attr) {
            if(!filtered(attr))
                super.visitAttribute(attr);
        }
    };


    private static final Attribute[] atts = new Attribute[]{
        new SignatureAttribute(),
        new LocalVariableTypeTableAttribute(),
        new EnclosingMethodAttribute(),
        new SourceDebugExtensionAttribute(),
        new RuntimeInvisibleAnnotations(),
        new RuntimeInvisibleParameterAnnotations(),
        new RuntimeVisibleAnnotations(),
        new RuntimeVisibleParameterAnnotations(),
        new AnnotationDefaultAttribute()
    };
    // read by default
    // LocalVariableTable
    // LineNumberTable
    // Synthetic
    // Deprecated
    // SourceFile
    // InnerClasses
    //
}
