pluginManagement {
    repositories {
        maven {
            url = uri("https://mirror.central-x.com/repository/maven-public/")
        }
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven {
            url = uri("https://mirror.central-x.com/repository/maven-public/")
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "central-framework-android"
include(":app")
include(":central-framework")
