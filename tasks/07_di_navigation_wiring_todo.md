# Step 7: DI + Navigation + Wiring

## Checklist

- [x] Add `koin-android` to `libs.versions.toml`, `composeApp/build.gradle.kts`, `androidApp/build.gradle.kts`
- [x] Create `TokenProviderImpl.android.kt` (SharedPreferences)
- [x] Create `TokenProviderImpl.ios.kt` (NSUserDefaults)
- [x] Create `PlatformModule.kt` (expect declaration in commonMain)
- [x] Create `PlatformModule.android.kt` (actual — OkHttp, Room, TokenProvider)
- [x] Create `PlatformModule.ios.kt` (actual — Darwin, Room, TokenProvider)
- [x] Create `AppModule.kt` (coreModule + authModule + appModules list)
- [x] Create `Routes.kt` (LoginRoute, HomeRoute)
- [x] Create `AppNavigation.kt` (NavHost with login → home flow)
- [x] Create `HomeScreen.kt` (minimal placeholder)
- [x] Wire `App.kt` with AppNavigation (KoinContext removed — deprecated in Koin 4.x)
- [x] Create `StruxoApplication.kt` (Android — startKoin + Napier)
- [x] Update `AndroidManifest.xml` (android:name)
- [x] Create `KoinHelper.kt` (iOS — startKoin + Napier)
- [x] Update `iOSApp.swift` (init → doInitKoin)
- [x] Add `napier` dependency to `androidApp/build.gradle.kts`
- [x] Android build passes: `./gradlew :androidApp:assembleDebug`
- [x] iOS framework build passes: `./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64`
- [x] All tests pass: `./gradlew :composeApp:allTests`
