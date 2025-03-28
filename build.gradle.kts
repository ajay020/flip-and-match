// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
    kotlin("plugin.serialization") version "1.9.21"
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    alias(libs.plugins.compose.compiler) apply false
}
