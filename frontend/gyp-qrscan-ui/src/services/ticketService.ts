import type {
  ParsedTicket,
  ScanConfig,
  ScanOutcome,
  ScanSettings,
  ScanSource,
  ValidationSummary,
} from '../types/scan'

const defaultValidatePath = '/tickets/validate'
const defaultScanConfigPath = '/events/{eventId}/scan-config'

type RecordValue = Record<string, unknown>

const isRecord = (value: unknown): value is RecordValue => typeof value === 'object' && value !== null

const firstString = (...values: unknown[]) =>
  values.find((value): value is string => typeof value === 'string' && value.trim().length > 0)

const safeJsonParse = (value: string) => {
  try {
    return JSON.parse(value) as unknown
  } catch {
    return null
  }
}

export const formatTime = (value: number | Date) =>
  new Intl.DateTimeFormat('vi-VN', {
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    day: '2-digit',
    month: '2-digit',
  }).format(typeof value === 'number' ? new Date(value) : value)

export const normalizePath = (baseUrl: string, path: string) => {
  const normalizedBase = baseUrl.trim().replace(/\/$/, '')
  const normalizedPath = path.startsWith('/') ? path : `/${path}`
  return `${normalizedBase}${normalizedPath}`
}

export const decodeTicketPayload = (rawValue: string): ParsedTicket => {
  const raw = rawValue.trim()

  if (!raw) {
    return { raw, ticketId: '' }
  }

  const jsonValue = safeJsonParse(raw)
  if (isRecord(jsonValue)) {
    return {
      raw,
      ticketId: firstString(
        jsonValue.ticketId,
        jsonValue.ticket_code,
        jsonValue.ticketCode,
        jsonValue.code,
        jsonValue.id,
        jsonValue.token,
        raw,
      )!,
      eventId: firstString(jsonValue.eventId, jsonValue.event_id),
      seatKey: firstString(jsonValue.seatKey, jsonValue.seat_key, jsonValue.seatId, jsonValue.seat_id),
      seatLabel: firstString(jsonValue.seatLabel, jsonValue.seat_label),
      attendeeName: firstString(jsonValue.attendeeName, jsonValue.attendee_name, jsonValue.name),
      email: firstString(jsonValue.email),
      validationHash: firstString(jsonValue.validationHash, jsonValue.validation_hash, jsonValue.hash),
    }
  }

  try {
    const asUrl = new URL(raw)
    const params = asUrl.searchParams

    return {
      raw,
      ticketId: firstString(
        params.get('ticketId'),
        params.get('ticket_code'),
        params.get('ticketCode'),
        params.get('code'),
        params.get('id'),
        asUrl.pathname.split('/').filter(Boolean).at(-1),
        raw,
      )!,
      eventId: firstString(params.get('eventId'), params.get('event_id')),
      seatKey: firstString(params.get('seatKey'), params.get('seat_key'), params.get('seatId'), params.get('seat_id')),
      seatLabel: firstString(params.get('seatLabel'), params.get('seat_label')),
      attendeeName: firstString(params.get('name'), params.get('attendeeName'), params.get('attendee_name')),
      email: firstString(params.get('email')),
      validationHash: firstString(params.get('hash'), params.get('validationHash'), params.get('validation_hash')),
    }
  } catch {
    // fall back to text payloads
  }

  if (raw.includes('&') && raw.includes('=')) {
    const params = new URLSearchParams(raw)
    return {
      raw,
      ticketId: firstString(
        params.get('ticketId'),
        params.get('ticket_code'),
        params.get('ticketCode'),
        params.get('code'),
        params.get('id'),
        raw,
      )!,
      eventId: firstString(params.get('eventId'), params.get('event_id')),
      seatKey: firstString(params.get('seatKey'), params.get('seat_key'), params.get('seatId'), params.get('seat_id')),
      seatLabel: firstString(params.get('seatLabel'), params.get('seat_label')),
      attendeeName: firstString(params.get('name'), params.get('attendeeName'), params.get('attendee_name')),
      email: firstString(params.get('email')),
      validationHash: firstString(params.get('hash'), params.get('validationHash'), params.get('validation_hash')),
    }
  }

  return { raw, ticketId: raw }
}

