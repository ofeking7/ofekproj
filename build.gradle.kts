buildscript {
    dependencies{
        classpath("com.google.gms:google-services:4.4.2")
    }
}

plugins{
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false

}