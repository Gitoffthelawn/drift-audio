const ICONS = {
  rain: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><line x1="6" y1="4" x2="4" y2="20"/><line x1="12" y1="4" x2="10" y2="20"/><line x1="18" y1="4" x2="16" y2="20"/></svg>`,
  heavyrain: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="square"><line x1="5" y1="2" x2="3" y2="22"/><line x1="10" y1="2" x2="8" y2="22"/><line x1="15" y1="2" x2="13" y2="22"/><line x1="20" y1="2" x2="18" y2="22"/></svg>`,
  forestrain: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><line x1="6" y1="3" x2="4" y2="8"/><line x1="12" y1="3" x2="10" y2="8"/><line x1="18" y1="3" x2="16" y2="8"/><path d="M3 13 Q 9 10, 12 13 Q 16 16, 21 13"/><path d="M3 18 Q 9 15, 12 18 Q 16 21, 21 18"/></svg>`,
  creek: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><path d="M3 10 Q 8 6, 12 10 T 21 10"/><path d="M3 16 Q 8 12, 12 16 T 21 16"/></svg>`,
  brook: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><path d="M3 13 Q 9 9, 12 13 T 21 13"/></svg>`,
  ocean: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><path d="M3 9 Q 8 5, 12 9 T 21 9"/><path d="M3 14 Q 8 10, 12 14 T 21 14"/><path d="M3 19 Q 8 15, 12 19 T 21 19"/></svg>`,
  thunder: `<svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor" stroke="currentColor" stroke-width="1.5" stroke-linecap="square" stroke-linejoin="miter"><polygon points="10,2 5,13 10,13 8,22 15,11 10,11 12,2"/></svg>`,
  fire: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square" stroke-linejoin="miter"><polyline points="3,22 7,12 11,16 15,5 18,13 21,22"/></svg>`,
  wind: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><path d="M3 8 Q 10 5, 17 8 T 21 8"/><path d="M3 14 Q 10 11, 17 14 T 21 14"/><path d="M3 20 Q 10 17, 17 20 T 21 20"/></svg>`,
  birds: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><path d="M3 15 L 6 11 L 9 15"/><path d="M15 15 L 18 11 L 21 15"/><path d="M7 11 Q 9 7, 12 7 Q 15 7, 17 11"/><circle cx="11" cy="10" r="0.8" fill="currentColor"/></svg>`,
  crickets: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><line x1="4" y1="7" x2="8" y2="7"/><line x1="10" y1="7" x2="14" y2="7"/><line x1="16" y1="7" x2="20" y2="7"/><line x1="3" y1="12" x2="6" y2="12"/><line x1="8" y1="12" x2="15" y2="12"/><line x1="17" y1="12" x2="21" y2="12"/><line x1="4" y1="17" x2="9" y2="17"/><line x1="11" y1="17" x2="13" y2="17"/><line x1="15" y1="17" x2="20" y2="17"/></svg>`,
  white: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><line x1="3" y1="5" x2="21" y2="5"/><line x1="3" y1="9" x2="21" y2="9"/><line x1="3" y1="13" x2="21" y2="13"/><line x1="3" y1="17" x2="21" y2="17"/><line x1="3" y1="21" x2="21" y2="21"/></svg>`,
  interstellarplasma: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><circle cx="12" cy="12" r="9"/><circle cx="12" cy="12" r="5"/><circle cx="12" cy="12" r="1.5" fill="currentColor"/></svg>`,
  enginerumble: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><rect x="4" y="7" width="3" height="10"/><rect x="10" y="4" width="3" height="16"/><rect x="16" y="9" width="3" height="6"/></svg>`,
  rocketfiring: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><rect x="6" y="3" width="4" height="3"/><rect x="14" y="3" width="4" height="3"/><rect x="6" y="18" width="4" height="3"/><rect x="14" y="18" width="4" height="3"/><line x1="8" y1="6" x2="8" y2="10"/><line x1="16" y1="6" x2="16" y2="10"/><line x1="8" y1="14" x2="8" y2="18"/><line x1="16" y1="14" x2="16" y2="18"/><rect x="9" y="10" width="6" height="4"/></svg>`,
  rocketthruster: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square" stroke-linejoin="miter"><polygon points="8,3 16,3 14,14 10,14"/><polyline points="9,14 7,19 12,22 17,19 15,14"/><line x1="12" y1="15" x2="12" y2="22" stroke-width="0.8"/></svg>`,
  planetside: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><circle cx="12" cy="12" r="9"/><path d="M3 12 Q 9 7, 15 12 T 21 12"/><line x1="3" y1="12" x2="21" y2="12"/></svg>`,
  deepspace: `<svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor" stroke="currentColor" stroke-width="1.5"><circle cx="6" cy="6" r="1"/><circle cx="18" cy="5" r="1"/><circle cx="19" cy="17" r="1"/><circle cx="4" cy="19" r="1"/><circle cx="13" cy="12" r="1"/><circle cx="9" cy="15" r="0.7"/><circle cx="16" cy="10" r="0.7"/><circle cx="11" cy="5" r="0.7"/></svg>`,
  voidDrone: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><path d="M3 12 C 7 2, 17 22, 21 12"/></svg>`,
  hypersleep: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><path d="M7 7 L 17 7 L 7 17 L 17 17"/></svg>`,
  warpDrive: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><circle cx="12" cy="12" r="2"/><line x1="12" y1="2" x2="12" y2="8"/><line x1="12" y1="16" x2="12" y2="22"/><line x1="2" y1="12" x2="8" y2="12"/><line x1="16" y1="12" x2="22" y2="12"/><line x1="5.5" y1="5.5" x2="8.8" y2="8.8"/><line x1="15.2" y1="15.2" x2="18.5" y2="18.5"/><line x1="18.5" y1="5.5" x2="15.2" y2="8.8"/><line x1="8.8" y1="15.2" x2="5.5" y2="18.5"/></svg>`,
  warp: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><circle cx="12" cy="12" r="2"/><line x1="12" y1="2" x2="12" y2="8"/><line x1="12" y1="16" x2="12" y2="22"/><line x1="2" y1="12" x2="8" y2="12"/><line x1="16" y1="12" x2="22" y2="12"/><line x1="5.5" y1="5.5" x2="8.8" y2="8.8"/><line x1="15.2" y1="15.2" x2="18.5" y2="18.5"/><line x1="18.5" y1="5.5" x2="15.2" y2="8.8"/><line x1="8.8" y1="15.2" x2="5.5" y2="18.5"/></svg>`,
  propulsion: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><path d="M5 9 L5 15 L14 17 L14 7 Z"/><line x1="14" y1="9" x2="20" y2="6"/><line x1="14" y1="12" x2="22" y2="12"/><line x1="14" y1="15" x2="20" y2="18"/></svg>`,
  spaceWhale: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><path d="M2 15 C 6 8, 11 8, 14 13 Q 18 17 22 11"/><line x1="19" y1="9" x2="22" y2="11"/><line x1="22" y1="11" x2="19" y2="13"/></svg>`,
  radio: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><circle cx="12" cy="13" r="1.5" fill="currentColor"/><path d="M9 10 Q 7 13 9 16"/><path d="M15 10 Q 17 13 15 16"/><path d="M6 7 Q 2 13 6 19"/><path d="M18 7 Q 22 13 18 19"/></svg>`,
  forest: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><path d="M12 3 L5 13 L9 13 L7 19 L17 19 L15 13 L19 13 Z"/><line x1="12" y1="19" x2="12" y2="22"/></svg>`,
  marswind: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><path d="M2 15 Q 7 11 12 14 Q 17 17 22 13"/><path d="M5 10 Q 8 8 11 10"/><circle cx="18" cy="7" r="2.5"/></svg>`,
  _default: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><path d="M3 12 Q 8 8, 12 12 T 21 12"/><path d="M3 16 Q 8 12, 12 16 T 21 16"/></svg>`,
};
function getIcon(id){ return ICONS[id] ?? ICONS._default; }

