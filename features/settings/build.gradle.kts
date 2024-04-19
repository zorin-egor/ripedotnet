plugins {
    alias(libs.plugins.sample.feature)
    alias(libs.plugins.sample.android.library.compose)
}

android {
    namespace = "com.sample.ripedotnet.feature.settings"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(projects.core.data)
}
