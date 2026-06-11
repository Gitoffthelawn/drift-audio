# Claude Code Brief — Drift Sound Catalogue Reorganization

**For: Claude Code (VS Code, working on `C:\dev\DRIFT`)**
**Workflow: READ-FIRST. Do not move, copy, create, or delete anything until I approve the plan.**

---

## What I want

Reorganize Drift's sound assets into a clean **folder-per-sound catalogue** so that originals, processed files, and license info live together, and there is never confusion about which sounds are in the app versus archived.

You have an advantage the planning docs didn't: you can see the real filesystem. Where this brief says "(verify)" or "SOURCE FILE NEEDED," that's a gap I want you to *fill from what's actually on disk* — real filenames, real locations, real embedded-vs-file status.

---

## Step 1 — READ ONLY. Report, change nothing.

Before touching anything, investigate and report back:

1. **List the real contents** of `C:\dev\DRIFT\sounds\` (and `C:\dev\DRIFT\my-app\www\sounds\` if it exists) — actual filenames, formats, sizes.
2. **Identify base64-embedded audio** in `www/index.html`: which sounds exist only as embedded base64 vs. which have real files on disk. (Do NOT modify index.html — it's protected. Just report.)
3. **Check git status** — is the repo clean/committed? If not, tell me, because I want a clean commit before any file moves.
4. **Propose a file mapping**: for each sound below, which real file(s) → which `original/` or `processed/` folder. Flag any sound where you can't find a source file.

Stop after step 1 and show me the mapping. I'll approve before you proceed.

---

## Step 2 — After I approve: build the structure

Target layout (under `C:\dev\DRIFT\sounds\`):

```
sounds/
├── CATALOGUE.md                    ← master index (you generate)
├── planetside/<id>/
│   ├── original/                   ← untouched source files
│   ├── processed/                  ← ffmpeg output the app ships
│   ├── LICENSE.txt                 ← per data below
│   └── notes.md                    ← per data below
├── space/<id>/
│   └── ...
└── _archive/<id>/                  ← CUT sounds: kept, clearly out of the app
    └── ... (same 4 items)
