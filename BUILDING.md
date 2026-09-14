# Building Drift

Drift is a plain Gradle Android project — no Node, npm, or Capacitor. If
you're coming from the pre-native (WebView) version, that whole toolchain is
gone; this is a standard Kotlin/Compose app.

## Requirements

- Android Studio (Narwhal or newer), which bundles the JDK (21) this project
  needs. If you're building from the command line instead, make sure
  `JAVA_HOME` points at a JDK 21+ and it's on your `PATH`.
- Android SDK with `compileSdk 37`, `minSdk 26` (Android 8.0), `targetSdk 36`
  — Android Studio will prompt to install whatever's missing.

## Build

```bash
git clone https://github.com/probably-oxy/drift-audio.git
cd drift-audio/app
./gradlew assembleDebug          # APK in app/build/outputs/apk/debug/
```

Or open the `app/` folder (not the repo root) in Android Studio and run on a
connected device or emulator.

Useful Gradle tasks while working:

```bash
./gradlew compileDebugKotlin     # fast type-check, no APK
./gradlew installDebug           # build + install on a connected device
./gradlew lint                   # Android lint
```

Tested on a OnePlus CPH2653 running Android 16; should run on any device in
the `minSdk`–`targetSdk` range.

## Project layout

```
drift-audio/
├── app/                  The native Android project (Gradle root — open this in Android Studio)
│   └── app/src/main/
│       ├── java/io/github/probably_oxy/drift/
│       │   ├── audio/    SoundSource, mixer engine, MediaSessionService
│       │   ├── data/     Catalogue, presets, settings
│       │   └── ui/       Compose screens, cockpit components, theme
│       ├── res/raw/      Bundled audio segments the app actually plays
│       └── assets/       Bundled font licences (OFL)
├── docs/                 Planning notes and screenshots
├── fastlane/             F-Droid store listing (description, screenshots, changelogs)
├── LICENSE
└── CREDITS.md            Audio and typeface attribution
```

All audio the app plays is bundled in `res/raw/` — no CDN, no
runtime downloads. `res/raw/` is the single source of truth for "is this
sound actually in the app."

Note: the full sound catalogue (original source recordings, per-sound
license files, ffmpeg processing notes, and the processing pipeline itself)
lives in `sounds/` and `tools/` locally but is **not part of this public
repo** (both are gitignored) — those are large source files and working
notes kept off GitHub. `res/raw/` and `CREDITS.md` are what ship; that's
enough to build and run the app.

## Release checklist

1. Bump `versionCode` and `versionName` in `app/app/build.gradle.kts`
   (`defaultConfig` block).
2. Commit the bump.
3. `git tag -a vX.Y.Z -m vX.Y.Z` on that commit.
4. `git push origin main --follow-tags`.

F-Droid's bot watches this repo's tags (`UpdateCheckMode: Tags`) and picks up
new releases automatically — no separate F-Droid submission needed for a
routine release. The `fastlane/metadata/` directory in this repo feeds the
store listing (description, screenshots, per-version changelog files named
after the `versionCode`), so update those alongside a release if the listing
needs to change.
