# Handoff Log

Shared working log between three assistants:
- **Claude** (primary — larger work)
- **Qwen** (local via Continue/Ollama — minor edits during Claude breaks)
- **Copilot** (minor edits during Claude breaks)

**All assistants: read this file at the start of a session. Append a dated entry
at the TOP when you finish. Newest first. Never delete history.**

## Entry format
`## YYYY-MM-DD — <assistant> — <one-line summary>`
then a few bullets: files changed, why, anything the next assistant should know.

---

## 2026-06-16 — Claude — Native Phases 1, 2a, 2b (data layer + screen-off audio engine)

Work on `native` branch. **Nothing committed yet** — all changes below are in the
working tree (mix of untracked files + modified build files). Suggested commit
message theme: "Native data layer + Media3 playback engine (Phases 1–2b)".

### Phase 1 — Data layer (DONE, compiles, reviewed)
The five data-layer files already existed untracked (prior session/handoff assistant).
I reviewed them against `sounds/CATALOGUE.md`, `www/app.js`, and per-sound `LICENSE.txt`
— **all data verified accurate** (Muges/ambientsounds, NASA/NOAA licensing, variants
match the old app exactly). Files: `audio/SoundSource.kt`, `audio/FileSoundSource.kt`,
`data/Sound.kt`, `data/Catalogue.kt`, `data/Preset.kt`. Compiles clean.
- Catalogue = 7 IN APP sounds (rain, brook, fire, wind, interstellarplasma, marswind,
  spaceWhale as REC; forest, propulsion, warp, radio as SYN). CANDIDATE sounds excluded.
- **Decision:** SYN sounds kept as non-playable catalogue entries (segmentCount=0, no
  files/synth yet) — fine for later UI testing.
- **Decision:** synths come back as pre-rendered baked files first (FileSoundSource);
  real-time Oboe synthesis deferred indefinitely (per MIGRATION_PLAN).
- Known deferred: `description` field may need a short-flavor vs long-context split in
  UI (NASA sounds want more context); processed filenames use hyphens (illegal in
  res/raw) — rename on import, whole sound library likely rebuilt later anyway.

### Phase 2a — One looping sound, app open (DONE, confirmed on device)
- Added Media3 ExoPlayer dep (media3 = 1.5.1) to libs.versions.toml + build.gradle.kts.
- Copied `sounds/rain/processed/aud-rain-0.mp3` -> `res/raw/aud_rain_0.mp3` (hyphens ->
  underscores; res/raw names must be [a-z0-9_]). First file in res/raw.
- Rewrote MainActivity into a `PlaybackTest` composable: single ExoPlayer, looping
  (REPEAT_MODE_ALL), PLAY/PAUSE button, released via DisposableEffect.
- Used `android.resource://` URI (stable API) instead of RawResourceDataSource (which is
  @UnstableApi and would need @OptIn). Same result.
- **On-device (OnePlus, Android 16): rain loops fine.** Minor volume dip at the loop
  seam — it's the FILE (single segment doesn't loop seamlessly), not the engine. Fixed
  later by Phase 2d gapless/crossfade + sound-library rebuild.

### Phase 2b — Survive screen-off via MediaSessionService (DONE — CONFIRMED ON DEVICE)
This is the milestone the whole rebuild exists for. **Confirmed on OnePlus (Android 16):
"Drift is running" media notification + lock-screen controls appear; playback survived
5-10 min screen-off with intermittent other-app use. The WebView screen-off bug is fixed.**
- Added media3-session dep.
- **New** `audio/PlaybackService.kt`: a `MediaSessionService` that now owns the ExoPlayer
  + a MediaSession (player moved out of the Activity). Same single looping rain file.
- AndroidManifest: added FOREGROUND_SERVICE, FOREGROUND_SERVICE_MEDIA_PLAYBACK,
  POST_NOTIFICATIONS permissions + the `<service>` with foregroundServiceType=mediaPlayback.
- MainActivity rewired: no longer owns a player; connects to the service via a
  `MediaController` (async, ListenableFuture), sends play/pause. Requests POST_NOTIFICATIONS
  on launch (Android 13+ needs it to show the media notification).
- `assembleDebug` succeeds (full APK, manifest merges).
- Audio focus confirmed: starting VLC pauses Drift (handleAudioFocus=true working).

**Deferred items surfaced during 2b testing (NOT bugs to fix now):**
- **UI play/pause button doesn't sync to real playback state.** When audio focus pause
  happens (or playback otherwise changes outside the button), the button label is stale —
  user had to tap twice to resume. Cause: we track `isPlaying` manually. Fix in Phase 3
  by adding a `Player.Listener` and driving button state from the controller's actual
  state. UI concern, deferred.
