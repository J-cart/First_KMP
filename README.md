<h1 align="center">First KMP</h1>



<p align="center">
📷🖼 A kotlin multiplatform app for taking notes
</p>
</br>

## Demo

https://github.com/J-cart/First_KMP/assets/82452881/1eebccda-86d4-4438-81a4-dde3891afcb4

https://github.com/J-cart/First_KMP/assets/82452881/969076a9-8bcb-4a47-aec7-2260be38522a


## Tech stack & Open-source libraries
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- [Jetpack compose multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/): Declarative framework for sharing UIs across multiple platforms. Based on Kotlin and Jetpack Compose.
- [kotlin multiplatform](https://kotlinlang.org/docs/multiplatform.html): The Kotlin Multiplatform technology is designed to simplify the development of cross-platform projects. It reduces time spent writing and maintaining the same code for different platforms while retaining the flexibility and benefits of native programming.
- [Precompose](https://github.com/Tlaster/PreCompose): Compose Multiplatform Navigation && ViewModel, inspired by Jetpack Navigation, ViewModel and Lifecycle, PreCompose provides similar (or even the same) components for you but in Kotlin, and it's Kotlin Multiplatform project.
- [Touchlab Kermit](https://github.com/touchlab/Kermit): Kermit by Touchlab is a Kotlin Multiplatform centralized logging utility.
- Squareup
  - [Okio](https://github.com/square/okio): Okio is a library that complements java.io and java.nio to make it much easier to access, store, and process your data.
  - SQLDelight([https://github.com/cashapp/sqldelight](https://cashapp.github.io/sqldelight/2.0.1/multiplatform_sqlite/))
- [IceRock Moko](https://github.com/icerockdev)
  - Lifecycle: Observe Android lifecycles and handle UI states upon the lifecycle changes.
  - ViewModel: Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
  - MVVM: This is a Kotlin Multiplatform library that provides architecture components of Model-View-ViewModel for UI applications. Components are lifecycle-aware on Android.
  - Flow
- Architecture
  - MVVM Architecture 
  - Repository Pattern
- [Material-Components](https://github.com/material-components/material-components-android): Material design components for building ripple animation, and CardView.

## Architecture
Each Kotlin Multiplatform project includes three modules:

-**shared** is a Kotlin module that contains the logic common for both Android and iOS applications – the code you share between platforms. It uses Gradle as the build system to help automate your build process.

-**androidApp** is a Kotlin module that builds into an Android application. It uses Gradle as the build system. The androidApp module depends on and uses the shared module as a regular Android library.

-**iosApp** is an Xcode project that builds into an iOS application. It depends on and uses the shared module as an iOS framework. The shared module can be used as a regular framework or as a CocoaPods dependency, based on what you've chosen in the previous step in iOS framework distribution.

In this case theres's one more which is the **desktopMain** which has everything related to the desktop application

<img src="https://github.com/J-cart/First_KMP/assets/82452881/ef673e44-4b2c-4a27-97d6-1bb39a258787" width="300" height ="300"/>
<img src="https://github.com/J-cart/First_KMP/assets/82452881/ea756996-597f-406d-9db6-67febf7a3ffe" width="300" height ="250"/>


