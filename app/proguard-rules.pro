#-repackageclasses 'o'
#-useuniqueclassmembernames
#
#-ignorewarnings
#-obfuscationdictionary dictionary.txt
#-packageobfuscationdictionary class-dictionary.txt
#-classobfuscationdictionary class-dictionary.txt
#
#-keep class co.id.klikacara.object { *;}
#
## These options are the minimal options for a functioning application
## using Proguard and the AWS SDK 2.1.5 for Android
#-keep class org.joda.time.tz.Provider                   { *; }
#-keep class org.joda.time.tz.NameProvider               { *; }
#
#-dontwarn com.fasterxml.jackson.databind.**
#-dontwarn javax.xml.stream.events.**
#-dontwarn org.codehaus.jackson.**
#-dontwarn org.apache.commons.logging.impl.**
#-dontwarn org.apache.http.conn.scheme.**
#-dontwarn org.apache.http.annotation.**
#-dontwarn org.ietf.jgss.**
#-dontwarn org.joda.convert.**
#-dontwarn com.amazonaws.org.joda.convert.**
#-dontwarn org.w3c.dom.bootstrap.**
#
## avloading
#-keep class com.wang.avi.** { *; }
#-keep class com.wang.avi.indicators.** { *; }
#
##fragment
#-keepclassmembers public class * extends android.support.v4.app.Fragment {
#   public <init>(...);
#}
#
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgent
#-keep public class * extends android.preference.Preference
#-keep public class * extends android.support.v4.app.Fragment
#-keep public class * extends android.support.v4.app.DialogFragment
#-keep public class * extends android.app.Fragment
#
##Bottom Navigation
#-keep public class android.support.design.widget.BottomNavigationView { *; }
#-keep public class android.support.design.internal.BottomNavigationMenuView { *; }
#-keep public class android.support.design.internal.BottomNavigationPresenter { *; }
#-keep public class android.support.design.internal.BottomNavigationItemView { *; }
-dontobfuscate
-keep class co.id.klikacara.object.** { *; }
-ignorewarnings

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
 native <methods>;
}

-keepnames class * implements java.io.Serializable

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-assumenosideeffects class android.util.Log {
    public static *** e(...);
    public static *** w(...);
    public static *** wtf(...);
    public static *** d(...);
    public static *** v(...);
}

-keep public class * extends android.view.View {
 public <init>(android.content.Context);
 public <init>(android.content.Context, android.util.AttributeSet);
 public <init>(android.content.Context, android.util.AttributeSet, int);
 public void set*(...);
}

-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
 public void *(android.view.View);
}

# Appcompat and support
-keep interface android.support.v7.** { *; }
-keep class android.support.v7.** { *; }

-dontwarn android.support.**
-dontwarn com.google.ads.**

# butter knife
-keep class **$$ViewBinder { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}