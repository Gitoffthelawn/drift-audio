# Drift — Development Tools

This directory contains tools used during Drift's development. None of the
files here ship with the Android app; they are design and experimentation
utilities only.

## sound_lab.html

A standalone workbench for designing synthesised sounds. Open the file in
Chrome (or any Chromium-based browser) — it runs entirely in the browser
with no server or build step required.

The lab provides:

- Live-adjustable parameter sliders for every synth patch in Drift
  (Ocean, Birds, Crickets, White Noise, Void Drone, Static Field,
  Hypersleep, Warp Drive)
- Per-patch signal meters
- Local persistence of tweaks between sessions
- Named preset save/load for multi-patch mixes
- JSON export/import for sharing configurations

### Usage

1. Open `sound_lab.html` in Chrome.
2. Click the play button on any patch card to start that synth.
3. Tweak sliders — changes apply in real time without restarting playback.
4. Save named presets for configurations you want to return to.
5. When you find values you want to ship, export JSON and transfer the
   relevant numbers into `www/app.js` by hand.

### Current status

The lab is **not yet integrated** with the shipping app. It is a separate
standalone tool. Planned integration (post-v1):

1. Refactor `app.js` to extract synth parameters into a clearly-marked
   config object.
2. Extend the lab to read/write `app.js` via the File System Access API.
3. Eventually, incorporate a simplified sound lab as an in-app screen.

### Licence

The lab shares Drift's MIT licence. It is intended as a useful reference
for anyone forking or studying the project, not as a shipping feature.


## process_audio.sh

Batch ffmpeg pipeline for processing raw source recordings into
Drift-ready MP3 segments. Run in WSL2 (Ubuntu) — not native Windows.

### Usage

```bash
# Copy source files into tools/ with the expected names:
# rain_source.wav, marswind_source.wav, whale_source.wav,
# underwater_source.wav, apollo_comms_source.wav, ventilation_source.wav

bash process_audio.sh

# Outputs land in www/sounds/ as aud-*.mp3
# Script skips missing source files and prints a license checklist
```

### Source file naming

| Source file | Sound | Expected origin |
|---|---|---|
| rain_source.wav | Rain | CC0 Freesound |
| marswind_source.wav | Mars Wind | NASA InSight, public domain |
| whale_source.wav | Space Whale | NOAA/NPS/MBARI, public domain |
| underwater_source.wav | Space Whale bed | Tim_Verberne CC0 |
| apollo_comms_source.wav | Radio Chatter | NASA Apollo, public domain |
| ventilation_source.wav | Propulsion bed | CC0 Freesound |

### Requirements
- WSL2 (Ubuntu) with ffmpeg and rubberband-cli installed
- See setup notes below