const CATEGORIES = [
  {id:"planetside", label:"Planetside", sounds:[
    {id:"rain",    label:"Rain",      desc:"Forest rain",    real:true,  segs:3,
      variants:['openair','ondeck','below','distant'], currentVariant:'openair',
      variantLabels:{openair:'AIR',ondeck:'DECK',below:'HULL',distant:'FAR'}},
    {id:"brook",   label:"Brook",     desc:"Flowing stream", real:true,  segs:3},
    {id:"fire",    label:"Fireplace", desc:"Crackling fire", real:true,  segs:3},
    {id:"wind",    label:"Wind",      desc:"Forest wind",    real:true,  segs:3},
    {id:"forest",  label:"Forest",    desc:"Biome ambience", real:false, segs:0,
      variants:['day','dusk','night'], currentVariant:'dusk'},
  ]},
  {id:"deepspace", label:"Deep Space", sounds:[
    {id:"voidDrone",         label:"Void Drone",          desc:"Low harmonic stack",       real:false, segs:0},
    {id:"white",             label:"Static Field",        desc:"Resonant hull noise",      real:false, segs:0},
    {id:"interstellarplasma",label:"Interstellar Plasma", desc:"Voyager 1 plasma waves",   real:true,  segs:3,
      sparse:true, burstSegs:[0,1], silentSegs:[2], eventRate:0.5,
      variants:['raw','drift'], currentVariant:'raw', variantLabels:{raw:'RAW',drift:'DRIFT'}},
    {id:"marswind",          label:"Mars Wind",           desc:"InSight lander recording", real:true,  segs:3},
    {id:"propulsion",        label:"Propulsion",          desc:"Ship engine drone",        real:false, segs:0,
      variants:['idle','cruise','burn'], currentVariant:'cruise'},
    {id:"warp",              label:"Warp",                desc:"Warp transit hum",         real:false, segs:0,
      variants:['engage','transit'], currentVariant:'engage'},
    {id:"spaceWhale",        label:"Space Whale",         desc:"Something vast nearby",    real:true,  segs:3,
      variants:['rare','normal','busy'], currentVariant:'normal'},
    {id:"radio",             label:"Radio Chatter",       desc:"Deep space comms",         real:false, segs:0,
      variants:['distant','near'], currentVariant:'distant'},
  ]},
];
const SOUNDS = CATEGORIES.flatMap(c => c.sounds.map(s => ({...s, cat:c.id})));

const PRESETS = [
  {name:"Hull Storm",    layers:{"rain":0.65,"propulsion":0.55,"wind":0.3},                  variants:{"rain":"below","propulsion":"cruise"}},
  {name:"Planetfall",    layers:{"forest":0.6,"wind":0.22,"brook":0.45},                     variants:{"forest":"dusk"}},
  {name:"Long Haul",     layers:{"propulsion":0.55,"interstellarplasma":0.4,"radio":0.3},    variants:{"propulsion":"cruise","interstellarplasma":"drift","radio":"distant"}},
  {name:"Contact",       layers:{"spaceWhale":0.65,"interstellarplasma":0.45,"radio":0.35},  variants:{"spaceWhale":"normal","interstellarplasma":"raw","radio":"distant"}},
  {name:"Green Sector",  layers:{"forest":0.5,"brook":0.4,"rain":0.35},                      variants:{"forest":"day","rain":"openair"}},
  {name:"Canopy Dark",   layers:{"forest":0.55,"rain":0.45,"brook":0.35},                    variants:{"forest":"night","rain":"below"}},
  {name:"Warp Corridor", layers:{"warp":0.6,"radio":0.3},                                    variants:{"warp":"transit","radio":"distant"}},
  {name:"Drift Orbit",   layers:{"propulsion":0.4,"marswind":0.35,"spaceWhale":0.5},          variants:{"propulsion":"idle","spaceWhale":"rare"}},
];

// ─── State ────────────────────────────────────────────────────────────────────
const active  = new Set();
const volumes = Object.fromEntries(SOUNDS.map(s => [s.id, 0.5]));
let timerMins=0, timerInterval=null, timerSecs=0;
const synthLayers = {};
let outputMode = localStorage.getItem('drift_output_mode') || 'speaker';
let outputBus = null;
let modeNodes = [];

// ─── Persistence ──────────────────────────────────────────────────────────────
const STORAGE_KEY = 'drift_state';
const USER_PRESETS_KEY = 'drift_user_presets';
let userPresets = [];

function saveState() {
  try {
    const eventRates = {};
    SOUNDS.filter(s => s.sparse).forEach(s => { eventRates[s.id] = s.eventRate ?? 0.5; });
    const variantStates = {};
    SOUNDS.filter(s => s.variants).forEach(s => { variantStates[s.id] = s.currentVariant; });
    localStorage.setItem(STORAGE_KEY, JSON.stringify({
      volumes: volumes,
      active: [...active],
      eventRates: eventRates,
      variantStates: variantStates,
    }));
  } catch(e) {}
}
function setVariant(id, variant) {
  const s = SOUNDS.find(x => x.id === id);
  if (!s || !s.variants) return;
  s.currentVariant = variant;
  if (s.real) {
    if (id === 'rain') buildRainFilterChain(variant);
    else if (id === 'interstellarplasma') buildPlasmaFilterChain(variant);
  } else {
    if (active.has(id)) { deactivateSynth(id); activateSynth(id); }
  }
  saveState();
}

function loadState() {
  try {
    const saved = JSON.parse(localStorage.getItem(STORAGE_KEY));
    if (!saved) return;
    if (saved.volumes) {
      Object.entries(saved.volumes).forEach(([id, v]) => {
        if (volumes[id] !== undefined) volumes[id] = v;
      });
    }
    // Restore variants first so active sounds launch with the correct variant/perspective
    if (saved.variantStates) {
      Object.entries(saved.variantStates).forEach(([id, variant]) => {
        const s = SOUNDS.find(x => x.id === id);
        if (s && s.variants && s.variants.includes(variant)) s.currentVariant = variant;
      });
    }
    if (saved.eventRates) {
      Object.entries(saved.eventRates).forEach(([id, rate]) => {
        const s = SOUNDS.find(x => x.id === id);
        if (s && s.sparse) s.eventRate = rate;
      });
    }
    if (saved.active && Array.isArray(saved.active)) {
      saved.active.forEach(id => {
        const s = SOUNDS.find(x => x.id === id);
        if (s) { active.add(id); if (s.real) activateReal(id); else activateSynth(id); }
      });
    }
    // Update pill UI after DOM exists
    if (saved.eventRates) {
      Object.entries(saved.eventRates).forEach(([id, rate]) => {
        const group = document.querySelector(`.rate-pills[data-sound-id="${id}"]`);
        if (group) group.querySelectorAll('.rate-pill').forEach(p => p.classList.toggle('active', parseFloat(p.dataset.rate) === rate));
      });
    }
    if (saved.variantStates) {
      Object.entries(saved.variantStates).forEach(([id, variant]) => {
        const group = document.querySelector(`.rate-pills[data-sound-id="${id}"]`);
        if (group) group.querySelectorAll('.rate-pill[data-variant]').forEach(p => p.classList.toggle('active', p.dataset.variant === variant));
      });
    }
  } catch(e) {}
}

function saveUserPresets() {
  try { localStorage.setItem(USER_PRESETS_KEY, JSON.stringify(userPresets)); } catch(e) {}
}
function loadUserPresets() {
  try {
    const saved = JSON.parse(localStorage.getItem(USER_PRESETS_KEY));
    if (Array.isArray(saved)) userPresets = saved;
  } catch(e) {}
}

// ─── Interstellar Plasma DRIFT filter chain ───────────────────────────────────
let plasmaFilterOutput=null;
let plasmaFilterNodes=[];
function initPlasmaFilter(){
  const ctx=getCtx();
  if(!plasmaFilterOutput){
    plasmaFilterOutput=mkG(ctx,1);
    const s=SOUNDS.find(x=>x.id==='interstellarplasma');
    if(s){for(let i=0;i<s.segs;i++){const el=document.getElementById(`aud-interstellarplasma-${i}`);if(el)try{ctx.createMediaElementSource(el).connect(plasmaFilterOutput);}catch{}}}
  }
  const s=SOUNDS.find(x=>x.id==='interstellarplasma');
  buildPlasmaFilterChain(s?s.currentVariant:'raw');
}
function buildPlasmaFilterChain(mode){
  if(!plasmaFilterOutput||!audioCtx)return;
  plasmaFilterOutput.disconnect();
  plasmaFilterNodes.forEach(n=>{try{n.disconnect();}catch{}});
  plasmaFilterNodes=[];
  const ctx=audioCtx;
  if(mode==='drift'){
    const lp=mkF(ctx,'lowpass',1500);
    const delay=ctx.createDelay(1.0);delay.delayTime.value=0.45;
    const fb=mkG(ctx,.4);const lpFb=mkF(ctx,'lowpass',800);const wetG=mkG(ctx,.4);
    plasmaFilterOutput.connect(lp);
    lp.connect(delay);delay.connect(fb);fb.connect(lpFb);lpFb.connect(delay);
    delay.connect(wetG);wetG.connect(outputBus);
    lp.connect(outputBus);
    plasmaFilterNodes=[lp,delay,fb,lpFb,wetG];
  } else {
    plasmaFilterOutput.connect(outputBus);
  }
}

