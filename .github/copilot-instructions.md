 Copilot instructions for drift-audio

  Purpose: quick, repository-specific guidance for Copilot/Copilot CLI sessions.

  -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

  Build / install

   - Requirements (from README): Node.js 20 LTS+, JDK 17, Android Studio, Android SDK with compileSdk 36 and minSdk
    24.
   - Install JS deps: npm install
   - Sync web assets into the Android project: npx cap sync android
   - Open android/ in Android Studio and build or run on device/emulator.

  Notes: there are no npm scripts defined in package.json; use the commands above directly.

  Tests / Lint

   - No test suites, test runner, or lint scripts are present in package.json. There is no single-test command to run.
   - If tests are added, place them in www/ or add npm scripts; prefer standard runners (Jest, Playwright) and expose npm test/npm run test:unit for Copilot to call.

  High-level architecture

   - The app is a single-page web app (vanilla JS/HTML/CSS) located in www/ and is wrapped by Capacitor for Android in android/.
   - No framework: UI and audio logic are in www/app.js, styling in www/style.css, markup in www/index.html.
   - Audio model:
    - "Real" sounds are bundled audio segments in www/sounds/ and exposed as <audio id="aud-<id>-<seg>"> elements in index.html.
    - Each real sound typically has 3 segments (seg indices
    
    0..2) used by a crossfade engine to play seamless loops.
    - "Synth" sounds are generated at runtime via the Web Audio API. Builders live in app.js (buildOcean, buildBirds, buildCrickets, buildWhite).
    - Sparse/evented samples use sparse, burstSegs, silentSegs, and eventRate on sound descriptors.
   - State persistence: localStorage key drift_state stores volumes, active layers, and sparse event rates.
   - Background / Android integration:
    - Optional Capacitor BackgroundAudio plugin integration is used when available.
    - exitApp() uses Capacitor's App plugin when present.
   - Presets are defined in app.js (PRESETS) and applied via applyPreset().

  Key repository conventions (important for Copilot)

   - Audio element naming: aud-<sound-id>-<segment-index> (e.g., aud-rain-0.mp3) — crossfade engine expects these IDs.
   - Sound descriptors in CATEGORIES/SOUNDS include fields: id, label, desc, real (boolean), segs (int), optional sparse/burstSegs/silentSegs/eventRate.
   - Crossfade engine expectations:
    - Real sounds: players[id] holds seg indices (segIdxA, segIdxB) and targetVol.
    - Use activateReal(id) / deactivateReal(id) to start/stop real sounds and setRealVolume(id, v) to change volume smoothly.
   - Synths: constructed via SYNTH_BUILDERS and managed in synthLayers[id]; use activateSynth, deactivateSynth, setSynthVolume.
   - UI updates: updateUI() syncs DOM sliders/fills and is called after state changes.
   - Storage key: drift_state (localStorage) — Copilot should avoid renaming unless updating load/save logic.
   - Offline-first: no network usage; audio assets are bundled. Avoid adding runtime network calls unless explicitly for CI/dev-only tools.

  Files to consult when editing audio or UI behavior

   - www/index.html — audio element IDs and overall markup.
   - www/app.js — audio engine, synth builders, UI wiring, presets, and persistence.
   - www/style.css — visual theming and layout.
   - android/ — native Android wrapper (open in Android Studio after npx cap sync android).

  AI / assistant config files

   - No CLAUDE.md, .cursorrules, AGENTS.md, .windsurfrules, CONVENTIONS.md, AIDER_CONVENTIONS.md, or .clinerules were found in this repository at scan time.

  -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

  If anything here looks off or you want additional coverage (for example: contributing guidelines, release steps, CI, or testing examples), say which area to expand and Copilot
  will add it.