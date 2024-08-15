import com.android.build.gradle.LibraryExtension
import com.sample.ripedotnet.configureAndroidCompose
import com.sample.ripedotnet.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")
            pluginManager.apply(libs.findPlugin("kotlin-compose-compiler").get().get().pluginId)
            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)
        }
    }
}
