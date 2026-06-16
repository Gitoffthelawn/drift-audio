# Drift — Claude Code instructions

## What this project is
Ambient soundscape mixer for sleep (Android, F-Droid). Currently migrating
from Capacitor/WebView to native Kotlin + Jetpack Compose + Media3.
Migration plan: docs/MIGRATION_PLAN.md — follow its phase order.

## Workflow rules (always)
- READ-FIRST: investigate and show me a plan before modifying anything.
  No file changes without my explicit approval.
- Explain as you build: I'm learning Kotlin/Android from scratch. For each
  piece of code, briefly explain what it does and why it's structured that way.
- Small steps: one phase sub-step at a time. Stop at each checkpoint.
- Never commit without asking. Never push.

## Branches
- `native` = the rebuild (work here). `main` = shipping Capacitor app (do not touch).

## Protected / do not modify
- www/ and android/ (the entire Capacitor app — it ships from main until native v1)
- sounds/**/original/ (untouched source files)
- android/app/src/main/assets/public/sounds/ (live app's audio)

## Architecture decisions (already made — do not relitigate)
- SoundSource interface; FileSoundSource is the only v1 implementation.
  SynthSoundSource (Oboe) is future — design the interface for it, don't build it.
- Sound data class: NO single category field. Tags come later (3 axes:
  texture/mood/environment) — leave a tags placeholder.
- Presets: source IDs + variant + volume. Output mode is NOT part of a preset.
- Audio: Media3/ExoPlayer, MediaSessionService for background playback,
  one player per active layer, files in res/raw/.
- Package ID stays io.github.probably_oxy.drift. Tags stay v-prefixed.

## Environment
- Windows, repo root C:\dev\DRIFT. Native project lives in app/.
- Test device: OnePlus CPH2653, Android 16, arm64.
- Build: Android Studio / Gradle. git identity: oxy (GitHub noreply email).
- compileSdk = 37 (plain major version, no minorApiLevel DSL), targetSdk 36, minSdk 26.

## UI rules (for Phase 3, later)
- Cockpit aesthetic: dark, amber/green phosphor, JetBrains Mono.
- Colorblind-safe: state never by color alone — border + text + animation.

## Handoff workflow
- Qwen (Continue/Ollama) and Copilot handle minor edits during Claude breaks.
- At session start, read `HANDOFF.md` to catch up on their changes.
- At session end, append a dated entry to the TOP of `HANDOFF.md`.
- Treat their edits as untrusted-but-well-meaning: skim `git diff` for scope creep
  (whole-file reformats, sneaky remote links, recolored theme) before building on them.
