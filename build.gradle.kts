import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    // Java support
    id("java")
    // Kotlin support
    id("org.jetbrains.kotlin.jvm") version "1.6.20"
    // gradle-intellij-plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
    id("org.jetbrains.intellij") version "1.5.2"
}



group = "com.emberjs"
version = "2022.1.2"

// Configure project's dependencies
repositories {
    mavenCentral()
}
dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.assertj:assertj-core:3.22.0")
    implementation(kotlin("test"))
}

// Configure gradle-intellij-plugin plugin.
// Read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    pluginName.set("EmberExperimental.js")

    // see https://www.jetbrains.com/intellij-repository/releases/
    // and https://www.jetbrains.com/intellij-repository/snapshots/
    version.set("2022.1")
    type.set("IU")

    downloadSources.set(!System.getenv().containsKey("CI"))
    updateSinceUntilBuild.set(true)

    // Plugin Dependencies -> https://plugins.jetbrains.com/docs/intellij/plugin-dependencies.html
    // Example: platformPlugins = com.intellij.java, com.jetbrains.php:203.4449.22
    //
    // com.dmarcotte.handlebars: see https://plugins.jetbrains.com/plugin/6884-handlebars-mustache/versions
    plugins.set(listOf("JavaScriptLanguage", "CSS", "yaml", "com.dmarcotte.handlebars:221.5080.218"))

    sandboxDir.set(project.rootDir.canonicalPath + "/.sandbox")
}

tasks {
    // Set the compatibility versions to 1.8
    withType<JavaCompile> {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    publishPlugin {
        token.set(System.getenv("ORG_GRADLE_PROJECT_intellijPublishToken"))
    }

}

tasks.test {
    systemProperty("idea.force.use.core.classloader", "true")
}
