plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "central.android"
    compileSdk = 33
    buildToolsVersion = "33.0.1"

    defaultConfig {
        minSdk = 28
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles.add(file("consumer-rules.pro"))
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles.add(getDefaultProguardFile ("proguard-android-optimize.txt"))
            proguardFiles.add(file("proguard-rules.pro"))
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

publishing {
    publications {
        register("debug", MavenPublication::class.java) {
            groupId = "com.central-x.android"
            artifactId = "central-framework"
            version = "1.0.0"

            afterEvaluate {
                from(components.getByName("debug"))
            }
        }

        register("release", MavenPublication::class.java) {
            groupId = "com.central-x.android"
            artifactId = "central-framework"
            version = "1.0.0"

            afterEvaluate {
                from(components.getByName("release"))
            }
        }
    }

    repositories {
        maven {
            name = "release"
            url = uri("https://deploy.yeh.cn/repository/maven-releases/")
            credentials {
                username = "admin"
                password = "showme"
            }
        }
        maven {
            name = "snapshot"
            url = uri("https://deploy.yeh.cn/repository/maven-snapshots/")
            credentials {
                username = "admin"
                password = "showme"
            }
        }
    }
}