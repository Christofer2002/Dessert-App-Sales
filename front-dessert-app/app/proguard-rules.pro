# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Retain annotations for injection, such as @Inject







#-keepattributes *Annotation*
#
## Keep fields and constructors annotated with @Inject in classes
#-keepclassmembers class ** {
#    @javax.inject.Inject <fields>;
#}
#-keepclassmembers class ** {
#    @javax.inject.Inject <init>(...);
#}
#
## Keep Dagger/Hilt generated classes
#-dontwarn dagger.**
#-keep class dagger.** { *; }
#-keep interface dagger.** { *; }
#
## Additional rule to keep Hilt-generated components (optional)
#-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }
#
## Keep Retrofit classes
#-keep class retrofit2.** { *; }
#-dontwarn retrofit2.**
#
## Keep Moshi classes (if used for JSON parsing)
#-keep class com.squareup.moshi.** { *; }
#-dontwarn com.squareup.moshi.**
#
## Keep models and entities used for Retrofit/Moshi responses
#-keep class com.project.dessertapp.model.** { *; }
#-keepclassmembers class com.project.dessertapp.model.** { *; }
#
## If using MutableStateFlow or other coroutines components
#-keep class kotlinx.coroutines.flow.MutableStateFlow { *; }
#-keep class kotlinx.coroutines.flow.StateFlow { *; }
#-dontwarn kotlinx.coroutines.flow.**
#
## Keep ViewModel classes and LiveData if used
#-keep class androidx.lifecycle.ViewModel { *; }
#-keepclassmembers class androidx.lifecycle.ViewModel { *; }
#-keep class androidx.lifecycle.LiveData { *; }
#-keepclassmembers class androidx.lifecycle.LiveData { *; }
#
## Retain all fields in the model package to ensure JSON parsing works as expected
#-keepattributes Signature
#-keepattributes *Annotation*
#
## Avoid warnings for Android test classes
#-dontwarn android.test.**
#-dontwarn junit.**
#-dontnote android.test.**
#-dontnote junit.**
#-keep class android.test.** { *; }
#-keep class junit.** { *; }
#
## Mantén todas las clases de Gson
#-keep class com.google.gson.** { *; }
#-dontwarn com.google.gson.**
#
## Mantén las clases utilizadas para el adaptador de tipos y token de tipos, que Gson usa internamente
#-keepclassmembers class com.google.gson.internal.bind.TypeAdapters { *; }
#-keepclassmembers class com.google.gson.internal.bind.util.ISO8601Utils { *; }
#-keepclassmembers class com.google.gson.reflect.TypeToken { *; }
#
#-keep class com.google.gson.internal.** { *; }
#-dontwarn com.google.gson.internal.**
#
#-keep class com.project.dessertapp.model.** { *; }
#-keepclassmembers class com.project.dessertapp.model.** { *; }
#-keepnames class com.project.dessertapp.model.** { *; }
#
## Mantén todas las clases generadas por Hilt
#-keep class hilt_aggregated_deps.** { *; }
#-keep class dagger.hilt.** { *; }
#-keep interface dagger.hilt.** { *; }
#-dontwarn dagger.hilt.**
#
## Mantén módulos y componentes generados internamente por Hilt
#-keep class dagger.hilt.android.internal.lifecycle.** { *; }
#-keep class dagger.hilt.android.internal.managers.** { *; }
#-keep class dagger.hilt.android.internal.modules.** { *; }
#
## Mantén todas las clases anotadas con @HiltViewModel, @Inject, @Module, etc.
#-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }
#-keepclassmembers class ** {
#    @javax.inject.Inject <fields>;
#}
#-keepclassmembers class ** {
#    @javax.inject.Inject <init>(...);
#}
#
## Mantén las clases de Coroutines
#-keep class kotlinx.** { *; }
#-keep class kotlinx.coroutines.internal.** { *; }
#-keep class kotlinx.coroutines.intrinsics.** { *; }
#-dontwarn kotlinx.**
#
## Mantén clases internas relacionadas con el manejo del tiempo y concurrencia
#-keepclassmembers class kotlinx.** { *; }
#-keepclassmembers class kotlinx.** { *; }
#-keepclassmembers class kotlinx.** { *; }
#
## Mantén todos los módulos y componentes internos generados por Dagger Hilt en `hilt_aggregated_deps`
#-keep class hilt_aggregated_deps.** { *; }
#-keep class com.project.dessertapp.DaggerDessertApp_HiltComponents_** { *; }
#-keepclassmembers class com.project.dessertapp.DaggerDessertApp_HiltComponents_** { *; }
#-dontwarn hilt_aggregated_deps.**
#
## Mantén clases con nombres especiales generados por Hilt para Builder Modules
#-keep class com.project.dessertapp.DessertApp_HiltComponents$*Builder { *; }
#-keep class com.project.dessertapp.DessertApp_HiltComponents$*BuilderModule { *; }
#
## Mantén todas las clases de kotlinx.coroutines, especialmente las internas
#-keep class kotlinx.coroutines.** { *; }
#-keep class kotlinx.coroutines.internal.** { *; }
#-keep class kotlinx.coroutines.intrinsics.** { *; }
#-keepclassmembers class kotlinx.coroutines.** { *; }
#-dontwarn kotlinx.coroutines.**
#
## Retener todas las anotaciones para inyección y reflexión
#-keepattributes *Annotation*
#
## Clases internas de Hilt específicas para la gestión de actividades, fragmentos y servicios
#-keep class dagger.** { *; }
#-keep interface dagger.hilt.** { *; }
#-keep public abstract class dagger.** { *; }
#-dontwarn dagger.hilt.**
#
## Mantener las clases internas y los módulos generados por Hilt (incluye hilt_aggregated_deps)
#-keep class hilt_aggregated_deps.** { *; }
#-keep class com.project.dessertapp.DaggerDessertApp_HiltComponents_** { *; }
#-keepclassmembers class com.project.dessertapp.DaggerDessertApp_HiltComponents_** { *; }
#-keep class com.project.dessertapp.DessertApp_HiltComponents$*Builder { *; }
#-keep class com.project.dessertapp.DessertApp_HiltComponents$*BuilderModule { *; }
#-dontwarn hilt_aggregated_deps.**
#
## Clases relacionadas con ViewModel y Componentes Hilt
#-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }
#-keep class dagger.hilt.android.internal.lifecycle.** { *; }
#-keep class dagger.hilt.android.internal.managers.** { *; }
#-keep class dagger.hilt.android.internal.modules.** { *; }
#
## kotlinx.coroutines - Mantén todas las clases, incluyendo internas, intrínsecas y funciones de tiempo
#-keep class kotlinx.** { *; }
#-keep interface kotlinx.** { *; }
#-keep abstract class kotlinx.** { *; }
#-keepclassmembers class kotlinx.** { *; }
#-dontwarn kotlinx.coroutines.**
#
## ViewModels - Mantener ViewModels inyectados y clases que puedan ser eliminadas
#-keep class com.project.dessertapp.presentation.viewmodel.** { *; }
#
## Retrofit y Moshi - Preserva clases esenciales para JSON y llamadas a API
#-keep class retrofit2.** { *; }
#-dontwarn retrofit2.**
#-keep class com.squareup.moshi.** { *; }
#-dontwarn com.squareup.moshi.**
#
## Mantén modelos utilizados en Retrofit/Moshi para evitar problemas de deserialización
#-keep class com.project.dessertapp.model.entities.** { *; }
#
## Evita advertencias adicionales para reflexión
#-dontwarn java.lang.reflect.**
#
#-keep class com.project.dessertapp.presentation.viewmodel.** { *; }

-printusage build/outputs/usage.txt