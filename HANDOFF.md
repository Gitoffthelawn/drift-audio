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