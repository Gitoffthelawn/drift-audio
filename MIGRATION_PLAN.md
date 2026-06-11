# Drift — Native Migration Plan
*Drafted June 2026 — the move from Capacitor/WebView to native Android (Kotlin + Media3 + Compose)*

---

## Why this migration exists

The current app plays audio through `<audio>` elements and Web Audio inside a Capacitor WebView. A WebView is a foreground UI component, so Android does not treat its audio as media playback. When the screen locks, the WebView is backgrounded: timers are throttled (the `setTimeout`/RAF crossfade engine stalls) and the page is suspended. Audio stops and only resumes on unlock.

This is fatal for a sleep/ambient app whose core job is continuous playback with the screen off. No amount of JS patching fully fixes it, because the platform has no way to know a WebView is a media player. The native media stack (Media3 + a `MediaSessionService`) is built for exactly this, and Android keeps it alive screen-off as a first-class media app.

**What this migration replaces:** the playback engine and the UI rendering.
**What it keeps:** the entire sound catalogue, licensing work, ffmpeg processing pipeline, sound-design plans, variant/preset designs, cockpit aesthetic, colorblind-safe principles, and the F-Droid distribution identity.

---

## Architectural through-line

Everything hinges on one abstraction: a clean **`SoundSource`** interface.

A `SoundSource` knows its ID, produces an audio stream, carries a current volume, and accepts a variant parameter. The first (and for v1, only) implementation is `FileSoundSource`, backed by a looping audio file. A future `SynthSoundSource` — for generative sounds like Anamnesis, Warp, and Space Whale — slots in behind the *same* interface without reworking anything else.

Design the interface now; ship only the file implementation; add synthesis when the time comes. The escape hatch costs almost nothing up front (it is a discipline of programming to the interface, not to the file) and saves a rewrite later.

---

## Build order: data → engine → UI

Each layer works before the next begins. You will have an ugly one-button app that plays sound with the screen off long before any cockpit styling exists. That is the correct order — it front-loads the risk (does screen-off playback work?) and defers the cosmetic.

### Phase 0 — Foundations (no Drift code yet)

You need far less than a full course. Target only:

- **Kotlin:** classes, `data class`, interfaces, `sealed class`, null safety, lambdas, `when`. Do the official Kotlin tour (kotlinlang.org/docs/kotlin-tour-welcome.html) — not a 40-hour course. Defer deep coroutines.
- **Android basics:** `Activity`, `Context`, the manifest, Gradle files (you half-know these from Capacitor). Google's "Android Basics with Compose" units 1–2 are enough to start; the rest is reference.
- **Compose:** just enough to render a stateful list. Learn the rest while building the UI in Phase 3.

Honest note: this phase has no Drift payoff, so motivation dips here. Do the Kotlin tour, skim enough Android to not be lost, then start Phase 1 immediately. Building teaches faster than tutorials once you have scaffolding.

### Phase 1 — The data layer (the spine)

Plain Kotlin, no audio or UI yet:

- **`SoundSource` interface** + one `FileSoundSource` implementation. Program to the interface.
- **The catalogue** — port `CATEGORIES`/`SOUNDS` to a Kotlin list of `data class Sound(...)`, carrying licensing metadata, variant definitions, and REC/SYN tags.
- **The preset model** — a `data class` of source IDs + variant + volume, deliberately excluding output mode (existing rule: output mode is a global listening-context setting, not part of a preset). Serialize with `kotlinx.serialization` to JSON. Your v0.2.1 saved-presets feature becomes "write this object to a file."

Reading: Kotlin docs on interfaces, data classes, and kotlinx.serialization. No audio, no UI — just modeling.

### Phase 2 — The audio engine (the bug fix)

Built in verifiable sub-steps:

