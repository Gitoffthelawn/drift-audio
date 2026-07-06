# Drift — Development Tools

This directory contains tooling used during Drift's development. Nothing here
ships with the Android app; these are audio-processing and experimentation
utilities only.

## process_audio.sh

Batch ffmpeg pipeline for processing raw source recordings into
Drift-ready MP3 segments. Run in WSL2 (Ubuntu) — not native Windows.

### Usage

```bash
# Copy source files into tools/ with the expected names:
# rain_source.wav, marswind_source.wav, whale_source.wav,
# underwater_source.wav, apollo_comms_source.wav, ventilation_source.wav

bash process_audio.sh

# Outputs land in tools/sounds_out/ as aud-*.mp3; copy the segments you want to
# ship into app/app/src/main/res/raw/ (named aud_<id>_<n>.mp3, hyphens→underscores)
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