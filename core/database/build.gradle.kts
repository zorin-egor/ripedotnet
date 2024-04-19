plugins {
    alias(libs.plugins.sample.android.library)
    alias(libs.plugins.sample.hilt)
    alias(libs.plugins.sample.room)
}

android {
    namespace = "com.sample.ripedotnet.core.database"
}

dependencies {
    api(projects.core.model)
    implementation(projects.core.model)
}