- **2a — One looping sound, app open.** A single ExoPlayer instance, one looping file. Proves basic plumbing. (Media3 "Getting started" + repeat modes.)
- **2b — Survive screen-off.** Wrap playback in a `MediaSessionService`. **This is the single most important step in the whole project.** When it works, you lock the phone, sound keeps playing, and a media notification appears. That is the moment the rebuild justifies itself. (Media3 "Background playback with MediaSessionService" — read carefully.)
- **2c — Multiple simultaneous layers.** One ExoPlayer per active sound, mixing through the system. Confirm several play at once, screen off.
- **2d — Crossfade / gapless.** Your "3 segments, never repeat" design. Media3 does gapless concatenation natively; crossfade is a volume ramp tied to real playback position — which kills the timer-drift class of bug for good.
- **2e — Output processing (deferrable).** Speaker/stereo/headphones become an `AudioProcessor` in the chain (mono-sum, EQ, limiter — cleaner native than in Web Audio). Design the slot now, implement when wanted.

The Oboe/synthesis escape hatch is **not** in this phase. It is a future `SynthSoundSource` sibling to `FileSoundSource`, added when Anamnesis is on the table — not a rework.

### Phase 3 — The UI (aesthetic reclaimed)

Compose in the cockpit style: Matrix green on black, JetBrains Mono, colorblind-safe multi-channel state (border + text + animation, never color alone — all port fine). Built last, driving the already-working engine.

Bonus for free: because you are now a real media app, lock-screen and notification media controls (play/pause/timer) come largely from the MediaSession you already built — a genuine sleep-app feature the web version could never offer.

---

## The synthesis escape hatch (future, not v1)

Anamnesis, Warp, and Space Whale are generative, not file playback — sparse randomized events, long algorithmic reverb tails, slow drift, never audibly looping. A file-only engine cannot truly produce them.

The plan: build file-first, but keep the door open via the `SoundSource` interface. When generative sound is wanted, add a `SynthSoundSource` backed by **Oboe** (Google's low-latency native audio library — the native equivalent of Web Audio's guts), with a real algorithmic reverb (Freeverb or a Dattorro plate, both small and FOSS-friendly).

Honest caveat: Oboe is C++ and a real step up in difficulty. Do **not** touch it for a long while. Early generative experiments can be done the lazy way — pre-render generative variations to several long files and shuffle them — graduating to real-time synthesis only once your feet are under you. Decide the interface now; defer the hard implementation indefinitely.

---

## GitHub & F-Droid: evolve, don't restart

**Keep the same repository, the same package ID (`io.github.probably_oxy.drift`), and the same `v`-prefixed tag scheme.** Treat the native rebuild as a major version bump inside the existing repo.

Why not a fresh repo or new package ID: your published F-Droid listing, auto-update pipeline, existing users' update path, and project history all live on that identity. A new identity orphans current users on the dead app and forces them to find and reinstall. The rewrite can be a clean break *inside* the repo without discarding the distribution identity.

**Why the F-Droid pipeline survives the rewrite:**
- F-Droid's `UpdateCheckMode: Tags` keys off git tags, which are build-system-agnostic. The bot sees a new `v`-tag with a higher versionCode and builds it whether the code is Capacitor or native.
- Keep the same tagging scheme (continue the `v` prefix) and keep pushing tags to origin — this is what lets the bot detect and add new versions.
- Native stores `versionCode`/`versionName` in `android/app/build.gradle` (Kotlin app) rather than where Capacitor placed them. Because update detection is tag-based, not file-parsed, this does not break detection — but the metadata's build recipe will need updating (see below).

**What you will need to redo on the F-Droid side:**
- **The build recipe in fdroiddata** (`metadata/io.github.probably_oxy.drift.yml`) — the `Builds:` section currently describes a Capacitor/npm build. A native app builds with plain Gradle, so the build commands, `subdir`, and any `sudo`/`init`/npm steps change. This is a metadata merge request to fdroiddata, not a new submission. The app entry, description, and listing stay.
- **Bump versionCode/versionName** in the new `build.gradle`, and ensure the changelog filename matches the integer `versionCode` (existing rule).
- **Descriptions and screenshots** (the open housekeeping item) — worth refreshing for the major version anyway, since the UI changes. Feature graphic (the 1024×500 banner) still outstanding.
- **First native release should probably be a clear major bump** (e.g. v1.0.0) to signal the architecture change to users reading the changelog.

