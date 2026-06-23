#!/usr/bin/env bash
# tools/process_audio.sh
# Audio processing pipeline for Drift v2 sound assets.
#
# Requirements: ffmpeg (https://ffmpeg.org/)
#               rubberband-cli for whale pitch/stretch (https://breakfastquay.com/rubberband/)
#   macOS:  brew install ffmpeg rubberband
#   Linux:  sudo apt-get install ffmpeg rubberband-cli
#   Windows: use WSL or Git Bash with the above installed
#
# Usage: ./tools/process_audio.sh [source_dir] [output_dir]
#   source_dir  directory containing raw source WAV files  (default: .)
#   output_dir  where to write aud-*.mp3 files             (default: sounds_out)
#
# After processing, copy the output segments into the native app's raw
# resources (rename aud-<id>-<n>.mp3 -> aud_<id>_<n>.mp3, hyphens to
# underscores) and commit them to git:
#   app/app/src/main/res/raw/

set -euo pipefail
SRCDIR="${1:-.}"
OUTDIR="${2:-sounds_out}"
mkdir -p "$OUTDIR"

LUFS="-16"
BITRATE="64k"

# ─── Helper: extract_segment <src> <id> <seg_index> <offset_secs> [extra_af] ──
# Extracts a 30 s segment, applies 2 s fade in/out for seamless looping,
# normalises to LUFS target, and encodes as mono MP3.
extract_segment() {
  local SRC="$1" ID="$2" IDX="$3" OFF="$4" EXTRA="${5:-}"
  local BASE_AF="afade=t=in:d=2:curve=tri,afade=t=out:st=28:d=2:curve=tri,loudnorm=I=${LUFS}:LRA=11:TP=-1.5"
  local AF="${EXTRA:+${EXTRA},}${BASE_AF}"
  local OUT="$OUTDIR/aud-${ID}-${IDX}.mp3"
  ffmpeg -y -i "$SRC" -ss "$OFF" -t 30 \
    -af "$AF" \
    -acodec libmp3lame -b:a "$BITRATE" -ac 1 \
    "$OUT"
  echo "  ✓  $OUT"
}

# ─── Rain (CC0 source) ─────────────────────────────────────────────────────────
# Target: long stationary recording, no animals/vehicles, good low end.
# Good sources on Freesound (search "rain steady CC0"):
#   freesound.org — filter by CC0, length > 3 min, search "rain no birds"
#   freesound.org/people/felix.blume/ has several long CC0 rain recordings
# Name the downloaded file: $SRCDIR/rain_source.wav
echo
echo "=== Rain ==="
if [ -f "$SRCDIR/rain_source.wav" ]; then
  extract_segment "$SRCDIR/rain_source.wav"  rain  0   0   "highpass=f=60"
  extract_segment "$SRCDIR/rain_source.wav"  rain  1   65  "highpass=f=60"
  extract_segment "$SRCDIR/rain_source.wav"  rain  2   130 "highpass=f=60"
else
  echo "  ⚠  missing: $SRCDIR/rain_source.wav"
  echo "     Download a CC0 rain recording (3+ min, no animals) from Freesound"
fi

# ─── Mars Wind (NASA InSight lander) ───────────────────────────────────────────
# Source: NASA InSight SEIS/apss wind recordings — public domain (US govt work)
# Download options:
#   https://mars.nasa.gov/insight/multimedia/sounds/
#   https://www.jpl.nasa.gov/news/sounds-of-mars (linked WAVs)
#   doi.org/10.17189/1506849 (PDS archive, full dataset)
# The "Sol 1" wind sample is longest and cleanest.
# Name the downloaded file: $SRCDIR/marswind_source.wav
echo
echo "=== Mars Wind ==="
if [ -f "$SRCDIR/marswind_source.wav" ]; then
  AF="lowpass=f=1200,acompressor=threshold=-20dB:ratio=3:attack=50:release=300"
  extract_segment "$SRCDIR/marswind_source.wav"  marswind  0   0   "$AF"
  extract_segment "$SRCDIR/marswind_source.wav"  marswind  1   35  "$AF"
  extract_segment "$SRCDIR/marswind_source.wav"  marswind  2   70  "$AF"
else
  echo "  ⚠  missing: $SRCDIR/marswind_source.wav"
  echo "     Download from mars.nasa.gov/insight/multimedia/sounds/"
fi

# ─── Space Whale (NOAA/NPS humpback recordings) ────────────────────────────────
# Source: public domain NOAA / NPS humpback whale song recordings.
# Preferred sources (in order):
#   archive.org/details/WhaleSong_928                           (NOAA)
#   archive.org/details/HumpbackWhalesSongsSoundsVocalizations  (NPS Glacier Bay)
#   archive.org/details/humpbackwhalesongs                      (MBARI)
# AVOID: Roger Payne "Songs of the Humpback Whale" — CRM Records, copyrighted.
# AVOID: Any Pixabay or Mixkit source — not FOSS-redistribution-compatible.
#
# Processing: pitch shift -1 octave + time stretch 2× via rubberband,
# then long reverb via delay, LP 4 kHz, gentle compression.
# Output segments become the recorded Space Whale layer (replacing its synth).
echo
echo "=== Space Whale ==="
if [ -f "$SRCDIR/whale_source.wav" ]; then
  if command -v rubberband &>/dev/null; then
    echo "  → pitch shift -12 st + time stretch 2× (rubberband)..."
    rubberband --pitch -12 --time 2.0 "$SRCDIR/whale_source.wav" /tmp/whale_shifted.wav
    AF="lowpass=f=4000,acompressor=threshold=-18dB:ratio=3:attack=100:release=500"
    extract_segment /tmp/whale_shifted.wav  spacewhale  0   0    "$AF"
    extract_segment /tmp/whale_shifted.wav  spacewhale  1   120  "$AF"
    extract_segment /tmp/whale_shifted.wav  spacewhale  2   240  "$AF"
    rm -f /tmp/whale_shifted.wav
  else
    echo "  ⚠  rubberband not found — install it for whale processing"
    echo "     macOS: brew install rubberband | Linux: apt install rubberband-cli"
  fi
