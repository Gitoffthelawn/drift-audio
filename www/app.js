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
  _default: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="square"><path d="M3 12 Q 8 8, 12 12 T 21 12"/><path d="M3 16 Q 8 12, 12 16 T 21 16"/></svg>`,
};
function getIcon(id){ return ICONS[id] ?? ICONS._default; }

const CATEGORIES = [
  {id:"planetside", label:"Planetside", sounds:[
    {id:"rain",       label:"Rain",       desc:"Forest rain",      real:true,  segs:3},
    {id:"forestrain", label:"Light Rain", desc:"Gentle forest rain", real:true,  segs:3},
    {id:"heavyrain",  label:"Downpour",   desc:"Heavy rain",       real:true,  segs:3},
    {id:"thunder",   label:"Thunder",   desc:"Rolling storm",    real:true,  segs:3},
    {id:"creek",     label:"Creek",     desc:"Rainforest creek", real:true,  segs:3},
    {id:"brook",     label:"Brook",     desc:"Flowing stream",   real:true,  segs:3},
    {id:"fire",      label:"Fireplace", desc:"Crackling fire",   real:true,  segs:3},
    {id:"wind",      label:"Wind",      desc:"Forest wind",      real:true,  segs:3},
    {id:"birds",     label:"Birds",     desc:"Dawn chorus",      real:false, segs:0},
    {id:"crickets",  label:"Crickets",  desc:"Night insects",    real:false, segs:0},
  ]},
  {id:"deepspace", label:"Deep Space", sounds:[
    {id:"ocean",         label:"Void Drone",    desc:"Low harmonic",     real:false, segs:0},
    {id:"white",         label:"Static Field",  desc:"Hull spectrum",    real:false, segs:0},
    {id:"interstellarplasma", label:"Interstellar Plasma", desc:"Voyager 1 plasma waves", real:true, segs:3,
      sparse: true,
      burstSegs: [0, 1],
      silentSegs: [2],
      eventRate: 0.5 },
    {id:"enginerumble",  label:"Engine Rumble", desc:"Engine rumble",    real:true,  segs:3},
    {id:"rocketthruster",label:"Thruster Drone",desc:"Thruster drone",   real:true,  segs:3},
    {id:"rocketfiring",  label:"RCS Firing",    desc:"RCS firing",       real:true,  segs:3,
      sparse: true,
      burstSegs: [0, 1],
      silentSegs: [2],
      eventRate: 0.5 },
  ]},
];
const SOUNDS = CATEGORIES.flatMap(c => c.sounds.map(s => ({...s, cat:c.id})));

const PRESETS = [
  {name:"Monsoon Run",    layers:{"rain":0.55,"fire":0.42}},
  {name:"Hull Storm",     layers:{"heavyrain":0.65,"thunder":0.7,"wind":0.3}},
  {name:"Planetfall",     layers:{"birds":0.55,"wind":0.22,"brook":0.45}},
  {name:"Drift Orbit",    layers:{"ocean":0.65,"wind":0.28,"crickets":0.32}},
  {name:"Long Haul",      layers:{"rain":0.3,"white":0.14,"brook":0.28}},
  {name:"Green Sector",   layers:{"rain":0.42,"birds":0.3,"wind":0.2,"brook":0.38}},
  {name:"Outpost",        layers:{"forestrain":0.6,"fire":0.38,"crickets":0.25}},
  {name:"Canopy Dark",    layers:{"creek":0.55,"crickets":0.45,"forestrain":0.3}},
  {name:"Outpost Patrol", layers:{"rocketthruster":0.65,"rocketfiring":0.45}, rates:{"rocketfiring":0.2}},
];

// ─── State ────────────────────────────────────────────────────────────────────
const active  = new Set();
const volumes = Object.fromEntries(SOUNDS.map(s => [s.id, 0.5]));
let timerMins=0, timerInterval=null, timerSecs=0;
const synthLayers = {};

// ─── Persistence ──────────────────────────────────────────────────────────────
const STORAGE_KEY = 'drift_state';

function saveState() {
  try {
    const eventRates = {};
    SOUNDS.filter(s => s.sparse).forEach(s => { eventRates[s.id] = s.eventRate ?? 0.5; });
    localStorage.setItem(STORAGE_KEY, JSON.stringify({
      volumes: volumes,
      active: [...active],
      eventRates: eventRates,
    }));
  } catch(e) {}
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
    if (saved.active && Array.isArray(saved.active)) {
      saved.active.forEach(id => {
        const s = SOUNDS.find(x => x.id === id);
        if (s) {
          active.add(id);
          if (s.real) activateReal(id); else activateSynth(id);
        }
      });
    }
    if (saved.eventRates) {
      Object.entries(saved.eventRates).forEach(([id, rate]) => {
        const s = SOUNDS.find(x => x.id === id);
        if (s && s.sparse) {
          s.eventRate = rate;
          const group = document.querySelector(`.rate-pills[data-sound-id="${id}"]`);
          if (group) {
            group.querySelectorAll('.rate-pill').forEach(p => {
              p.classList.toggle('active', parseFloat(p.dataset.rate) === rate);
            });
          }
        }
      });
    }
  } catch(e) {}
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
function getCtx(){if(!audioCtx)audioCtx=new(window.AudioContext||window.webkitAudioContext)();if(audioCtx.state==='suspended')audioCtx.resume();return audioCtx;}
const fadeGain=(g,v,t=2)=>{const ctx=getCtx(),n=ctx.currentTime;g.gain.cancelScheduledValues(n);g.gain.setValueAtTime(g.gain.value,n);g.gain.linearRampToValueAtTime(v,n+t);};

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
function buildWhite(ctx,out){const s=mkNoise(ctx,fillWhite,10);const g=mkG(ctx,.55);s.connect(g).connect(out);s.start();return{n:[s,g],stop:()=>{try{s.stop();s.disconnect();}catch{}try{g.disconnect();}catch{};}}; }
const SYNTH_BUILDERS={ocean:buildOcean,birds:buildBirds,crickets:buildCrickets,white:buildWhite};

function activateSynth(id){
  if(synthLayers[id])return;
  const ctx=getCtx();const g=mkG(ctx,0);g.connect(ctx.destination);
  const eng=SYNTH_BUILDERS[id](ctx,g);
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
function applyPreset(layerMap, ratesMap){
  stopAll();
  setTimeout(()=>{
    Object.entries(layerMap).forEach(([id,v])=>{volumes[id]=v;const fill=document.getElementById('fill-'+id);const range=document.getElementById('range-'+id);if(fill)fill.style.width=(v*100)+'%';if(range)range.value=v;});
    if (ratesMap) {
      Object.entries(ratesMap).forEach(([id, rate]) => {
        const s = SOUNDS.find(x => x.id === id);
        if (s && s.sparse) {
          s.eventRate = rate;
          const group = document.querySelector(`.rate-pills[data-sound-id="${id}"]`);
          if (group) {
            group.querySelectorAll('.rate-pill').forEach(p => {
              p.classList.toggle('active', parseFloat(p.dataset.rate) === rate);
            });
          }
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
  PRESETS.forEach(p=>{const btn=document.createElement('button');btn.className='preset-btn';btn.textContent=p.name;btn.onclick=()=>applyPreset(p.layers, p.rates||{});pc.appendChild(btn);});
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
    div.innerHTML=`<div class="layer-inner"><button class="layer-btn" id="btn-${s.id}">${getIcon(s.id)}</button><div class="layer-info"><div class="layer-header"><span class="layer-name" id="name-${s.id}">${s.label}${s.real?'<span class="rec-badge">REC</span>':''}</span><span class="layer-desc">${s.desc}</span></div><div class="vol-track"><div class="vol-fill" id="fill-${s.id}" style="width:${v*100}%;"></div><input type="range" class="vol-range" id="range-${s.id}" min="0" max="1" step="0.01" value="${v}"></div>${rateRow}</div><div class="layer-dot" id="dot-${s.id}"></div></div>`;
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
}

// ─── Sparse sound helpers ─────────────────────────────────────────────────────
function findSoundById(id) { return SOUNDS.find(s => s.id === id); }

document.addEventListener('click', function(e) {
  const pill = e.target.closest('.rate-pill');
  if (!pill) return;
  const group = pill.closest('.rate-pills');
  if (!group) return;
  const soundId = group.dataset.soundId;
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
  const r=active.has('rain')||active.has('heavyrain')||active.has('thunder');
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