// ─── Real sound crossfade engine ──────────────────────────────────────────────
// Each real sound uses TWO audio elements (A/B), crossfading between segments.
// When segment A is ~3s from ending, B starts the next (random) segment and
// we fade A→0 while B→target_volume over 2.5s. Completely seamless.

const players = {}; // id -> {elA, elB, activeEl:'A'|'B', segIdx, xfTimer, targetVol}

function getEl(id, slot) {
  const p = players[id];
  const segIdx = slot === 'A' ? p.segIdxA : p.segIdxB;
  return document.getElementById(`aud-${id}-${segIdx}`);
}

function initPlayer(id) {
  const sound = SOUNDS.find(s=>s.id===id);
  if (!sound.real || !sound.segs) return;
  const segA = Math.floor(Math.random() * sound.segs);
  let segB = Math.floor(Math.random() * sound.segs);
  if (sound.segs > 1 && segB === segA) segB = (segA + 1) % sound.segs;
  players[id] = {
    segIdxA: segA, segIdxB: segB,
    active: 'A',
    xfTimer: null,
    targetVol: volumes[id],
  };
}

function startPlayer(id) {
  const p = players[id];
  const elA = document.getElementById(`aud-${id}-${p.segIdxA}`);
  elA.currentTime = 0;
  elA.volume = 0;
  const playPromise = elA.play();
  if (playPromise) playPromise.catch(()=>{});
  fadeElTo(elA, p.targetVol, 2000);
  scheduleXfade(id);
}

function scheduleXfade(id) {
  const p = players[id];
  if (!p) return;
  const sound = SOUNDS.find(s=>s.id===id);
  if (sound.segs <= 1) return; // single segment loops via HTML loop attribute
  const activeSegIdx = p.active === 'A' ? p.segIdxA : p.segIdxB;
  const el = document.getElementById(`aud-${id}-${activeSegIdx}`);
  const dur = el.duration || 24;
  const xfStart = Math.max(1, (dur - 3) * 1000);
  clearTimeout(p.xfTimer);
  p.xfTimer = setTimeout(() => doXfade(id), xfStart);
}

function pickNextSegment(sound) {
  if (sound.sparse) {
    const rate = sound.eventRate ?? 0.5;
    const pool = Math.random() < rate ? sound.burstSegs : sound.silentSegs;
    return pool[Math.floor(Math.random() * pool.length)];
  }
  // existing behaviour — random from 0..segs-1
  return Math.floor(Math.random() * sound.segs);
}

function doXfade(id) {
  const p = players[id];
  if (!p) return;
  const sound = SOUNDS.find(s=>s.id===id);
  const outSlot = p.active;
  const inSlot  = p.active === 'A' ? 'B' : 'A';
  const outSegIdx = outSlot === 'A' ? p.segIdxA : p.segIdxB;
  const inSegIdx  = inSlot  === 'A' ? p.segIdxA : p.segIdxB;
  let nextSeg = pickNextSegment(sound);
  if (!sound.sparse && sound.segs > 1 && nextSeg === outSegIdx) nextSeg = (nextSeg + 1) % sound.segs;
  if (inSlot === 'A') p.segIdxA = nextSeg;
  else p.segIdxB = nextSeg;
  const elOut = document.getElementById(`aud-${id}-${outSegIdx}`);
  const elIn  = document.getElementById(`aud-${id}-${nextSeg}`);
  elIn.currentTime = 0;
  elIn.volume = 0;
  const pp = elIn.play();
  if (pp) pp.catch(()=>{});
  const xfDur = 2500;
  fadeElTo(elIn, p.targetVol, xfDur);
  fadeElTo(elOut, 0, xfDur, () => {
    elOut.pause();
    elOut.currentTime = 0;
  });
  p.active = inSlot;
  scheduleXfade(id);
}

// Smooth volume fade using requestAnimationFrame
function fadeElTo(el, target, durationMs, onDone) {
  const start = performance.now();
  const from  = el.volume;
  const tick  = (now) => {
    const t = Math.min(1, (now - start) / durationMs);
    const ease = t < 0.5 ? 2*t*t : -1+(4-2*t)*t;
    el.volume = Math.max(0, Math.min(1, from + (target - from) * ease));
    if (t < 1) {
      requestAnimationFrame(tick);
    } else {
      el.volume = target;
      if (onDone) onDone();
    }
  };
  requestAnimationFrame(tick);
}

function activateReal(id) {
  if (!players[id]) initPlayer(id);
  players[id].targetVol = volumes[id];
  if (id === 'rain') initRainFilter();
  else if (id === 'interstellarplasma') initPlasmaFilter();
  startPlayer(id);
}

function deactivateReal(id) {
  const p = players[id];
  if (!p) return;
  clearTimeout(p.xfTimer);
  p.xfTimer = null;
  const sound = SOUNDS.find(s=>s.id===id);
  for (let i=0; i<sound.segs; i++) {
    const el = document.getElementById(`aud-${id}-${i}`);
    if (!el.paused) {
      fadeElTo(el, 0, 2000, () => { el.pause(); el.currentTime=0; });
    }
  }
}

function setRealVolume(id, v) {
  const p = players[id];
  if (!p) return;
  p.targetVol = v;
  const activeSegIdx = p.active === 'A' ? p.segIdxA : p.segIdxB;
  const el = document.getElementById(`aud-${id}-${activeSegIdx}`);
  if (!el.paused) fadeElTo(el, v, 300);
}

// ─── Synth helpers ────────────────────────────────────────────────────────────
const mkG=(ctx,v=1)=>{const g=ctx.createGain();g.gain.value=v;return g;};
const mkF=(ctx,t,f,q)=>{const n=ctx.createBiquadFilter();n.type=t;n.frequency.value=f;if(q!=null)n.Q.value=q;return n;};
const mkO=(ctx,f,t='sine')=>{const o=ctx.createOscillator();o.type=t;o.frequency.value=f;return o;};
const mkNoise=(ctx,fn,s)=>{const b=ctx.createBuffer(2,Math.floor(ctx.sampleRate*s),ctx.sampleRate);fn(b.getChannelData(0));fn(b.getChannelData(1));const src=ctx.createBufferSource();src.buffer=b;src.loop=true;src.loopStart=Math.random()*s*.4;src.loopEnd=s;return src;};
const fillBrown=d=>{let l=0;for(let i=0;i<d.length;i++){const w=Math.random()*2-1;d[i]=(l+.02*w)/1.02;l=d[i];d[i]*=3.5;}};
const fillPink=d=>{let b0=0,b1=0,b2=0,b3=0,b4=0,b5=0,b6=0;for(let i=0;i<d.length;i++){const w=Math.random()*2-1;b0=.99886*b0+w*.0555179;b1=.99332*b1+w*.0750759;b2=.96900*b2+w*.153852;b3=.86650*b3+w*.3104856;b4=.55*b4+w*.5329522;b5=-.7616*b5-w*.016898;d[i]=(b0+b1+b2+b3+b4+b5+b6+w*.5362)/5.5;b6=w*.115926;}};
const fillWhite=d=>{for(let i=0;i<d.length;i++)d[i]=Math.random()*2-1;};
const mkReverb=(ctx,dec=1.8)=>{const len=Math.floor(ctx.sampleRate*dec);const ir=ctx.createBuffer(2,len,ctx.sampleRate);for(let c=0;c<2;c++){const d=ir.getChannelData(c);for(let i=0;i<len;i++)d[i]=(Math.random()*2-1)*Math.pow(1-i/len,2.2);}const rv=ctx.createConvolver();rv.buffer=ir;return rv;};

