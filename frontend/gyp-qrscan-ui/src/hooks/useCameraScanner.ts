import { useEffect, useRef, useState } from 'react'

type BarcodeResult = {
  rawValue: string
}

type BarcodeDetectorInstance = {
  detect(source: CanvasImageSource | ImageBitmap): Promise<BarcodeResult[]>
}

type BarcodeDetectorConstructor = {
  new (options?: { formats?: string[] }): BarcodeDetectorInstance
}

declare global {
  interface Window {
    BarcodeDetector?: BarcodeDetectorConstructor
  }
}

type UseCameraScannerOptions = {
  active: boolean
  deviceId: string
  useRearCamera: boolean
  onDetected: (value: string) => void
}

export const useCameraScanner = ({ active, deviceId, useRearCamera, onDetected }: UseCameraScannerOptions) => {
  const videoRef = useRef<HTMLVideoElement | null>(null)
  const canvasRef = useRef<HTMLCanvasElement | null>(null)
  const streamRef = useRef<MediaStream | null>(null)
  const detectorRef = useRef<BarcodeDetectorInstance | null>(null)
  const loopRef = useRef<number | null>(null)
  const lastSeenCodeRef = useRef<string>('')
  const lastSeenAtRef = useRef<number>(0)
  const onDetectedRef = useRef(onDetected)
  const [cameraError, setCameraError] = useState('')
  const [cameraLabel, setCameraLabel] = useState('Rear camera')
  const [devices, setDevices] = useState<MediaDeviceInfo[]>([])
  const [canUseTorch, setCanUseTorch] = useState(false)
  const [torchEnabled, setTorchEnabled] = useState(false)

  useEffect(() => {
    onDetectedRef.current = onDetected
  }, [onDetected])

  useEffect(() => {
    const refreshDevices = async () => {
      if (!navigator.mediaDevices?.enumerateDevices) {
        return
      }

      const mediaDevices = await navigator.mediaDevices.enumerateDevices()
      setDevices(mediaDevices.filter((device) => device.kind === 'videoinput'))
    }

    void refreshDevices()
  }, [])

  useEffect(() => {
    const stop = () => {
      if (loopRef.current !== null) {
        window.clearInterval(loopRef.current)
        loopRef.current = null
      }

      if (streamRef.current) {
        streamRef.current.getTracks().forEach((track) => track.stop())
        streamRef.current = null
      }

      if (videoRef.current) {
        videoRef.current.srcObject = null
      }
    }

    if (!active) {
      stop()
      setCameraError('')
      return undefined
    }

    let cancelled = false

    const open = async () => {
      if (!navigator.mediaDevices?.getUserMedia) {
        setCameraError('Trình duyệt này không hỗ trợ camera web.')
        return
      }

      stop()
      setCameraError('')

      try {
        const stream = await navigator.mediaDevices.getUserMedia({
          video: deviceId
            ? { deviceId: { exact: deviceId } }
            : {
                facingMode: useRearCamera ? { ideal: 'environment' } : { ideal: 'user' },
                width: { ideal: 1280 },
                height: { ideal: 720 },
              },
          audio: false,
        })

        if (cancelled) {
          stream.getTracks().forEach((track) => track.stop())
          return
        }

        streamRef.current = stream
        const [videoTrack] = stream.getVideoTracks()
        const capabilities = videoTrack?.getCapabilities?.()
        setCanUseTorch(Boolean(capabilities && 'torch' in capabilities))
        setTorchEnabled(false)

        if (videoRef.current) {
          videoRef.current.srcObject = stream
          await videoRef.current.play().catch(() => undefined)
        }

        const detectorClass = window.BarcodeDetector
        detectorRef.current = detectorClass ? new detectorClass({ formats: ['qr_code'] }) : null
        setCameraLabel(
          deviceId
            ? devices.find((device) => device.deviceId === deviceId)?.label || 'Selected camera'
            : useRearCamera
              ? 'Rear camera'
              : 'Front camera',
        )

        loopRef.current = window.setInterval(() => {
          const video = videoRef.current
          const canvas = canvasRef.current
          const detector = detectorRef.current

          if (!video || !canvas || !detector || video.readyState < HTMLMediaElement.HAVE_ENOUGH_DATA) {
            return
          }

          const { videoWidth, videoHeight } = video
          if (!videoWidth || !videoHeight) {
            return
          }

          canvas.width = videoWidth
          canvas.height = videoHeight

          const context = canvas.getContext('2d', { willReadFrequently: true })
          if (!context) {
            return
          }

          context.drawImage(video, 0, 0, videoWidth, videoHeight)

          void detector.detect(canvas).then((barcodes) => {
            const rawValue = barcodes[0]?.rawValue?.trim()
            if (!rawValue) {
              return
            }

            const now = Date.now()
            if (rawValue === lastSeenCodeRef.current && now - lastSeenAtRef.current < 1500) {
              return
            }

            lastSeenCodeRef.current = rawValue
            lastSeenAtRef.current = now
            onDetectedRef.current(rawValue)
          })
        }, 500)
      } catch (error) {
        setCameraError(error instanceof Error ? error.message : 'Không thể mở camera.')
      }
    }

    void open()

    return () => {
      cancelled = true
      stop()
    }
  }, [active, deviceId, devices, useRearCamera])

  useEffect(() => {
    if (!torchEnabled || !streamRef.current) {
      return
    }

    const [track] = streamRef.current.getVideoTracks()
    if (!track) {
      return
    }

    const torchConstraints = { advanced: [{ torch: true }] } as unknown as MediaTrackConstraints
    void track.applyConstraints(torchConstraints).catch(() => undefined)
  }, [torchEnabled])

  return {
    videoRef,
    canvasRef,
    devices,
    cameraError,
    cameraLabel,
    canUseTorch,
    torchEnabled,
    setTorchEnabled,
    setDevices,
  }
}