export const normalizeScanResponse = (
  response: Response,
  body: unknown,
  parsedTicket: ParsedTicket,
  source: ScanSource,
): ValidationSummary => {
  const bodyRecord = isRecord(body) ? body : null
  const responseOutcome =
    (typeof bodyRecord?.outcome === 'string' && bodyRecord.outcome) ||
    (typeof bodyRecord?.status === 'string' && bodyRecord.status) ||
    ''

  const message =
    firstString(bodyRecord?.message, bodyRecord?.detail, bodyRecord?.description, bodyRecord?.result) ||
    (response.ok ? 'Ticket đã được xác thực.' : 'Ticket bị từ chối bởi API.')

  const accepted =
    response.ok &&
    responseOutcome.toUpperCase() !== 'USED' &&
    responseOutcome.toUpperCase() !== 'DUPLICATE' &&
    responseOutcome.toUpperCase() !== 'INVALID' &&
    bodyRecord?.valid !== false &&
    bodyRecord?.success !== false

  const duplicate =
    response.status === 409 ||
    responseOutcome.toUpperCase() === 'USED' ||
    responseOutcome.toUpperCase() === 'DUPLICATE'

  const outcome: ScanOutcome = accepted ? 'success' : duplicate ? 'duplicate' : response.ok ? 'rejected' : 'error'

  return {
    ok: accepted,
    outcome,
    title: firstString(
      bodyRecord?.title,
      bodyRecord?.status,
      accepted ? 'Entry allowed' : duplicate ? 'Ticket already used' : 'Ticket rejected',
    )!,
    message,
    ticketId: firstString(bodyRecord?.ticketId, bodyRecord?.ticket_code, parsedTicket.ticketId)!,
    eventId: firstString(bodyRecord?.eventId, bodyRecord?.event_id, parsedTicket.eventId),
    seatKey: firstString(bodyRecord?.seatKey, bodyRecord?.seat_key, parsedTicket.seatKey),
    seatLabel: firstString(bodyRecord?.seatLabel, bodyRecord?.seat_label, parsedTicket.seatLabel),
    attendeeName: firstString(bodyRecord?.attendeeName, bodyRecord?.attendee_name, parsedTicket.attendeeName),
    source,
    raw: parsedTicket.raw,
  }
}

export const submitTicketValidation = async ({
  settings,
  ticket,
  source,
}: {
  settings: ScanSettings
  ticket: ParsedTicket
  source: ScanSource
}) => {
  if (settings.demoMode) {
    return {
      ok: true,
      outcome: 'success' as const,
      title: 'Demo mode accepted',
      message: `Vé ${ticket.ticketId} hợp lệ trong chế độ demo.`,
      ticketId: ticket.ticketId,
      eventId: ticket.eventId || settings.eventId,
      seatKey: ticket.seatKey,
      seatLabel: ticket.seatLabel,
      attendeeName: ticket.attendeeName,
      source,
      raw: ticket.raw,
    }
  }

  const response = await fetch(normalizePath(settings.apiBaseUrl, defaultValidatePath), {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      ...(settings.authToken ? { Authorization: `Bearer ${settings.authToken.trim()}` } : {}),
    },
    body: JSON.stringify({
      eventId: ticket.eventId || settings.eventId,
      scannerName: settings.scannerName,
      gateName: settings.gateName,
      origin: source,
      ticketId: ticket.ticketId,
      raw: ticket.raw,
      seatKey: ticket.seatKey,
      seatLabel: ticket.seatLabel,
      attendeeName: ticket.attendeeName,
      email: ticket.email,
      validationHash: ticket.validationHash,
      scannedAt: new Date().toISOString(),
    }),
  })

  let body: unknown = null
  try {
    body = await response.json()
  } catch {
    body = null
  }

  return normalizeScanResponse(response, body, ticket, source)
}

export const loadScanConfig = async ({
  apiBaseUrl,
  authToken,
  eventId,
}: {
  apiBaseUrl: string
  authToken: string
  eventId: string
}) => {
  const response = await fetch(
    normalizePath(apiBaseUrl, defaultScanConfigPath.replace('{eventId}', encodeURIComponent(eventId.trim()))),
    {
      headers: {
        ...(authToken ? { Authorization: `Bearer ${authToken.trim()}` } : {}),
      },
    },
  )

  let body: unknown = null
  try {
    body = await response.json()
  } catch {
    body = null
  }

  const bodyRecord = isRecord(body) ? body : null
  const config: ScanConfig = {
    eventName: firstString(bodyRecord?.eventName, bodyRecord?.name, bodyRecord?.title),
    gateName: firstString(bodyRecord?.gateName, bodyRecord?.gate),
    scannerName: firstString(bodyRecord?.scannerName, bodyRecord?.scanner),
    openFrom: firstString(bodyRecord?.openFrom, bodyRecord?.open_from),
    closeAt: firstString(bodyRecord?.closeAt, bodyRecord?.close_at),
    venueName: firstString(bodyRecord?.venueName, bodyRecord?.venue_name),
    seatMode: firstString(bodyRecord?.seatMode, bodyRecord?.seat_mode),
  }

  return { response, body, config }
}

export const getDefaultValidatePath = () => defaultValidatePath