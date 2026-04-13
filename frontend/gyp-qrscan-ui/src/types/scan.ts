export type ScanOutcome = 'success' | 'duplicate' | 'rejected' | 'offline' | 'error'

export type ScanSource = 'camera' | 'manual' | 'image' | 'demo' | 'config'

export type ParsedTicket = {
  raw: string
  ticketId: string
  eventId?: string
  seatKey?: string
  seatLabel?: string
  attendeeName?: string
  email?: string
  validationHash?: string
}

export type ValidationRecord = {
  id: number
  time: string
  outcome: ScanOutcome
  title: string
  message: string
  ticketId: string
  eventId?: string
  seatKey?: string
  seatLabel?: string
  attendeeName?: string
  source: ScanSource
  raw: string
}

export type ValidationSummary = {
  ok: boolean
  outcome: ScanOutcome
  title: string
  message: string
  ticketId: string
  eventId?: string
  seatKey?: string
  seatLabel?: string
  attendeeName?: string
  source: ScanSource
  raw: string
}

export type ScanConfig = {
  eventName?: string
  gateName?: string
  scannerName?: string
  openFrom?: string
  closeAt?: string
  venueName?: string
  seatMode?: string
}

export type ScanSettings = {
  eventId: string
  gateName: string
  scannerName: string
  apiBaseUrl: string
  authToken: string
  demoMode: boolean
}

export type ScanContextValue = {
  records: ValidationRecord[]
  latestRecord: ValidationRecord | null
  acceptedCount: number
  duplicateCount: number
  rejectedCount: number
  scanCount: number
  registerRecord: (record: Omit<ValidationRecord, 'id' | 'time'>) => void
  clearRecords: () => void
}