- **Notification cleanup wanted.** User wants to drop the seekbar/timeline + album-art
  placeholder and show just Play / Stop / Mute. Recorded in MIGRATION_PLAN Phase 3.
  Reality: on Android 13+ the media template is largely system-controlled — achievable
  parts are custom action buttons (Play/Stop + a custom Mute command) and suppressing the
  scrubber by modelling the loop as having no seekable duration; fully removing the art
  slot fights the system template. Phase 3 polish.
- **Notification permission is OPTIONAL for playback** (F-DROID-RELEVANT): a foreground
  service runs even if POST_NOTIFICATIONS is denied — playback continues uninterrupted,
  the user just loses the visible notification/controls. Worth an explicit deny-then-test
  confirmation on the OnePlus (OEM battery management aside). The 3 permissions
  (FOREGROUND_SERVICE, FOREGROUND_SERVICE_MEDIA_PLAYBACK, POST_NOTIFICATIONS) are all
  standard media-app permissions, NOT F-Droid anti-features; document them in the metadata.

### Audio focus / "Mix with other audio" — recorded in MIGRATION_PLAN (Phase 2 section)
User asked why pressing play in VLC pauses AntennaPod (answer: audio focus, a system
mechanism; well-behaved apps pause on focus loss). Currently the engine sets
`handleAudioFocus = true` so Drift behaves the same (interrupts other media). Planned
user toggle "Mix with other audio / background mode" flips it to false so Drift layers
under other audio as an ambient soundscape. One-line engine change; global
listening-context setting (like output mode), NOT part of a preset; UI in Phase 3.

### Build notes for next session
- JAVA_HOME isn't set in shell; the bundled JDK is at
  `C:\Program Files\Android\Android Studio\jbr`. Compile via:
  `JAVA_HOME=... app/gradlew.bat -p app :app:assembleDebug`.
- Untracked working-tree state also includes ui/theme/* and modified CLAUDE.md,
  build.gradle.kts, libs.versions.toml. Confirm scope before the big commit.

---

## 2026-06-09 — Claude — fadeElTo race condition fix (real sounds stopping bug)

**Bug investigated:** Recorded sounds (Rain, Fireplace, etc.) stop after a few minutes; synths keep going. Root cause: `fadeElTo` had no RAF cancellation. Multiple overlapping RAF loops could race on the same `<audio>` element — a stale fade-out loop's `onDone` callback would call `.pause()` on an element that a newer fade-in loop was trying to bring up.

**Fix applied — `app.js` only, lines 292-308:**
- Added `el._fadeRAF` property to store the in-flight RAF ID per element
- New `fadeElTo` cancels any running fade on `el` before starting its own: `if (el._fadeRAF) cancelAnimationFrame(el._fadeRAF)`
- Each `requestAnimationFrame(tick)` result written back to `el._fadeRAF`; cleared to `null` on natural completion
- A canceled fade never reaches its `onDone` (RAF callback is unscheduled before it can fire)

**Testing in progress as of 2026-06-09.** No other files touched.

**Other findings from investigation (not yet fixed):**
- All 3 `<audio>` elements per sound have `loop` attribute — correct, no issue
- Schedule timer (`setTimeout`) is already properly cleared (`clearTimeout(p.xfTimer)`) before each reschedule — no stacking
- 7 orphaned sound entries in HTML (`forestrain`, `creek`, `heavyrain`, `thunder`, `enginerumble`, `rocketthruster`, `rocketfiring`) exist in the DOM but have no `real:true` entry in SOUNDS — dead weight, harmless
- Zero error handlers on `<audio>` elements; all `.play()` rejections silently swallowed

---

## 2026-06-05 — Claude — Handoff layer complete
- Created 04-local-guardrails.md (alwaysApply: true) with hard limits and corrected
  project facts (real MP3 files in www/sounds/, entry = www/index.html, Shiv theme).
- Appended handoff workflow sections to CLAUDE.md and copilot-instructions.md.
- Added "Copilot Chat only" note to copilot-instructions.md.
- .gitignore keeps .continue/rules/ committed, ignores caches.
- All three assistants may now make minor edits in www/ (excluding www/sounds/).

## 2026-06-05 — Claude — Continue + handoff setup
- Created .continue/rules/ (01-project, 02-web, 03-audio-pipeline), config verified,
  .gitignore updated. No app code touched.
- Qwen and Copilot cleared for minor edits in www/ only (excluding www/sounds/).