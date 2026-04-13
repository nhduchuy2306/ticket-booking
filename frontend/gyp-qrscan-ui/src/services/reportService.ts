import type { ValidationRecord } from '../types/scan'

export type ReportInsight = {
  label: string
  value: string
  note: string
}

export type ReportTimelinePoint = {
  label: string
  count: number
}

export const buildReportInsights = (records: ValidationRecord[]): ReportInsight[] => {
  const total = records.length
  const accepted = records.filter((record) => record.outcome === 'success').length
  const duplicate = records.filter((record) => record.outcome === 'duplicate').length
  const rejected = records.filter((record) => record.outcome === 'rejected' || record.outcome === 'error').length
  const coverage = total === 0 ? 0 : Math.round((accepted / total) * 100)

  return [
    { label: 'Total scans', value: String(total), note: 'Tổng số lượt quét đã ghi nhận' },
    { label: 'Accepted', value: String(accepted), note: `Tỷ lệ pass ${coverage}%` },
    { label: 'Duplicate', value: String(duplicate), note: 'Lượt quét trùng cần chặn' },
    { label: 'Rejected', value: String(rejected), note: 'Sai vé, sai event hoặc lỗi API' },
  ]
}

export const buildOutcomeBreakdown = (records: ValidationRecord[]) => {
  const buckets: Array<{ key: string; label: string; count: number; tone: string }> = [
    { key: 'success', label: 'Accepted', count: 0, tone: 'from-emerald-300 to-cyan-300' },
    { key: 'duplicate', label: 'Duplicate', count: 0, tone: 'from-amber-300 to-orange-300' },
    { key: 'rejected', label: 'Rejected', count: 0, tone: 'from-rose-300 to-red-300' },
    { key: 'offline', label: 'Offline', count: 0, tone: 'from-sky-300 to-indigo-300' },
    { key: 'error', label: 'Error', count: 0, tone: 'from-fuchsia-300 to-pink-300' },
  ]

  records.forEach((record) => {
    const bucket = buckets.find((item) => item.key === record.outcome)
    if (bucket) {
      bucket.count += 1
    }
  })

  return buckets.filter((bucket) => bucket.count > 0)
}

export const buildTimeline = (records: ValidationRecord[]): ReportTimelinePoint[] => {
  const counts = new Map<string, number>()

  records.forEach((record) => {
    const hour = record.time.slice(0, 2) || '--'
    counts.set(hour, (counts.get(hour) || 0) + 1)
  })

  return Array.from(counts.entries())
    .map(([label, count]) => ({ label: `${label}h`, count }))
    .sort((left, right) => left.label.localeCompare(right.label))
    .slice(-8)
}

export const buildSeatRows = (records: ValidationRecord[]) =>
  records
    .filter((record) => Boolean(record.seatKey))
    .map((record) => ({
      seatKey: record.seatKey || 'N/A',
      ticketId: record.ticketId,
      attendeeName: record.attendeeName || 'Unknown',
      outcome: record.outcome,
      time: record.time,
    }))
    .slice(0, 10)