let audioCtx=null;
function getCtx(){
  if(!audioCtx){
    audioCtx=new(window.AudioContext||window.webkitAudioContext)();
    outputBus=audioCtx.createGain();outputBus.gain.value=1;
    buildMasterChain();
  }
  if(audioCtx.state==='suspended')audioCtx.resume();
  return audioCtx;
}
const fadeGain=(g,v,t=2)=>{const ctx=getCtx(),n=ctx.currentTime;g.gain.cancelScheduledValues(n);g.gain.setValueAtTime(g.gain.value,n);g.gain.linearRampToValueAtTime(v,n+t);};

// ─── Output mode processing chain ────────────────────────────────────────────
function buildMasterChain(){
  if(!audioCtx||!outputBus)return;
  outputBus.disconnect();
  modeNodes.forEach(n=>{try{n.disconnect();}catch{}});
  modeNodes=[];
  if(outputMode==='speaker'){
    const splitter=audioCtx.createChannelSplitter(2);
    const merger=audioCtx.createChannelMerger(1);
    const hiShelf=audioCtx.createBiquadFilter();
    hiShelf.type='highshelf';hiShelf.frequency.value=8000;hiShelf.gain.value=-4;
    const bassBoost=audioCtx.createBiquadFilter();
    bassBoost.type='peaking';bassBoost.frequency.value=180;bassBoost.Q.value=1.2;bassBoost.gain.value=2.5;
    const limiter=audioCtx.createDynamicsCompressor();
    limiter.threshold.value=-3;limiter.ratio.value=20;limiter.attack.value=0.001;limiter.release.value=0.1;
    outputBus.connect(splitter);
    splitter.connect(merger,0,0);splitter.connect(merger,1,0);
    merger.connect(hiShelf);hiShelf.connect(bassBoost);bassBoost.connect(limiter);limiter.connect(audioCtx.destination);
    modeNodes=[splitter,merger,hiShelf,bassBoost,limiter];
  } else {
    outputBus.connect(audioCtx.destination);
  }
}
function setOutputMode(mode){
  outputMode=mode;
  localStorage.setItem('drift_output_mode',mode);
  document.querySelectorAll('.output-pill').forEach(p=>p.classList.toggle('active',p.dataset.mode===mode));
  if(audioCtx&&outputBus)buildMasterChain();
}

// ─── Rain perspective filter chain ────────────────────────────────────────────
let rainFilterOutput=null;
let rainFilterNodes=[];
function initRainFilter(){
  const ctx=getCtx();
  if(!rainFilterOutput){
    rainFilterOutput=mkG(ctx,1);
    const s=SOUNDS.find(x=>x.id==='rain');
    if(s){for(let i=0;i<s.segs;i++){const el=document.getElementById(`aud-rain-${i}`);if(el)try{ctx.createMediaElementSource(el).connect(rainFilterOutput);}catch{}}}
  }
  const s=SOUNDS.find(x=>x.id==='rain');
  buildRainFilterChain(s?s.currentVariant:'openair');
}
function buildRainFilterChain(perspective){
  if(!rainFilterOutput||!audioCtx)return;
  rainFilterOutput.disconnect();
  rainFilterNodes.forEach(n=>{try{n.disconnect();}catch{}});
  rainFilterNodes=[];
  const ctx=audioCtx;
  const cfgs={
    openair:{},
    ondeck: {hp:150,lp:3000,res:200},
    below:  {hp:80, lp:1500,res:180},
    distant:{hp:60, lp:800, lfo:true}
  };
  const cfg=cfgs[perspective]||{};
  let prev=rainFilterOutput;
  if(cfg.hp){const n=mkF(ctx,'highpass',cfg.hp);prev.connect(n);prev=n;rainFilterNodes.push(n);}
  if(cfg.lp){const n=mkF(ctx,'lowpass',cfg.lp);prev.connect(n);prev=n;rainFilterNodes.push(n);}
  if(cfg.res){const n=mkF(ctx,'peaking',cfg.res);n.Q.value=4;n.gain.value=4;prev.connect(n);prev=n;rainFilterNodes.push(n);}
  if(cfg.lfo){
    const vca=mkG(ctx,1);const lfo=ctx.createOscillator();lfo.frequency.value=0.05;
    const lfoD=mkG(ctx,.15);lfo.connect(lfoD);lfoD.connect(vca.gain);lfo.start();
    prev.connect(vca);prev=vca;rainFilterNodes.push(vca,lfo,lfoD);
  }
  prev.connect(outputBus);
}