**What stays untouched:** the GitHub repo URL and identity, the package ID, the MIT license, your F-Droid listing entry, and the `AutoUpdateMode`/`UpdateCheckMode` config.

**Branching:** do the rewrite on a long-lived branch (e.g. `native`), keep `main` as the shipping Capacitor app until the native build is real on-device, then merge and tag. This keeps a working app published throughout the rebuild.

---

## Repo structure: clean, lean target

A proposed layout that separates the Android app, the sound assets, and the tooling/planning, so there is never future confusion about what is in the app versus what is catalogued.

```
drift/                              ← repo root
├── README.md
├── LICENSE                         ← MIT
├── docs/                           ← planning + reference (your .md docs live here)
│   ├── MIGRATION_PLAN.md
│   ├── SOUND_REWORK_PLAN.md
│   ├── OUTPUT_MODE_SPEC.md
│   └── drift_project_context.md
├── tools/                          ← dev tooling, not shipped
│   ├── sound_scout.html
│   └── process_audio.sh
├── sounds/                         ← the catalogue (see sound structure below)
│   └── ...                         ← may or may not live in-repo; see note
└── app/                            ← the native Android project (Gradle root)
    ├── build.gradle(.kts)
    ├── settings.gradle(.kts)
    └── app/
        └── src/main/
            ├── java/io/github/probably_oxy/drift/
            │   ├── audio/          ← SoundSource, engine, MediaSessionService
            │   ├── data/           ← catalogue, preset model
            │   └── ui/             ← Compose screens, theme
            ├── res/raw/            ← shipped audio files the app actually plays
            └── AndroidManifest.xml
```

**Decision — where do the audio assets live?**
Two clean options:

1. **Processed files in `app/src/main/res/raw/` (shipped), full catalogue elsewhere.** The app only ever sees the final, in-app files. The richer catalogue (originals, license docs, working files) lives in `sounds/` either in-repo or on your PC. Cleanest separation: `res/raw/` *is* the definition of "what's in the app."
2. **Everything in `sounds/` in-repo, a build step copies shipped files into `res/raw/`.** More automated but more machinery; probably overkill for a solo project right now.

Recommendation: **Option 1.** "Is the file in `res/raw/`?" becomes the single source of truth for "is this sound in the app?" — which is exactly the future-confusion problem you want to eliminate. The full catalogue is reference; `res/raw/` is the app.

**Note on repo size:** base64 audio in the old `index.html` bloated the APK. Native lets you drop base64 entirely and ship real files in `res/raw/`, which is smaller and cleaner. Keep large original WAVs OUT of the git repo (or in a separate archive / Git LFS) — ship only the processed, compressed files.

---

## Sound catalogue: folder-per-sound

A structure that keeps originals, modified versions, and license info together, so further work is unambiguous.

```
sounds/
├── CATALOGUE.md                    ← master index: every sound, status, license, in-app yes/no
├── planetside/
│   ├── rain/
│   │   ├── original/               ← untouched source files as downloaded
│   │   │   └── forestrain_tim.kahn_169031.wav
│   │   ├── processed/              ← ffmpeg output: segments, loop-baked, normalized
│   │   │   ├── rain_seg1.mp3
│   │   │   ├── rain_seg2.mp3
│   │   │   └── rain_seg3.mp3
│   │   ├── LICENSE.txt             ← source, author, license, URL, attribution text
│   │   └── notes.md                ← processing recipe used, EQ decisions, status
│   ├── brook/
│   │   └── ...
│   └── fire/
│       └── ...
├── space/
│   ├── space_whale/
│   │   ├── original/
│   │   ├── processed/
│   │   ├── LICENSE.txt
│   │   └── notes.md
│   └── ...
└── _archive/                       ← sounds cut from the app but kept for reference
    └── ocean_synth/                ← e.g. cut synth Ocean, kept so it's not lost
```

