# ProGuard configuration for Quotex Signal Trading Bot
-keepattributes SourceFile,LineNumberTable
-keep public class * {
    public protected *;
}
-keep class com.quotex.signal.trading.** { *; }
-keep interface com.quotex.signal.trading.** { *; }