function buildOcean(ctx,out){
  const n=[],ft={ref:null};
  const w1=mkNoise(ctx,fillBrown,58);const g1=mkG(ctx,.38);w1.connect(mkF(ctx,'lowpass',320)).connect(g1).connect(out);const l1=mkO(ctx,.152);const lg1=mkG(ctx,.52);l1.connect(lg1);lg1.connect(g1.gain);
  const w2=mkNoise(ctx,fillBrown,68);const g2=mkG(ctx,.28);w2.connect(mkF(ctx,'lowpass',220)).connect(g2).connect(out);const l2=mkO(ctx,.107);const lg2=mkG(ctx,.36);l2.connect(lg2);lg2.connect(g2.gain);
  const sub=mkNoise(ctx,fillBrown,40);const sg=mkG(ctx,.12);sub.connect(mkF(ctx,'lowpass',65)).connect(sg).connect(out);
  [w1,w2,sub,l1,l2].forEach(x=>x.start());n.push(w1,w2,sub,l1,l2,g1,g2,lg1,lg2,sg);
  const foam=()=>{ft.ref=setTimeout(()=>{try{const d=.7+Math.random()*.5;const x=mkNoise(ctx,fillWhite,Math.ceil(d+.2));const g=mkG(ctx,0);x.connect(mkF(ctx,'bandpass',3000,.8)).connect(g).connect(out);const t=ctx.currentTime;g.gain.setValueAtTime(0,t);g.gain.linearRampToValueAtTime(.1,t+.06);g.gain.exponentialRampToValueAtTime(.0001,t+d);x.start(t);x.stop(t+d+.1);}catch{}foam();},6000+Math.random()*1400);};foam();
  return{n,stop:()=>{clearTimeout(ft.ref);n.forEach(x=>{try{if(x.stop)x.stop();x.disconnect();}catch{}});}};
}
function buildBirds(ctx,out){
  const n=[],tm={ref:null};
  const rv=mkReverb(ctx,2.2);const master=mkG(ctx,1);const dry=mkG(ctx,.55);const wet=mkG(ctx,.45);
  master.connect(dry).connect(out);master.connect(wet).connect(rv).connect(out);
  const rust=mkNoise(ctx,fillPink,28);const rg=mkG(ctx,.04);rust.connect(mkF(ctx,'bandpass',2800,2)).connect(rg).connect(master);rust.start();n.push(rust,rg,rv,dry,wet,master);
  const SP=[{f:2600,sl:1.15,fm:[2800,4200],d:.08,a:.28},{f:1800,sl:.82,fm:[2000,3600],d:.18,a:.22},{f:3400,sl:1.22,fm:[3600,5200],d:.05,a:.25},{f:1400,sl:.90,fm:[1600,3000],d:.25,a:.18},{f:2200,sl:1.0,fm:[2400,4100],d:.12,a:.2}];
  const chirp=()=>{tm.ref=setTimeout(()=>{try{const B=SP[Math.floor(Math.random()*SP.length)];const osc=mkO(ctx,B.f);const env=mkG(ctx,0);const f1=mkF(ctx,'bandpass',B.fm[0],8);const f2=mkF(ctx,'bandpass',B.fm[1],6);const mix=mkG(ctx,.5);osc.connect(f1).connect(mix);osc.connect(f2).connect(mix);mix.connect(env).connect(master);const t=ctx.currentTime,t2=t+B.d;osc.frequency.setValueAtTime(B.f,t);osc.frequency.exponentialRampToValueAtTime(B.f*B.sl,t2);env.gain.setValueAtTime(0,t);env.gain.linearRampToValueAtTime(B.a,t+B.d*.3);env.gain.exponentialRampToValueAtTime(.001,t2+.04);osc.start(t);osc.stop(t2+.1);if(Math.random()>.55)setTimeout(()=>{try{const B2=SP[Math.floor(Math.random()*SP.length)];const o2=mkO(ctx,B2.f*(.7+Math.random()*.6));const e2=mkG(ctx,0);o2.connect(mkF(ctx,'bandpass',B2.fm[0],7)).connect(e2).connect(master);const t3=ctx.currentTime;e2.gain.setValueAtTime(0,t3);e2.gain.linearRampToValueAtTime(B2.a*.75,t3+.018);e2.gain.exponentialRampToValueAtTime(.001,t3+B2.d*.8);o2.start(t3);o2.stop(t3+B2.d+.08);}catch{}},150+Math.random()*500);}catch{}chirp();},900+Math.random()*3800);};chirp();
  return{n,stop:()=>{clearTimeout(tm.ref);n.forEach(x=>{try{if(x.stop)x.stop();x.disconnect();}catch{}});}};
}
function buildCrickets(ctx,out){
  const n=[];const rv=mkReverb(ctx,.7);const mix=mkG(ctx,.28);mix.connect(rv).connect(out);mix.connect(out);
  for(let v=0;v<2;v++){const cf=[4100,3860][v];const mf=[18.8,21.4][v];const carrier=mkO(ctx,cf);const mod=mkO(ctx,mf);const mg=mkG(ctx,.5);const env=mkG(ctx,0);mod.connect(mg);mg.connect(env.gain);carrier.connect(env).connect(mix);carrier.start();mod.start();let t=ctx.currentTime+v*.45+.1;const step=()=>{const on=1.5+Math.random()*2.5,off=.5+Math.random()*2.2;env.gain.setValueAtTime(.18+Math.random()*.07,t);t+=on;env.gain.setValueAtTime(0,t);t+=off;if(t<ctx.currentTime+300)step();};step();n.push(carrier,mod,mg,env);}
  n.push(rv,mix);return{n,stop:()=>{n.forEach(x=>{try{if(x.stop)x.stop();x.disconnect();}catch{}});}};
}
function buildWhite(ctx,out){
  const n=[];
  const src=mkNoise(ctx,fillPink,8);
  const filter=mkF(ctx,'bandpass',1200,6);
  src.connect(filter).connect(out);
  const lfo=mkO(ctx,0.05);
  const lfoDepth=mkG(ctx,144);
  lfo.connect(lfoDepth).connect(filter.frequency);
  src.start();lfo.start();
  n.push(src,filter,lfo,lfoDepth);
  return{n,stop:()=>{n.forEach(x=>{try{if(x.stop)x.stop();x.disconnect();}catch{}});}};
}
function buildVoidDrone(ctx,out){
  const n=[];
  const fund=mkO(ctx,55);
  const h2=mkO(ctx,110);
  const h3=mkO(ctx,165,'triangle');
  const fundG=mkG(ctx,.6);
  const h2G=mkG(ctx,.12);
  const h3G=mkG(ctx,.04);
  const pulse=mkG(ctx,1);
  fund.connect(fundG).connect(pulse).connect(out);
  h2.connect(h2G).connect(pulse);
  h3.connect(h3G).connect(pulse);
  const lfo=mkO(ctx,.08);
  const lfoDepth=mkG(ctx,.045);
  lfo.connect(lfoDepth).connect(pulse.gain);
  [fund,h2,h3,lfo].forEach(x=>x.start());
  n.push(fund,h2,h3,lfo,fundG,h2G,h3G,pulse,lfoDepth);
  return{n,stop:()=>{n.forEach(x=>{try{if(x.stop)x.stop();x.disconnect();}catch{}});}};
}
function buildHypersleep(ctx,out){
  const fund=mkO(ctx,180);const h2=mkO(ctx,360);const h3=mkO(ctx,540,'triangle');
  const g=mkG(ctx,.5);fund.connect(g);h2.connect(g);h3.connect(g);g.connect(out);
  fund.start();h2.start();h3.start();
  const n=[fund,h2,h3,g];
  return{n,stop:()=>{try{fund.stop();h2.stop();h3.stop();}catch(_){}n.forEach(x=>{try{x.disconnect();}catch{}});}};
}
function buildForest(ctx,out,variant){
  const bus=mkG(ctx,1);bus.connect(out);
  const engines=[];
  if(variant!=='night')engines.push(buildBirds(ctx,bus));
  if(variant!=='day')  engines.push(buildCrickets(ctx,bus));
  const n=[bus,...engines.flatMap(e=>e.n)];
  return{n,stop:()=>{engines.forEach(e=>e.stop());}};
}
function buildWarpDrive(ctx,out){
  const fund=mkO(ctx,55);const h2=mkO(ctx,110);const h3=mkO(ctx,165,'triangle');
  const g=mkG(ctx,.55);fund.connect(g);h2.connect(g);h3.connect(g);g.connect(out);
  fund.start();h2.start();h3.start();
  const n=[fund,h2,h3,g];
  return{n,stop:()=>{try{fund.stop();h2.stop();h3.stop();}catch(_){}n.forEach(x=>{try{x.disconnect();}catch{}});}};
}
function buildWarp(ctx,out,variant){
  const n=[],oscs=[];
  const f0=variant==='transit'?95:75;
  const bus=mkG(ctx,1);bus.connect(out);
  [[1,.4],[2,.2],[3,.1],[4,.05]].forEach(([ratio,amp])=>{
    const osc=mkO(ctx,f0*ratio);
    const lfo=mkO(ctx,.07+ratio*.03);const lfoG=mkG(ctx,f0*ratio*.002);
    lfo.connect(lfoG);lfoG.connect(osc.frequency);lfo.start();
    const g=mkG(ctx,amp);osc.connect(g);g.connect(bus);
    osc.start();oscs.push(osc,lfo);n.push(osc,lfo,g,lfoG);
  });
  const delay=ctx.createDelay(2.0);delay.delayTime.value=.85;
  const delayFb=mkG(ctx,.55);const lpD=mkF(ctx,'lowpass',1200);const wetG=mkG(ctx,.35);
  bus.connect(delay);delay.connect(delayFb);delayFb.connect(lpD);lpD.connect(delay);
  delay.connect(wetG);wetG.connect(out);
  n.push(bus,delay,delayFb,lpD,wetG);
  return{n,stop:()=>{oscs.forEach(o=>{try{o.stop();}catch{}});n.forEach(x=>{try{x.disconnect();}catch{}});}};
}
function buildPropulsion(ctx,out,variant){
  const n=[],oscs=[];
  const f0={idle:52,cruise:68,burn:85}[variant]||68;
  [[0,'sawtooth',.35],[.3,'sine',.15],[-.2,'sine',.15]].forEach(([det,type,amp])=>{
    const osc=ctx.createOscillator();osc.type=type;osc.frequency.value=f0+det;
    const g=mkG(ctx,amp);const lp=mkF(ctx,'lowpass',outputMode==='speaker'?400:800);
    osc.connect(lp);lp.connect(g);g.connect(out);osc.start();oscs.push(osc);n.push(osc,g,lp);
  });
  const bufSize=ctx.sampleRate*2;const buf=ctx.createBuffer(1,bufSize,ctx.sampleRate);
  const d=buf.getChannelData(0);for(let i=0;i<bufSize;i++)d[i]=Math.random()*2-1;
  const noise=ctx.createBufferSource();noise.buffer=buf;noise.loop=true;
  const bp=mkF(ctx,'bandpass',420);bp.Q.value=3;const ng=mkG(ctx,.04);
  noise.connect(bp);bp.connect(ng);ng.connect(out);noise.start();n.push(noise,bp,ng);
  return{n,stop:()=>{oscs.forEach(o=>{try{o.stop();}catch{}});try{noise.stop();}catch{}n.forEach(x=>{try{x.disconnect();}catch{}});}};
}
function buildSpaceWhale(ctx,out,variant){
  const rates={rare:[18,35],normal:[10,20],busy:[5,12]};
  const [minGap,maxGap]=rates[variant||'normal']||[10,20];
  let alive=true,callTimer=null;
  function makeCall(){
    if(!alive)return;
    const now=ctx.currentTime;
    const startF=55+Math.random()*30;
    const osc=mkO(ctx,startF);
    osc.frequency.linearRampToValueAtTime(startF*(0.5+Math.random()*.8),now+4);
    const env=mkG(ctx,0);
    env.gain.setValueAtTime(0,now);
    env.gain.linearRampToValueAtTime(.7,now+1.5);
    env.gain.linearRampToValueAtTime(0,now+5);
    const delay=ctx.createDelay(3.0);delay.delayTime.value=1.2;
    const fb=mkG(ctx,.6);const lpD=mkF(ctx,'lowpass',800);const wetG=mkG(ctx,.5);
    osc.connect(env);
    env.connect(out);env.connect(delay);
    delay.connect(fb);fb.connect(lpD);lpD.connect(delay);
    delay.connect(wetG);wetG.connect(out);
    osc.start(now);osc.stop(now+6);
    callTimer=setTimeout(makeCall,(minGap+Math.random()*(maxGap-minGap))*1000);
  }
  makeCall();
  return{n:[],stop:()=>{alive=false;clearTimeout(callTimer);}};
}
function buildRadio(ctx,out,variant){
  const bufSize=ctx.sampleRate*4;const buf=ctx.createBuffer(1,bufSize,ctx.sampleRate);
  const d=buf.getChannelData(0);for(let i=0;i<bufSize;i++)d[i]=Math.random()*2-1;
  const noise=ctx.createBufferSource();noise.buffer=buf;noise.loop=true;
  const bp=mkF(ctx,'bandpass',variant==='near'?1200:800);bp.Q.value=variant==='near'?2:5;
  const wobbleLFO=mkO(ctx,.3);const wobbleG=mkG(ctx,30);
  wobbleLFO.connect(wobbleG);wobbleG.connect(bp.frequency);wobbleLFO.start();
  const radioG=mkG(ctx,.4);
  noise.connect(bp);bp.connect(radioG);radioG.connect(out);noise.start();
  let alive=true,gateTimer=null;
  function gate(){
    if(!alive)return;
    const t=ctx.currentTime;
    radioG.gain.cancelScheduledValues(t);
    radioG.gain.linearRampToValueAtTime(Math.random()>.4?.4:.001,t+.1);
    gateTimer=setTimeout(gate,Math.random()>.5?500+Math.random()*2000:800+Math.random()*3000);
  }
  gate();
  const n=[noise,bp,wobbleLFO,wobbleG,radioG];
  return{n,stop:()=>{alive=false;clearTimeout(gateTimer);try{noise.stop();}catch{}try{wobbleLFO.stop();}catch{}n.forEach(x=>{try{x.disconnect();}catch{}});}};
}
const SYNTH_BUILDERS={ocean:buildOcean,white:buildWhite,voidDrone:buildVoidDrone,forest:buildForest,warp:buildWarp,propulsion:buildPropulsion,radio:buildRadio};