```

**Rules:**
- **Copy, don't move,** on the first pass (or confirm git is committed first). Originals must stay recoverable.
- Sounds marked **CUT** go under `_archive/<id>/`, not their category folder.
- `original/` = untouched source as downloaded. `processed/` = ffmpeg segments the app ships.
- Don't overwrite any file I've already edited without asking.
- Use the per-sound license/EQ data below verbatim for `LICENSE.txt` and `notes.md`; supplement with real filenames you found.

---

## Per-sound data (authoritative — from project context)

Status: **IN APP** = shipped · **CANDIDATE** = under review · **CUT** = archived
Type: **REC** = recording · **SYN** = synth

### PLANETSIDE

| id | name | status | type | license | source / author | URL | attribution | EQ notes |
|---|---|---|---|---|---|---|---|---|
| `rain` | Rain | IN APP | REC | CC0 | Muges/ambientsounds | github.com/Muges/ambientsounds | none (CC0) | v2: consolidate into one source + 4 perspective pills |
| `heavyrain` | Downpour | CUT | REC | CC0 | Muges | github.com/Muges/ambientsounds | none (CC0) | v2: cut, covered by Rain pills |
| `thunder` | Thunder | IN APP | REC | CC0 | Muges | github.com/Muges/ambientsounds | none (CC0) | Double HPF 70+95Hz; -4dB @180Hz. NOT Sam80 #118765 (CC BY-NC, excluded) |
| `brook` | Brook | IN APP | REC | CC0 | Muges | github.com/Muges/ambientsounds | none (CC0) | -2.5dB @320Hz; +1.5dB @2.2kHz. v2: keep |
| `fire` | Fireplace | IN APP | REC | CC0 | Muges | github.com/Muges/ambientsounds | none (CC0) | +2dB @200Hz; -2dB above 5kHz. v2: keep |
| `wind` | Wind | IN APP | REC | CC0 | Muges | github.com/Muges/ambientsounds | none (CC0) | Double HPF 100+130Hz (rumble). v2: keep |
| `forestrain` | Light Rain | CUT | REC | CC BY 4.0 | tim.kahn / Freesound #169031 | freesound.org/people/tim.kahn/sounds/169031/ | "Light Forest Rain" by tim.kahn, CC BY 4.0 | v2: folded into Rain; seek CC0 equivalent |
| `creek` | Creek | IN APP | REC | CC BY 3.0 | flood-mix / Freesound #413325 | freesound.org/people/flood-mix/sounds/413325/ | "Gentle Creek in Rain Forest with Cicadas" by flood-mix, CC BY 3.0 | rainforest creek w/ cicadas |
| `ocean` | Ocean | CUT | SYN | N/A (algorithmic) | oxy | — | none | dual brown-noise LFO + foam. v2: cut, too similar to Brook |
| `birds` | Birds | IN APP | SYN | N/A | oxy | — | none | formant synth, 5 species. v2: Daytime layer of Forest biome |
| `crickets` | Crickets | IN APP | SYN | N/A | oxy | — | none | detuned AM stridulation. v2: Night layer of Forest biome |
| `whitenoise` | White Noise | IN APP | SYN | N/A | oxy | — | none | looping white noise buffer |

### SPACE

| id | name | status | type | license | source / author | URL | attribution | EQ notes |
|---|---|---|---|---|---|---|---|---|
| `voidDrone` | Void Drone | CANDIDATE | SYN | N/A | oxy | — | none | low harmonic. v2: ASSESS — may keep as bass layer |
| `staticField` | Static Field | CANDIDATE | SYN | N/A | oxy | — | none | hull spectrum. v2: ASSESS vs Warp, maybe redundant |
| `enginerumble` | Engine Rumble | CUT | REC | CC0 (verify) | Freesound (verify URL) | **find on disk** | none (CC0) — VERIFY | restored v0.2.0. v2: merge into Propulsion. **SOURCE FILE NEEDED** |
| `rocketthruster` | Rocket Thruster | CUT | REC | CC0 (verify) | Freesound (verify URL) | **find on disk** | none (CC0) — VERIFY | restored v0.2.0. v2: merge into Propulsion or cut. **SOURCE FILE NEEDED** |

> Note: I changed Void Drone and Static Field to **CANDIDATE** (not IN APP) — the rework plan marks them "ASSESS." Put them in their `space/` folders for now, but flag in notes that their fate is undecided.

---

## `LICENSE.txt` template (per sound)

```
SOUND:        <name>  (id: <id>)
CATEGORY:     <category>
TYPE:         <REC|SYN>
STATUS:       <IN APP|CANDIDATE|CUT>

SOURCE:       <source>
AUTHOR:       <author>
LICENSE:      <license>
URL:          <url — fill real one if found on disk>

ATTRIBUTION (use verbatim if credit required):
<attribution string, or "None required (CC0)">

REAL FILES FOUND:
<list the actual filenames you placed in original/ and processed/>

----------------------------------------------------------------------
CC0 = no credit. CC BY = credit required. CC BY-NC = DO NOT USE (FOSS-incompatible).
```

## `notes.md` template (per sound)

```
# <name>

- ID: `<id>` · Category: <category> · Type: <REC|SYN> · Status: <status> · License: <license>

## Description / plan
<the v2 plan note from the table>

## Processing recipe / EQ
<EQ notes from table>
Standard pipeline: original WAV → ffmpeg (HPF/EQ/compression) → 3 segments →
loudnorm -16 LUFS → acrossfade loop bake (2s) → mono MP3 64kbps.

## Files
- original/ — <real filenames, or "SOURCE FILE NEEDED">
- processed/ — <real filenames, or empty>

## Status log
- <today's date> — folder created, files sorted by Claude Code.
```

---

## `CATALOGUE.md` (you generate)

A master table: Name | id | Category | Type | Status | License | Folder path.
Then a "Attribution required (CC BY)" section listing the tim.kahn and flood-mix strings.
Then an "Outstanding" section listing: SOURCE-FILE-NEEDED sounds, the synth pre-render decision, Void Drone / Static Field undecided status, and a pointer to `SOUND_REWORK_PLAN.md` for the full v2 roster.

---

## Step 3 — After files are sorted, report

Tell me:
- Which sounds got real files and which are still missing a source.
- Any base64-only sounds that need extracting from index.html (separate task — don't do it yet).
- Anything in the data above that contradicts what you found on disk (e.g. a file with a different license than I listed).

Do NOT edit index.html, the crossfade engine, or any app code. This task is purely the `sounds/` catalogue.
```
```
