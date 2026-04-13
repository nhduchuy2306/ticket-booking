import { createContext, useContext, useEffect, useMemo, useRef, useState, type ReactNode } from 'react'
import type { ScanContextValue, ValidationRecord } from '../types/scan'

const STORAGE_KEY = 'gyp-qrscan-ui-records'

const ScanContext = createContext<ScanContextValue | null>(null)

const readStoredRecords = () => {
  try {
    const raw = window.localStorage.getItem(STORAGE_KEY)
    if (!raw) {
      return [] as ValidationRecord[]
    }

    const parsed = JSON.parse(raw) as unknown
    if (Array.isArray(parsed)) {
      return parsed as ValidationRecord[]
    }
  } catch {
    // ignore storage parse errors
  }

  return [] as ValidationRecord[]
}

export const ScanProvider = ({ children }: { children: ReactNode }) => {
  const [records, setRecords] = useState<ValidationRecord[]>(() => readStoredRecords())
  const recordIdRef = useRef(records.length + 1)

  useEffect(() => {
    window.localStorage.setItem(STORAGE_KEY, JSON.stringify(records))
  }, [records])

  const value = useMemo<ScanContextValue>(() => {
    const latestRecord = records[0] || null
    const acceptedCount = records.filter((record) => record.outcome === 'success').length
    const duplicateCount = records.filter((record) => record.outcome === 'duplicate').length
    const rejectedCount = records.filter((record) => record.outcome === 'rejected' || record.outcome === 'error').length

    return {
      records,
      latestRecord,
      acceptedCount,
      duplicateCount,
      rejectedCount,
      scanCount: records.length,
      registerRecord: (record) => {
        const nextRecord: ValidationRecord = {
          ...record,
          id: recordIdRef.current++,
          time: new Intl.DateTimeFormat('vi-VN', {
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit',
            day: '2-digit',
            month: '2-digit',
          }).format(new Date()),
        }

        setRecords((current) => [nextRecord, ...current])
      },
      clearRecords: () => setRecords([]),
    }
  }, [records])

  return <ScanContext.Provider value={value}>{children}</ScanContext.Provider>
}

export const useScanContext = () => {
  const context = useContext(ScanContext)
  if (!context) {
    throw new Error('useScanContext must be used inside ScanProvider')
  }

  return context
}
