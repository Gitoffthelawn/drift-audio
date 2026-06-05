# DRIFT – Claude Code instructions

## Project overview
Capacitor 8 Android ambient soundscape app. Vanilla JS/HTML/CSS web app in `android/app/src/main/assets/public/` (web assets committed directly, no build step). Wrapped for Android via Capacitor in `android/`.

See `.github/copilot-instructions.md` for full architecture, audio model, and naming conventions.

## F-Droid release workflow

Before tagging a release, confirm together:
- Is the change user-facing and stable enough to ship?
- Have `versionCode` (integer) and `versionName` (string) been incremented in `android/app/build.gradle`?

If yes, tag and push:
```bash
git tag <versionName>        # e.g. git tag 0.2.0
git push origin <versionName>
```

F-Droid's bot detects the new tag via `UpdateCheckMode: Tags`, updates the fdroiddata YAML automatically, and queues a build. No manual MR needed after the initial submission.

**Rules:**
- Always increment `versionCode` on every release (F-Droid uses this to detect updates)
- Tag name must match `versionName` in `build.gradle`
- The fdroiddata metadata `commit:` field must be the **full 40-character commit hash** (`git rev-parse HEAD`), never a short hash or tag name
- The fdroiddata metadata lives at `gitlab.com/probably-oxy/fdroiddata` on the `add-drift-audio` branch (merged after initial acceptance)

## Android build config
- AGP 8.9.1, Gradle 8.11.1, compileSdk 36, minSdk 24
- No signing config in `build.gradle` — F-Droid signs with its own key
- `node_modules/` is gitignored; F-Droid's build server runs `npm install` via the metadata `init:` field before Gradle
