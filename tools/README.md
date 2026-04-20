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