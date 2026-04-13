import { useMemo, useState } from 'react'
import heroImg from '../assets/hero.png'
import { CameraPanel } from '../components/scanner/CameraPanel'
import { ScanHistoryList } from '../components/scanner/ScanHistoryList'
import { ScannerControls } from '../components/scanner/ScannerControls'
import { ValidationCard } from '../components/scanner/ValidationCard'
import { MetricCard } from '../components/shared/MetricCard'
import { useScanContext } from '../context/ScanContext'
import { useCameraScanner } from '../hooks/useCameraScanner'
import { decodeTicketPayload, formatTime, getDefaultValidatePath, loadScanConfig, normalizePath, submitTicketValidation } from '../services/ticketService'
import type { ScanSettings, ValidationSummary } from '../types/scan'

const defaultSettings: ScanSettings = {
  eventId: 'EVT-2026-0424',
  gateName: 'Main Entrance',
  scannerName: 'Gate A - Device 01',
  apiBaseUrl: '/api',
  authToken: '',
  demoMode: false,
}

export const ScannerPage = () => {
  const { records, latestRecord, acceptedCount, duplicateCount, rejectedCount, scanCount, registerRecord, clearRecords } =
    useScanContext()
  const [settings, setSettings] = useState(defaultSettings)
  const [manualPayload, setManualPayload] = useState('')
  const [statusMessage, setStatusMessage] = useState('Bật camera hoặc dán payload QR để xác thực vé.')
  const [statusTitle, setStatusTitle] = useState('Sẵn sàng quét')
  const [validationSummary, setValidationSummary] = useState<ValidationSummary | null>(null)
  const [isCameraActive, setIsCameraActive] = useState(false)
  const [selectedDeviceId, setSelectedDeviceId] = useState('')
  const [useRearCamera, setUseRearCamera] = useState(true)
  const [isLoadingConfig, setIsLoadingConfig] = useState(false)
  const [configMessage, setConfigMessage] = useState('Chưa tải scan config từ backend.')

  const handleScan = async (rawValue: string, source: ValidationSummary['source']) => {
    const parsedTicket = decodeTicketPayload(rawValue)

    if (!parsedTicket.ticketId) {
      const invalidSummary: ValidationSummary = {
        ok: false,
        outcome: 'rejected',
        title: 'QR payload trống',
        message: 'Không đọc được ticketId trong dữ liệu QR.',
        ticketId: '',
        source,
        raw: rawValue,
      }

      setValidationSummary(invalidSummary)
      setStatusTitle(invalidSummary.title)
      setStatusMessage(invalidSummary.message)
      registerRecord(invalidSummary)
      return
    }

    const summary = await submitTicketValidation({
      settings,
      ticket: parsedTicket,
      source,
    })

    setValidationSummary(summary)
    setStatusTitle(summary.title)
    setStatusMessage(summary.message)
    registerRecord(summary)
  }

  const { videoRef, cameraError, cameraLabel, canUseTorch, torchEnabled, setTorchEnabled, devices } = useCameraScanner({
    active: isCameraActive,
    deviceId: selectedDeviceId,
    useRearCamera,
    onDetected: (value) => {
      void handleScan(value, 'camera')
    },
  })

  const metrics = useMemo(
    () => [
      { label: 'Quét hôm nay', value: String(scanCount), note: 'Tổng lượt đọc QR' },
      { label: 'Hợp lệ', value: String(acceptedCount), note: 'Cho phép vào cổng' },
      { label: 'Bị từ chối', value: String(rejectedCount), note: 'Sai vé hoặc sai sự kiện' },
      { label: 'Trùng vé', value: String(duplicateCount), note: 'Cần chặn check-in lại' },
    ],
    [acceptedCount, duplicateCount, rejectedCount, scanCount],
  )

  const loadConfig = async () => {
    if (!settings.eventId.trim()) {
      setConfigMessage('Cần eventId để tải scan config.')
      return
    }

    setIsLoadingConfig(true)
    setConfigMessage('Đang tải scan config từ backend...')

    try {
      const { response, config } = await loadScanConfig({
        apiBaseUrl: settings.apiBaseUrl,
        authToken: settings.authToken,
        eventId: settings.eventId,
      })

      if (!response.ok) {
        setConfigMessage(`Không tải được scan config: ${response.statusText}`)
        return
      }

      setSettings((current) => ({
        ...current,
        gateName: config.gateName || current.gateName,
        scannerName: config.scannerName || current.scannerName,
      }))
      setConfigMessage(`${config.eventName || 'Scan config loaded'}. ${response.status} OK.`)
    } catch (error) {
      setConfigMessage(error instanceof Error ? `Không tải được scan config: ${error.message}` : 'Không tải được scan config.')
    } finally {
      setIsLoadingConfig(false)
    }
  }

  const submitManualPayload = async () => {
    const value = manualPayload.trim()
    if (!value) {
      setStatusTitle('Chưa có payload')
      setStatusMessage('Dán QR payload hoặc mã vé vào ô bên dưới trước khi xác thực.')
      return
    }

    await handleScan(value, 'manual')
  }

  const handleDemoScan = async () => {
    await handleScan(
      JSON.stringify({
        ticketId: 'TK-9032-44A',
        eventId: settings.eventId,
        seatKey: 'A-12',
        seatLabel: 'A12',
        attendeeName: 'Nguyen Van A',
        validationHash: 'demo-qr-validate-hash',
      }),
      'demo',
    )
  }

  return (
    <div className="space-y-6">
      <section className="grid gap-6 xl:grid-cols-[minmax(0,1.18fr)_minmax(320px,0.82fr)]">
        <div className="rounded-[2.25rem] border border-white/8 bg-white/5 p-6 shadow-[0_30px_80px_rgba(0,0,0,0.26)] backdrop-blur-xl sm:p-8">
          <p className="text-[0.7rem] uppercase tracking-[0.35em] text-violet-200/70">Organizer QR scan console</p>
          <h2 className="mt-4 max-w-[11ch] text-4xl font-semibold tracking-tight text-white sm:text-6xl">
            Check in vé nhanh, rõ trạng thái ghế, chống scan trùng.
          </h2>
          <p className="mt-4 max-w-3xl text-base leading-7 text-slate-300/80 sm:text-lg">
            Ứng dụng web responsive cho staff tại cổng, tối ưu mobile trước, desktop vẫn đủ rõ để vận hành quầy check-in.
            Camera preview, API validation, seat usage log và realtime trạng thái đều nằm trên một màn hình.
          </p>

          <div className="mt-6 flex flex-wrap gap-2">
            {['Camera web', 'Scan từ ảnh', 'Manual fallback', 'API validation', 'Seat used tracking'].map((item) => (
              <span key={item} className="rounded-full border border-white/10 bg-white/6 px-3 py-2 text-sm text-slate-100/90">
                {item}
              </span>
            ))}
          </div>

          <div className="mt-6 flex flex-wrap gap-3">
            <button
              className="rounded-2xl bg-gradient-to-r from-violet-200 via-fuchsia-200 to-sky-200 px-5 py-3 text-sm font-semibold text-slate-950 transition hover:-translate-y-0.5"
              onClick={() => setIsCameraActive(true)}
              type="button"
            >
              Bật camera
            </button>
            <button
              className="rounded-2xl border border-white/10 bg-white/6 px-5 py-3 text-sm font-semibold text-white transition hover:-translate-y-0.5 hover:bg-white/10"
              onClick={handleDemoScan}
              type="button"
            >
              Demo một lượt quét
            </button>
          </div>
        </div>

        <div className="rounded-[2.25rem] border border-white/8 bg-white/5 p-4 shadow-[0_30px_80px_rgba(0,0,0,0.26)] backdrop-blur-xl sm:p-6">
          <div className="rounded-[2rem] border border-white/8 bg-slate-950/50 p-4 sm:p-5">
            <img src={heroImg} alt="Scanner illustration" className="mx-auto w-full max-w-[420px] object-contain" />
          </div>
          <div className="mt-4 grid gap-3 sm:grid-cols-3">
            <MetricCard label={metrics[0].label} value={metrics[0].value} note={metrics[0].note} />
            <MetricCard label={metrics[1].label} value={metrics[1].value} note={metrics[1].note} accent="from-emerald-300 to-cyan-300" />
            <MetricCard label={metrics[3].label} value={metrics[3].value} note={metrics[3].note} accent="from-amber-300 to-orange-300" />
          </div>
        </div>
      </section>

      <section className="grid gap-6 xl:grid-cols-[minmax(0,1.1fr)_minmax(300px,0.9fr)]">
        <CameraPanel cameraError={cameraError} cameraLabel={cameraLabel} gateName={settings.gateName} isCameraActive={isCameraActive} videoRef={videoRef} />

        <ScannerControls
          apiBaseUrl={settings.apiBaseUrl}
          authToken={settings.authToken}
          canUseTorch={canUseTorch}
          cameraDevices={devices}
          demoMode={settings.demoMode}
          eventId={settings.eventId}
          gateName={settings.gateName}
          isCameraActive={isCameraActive}
          isLoadingConfig={isLoadingConfig}
          scannerName={settings.scannerName}
          selectedDeviceId={selectedDeviceId}
          torchEnabled={torchEnabled}
          useRearCamera={useRearCamera}
          onApiBaseUrlChange={(value) => setSettings((current) => ({ ...current, apiBaseUrl: value }))}
          onAuthTokenChange={(value) => setSettings((current) => ({ ...current, authToken: value }))}
          onClearPayload={() => setManualPayload('')}
          onDemoModeChange={(value) => setSettings((current) => ({ ...current, demoMode: value }))}
          onEventIdChange={(value) => setSettings((current) => ({ ...current, eventId: value }))}
          onGateNameChange={(value) => setSettings((current) => ({ ...current, gateName: value }))}
          onLoadConfig={loadConfig}
          onScannerNameChange={(value) => setSettings((current) => ({ ...current, scannerName: value }))}
          onSelectedDeviceIdChange={setSelectedDeviceId}
          onToggleCamera={() => setIsCameraActive((current) => !current)}
          onToggleFlash={() => setTorchEnabled((current) => !current)}
          onToggleRearCamera={() => setUseRearCamera((current) => !current)}
        />
      </section>

      <section className="grid gap-6 xl:grid-cols-[minmax(0,1.1fr)_minmax(300px,0.9fr)]">
        <ValidationCard statusMessage={statusMessage} summary={validationSummary} />

        <section className="rounded-[2rem] border border-white/8 bg-white/5 p-5 shadow-[0_22px_64px_rgba(0,0,0,0.22)] backdrop-blur-xl sm:p-6">
          <div className="flex items-start justify-between gap-3">
            <div>
              <p className="text-[0.7rem] uppercase tracking-[0.3em] text-violet-200/70">Manual input</p>
              <h2 className="text-xl font-semibold text-white sm:text-2xl">QR payload / raw code</h2>
            </div>
            <span className="rounded-full border border-white/8 bg-white/5 px-3 py-1 text-xs uppercase tracking-[0.18em] text-slate-200/75">{statusTitle}</span>
          </div>

          <textarea
            className="mt-5 min-h-36 w-full rounded-3xl border border-white/10 bg-white/5 px-4 py-4 text-sm leading-6 text-white outline-none placeholder:text-slate-500 focus:border-violet-300/70 focus:ring-4 focus:ring-violet-500/15"
            rows={6}
            value={manualPayload}
            onChange={(event) => setManualPayload(event.target.value)}
            placeholder="Dán JSON, URL hoặc chuỗi mã vé vào đây"
          />

          <div className="mt-4 flex flex-wrap gap-3">
            <button className="rounded-2xl bg-gradient-to-r from-violet-200 via-fuchsia-200 to-sky-200 px-4 py-3 text-sm font-semibold text-slate-950 transition hover:-translate-y-0.5" onClick={submitManualPayload} type="button">
              Xác thực payload
            </button>
            <button className="rounded-2xl border border-white/10 bg-white/6 px-4 py-3 text-sm font-semibold text-white transition hover:-translate-y-0.5 hover:bg-white/10" onClick={clearRecords} type="button">
              Clear history
            </button>
          </div>

          <div className="mt-4 rounded-[1.5rem] border border-white/8 bg-white/4 p-4 text-sm text-slate-300/75">
            <strong className="block text-white">Scan config</strong>
            <p className="mt-2 leading-6">{configMessage}</p>
            <p className="mt-2 text-slate-400">
              POST <span className="text-amber-200">{normalizePath(settings.apiBaseUrl, getDefaultValidatePath())}</span> để xác thực vé và update seat status sang <span className="text-amber-200">USED</span>.
            </p>
          </div>
        </section>
      </section>

      <section className="grid gap-6 xl:grid-cols-[minmax(0,1.1fr)_minmax(300px,0.9fr)]">
        <ScanHistoryList records={records} />

        <section className="rounded-[2rem] border border-white/8 bg-white/5 p-5 shadow-[0_22px_64px_rgba(0,0,0,0.22)] backdrop-blur-xl sm:p-6">
          <p className="text-[0.7rem] uppercase tracking-[0.3em] text-violet-200/70">Live analytics</p>
          <h2 className="mt-2 text-xl font-semibold text-white sm:text-2xl">Trạng thái vận hành</h2>

          <div className="mt-5 grid gap-3 sm:grid-cols-2">
            {metrics.map((metric) => (
              <MetricCard key={metric.label} label={metric.label} value={metric.value} note={metric.note} />
            ))}
          </div>

          <div className="mt-5 rounded-[1.5rem] border border-white/8 bg-slate-950/55 p-4">
            <p className="text-sm uppercase tracking-[0.2em] text-slate-300/60">Latest seat update</p>
            <strong className="mt-2 block text-lg text-white">{latestRecord?.seatKey || latestRecord?.seatLabel || 'No seat marked yet'}</strong>
            <p className="mt-2 text-sm leading-6 text-slate-300/75">
              {latestRecord
                ? `Seat ${latestRecord.seatKey || latestRecord.seatLabel || 'N/A'} đã được chuyển sang USED và ghi nhận lúc ${latestRecord.time}.`
                : 'Khi API xác nhận thành công, seat status sẽ được đẩy vào log ở đây.'}
            </p>
          </div>

          <div className="mt-5 rounded-[1.5rem] border border-white/8 bg-white/4 p-4 text-sm text-slate-300/75">
            <p className="text-xs uppercase tracking-[0.2em] text-slate-300/60">Quick summary</p>
            <div className="mt-3 grid gap-2">
              <div className="flex items-center justify-between"><span>Accepted</span><strong className="text-white">{acceptedCount}</strong></div>
              <div className="flex items-center justify-between"><span>Duplicate</span><strong className="text-white">{duplicateCount}</strong></div>
              <div className="flex items-center justify-between"><span>Rejected</span><strong className="text-white">{rejectedCount}</strong></div>
              <div className="flex items-center justify-between"><span>Updated</span><strong className="text-white">{formatTime(Date.now())}</strong></div>
            </div>
          </div>
        </section>
      </section>
    </div>
  )
}