# Drift

[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Android 8.0+](https://img.shields.io/badge/android-8.0%2B-green.svg)](#build-from-source)

Drift is an ambient soundscape mixer for sleep and focus. Layer nature
recordings and deep-space textures, set each to taste, and let the sleep
timer fade things out. It keeps playing with the screen off, as a proper
background media app should.

Drift is a native Android app — Kotlin, Jetpack Compose, and Media3. It was
rebuilt from an earlier Capacitor/WebView version specifically to fix
screen-off playback, which a WebView cannot do reliably.

## Design

The interface borrows from cockpit instrument panels: phosphor green on
near-black, a monospace face, segmented meters, and a status display for the
mix and the sleep timer. Built for the hours when most things have gone quiet.
State is never signalled by colour alone (border, text, and motion all carry
it) so the UI stays legible for colourblind users.

## Sounds

Recorded layers (shipping):

- **Planetside** — Rain, Brook, Fireplace, Wind.
- **Deep Space** — Interstellar Plasma (Voyager 1), Mars Wind (InSight
  lander), Space Whale.

Synthesised layers (Forest, Propulsion, Warp, Radio Chatter) are scaffolded
in the catalogue but ship without audio yet — they arrive with a later
synthesis pass. Full attribution is in [CREDITS.md](CREDITS.md).

## Features

- Mix any number of recorded layers at independent volumes.
- Each layer crossfades between segments so it never audibly loops.
- Mute the whole mix without tearing it down; Stop All clears it.
- Built-in presets plus save/recall/delete of your own mixes.
- Sleep timer (15 / 30 / 60 / 90 minutes) that fades out at zero and keeps
  counting with the screen off.
- Output voicing for phone speaker, external speakers, or headphones.
- Background playback with lock-screen and notification media controls.
- Live theme switching (Phosphor / Amber / Ice / Mono) and per-effect
  motion toggles.
- Fully offline. No network access — the `INTERNET` permission is not declared.
- Open source under the MIT licence. Recordings under Creative Commons / public
  domain, full attribution in [CREDITS.md](CREDITS.md).

## Screenshots

<p align="center">
  <img src="docs/screenshots/drift-main.png" alt="Drift home screen" width="250"/>
  <img src="docs/screenshots/drift-layers.png" alt="Layer cards with volume meters" width="250"/>
  <img src="docs/screenshots/drift-presets.png" alt="Presets" width="250"/>
</p>

## Install

Drift is published on [F-Droid](https://f-droid.org) under the package
`io.github.probably_oxy.drift`. Until the native release lands there, build
from source using the instructions below.

## Build from source

### Requirements

- Android Studio (Narwhal or newer) with the bundled JDK (21)
- Android SDK: `compileSdk 37`, `minSdk 26` (Android 8.0), `targetSdk 36`

No Node, npm, or Capacitor — this is a plain Gradle Android project.

### Build steps

```bash
git clone https://github.com/probably-oxy/drift-audio.git
cd drift-audio/app
./gradlew assembleDebug          # APK in app/build/outputs/apk/debug/
```

Or open the `app/` folder in Android Studio and run on a connected device or
emulator. Tested on a OnePlus CPH2653 running Android 16.

## Project structure

```
drift-audio/
├── app/                  The native Android project (Gradle root)
│   └── app/src/main/
│       ├── java/io/github/probably_oxy/drift/
│       │   ├── audio/    SoundSource, mixer engine, MediaSessionService
│       │   ├── data/     Catalogue, presets, settings
│       │   └── ui/       Compose screens, cockpit components, theme
│       ├── res/raw/      Bundled audio segments the app plays
│       └── assets/       Bundled font licences (OFL)
├── docs/                 Planning notes and screenshots
├── tools/                Development tools (not shipped with the app)
├── fastlane/             F-Droid listing metadata
├── LICENSE
└── CREDITS.md            Audio and typeface attribution
```

All audio is bundled in `res/raw` — no CDN, no external dependencies at runtime.

## Development tools

The `tools/` directory holds the ffmpeg audio-processing pipeline
(`process_audio.sh`) that turns raw source recordings into the app's segments.
It does not ship with the app. See [tools/TOOLS_README.md](tools/TOOLS_README.md).

## Privacy

Drift does not collect, transmit, or store any personal data. It does not
connect to the network. There are no ads, no analytics, no crash reporting,
and no account system. The `INTERNET` permission is not declared in the
manifest; the only permissions are the standard media-app set
(`FOREGROUND_SERVICE`, `FOREGROUND_SERVICE_MEDIA_PLAYBACK`,
`POST_NOTIFICATIONS`, `WAKE_LOCK`).

## Licence

Source code is under the [MIT licence](LICENSE).

Audio recordings and the bundled typefaces are under their own licences:

- Recordings: Creative Commons (CC0 and CC BY) and public-domain government
  works. Full attribution in [CREDITS.md](CREDITS.md).
- JetBrains Mono and Share Tech Mono: SIL Open Font License 1.1 (the full text
  ships in the app under `assets/`).

## Credits

See [CREDITS.md](CREDITS.md) for audio and typeface attribution. In-app credits
(generated from the live catalogue) are available from the menu.
