import java.nio.file.Files
import kotlin.io.path.inputStream

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.jetbrains.kotlin.android)
}

android {
  namespace = "com.thindie.animspecs"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.thindie.animspecs"
    minSdk = 26
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.1"
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

tasks.register("downloadLocalizationFiles") {
  doLast {
    val locales = listOf("en", "ru")
    // lets pretend its downloading, in below
    val stringEn = "<resources>\n" +
            "    <string name=\"app_name\">animspecs</string>\n" +
            "    <string name=\"button_1\">default</string>\n" +
            "    <string name=\"button_russian\">russian</string>\n" +
            "    <string name=\"button_english\">english</string>\n" +
            "    <string name=\"append\">append</string>\n" +
            "    <string name=\"erase\">erase</string>\n" +
            "</resources>"

    val stringRu = "<resources>\n" +
            "    <string name=\"app_name\">animspecs</string>\n" +
            "    <string name=\"button_1\">обычный</string>\n" +
            "    <string name=\"button_russian\">русский</string>\n" +
            "    <string name=\"button_english\">английский</string>\n" +
            "    <string name=\"append\">добавить</string>\n" +
            "    <string name=\"erase\">стереть</string>\n" +
            "</resources>"

    val fileEn = Files.createTempFile("en", ".txt")
    Files.write(fileEn, stringEn.toByteArray())

    val fileRu = Files.createTempFile("ru", ".txt")
    Files.write(fileRu, stringRu.toByteArray())

    locales.forEach { locale ->
      val outputFile =
        file("${layout.projectDirectory}/src/main/res/values-$locale/strings.xml")
      outputFile.parentFile.mkdirs()

      if (locale == "en") {
        fileEn.inputStream().use { input ->
          outputFile.outputStream().use { out ->
            input.copyTo(out)
          }
        }
      } else {
        fileRu.inputStream().use { input ->
          outputFile.outputStream().use { out ->
            input.copyTo(out)
          }
        }
      }
    }
  }
}

dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)
}

tasks.named("preBuild").configure {
  dependsOn("downloadLocalizationFiles")
}