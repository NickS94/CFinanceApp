// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()

    }
    dependencies {
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
        classpath(libs.google.services)
    }
}




plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.devtools.ksp") version "1.9.20-1.0.14" apply false



    }