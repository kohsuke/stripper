package org.kohsuke.stripper;

/**
 * Possible types of information.
 * 
 * @author Kohsuke Kawaguchi
 */
public enum Info {
    EnclosingMethod,
    LocalVariableTypeTable,
    Signature,
    SourceDebugExtension,
    RuntimeInvisibleAnnotations,
    RuntimeInvisibleParameterAnnotations,
    RuntimeVisibleAnnotations,
    RuntimeVisibleParameterAnnotations,
    AnnotationDefault,
    LocalVariableTable,
    LineNumberTable,
    Synthetic,
    Deprecated,
    SourceFile,
    InnerClasses
}
