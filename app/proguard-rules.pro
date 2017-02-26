# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/ty/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontwarn rx.**
-dontwarn com.rey.**
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8
-dontwarn org.springframework.**
-dontwarn org.simpleframework.xml.stream.**

-dontwarn com.squareup.okhttp.*

-dontwarn retrofit.appengine.UrlFetchClient

-keepattributes *Annotation*

-keep class retrofit2.** { *; }

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-keepattributes Signature

-keep public class moe.kurumi.moegallery.model.*

