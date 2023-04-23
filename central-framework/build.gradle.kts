plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

version = "1.0.x-SNAPSHOT"

android {
    namespace = "central.android"
    compileSdk = project.ext.get("android.compileSdk").toString().toInt()
    buildToolsVersion = project.ext.get("android.buildToolsVersion").toString()

    defaultConfig {
        minSdk = project.ext.get("android.minSdk").toString().toInt()
        buildConfigField("String", "VERSION", "\"${project.version}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles.add(file("consumer-rules.pro"))
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles.add(getDefaultProguardFile("proguard-android-optimize.txt"))
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

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("com.google.code.gson:gson:2.10.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

publishing {
    publications {
        register("release", MavenPublication::class.java) {
            groupId = "com.central-x.android"
            artifactId = "central-framework"
            version = project.version.toString()
            pom {
                name.set("central-framework-android")
                description.set("Central Framework for Android")
                url.set("https://central-x.com")
                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        name.set("Alan Yeh")
                        email.set("alan@yeh.cn")
                        url.set("https://github.com/alan-yeh")
                    }
                }
                scm {
                    connection.set("scm:https://github.com/central-x/central-framework-android.git")
                    url.set("https://github.com/central-x/central-framework-android")
                }
            }

            afterEvaluate {
                from(components.getByName("release"))
            }
        }

        repositories {
            val releaseUrl = if (project.version.toString().endsWith("-SNAPSHOT")) {
                "https://deploy.central-x.com/repository/maven-snapshots/"
            } else if (project.version.toString().endsWith(".RELEASE")){
                "https://deploy.central-x.com/repository/maven-releases/"
            } else {
                throw IllegalArgumentException("Version malformed: Project version must end with '.RELEASE' or '-SNAPSHOT'")
            }

            maven {
                name = "nexus"
                url = uri(releaseUrl)
                credentials {
                    username = project.extra.get("NEXUS_USERNAME")?.toString()
                    password = project.extra.get("NEXUS_PASSWORD")?.toString()
                }
            }
        }
    }
}