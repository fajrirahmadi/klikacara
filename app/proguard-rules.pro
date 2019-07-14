-repackageclasses 'o'
-useuniqueclassmembernames

-ignorewarnings
-obfuscationdictionary dictionary.txt
-packageobfuscationdictionary class-dictionary.txt
-classobfuscationdictionary class-dictionary.txt

-keep class co.id.klikacara.object { *;}

# These options are the minimal options for a functioning application
# using Proguard and the AWS SDK 2.1.5 for Android
-keep class org.joda.time.tz.Provider                   { *; }
-keep class org.joda.time.tz.NameProvider               { *; }

-dontwarn com.fasterxml.jackson.databind.**
-dontwarn javax.xml.stream.events.**
-dontwarn org.codehaus.jackson.**
-dontwarn org.apache.commons.logging.impl.**
-dontwarn org.apache.http.conn.scheme.**
-dontwarn org.apache.http.annotation.**
-dontwarn org.ietf.jgss.**
-dontwarn org.joda.convert.**
-dontwarn com.amazonaws.org.joda.convert.**
-dontwarn org.w3c.dom.bootstrap.**

# avloading
-keep class com.wang.avi.** { *; }
-keep class com.wang.avi.indicators.** { *; }

#fragment
-keepclassmembers public class * extends android.support.v4.app.Fragment {
   public <init>(...);
}

#Bottom Navigation
-keep public class android.support.design.widget.BottomNavigationView { *; }
-keep public class android.support.design.internal.BottomNavigationMenuView { *; }
-keep public class android.support.design.internal.BottomNavigationPresenter { *; }
-keep public class android.support.design.internal.BottomNavigationItemView { *; }