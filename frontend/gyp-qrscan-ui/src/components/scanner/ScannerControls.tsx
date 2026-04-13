type ScannerControlsProps = {
  apiBaseUrl: string
  authToken: string
  cameraDevices: MediaDeviceInfo[]
  canUseTorch: boolean
  demoMode: boolean
  eventId: string
  gateName: string
  isCameraActive: boolean
  isLoadingConfig: boolean
  scannerName: string
  selectedDeviceId: string
  torchEnabled: boolean
  useRearCamera: boolean
  onApiBaseUrlChange: (value: string) => void
  onAuthTokenChange: (value: string) => void
  onClearPayload: () => void
  onDemoModeChange: (value: boolean) => void
  onEventIdChange: (value: string) => void
  onGateNameChange: (value: string) => void
  onLoadConfig: () => void
  onScannerNameChange: (value: string) => void
  onSelectedDeviceIdChange: (value: string) => void
  onToggleCamera: () => void
  onToggleFlash: () => void
  onToggleRearCamera: () => void
}

export const ScannerControls = ({
  apiBaseUrl,
  authToken,
  cameraDevices,
  canUseTorch,
  demoMode,
  eventId,
  gateName,
  isCameraActive,
  isLoadingConfig,
  scannerName,
  selectedDeviceId,
  torchEnabled,
  useRearCamera,
  onApiBaseUrlChange,
  onAuthTokenChange,
  onClearPayload,
  onDemoModeChange,
  onEventIdChange,
  onGateNameChange,
  onLoadConfig,
  onScannerNameChange,
  onSelectedDeviceIdChange,
  onToggleCamera,
  onToggleFlash,
  onToggleRearCamera,
}: ScannerControlsProps) => (
  <section className="space-y-4 rounded-[2rem] border border-white/8 bg-white/5 p-5 shadow-[0_22px_64px_rgba(0,0,0,0.22)] backdrop-blur-xl sm:p-6">
    <div className="flex items-start justify-between gap-3">
      <div>
        <p className="text-[0.7rem] uppercase tracking-[0.3em] text-violet-200/70">Integration</p>
        <h2 className="text-xl font-semibold text-white sm:text-2xl">API và event config</h2>
      </div>
      <label className="inline-flex items-center gap-2 rounded-full border border-white/8 bg-white/5 px-3 py-2 text-sm text-slate-200/80">
        <input
          className="h-4 w-4 rounded border-white/20 bg-white/5"
          type="checkbox"
          checked={demoMode}
          onChange={(event) => onDemoModeChange(event.target.checked)}
        />
        Demo mode
      </label>
    </div>

    <div className="grid gap-3 sm:grid-cols-2">
      <label className="grid gap-2 text-sm text-slate-200/80">
        <span>Event ID</span>
        <input className="rounded-2xl border border-white/10 bg-white/5 px-4 py-3 text-white outline-none transition placeholder:text-slate-500 focus:border-violet-300/70 focus:ring-4 focus:ring-violet-500/15" value={eventId} onChange={(event) => onEventIdChange(event.target.value)} />
      </label>
      <label className="grid gap-2 text-sm text-slate-200/80">
        <span>Gate name</span>
        <input className="rounded-2xl border border-white/10 bg-white/5 px-4 py-3 text-white outline-none transition placeholder:text-slate-500 focus:border-violet-300/70 focus:ring-4 focus:ring-violet-500/15" value={gateName} onChange={(event) => onGateNameChange(event.target.value)} />
      </label>
      <label className="grid gap-2 text-sm text-slate-200/80">
        <span>Scanner name</span>
        <input className="rounded-2xl border border-white/10 bg-white/5 px-4 py-3 text-white outline-none transition placeholder:text-slate-500 focus:border-violet-300/70 focus:ring-4 focus:ring-violet-500/15" value={scannerName} onChange={(event) => onScannerNameChange(event.target.value)} />
      </label>
      <label className="grid gap-2 text-sm text-slate-200/80">
        <span>API base URL</span>
        <input className="rounded-2xl border border-white/10 bg-white/5 px-4 py-3 text-white outline-none transition placeholder:text-slate-500 focus:border-violet-300/70 focus:ring-4 focus:ring-violet-500/15" value={apiBaseUrl} onChange={(event) => onApiBaseUrlChange(event.target.value)} />
      </label>
    </div>

    <label className="grid gap-2 text-sm text-slate-200/80">
      <span>Bearer token</span>
      <input className="rounded-2xl border border-white/10 bg-white/5 px-4 py-3 text-white outline-none transition placeholder:text-slate-500 focus:border-violet-300/70 focus:ring-4 focus:ring-violet-500/15" type="password" value={authToken} onChange={(event) => onAuthTokenChange(event.target.value)} placeholder="Optional" />
    </label>

    <div className="flex flex-wrap gap-3">
      <button className="rounded-2xl bg-gradient-to-r from-violet-200 via-fuchsia-200 to-sky-200 px-4 py-3 text-sm font-semibold text-slate-950 transition hover:-translate-y-0.5" onClick={onToggleCamera} type="button">
        {isCameraActive ? 'Tắt camera' : 'Bật camera'}
      </button>
      <button className="rounded-2xl border border-white/10 bg-white/6 px-4 py-3 text-sm font-semibold text-white transition hover:-translate-y-0.5 hover:bg-white/10 disabled:cursor-not-allowed disabled:opacity-50" onClick={onToggleRearCamera} type="button">
        {useRearCamera ? 'Rear camera' : 'Front camera'}
      </button>
      <button className="rounded-2xl border border-white/10 bg-white/6 px-4 py-3 text-sm font-semibold text-white transition hover:-translate-y-0.5 hover:bg-white/10 disabled:cursor-not-allowed disabled:opacity-50" onClick={onToggleFlash} type="button" disabled={!canUseTorch}>
        {torchEnabled ? 'Tắt đèn flash' : 'Bật đèn flash'}
      </button>
    </div>

    <div className="grid gap-3 sm:grid-cols-2">
      <label className="grid gap-2 text-sm text-slate-200/80">
        <span>Camera device</span>
        <select className="rounded-2xl border border-white/10 bg-white/5 px-4 py-3 text-white outline-none transition focus:border-violet-300/70 focus:ring-4 focus:ring-violet-500/15" value={selectedDeviceId} onChange={(event) => onSelectedDeviceIdChange(event.target.value)}>
          <option value="">Auto select</option>
          {cameraDevices.map((device) => (
            <option key={device.deviceId} value={device.deviceId}>
              {device.label || `Camera ${device.deviceId.slice(0, 6)}`}
            </option>
          ))}
        </select>
      </label>
      <div className="grid gap-2 text-sm text-slate-200/80">
        <span>Load scan config</span>
        <button className="rounded-2xl border border-white/10 bg-white/6 px-4 py-3 text-sm font-semibold text-white transition hover:-translate-y-0.5 hover:bg-white/10 disabled:cursor-not-allowed disabled:opacity-50" onClick={onLoadConfig} type="button" disabled={isLoadingConfig}>
          {isLoadingConfig ? 'Đang tải...' : 'Load scan config'}
        </button>
      </div>
    </div>

    <div className="flex flex-wrap gap-3">
      <button className="rounded-2xl border border-white/10 bg-white/6 px-4 py-3 text-sm font-semibold text-white transition hover:-translate-y-0.5 hover:bg-white/10" onClick={onClearPayload} type="button">
        Clear payload
      </button>
      <span className="rounded-full border border-white/8 bg-white/5 px-3 py-2 text-xs uppercase tracking-[0.22em] text-slate-300/70">{gateName}</span>
    </div>
  </section>
)