else
  echo "  ⚠  missing: $SRCDIR/whale_source.wav"
  echo "     Download from archive.org/details/WhaleSong_928 (NOAA, public domain)"
fi

# ─── Underwater ambient (hydrophone bed for Space Whale) ───────────────────────
# Optional: provides the between-call ambient layer for Space Whale.
# Source: Tim_Verberne CC0 hydrophone recording
#   freesound.org/people/Tim_Verberne/sounds/482167/
# Name the downloaded file: $SRCDIR/underwater_source.wav
echo
echo "=== Underwater ambient (optional Space Whale bed) ==="
if [ -f "$SRCDIR/underwater_source.wav" ]; then
  AF="lowpass=f=2000,highpass=f=30"
  extract_segment "$SRCDIR/underwater_source.wav"  underwater  0   0   "$AF"
  extract_segment "$SRCDIR/underwater_source.wav"  underwater  1   90  "$AF"
  extract_segment "$SRCDIR/underwater_source.wav"  underwater  2   180 "$AF"
else
  echo "  ⚠  missing: $SRCDIR/underwater_source.wav (optional)"
  echo "     freesound.org/people/Tim_Verberne/sounds/482167/ (CC0)"
fi

# ─── Radio Chatter (NASA Apollo comms) ─────────────────────────────────────────
# Source: NASA Apollo mission communications — public domain (US govt work).
#   archive.org/details/nasaaudiocollection
# Target: clear voice transmissions with natural pauses.
# This processing chain adds telephone bandwidth and radio character.
# Burst gating (sparse transmissions) is a runtime behaviour to reapply.
# When real files are ready: add them to res/raw, set the Catalogue radio
# entry to type=REC with segmentCount=3, and wire AudioFiles.kt.
echo
echo "=== Radio Chatter ==="
if [ -f "$SRCDIR/apollo_comms_source.wav" ]; then
  # Telephone bandwidth BPF + subtle compression + level reduction (-12 dB)
  AF="bandpass=f=900:width_type=o:w=2,acompressor=threshold=-24dB:ratio=8:attack=5:release=100,volume=-12dB"
  extract_segment "$SRCDIR/apollo_comms_source.wav"  radio  0   0   "$AF"
  extract_segment "$SRCDIR/apollo_comms_source.wav"  radio  1   45  "$AF"
  extract_segment "$SRCDIR/apollo_comms_source.wav"  radio  2   90  "$AF"
else
  echo "  ⚠  missing: $SRCDIR/apollo_comms_source.wav"
  echo "     archive.org/details/nasaaudiocollection (public domain)"
fi

# ─── Propulsion ventilation layer (optional real-recording bed) ─────────────────
# Optional: adds realism under the Propulsion synth's oscillators.
# Search Freesound for: "ventilation hum CC0" / "server room CC0" / "engine room CC0"
# CC0 only — no CC BY-NC or "royalty free" sites.
# Name the downloaded file: $SRCDIR/ventilation_source.wav
echo
echo "=== Propulsion ventilation bed (optional) ==="
if [ -f "$SRCDIR/ventilation_source.wav" ]; then
  AF="lowpass=f=600,highpass=f=50,acompressor=threshold=-20dB:ratio=4:attack=20:release=200"
  extract_segment "$SRCDIR/ventilation_source.wav"  ventilation  0   0   "$AF"
  extract_segment "$SRCDIR/ventilation_source.wav"  ventilation  1   40  "$AF"
  extract_segment "$SRCDIR/ventilation_source.wav"  ventilation  2   80  "$AF"
else
  echo "  ⚠  missing: $SRCDIR/ventilation_source.wav (optional)"
  echo "     Search freesound.org for 'ventilation hum CC0' or 'engine room CC0'"
fi

echo
echo "================================================================"
echo "Done. Output in: $OUTDIR/"
echo
echo "Next steps:"
echo "  1. Review each file — listen for clipping, background noise, level"
echo "  2. Copy segments into app/app/src/main/res/raw/ (aud_<id>_<n>.mp3)"
echo "  3. Wire them in app/.../audio/AudioFiles.kt and the Catalogue entry"
echo "     (set segmentCount, type=REC); SYN layers gain audio this way"
echo "  4. Commit sounds/ and the app changes together"
echo
echo "License checklist before committing:"
echo "  rain_source      — must be CC0 (check Freesound licence)"
echo "  marswind_source  — NASA/JPL public domain (US govt work) ✓"
echo "  whale_source     — NOAA/NPS public domain ✓ (avoid Roger Payne)"
echo "  underwater       — Tim_Verberne CC0 on Freesound ✓"
echo "  apollo_comms     — NASA public domain ✓"
echo "  ventilation      — must be CC0 (check Freesound licence)"