function activateSynth(id){
  if(synthLayers[id])return;
  const ctx=getCtx();const g=mkG(ctx,0);g.connect(outputBus);
  const s=SOUNDS.find(x=>x.id===id);
  const eng=SYNTH_BUILDERS[id](ctx,g,s&&s.currentVariant);
  synthLayers[id]={gainNode:g,engine:eng};
  fadeGain(g,volumes[id],2);
}
function deactivateSynth(id){
  if(!synthLayers[id])return;
  const{gainNode:g,engine:eng}=synthLayers[id];delete synthLayers[id];
  fadeGain(g,0,2);setTimeout(()=>{try{eng.stop();g.disconnect();}catch{}},2200);
}
function setSynthVolume(id,v){if(synthLayers[id])fadeGain(synthLayers[id].gainNode,v,.3);}

// ─── Toggle ───────────────────────────────────────────────────────────────────
function toggleLayer(id){
  const s=SOUNDS.find(x=>x.id===id);
  if(active.has(id)){active.delete(id);if(s.real)deactivateReal(id);else deactivateSynth(id);}
  else{active.add(id);if(s.real)activateReal(id);else activateSynth(id);}
  updateUI();
  saveState();
  updateMediaSession();
  updateBackgroundService();
}
function setVolume(id,v){
  volumes[id]=v;
  const fill=document.getElementById('fill-'+id);if(fill)fill.style.width=(v*100)+'%';
  const s=SOUNDS.find(x=>x.id===id);
  if(active.has(id)){if(s.real)setRealVolume(id,v);else setSynthVolume(id,v);}
  saveState();
}
function applyPreset(layerMap, ratesMap, variantsMap){
  stopAll();
  setTimeout(()=>{
    Object.entries(layerMap).forEach(([id,v])=>{volumes[id]=v;const fill=document.getElementById('fill-'+id);const range=document.getElementById('range-'+id);if(fill)fill.style.width=(v*100)+'%';if(range)range.value=v;});
    if (ratesMap) {
      Object.entries(ratesMap).forEach(([id, rate]) => {
        const s = SOUNDS.find(x => x.id === id);
        if (s && s.sparse) {
          s.eventRate = rate;
          const group = document.querySelector(`.rate-pills[data-sound-id="${id}"]`);
          if (group) group.querySelectorAll('.rate-pill').forEach(p => p.classList.toggle('active', parseFloat(p.dataset.rate) === rate));
        }
      });
    }
    if (variantsMap) {
      Object.entries(variantsMap).forEach(([id, variant]) => {
        const s = SOUNDS.find(x => x.id === id);
        if (s && s.variants && s.variants.includes(variant)) {
          s.currentVariant = variant;
          const group = document.querySelector(`.rate-pills[data-sound-id="${id}"]`);
          if (group) group.querySelectorAll('.rate-pill[data-variant]').forEach(p => p.classList.toggle('active', p.dataset.variant === variant));
        }
      });
    }
    Object.entries(layerMap).forEach(([id])=>{active.add(id);const s=SOUNDS.find(x=>x.id===id);if(s.real)activateReal(id);else activateSynth(id);});
    updateUI();
    saveState();
    updateMediaSession();
    updateBackgroundService();
  },100);
}
// ─── User presets ─────────────────────────────────────────────────────────────
function applyUserPreset(preset) {
  const layerMap = {}, variantsMap = {};
  Object.entries(preset.layers).forEach(([id, saved]) => {
    layerMap[id] = saved.vol;
    if (saved.variant != null) variantsMap[id] = saved.variant;
  });
  applyPreset(layerMap, {}, variantsMap);
}

function saveCurrentAsPreset() {
  if (active.size === 0) return;
  const n = userPresets.length + 1;
  const raw = prompt('Preset name:', 'Preset ' + n);
  if (raw === null) return;
  const name = raw.trim();
  if (!name) return;
  const dupIdx = userPresets.findIndex(p => p.name === name);
  if (dupIdx !== -1 && !confirm('Overwrite "' + name + '"?')) return;
  const layers = {};
  [...active].forEach(id => {
    const s = SOUNDS.find(x => x.id === id);
    layers[id] = { vol: volumes[id], variant: s && s.variants ? s.currentVariant : undefined };
  });
  const entry = { name, layers };
  if (dupIdx !== -1) userPresets[dupIdx] = entry;
  else userPresets.push(entry);
  saveUserPresets();
  renderUserPresets();
}

function deleteUserPreset(name) {
  if (!confirm('Delete preset "' + name + '"?')) return;
  userPresets = userPresets.filter(p => p.name !== name);
  saveUserPresets();
  renderUserPresets();
}

function renderUserPresets() {
  const c = document.getElementById('user-presets-container');
  if (!c) return;
  c.innerHTML = '';
  if (userPresets.length === 0) {
    c.innerHTML = '<span class="presets-empty">— no saved presets —</span>';
    return;
  }
  userPresets.forEach(p => {
    const row = document.createElement('div');
    row.className = 'user-preset-row';
    const applyBtn = document.createElement('button');
    applyBtn.className = 'preset-btn user-preset-btn';
    applyBtn.textContent = p.name;
    applyBtn.onclick = () => applyUserPreset(p);
    const delBtn = document.createElement('button');
    delBtn.className = 'preset-delete-btn';
    delBtn.setAttribute('aria-label', 'Delete preset ' + p.name);
    delBtn.innerHTML = '<svg width="10" height="10" viewBox="0 0 10 10" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="square"><line x1="2" y1="2" x2="8" y2="8"/><line x1="8" y1="2" x2="2" y2="8"/></svg> DEL';
    delBtn.onclick = () => deleteUserPreset(p.name);
    row.appendChild(applyBtn);
    row.appendChild(delBtn);
    c.appendChild(row);
  });
}

