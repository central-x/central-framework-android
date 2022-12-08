pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven {
            url = uri("https://mirror.yeh.cn/repository/maven-public/")
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "central-framework-android"
include(":app")
include(":central-framework")
