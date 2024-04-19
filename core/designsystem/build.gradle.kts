plugins {
    alias(libs.plugins.sample.android.library)
    alias(libs.plugins.sample.android.library.compose)
}

android {
    namespace = "com.sample.ripedotnet.core.designsystem"
}

dependencies {
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.util)
    implementation(libs.coil.kt.compose)
}
