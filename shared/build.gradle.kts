import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.build.konfig)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            commonWebpackConfig {
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(project.projectDir.path)
                    }
                }
            }
        }
    }

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm()

    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
            api(libs.kotlinx.serialization.json)
        }
    }
}

android {
    namespace = "com.probro.khoded.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
dependencies {
    implementation(libs.androidx.tools.core)
}


buildkonfig {
    packageName = "com.probro.khoded"
    objectName = "KhodedConfig"
//    exposeObjectWithName = "KhodedConfig"
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())

    defaultConfigs {
        //Gmail Config
        buildConfigField(FieldSpec.Type.STRING, "Type", properties.getProperty("type"))
        buildConfigField(FieldSpec.Type.STRING, "ProjectID", properties.getProperty("project_id"))
        buildConfigField(
            FieldSpec.Type.STRING,
            "PrivateKeyID",
            properties.getProperty("private_key_id")
        )
        buildConfigField(FieldSpec.Type.STRING, "PrivateKey", properties.getProperty("private_key"))
        buildConfigField(
            FieldSpec.Type.STRING,
            "ClientEmail",
            properties.getProperty("client_email")
        )
        buildConfigField(FieldSpec.Type.STRING, "ClientID", properties.getProperty("client_id"))
        buildConfigField(FieldSpec.Type.STRING, "AuthUri", properties.getProperty("auth_uri"))
        buildConfigField(FieldSpec.Type.STRING, "TokenUri", properties.getProperty("token_uri"))
        buildConfigField(
            FieldSpec.Type.STRING,
            "AuthProviderUrl",
            properties.getProperty("auth_provider_x509_cert_url")
        )
        buildConfigField(
            FieldSpec.Type.STRING,
            "ClientCertUrl",
            properties.getProperty("client_x509_cert_url")
        )
        buildConfigField(
            FieldSpec.Type.STRING,
            "UniversDomain",
            properties.getProperty("universe_domain")
        )


        //Postgres values
        buildConfigField(FieldSpec.Type.STRING, "devUri", "dev_Uri")
        buildConfigField(FieldSpec.Type.STRING, "devUsername", "test_user")
        buildConfigField(FieldSpec.Type.STRING, "devPassword", "test_password")
        buildConfigField(FieldSpec.Type.STRING, "prodUri", "prod_uri")
        buildConfigField(FieldSpec.Type.STRING, "prodUsername", "prod_user")
        buildConfigField(FieldSpec.Type.STRING, "prodPassword", "prod_password")
    }
}