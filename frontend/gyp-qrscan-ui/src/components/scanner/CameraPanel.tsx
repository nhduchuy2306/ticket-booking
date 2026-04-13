import type { RefObject } from 'react'

type CameraPanelProps = {
  cameraError: string
  cameraLabel: string
  gateName: string
  isCameraActive: boolean
  videoRef: RefObject<HTMLVideoElement | null>
}

export const CameraPanel = ({ cameraError, cameraLabel, gateName, isCameraActive, videoRef }: CameraPanelProps) => (
  <section className="overflow-hidden rounded-[2rem] border border-white/8 bg-white/5 shadow-[0_28px_80px_rgba(0,0,0,0.3)] backdrop-blur-xl">
    <div className="flex items-start justify-between gap-3 border-b border-white/8 px-5 py-4 sm:px-6">
      <div>
        <p className="text-[0.7rem] uppercase tracking-[0.3em] text-violet-200/70">Live camera</p>
        <h2 className="text-xl font-semibold text-white sm:text-2xl">Quét QR trên camera</h2>
      </div>
      <span className={`rounded-full border px-3 py-1 text-xs uppercase tracking-[0.18em] ${isCameraActive ? 'border-emerald-400/30 bg-emerald-400/15 text-emerald-100' : 'border-sky-400/30 bg-sky-400/15 text-sky-100'}`}>
        {isCameraActive ? 'Camera on' : 'Camera off'}
      </span>
    </div>

    <div className="relative min-h-[360px] bg-[radial-gradient(circle_at_top,_rgba(168,85,247,0.25),_transparent_30%),linear-gradient(180deg,_rgba(17,19,29,0.96),_rgba(7,8,13,1))] sm:min-h-[520px]">
      <video ref={videoRef} className="absolute inset-0 h-full w-full object-cover" playsInline muted autoPlay />

      <div className="pointer-events-none absolute inset-0">
        <div className="absolute inset-[11%_14%] rounded-[1.75rem] border border-white/20 shadow-[inset_0_0_0_9999px_rgba(6,8,13,0.28)]" />
        <div className="absolute inset-x-[14%] top-[11%] h-0.5 animate-[scanline_2.6s_linear_infinite] bg-gradient-to-r from-transparent via-violet-400 to-cyan-300 opacity-90" />
        <span className="absolute left-[14%] top-[11%] h-7 w-7 rounded-tl-[1.1rem] border-l-2 border-t-2 border-violet-200/80" />
        <span className="absolute right-[14%] top-[11%] h-7 w-7 rounded-tr-[1.1rem] border-r-2 border-t-2 border-violet-200/80" />
        <span className="absolute bottom-[11%] left-[14%] h-7 w-7 rounded-bl-[1.1rem] border-b-2 border-l-2 border-violet-200/80" />
        <span className="absolute bottom-[11%] right-[14%] h-7 w-7 rounded-br-[1.1rem] border-b-2 border-r-2 border-violet-200/80" />
      </div>

      <div className="absolute inset-x-4 bottom-4 flex flex-col gap-3 rounded-3xl border border-white/8 bg-slate-950/70 p-4 backdrop-blur-md sm:flex-row sm:items-end sm:justify-between">
        <div>
          <p className="text-sm font-medium text-white">{cameraLabel}</p>
          <p className="mt-1 text-sm text-slate-300/70">{isCameraActive ? 'Camera đang hoạt động' : 'Camera đang tắt'}</p>
        </div>
        <div className="rounded-full border border-white/12 bg-white/6 px-3 py-2 text-sm text-violet-100">{gateName}</div>
      </div>

      {cameraError ? (
        <div className="absolute left-4 right-4 top-4 rounded-2xl border border-rose-400/20 bg-rose-400/12 px-4 py-3 text-sm text-rose-100">
          {cameraError}
        </div>
      ) : null}
    </div>
  </section>
)