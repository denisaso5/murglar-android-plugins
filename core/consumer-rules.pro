# https://www.guardsquare.com/en/products/proguard/manual/usage

-keepattributes Exceptions, Signature, InnerClasses, *Annotation*, SourceFile, LineNumberTable, EnclosingMethod
-renamesourcefileattribute SourceFile
-repackageclasses 'murglar'
-allowaccessmodification

-keep public class * extends java.lang.Exception

-keep class * implements com.badmanners.murglar.lib.core.service.Murglar {
    <init>(...);
}
-keep class * implements com.badmanners.murglar.lib.core.model.node.Node {*;}