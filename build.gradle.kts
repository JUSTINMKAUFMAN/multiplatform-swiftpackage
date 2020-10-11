plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
    signing
}

version = "1.0.0"

repositories {
    jcenter()
}

dependencies {
    compileOnly(kotlin("gradle-plugin"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    withJavadocJar()
    withSourcesJar()
}

gradlePlugin {
    plugins {
        create("pluginMaven") {
            id = "com.chromaticnoise.multiplatform-swiftpackage"
            implementationClass = "com.chromaticnoise.multiplatformswiftpackage.MultiplatformSwiftPackagePlugin"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("pluginMaven") {
            pom {
                groupId = "com.chromaticnoise.multiplatform-swiftpackage"
                artifactId = "com.chromaticnoise.multiplatform-swiftpackage.gradle.plugin"

                name.set("Multiplatform Swift Package")
                description.set("Gradle plugin to generate a Swift.package file and XCFramework to distribute a Kotlin Multiplatform iOS library")
                url.set("https://github.com/ge-org/multiplatform-swiftpackage")
                licenses {
                    license {
                        name.set("GNU General Public License, Version 3")
                        url.set("https://www.gnu.org/licenses/gpl-3.0.txt")
                    }
                }
                developers {
                    developer {
                        name.set("Georg Dresler")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/ge-org/multiplatform-swiftpackage.git")
                    developerConnection.set("scm:git:ssh://github.com:ge-org/multiplatform-swiftpackage.git")
                    url.set("http://github.com/ge-org/multiplatform-swiftpackage/tree/master")
                }
            }
        }
    }

    repositories {
        maven {
            val releasesUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsUrl = "https://oss.sonatype.org/content/repositories/snapshots/"
            name = "mavencentral"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsUrl else releasesUrl)
            credentials {
                username = System.getenv("SONATYPE_NEXUS_USERNAME")
                password = System.getenv("SONATYPE_NEXUS_PASSWORD")
            }
        }
    }
}

signing {
    sign(publishing.publications["pluginMaven"])
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}