function cancelTimer(){
  clearInterval(timerInterval);timerInterval=null;timerMins=0;timerSecs=0;
  document.getElementById('mfd-timer-presets').hidden=false;
  document.getElementById('mfd-timer-running').hidden=true;
  document.querySelectorAll('.mfd-timer-preset').forEach(b=>b.classList.remove('active'));
  document.getElementById('mfd-timer').classList.remove('active');
}
function stopAll(){
  [...active].forEach(id=>{const s=SOUNDS.find(x=>x.id===id);if(s.real)deactivateReal(id);else deactivateSynth(id);});
  active.clear();
  updateUI();
  saveState();
  updateMediaSession();
  updateBackgroundService();
}
function setTimer(mins){
  timerMins=mins;
  document.querySelectorAll('.mfd-timer-preset').forEach(b=>b.classList.toggle('active',+b.dataset.mins===mins));
  clearInterval(timerInterval);timerInterval=null;
  document.getElementById('mfd-timer-presets').hidden=false;
  document.getElementById('mfd-timer-running').hidden=true;
  document.getElementById('mfd-timer').classList.remove('active');
  if(!mins)return;
  timerSecs=mins*60;
  document.getElementById('mfd-timer-presets').hidden=true;
  document.getElementById('mfd-timer-running').hidden=false;
  document.getElementById('mfd-timer-text').textContent=fmt(timerSecs);
  document.getElementById('mfd-timer').classList.add('active');
  timerInterval=setInterval(()=>{
    timerSecs--;
    document.getElementById('mfd-timer-text').textContent=fmt(timerSecs);
    if(timerSecs<=0){clearInterval(timerInterval);timerInterval=null;stopAll();cancelTimer();}
  },1000);
}
function fmt(s){return `${Math.floor(s/60)}:${String(s%60).padStart(2,'0')}`;}

// ─── UI ───────────────────────────────────────────────────────────────────────
function updateLayerStyle(id){
  const el=document.getElementById('layer-'+id);
  if(!el)return;
  el.classList.toggle('active',active.has(id));
}
let activeCat='planetside';
function updateUI(){
  const n=active.size;
  document.getElementById('mfd-status-text').textContent=n===0?'LAYERS: 0 / IDLE':`LAYERS: ${n} / ACTIVE`;
  document.getElementById('mfd-status-text').classList.toggle('idle',n===0);
  document.getElementById('mfd-status').classList.toggle('active',n>0);
  document.getElementById('mfd-stop-btn').disabled=n===0;
  SOUNDS.forEach(s=>updateLayerStyle(s.id));
  showCategory(activeCat);
  // Sync sliders to current volumes after state restore
  SOUNDS.forEach(s=>{
    const range=document.getElementById('range-'+s.id);
    if(range) range.value=volumes[s.id];
    const fill=document.getElementById('fill-'+s.id);
    if(fill) fill.style.width=(volumes[s.id]*100)+'%';
  });
}
function showCategory(catId){
  activeCat=catId;
  document.querySelectorAll('.layer').forEach(el=>{
    const id=el.id.replace('layer-','');
    el.style.display=el.dataset.cat===catId?'':'none';
  });
  document.querySelectorAll('.cat-tab').forEach(t=>t.classList.toggle('active',t.dataset.cat===catId));
}
function buildUI(){
  const pc=document.getElementById('presets-container');
  PRESETS.forEach(p=>{const btn=document.createElement('button');btn.className='preset-btn';btn.textContent=p.name;btn.onclick=()=>applyPreset(p.layers, p.rates||{}, p.variants||{});pc.appendChild(btn);});
  document.querySelectorAll('.cat-tab').forEach(tab=>{
    const catId=tab.dataset.cat;
    const label=tab.textContent.trim();
    tab.innerHTML=`${getIcon(catId)}<span>${label}</span>`;
    tab.addEventListener('click',()=>showCategory(catId));
  });
  const lc=document.getElementById('layers-container');
  SOUNDS.forEach(s=>{
    const v=volumes[s.id];
    const div=document.createElement('div');div.className='layer';div.id='layer-'+s.id;div.dataset.cat=s.cat;
    const rateRow = s.sparse ? `<div class="rate-row"><span class="rate-label">RATE</span><div class="rate-pills" data-sound-id="${s.id}"><button class="rate-pill" data-rate="0.2">RARE</button><button class="rate-pill active" data-rate="0.5">NORMAL</button><button class="rate-pill" data-rate="0.8">BUSY</button></div></div>` : '';
    const variantRow = s.variants ? `<div class="rate-row"><span class="rate-label">MODE</span><div class="rate-pills" data-sound-id="${s.id}">${s.variants.map(v=>`<button class="rate-pill${v===s.currentVariant?' active':''}" data-variant="${v}">${(s.variantLabels&&s.variantLabels[v])||v.toUpperCase()}</button>`).join('')}</div></div>` : '';
    div.innerHTML=`<div class="layer-inner"><button class="layer-btn" id="btn-${s.id}">${getIcon(s.id)}</button><div class="layer-info"><div class="layer-header"><span class="layer-name" id="name-${s.id}">${s.label}${s.real?'<span class="rec-badge">REC</span>':''}</span><span class="layer-desc">${s.desc}</span></div><div class="vol-track"><div class="vol-fill" id="fill-${s.id}" style="width:${v*100}%;"></div><input type="range" class="vol-range" id="range-${s.id}" min="0" max="1" step="0.01" value="${v}"></div>${rateRow}${variantRow}</div><div class="layer-dot" id="dot-${s.id}"></div></div>`;
    div.querySelector('.layer-btn').addEventListener('click',()=>toggleLayer(s.id));
    div.querySelector('.vol-range').addEventListener('input',function(){setVolume(s.id,parseFloat(this.value));});
    lc.appendChild(div);
  });
  showCategory('planetside');
  document.querySelectorAll('.mfd-timer-preset').forEach(btn=>{
    btn.disabled=false;
    btn.addEventListener('click',()=>setTimer(parseInt(btn.dataset.mins,10)));
  });
  document.getElementById('mfd-stop-btn').addEventListener('click',stopAll);
  document.getElementById('mfd-cancel-btn').addEventListener('click',cancelTimer);
  document.querySelectorAll('.output-pill').forEach(pill=>{
    pill.classList.toggle('active',pill.dataset.mode===outputMode);
    pill.addEventListener('click',()=>setOutputMode(pill.dataset.mode));
  });
  document.getElementById('save-preset-btn').addEventListener('click', saveCurrentAsPreset);
  renderUserPresets();
}

// ─── Sparse sound helpers ─────────────────────────────────────────────────────
function findSoundById(id) { return SOUNDS.find(s => s.id === id); }

document.addEventListener('click', function(e) {
  const pill = e.target.closest('.rate-pill');
  if (!pill) return;
  const group = pill.closest('.rate-pills');
  if (!group) return;
  const soundId = group.dataset.soundId;
  if (pill.dataset.variant !== undefined) {
    group.querySelectorAll('.rate-pill').forEach(p => p.classList.toggle('active', p === pill));
    setVariant(soundId, pill.dataset.variant);
    return;
  }
  const rate = parseFloat(pill.dataset.rate);
  const sound = findSoundById(soundId);
  if (!sound) return;
  sound.eventRate = rate;
  group.querySelectorAll('.rate-pill').forEach(p => p.classList.toggle('active', p === pill));
  if (typeof saveState === 'function') saveState();
});

// ─── Background audio (Android foreground service) ───────────────────────────
let BackgroundAudio = null;
if (window.Capacitor && window.Capacitor.Plugins && window.Capacitor.Plugins.BackgroundAudio) {
  BackgroundAudio = window.Capacitor.Plugins.BackgroundAudio;
}

function updateBackgroundService() {
  if (!BackgroundAudio) return;
  if (active.size === 0) {
    BackgroundAudio.disable().catch(() => {});
  } else {
    const names = [...active].map(id => SOUNDS.find(s => s.id === id)?.label).filter(Boolean);
    BackgroundAudio.enable({
      title: 'Drift',
      content: names.join(' · '),
    }).catch(() => {});
  }
}

