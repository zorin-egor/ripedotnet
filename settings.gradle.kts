pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name="SampleRipeNet"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":core:common")
include(":core:model")
include(":core:network")
include(":core:datastore")
include(":core:datastore-proto")
include(":core:database")
include(":core:designsystem")
include(":core:data")
include(":core:domain")
include(":core:ui")
include(":features:settings")
include(":features:organizations_by_name")
include(":features:inetnum_by_ip")
include(":features:inetnums_by_org_id")
