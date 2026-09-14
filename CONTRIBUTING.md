# Contributing to Drift

Drift is a small, mostly solo project — issues and small PRs are welcome, but
please open an issue before starting anything larger than a bug fix so it
isn't wasted effort.

## Reporting bugs / requesting features

Use [GitHub Issues](https://github.com/probably-oxy/drift-audio/issues).
Useful details: Drift version (Settings → About, or the F-Droid/app listing),
Android version, and device model. For audio issues, note which sound(s),
which output mode (speaker/stereo/headphones), and roughly how long into
playback the problem shows up.

## Building

See [BUILDING.md](BUILDING.md) for setup and build steps.

## Code contributions

- Kotlin + Jetpack Compose + Media3, plain Gradle — no other frameworks.
- Follow the existing style in the file you're editing; there's no linter
  gate beyond Android Lint.
- Keep PRs focused. A bug fix shouldn't carry an unrelated refactor.
- The `SoundSource` interface is the one deliberate abstraction point (for a
  future synthesis backend); don't add other abstractions speculatively.

## Suggesting a new sound

The full sound-sourcing workflow (original recordings, per-sound license
files, the ffmpeg processing pipeline in `sounds/`/`tools/`) is kept locally
and isn't part of this public repo, so it isn't something you can pick up
directly from a clone. If you have a good CC0 or public-domain recording in
mind, open an issue with a link and a description rather than sending a PR
with new files under `res/raw/` — new audio needs to go through licensing
review and the processing pipeline first.

If you're touching an *existing* shipped sound's metadata (e.g. `Catalogue.kt`
licensing fields, `CREDITS.md`), keep it consistent with what's actually
credited in-app — `CREDITS.md` is the file most likely to drift out of sync
with `Catalogue.kt`, so double-check both when either changes.

## What not to touch without discussion first

- `sounds/**/original/` — untouched source files; never edit or replace these
  in place, add a new dated version instead.
- Architecture decisions already made and documented in the codebase (e.g.
  the preset model deliberately excludes output mode, `SoundSource` is the
  only synthesis-ready seam). If you think one of these is wrong, open an
  issue to discuss before sending a PR that changes it.

## License

By contributing, you agree your contribution is licensed under the project's
[MIT license](LICENSE). Audio contributions must be under a license
compatible with the terms in `sounds/CATALOGUE.md` above.