// ─── Media Session ────────────────────────────────────────────────────────────
function updateMediaSession() {
  if (!('mediaSession' in navigator)) return;
  if (active.size === 0) {
    navigator.mediaSession.playbackState = 'none';
    return;
  }
  const names = [...active].map(id => SOUNDS.find(s => s.id === id)?.label).filter(Boolean);
  navigator.mediaSession.metadata = new MediaMetadata({
    title: names.join(' · '),
    artist: 'Drift',
    album: 'Ambient Soundscape',
  });
  navigator.mediaSession.playbackState = 'playing';
  navigator.mediaSession.setActionHandler('pause', () => stopAll());
  navigator.mediaSession.setActionHandler('stop',  () => stopAll());
}

// ─── Stars canvas ─────────────────────────────────────────────────────────────
(function(){
  const sc=document.getElementById('stars-canvas');
  const sx=sc.getContext('2d');
  let stars=[];
  let twinkleRaf=null;
  function initStars(){
    sc.width=innerWidth*devicePixelRatio;sc.height=innerHeight*devicePixelRatio;
    sx.scale(devicePixelRatio,devicePixelRatio);
    const W=innerWidth,H=innerHeight,N=320;
    stars=Array.from({length:N},()=>({
      x:Math.random()*W, y:Math.random()*H,
      r:Math.random()<.12 ? .9+Math.random()*.9 : .25+Math.random()*.55,
      base:0, op:0,
      amber:Math.random()<.08,
      phase:Math.random()*Math.PI*2,
      speed:.0004+Math.random()*.0008,
      twinkle:Math.random()<.22,
    }));
    stars.forEach(s=>{s.base=.08+Math.random()*.55;s.op=s.base;});
    drawStars(performance.now());
  }
  function drawStars(t){
    if(document.hidden){twinkleRaf=null;return;}
    const W=innerWidth,H=innerHeight;
    sx.clearRect(0,0,W,H);
    stars.forEach(s=>{
      if(s.twinkle) s.op=s.base*(0.55+0.45*Math.sin(t*s.speed*1000+s.phase));
      const r=s.r, x=s.x, y=s.y;
      sx.beginPath();sx.arc(x,y,r,0,Math.PI*2);
      if(s.amber) sx.fillStyle=`rgba(255,220,140,${s.op})`;
      else        sx.fillStyle=`rgba(210,230,255,${s.op})`;
      sx.fill();
      if(r>.8){
        const gl=r*3.5, op=s.op*.22;
        sx.strokeStyle=s.amber?`rgba(255,210,100,${op})`:`rgba(200,220,255,${op})`;
        sx.lineWidth=.5;
        sx.beginPath();sx.moveTo(x-gl,y);sx.lineTo(x+gl,y);sx.stroke();
        sx.beginPath();sx.moveTo(x,y-gl);sx.lineTo(x,y+gl);sx.stroke();
      }
    });
    twinkleRaf=requestAnimationFrame(drawStars);
  }
  window.starsResume=()=>{if(!twinkleRaf)drawStars(performance.now());};
  window.addEventListener('resize',()=>{cancelAnimationFrame(twinkleRaf);twinkleRaf=null;initStars();});
  initStars();
})();

// ─── Rain canvas ──────────────────────────────────────────────────────────────
const canvas=document.getElementById('rain-canvas'),ctx2d=canvas.getContext('2d');
let drops=[],streaks=[],rainRaf=null;
function initCanvas(){canvas.width=innerWidth*devicePixelRatio;canvas.height=innerHeight*devicePixelRatio;ctx2d.scale(devicePixelRatio,devicePixelRatio);const W=innerWidth,H=innerHeight;drops=Array.from({length:18},()=>{return{x:Math.random()*W,y:Math.random()*H,r:1.5+Math.random()*5,speed:.14+Math.random()*.5,opacity:.09+Math.random()*.3,trail:[],pause:140+Math.random()*460,pt:Math.random()*360};});streaks=Array.from({length:22},()=>{return{x:Math.random()*W,y:Math.random()*H-H,len:10+Math.random()*30,speed:3.5+Math.random()*5.5,opacity:.012+Math.random()*.032,delay:Math.random()*700};});}
function drawCanvas(){
  if(document.hidden){rainRaf=null;return;}
  const W=innerWidth,H=innerHeight;ctx2d.clearRect(0,0,W,H);
  const r=active.has('rain');
  streaks.forEach(s=>{if(s.delay>0){if(r)s.delay--;return;}ctx2d.beginPath();ctx2d.moveTo(s.x,s.y);ctx2d.lineTo(s.x-s.len*.09,s.y+s.len);ctx2d.strokeStyle=`rgba(175,210,240,${s.opacity})`;ctx2d.lineWidth=.45;ctx2d.stroke();if(r){s.y+=s.speed;if(s.y>H+20){s.y=-s.len;s.x=Math.random()*W;s.delay=Math.random()*500;}}});
  drops.forEach(d=>{ctx2d.beginPath();ctx2d.arc(d.x,d.y,d.r,0,Math.PI*2);const g=ctx2d.createRadialGradient(d.x-d.r*.35,d.y-d.r*.35,0,d.x,d.y,d.r);g.addColorStop(0,`rgba(225,238,255,${d.opacity})`);g.addColorStop(.55,`rgba(155,198,240,${d.opacity*.5})`);g.addColorStop(1,`rgba(95,148,212,${d.opacity*.1})`);ctx2d.fillStyle=g;ctx2d.fill();ctx2d.beginPath();ctx2d.arc(d.x-d.r*.3,d.y-d.r*.36,d.r*.22,0,Math.PI*2);ctx2d.fillStyle=`rgba(255,255,255,${d.opacity*.42})`;ctx2d.fill();if(d.trail.length>1){ctx2d.beginPath();ctx2d.moveTo(d.trail[0].x,d.trail[0].y);d.trail.forEach(p=>ctx2d.lineTo(p.x,p.y));ctx2d.strokeStyle=`rgba(145,192,232,${d.opacity*.14})`;ctx2d.lineWidth=d.r*.38;ctx2d.lineCap='round';ctx2d.stroke();}if(r){if(d.pt>0){d.pt--;return;}d.trail.push({x:d.x,y:d.y});if(d.trail.length>10)d.trail.shift();d.x+=(Math.random()-.5)*.18;d.y+=d.speed;if(d.y>H+d.r*2){d.y=-d.r*2-Math.random()*H*.55;d.x=Math.random()*W;d.trail=[];d.r=1.5+Math.random()*5;d.speed=.14+Math.random()*.5;d.pt=d.pause=140+Math.random()*460;}}});
  rainRaf=requestAnimationFrame(drawCanvas);
}
window.addEventListener('resize',initCanvas);

// ─── Visibility / background pause ───────────────────────────────────────────
document.addEventListener('visibilitychange', () => {
  if (!document.hidden) {
    // Resume canvas loops when app comes back to foreground
    if (window.starsResume) window.starsResume();
    if (!rainRaf) { rainRaf = requestAnimationFrame(drawCanvas); }
    // Resume audio context if suspended
    if (audioCtx && audioCtx.state === 'suspended') audioCtx.resume();
  }
  // When hidden: drawCanvas/drawStars check document.hidden and stop themselves
});

initCanvas();
rainRaf=requestAnimationFrame(drawCanvas);
loadUserPresets();
buildUI();
loadState();
updateUI();
updateMediaSession();
updateBackgroundService();

// ─── MFD spinners ─────────────────────────────────────────────────────────────
const SPINNER_FRAMES = ['\\', '|', '/', '-'];
let spinnerFrame = 0;
setInterval(() => {
  spinnerFrame = (spinnerFrame + 1) % SPINNER_FRAMES.length;
  const f = SPINNER_FRAMES[spinnerFrame];
  const statusPanel = document.getElementById('mfd-status');
  const timerPanel  = document.getElementById('mfd-timer');
  document.getElementById('mfd-status-spinner').textContent =
    statusPanel.classList.contains('active') ? f : '\u00A0';
  document.getElementById('mfd-timer-spinner').textContent =
    timerPanel.classList.contains('active')  ? f : '\u00A0';
}, 250);