**Per-sound `LICENSE.txt` should capture:** source name, author, license (CC0 / CC BY 3.0 / CC BY 4.0 / public domain), source URL, and the exact attribution string if credit is required. This makes the credits modal trivially generatable and keeps you audit-ready.

**Per-sound `notes.md` should capture:** which original was used, the ffmpeg recipe applied (HPF/EQ/compression/loudnorm settings), segment offsets, and a one-line status (`IN APP` / `CANDIDATE` / `CUT`). This is where your existing per-sound EQ notes (wind double-HPF, thunder -4dB at 180Hz, etc.) should live permanently instead of scattered across context docs.

**`CATALOGUE.md`** is the master index — one table: sound name, category, license, in-app status, source folder. This is the human-readable answer to "what sounds exist and which are shipped."

**The `_archive/` folder** is the key anti-confusion move: cut sounds don't get deleted (losing work) and don't linger ambiguously in the active set. They sit clearly labeled as not-in-app. This directly solves the "which sounds should be in the app versus not" problem — active folders = candidates/shipped, `_archive/` = explicitly out.

---

## Other cleanup worth doing while you learn

- **Drop base64 entirely.** The native app ships real files; the whole base64-in-HTML approach (and its APK bloat) goes away.
- **Consolidate planning docs into `docs/`.** Right now `drift_project_context.md`, `SOUND_REWORK_PLAN.md`, and `OUTPUT_MODE_SPEC.md` are project knowledge; gathering them in `docs/` keeps the repo root clean and the references in one place.
- **Move per-sound EQ/processing notes out of context docs and into each sound's `notes.md`.** Single source of truth per sound.
- **Reconcile the spreadsheet.** `drift_sounds.xlsx` and a markdown `CATALOGUE.md` will drift apart. Pick one as canonical (markdown is diff-friendly and lives in the repo) or generate one from the other.
- **A `.gitignore` for the native project** (Android Studio's standard one) — keeps build artifacts, `.gradle/`, and local config out of the repo.
- **A `CONTRIBUTING`/`BUILDING.md`** noting the build is now plain Gradle (helps the F-Droid maintainer and future-you).

---

## Suggested sequencing

1. **Now (while learning):** set up the clean repo structure and migrate the sound catalogue into folder-per-sound with license/notes. This is satisfying, low-skill, high-value work that makes everything later easier — and it can happen entirely in parallel with Phase 0 Kotlin learning.
2. **Phase 0:** Kotlin tour + minimal Android/Compose.
3. **Phases 1–2:** data spine, then the engine — stop and celebrate at 2b (screen-off playback).
4. **Phase 3:** Compose cockpit UI.
5. **Release:** native branch → merge → major version tag (v1.0.0) → F-Droid build-recipe MR → refresh descriptions/screenshots/feature graphic.

Keep the current Capacitor app published on `main` until the native build is confirmed on-device, so users always have a working (if imperfect) app during the rebuild.

---

## Interim: the live bug and the current user

The native rebuild is weeks of learning away; there is a real user (Gitoffthelawn) hitting a real bug now. Two honest options:

- **Stopgap patch** the current app's screen-off behavior (MediaSession via the web Media Session API + a foreground keep-alive) to buy time. Fragile, battery-costly, and the kind of hack F-Droid review dislikes — but it keeps existing users functional during the rebuild.
- **Accept the current version stays imperfect** and put all energy into native v1.

Either way: an honest reply to the issue (reproduced, root cause identified, larger architectural fix underway) is better than a rushed patch. Hold any "fixed in vX" claim until confirmed on-device.
