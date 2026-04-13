import type { ValidationRecord } from '../../types/scan'

type ScanHistoryListProps = {
  records: ValidationRecord[]
}

export const ScanHistoryList = ({ records }: ScanHistoryListProps) => (
  <section className="rounded-[2rem] border border-white/8 bg-white/5 p-5 shadow-[0_22px_64px_rgba(0,0,0,0.22)] backdrop-blur-xl sm:p-6">
    <div className="flex items-start justify-between gap-3">
      <div>
        <p className="text-[0.7rem] uppercase tracking-[0.3em] text-violet-200/70">Live log</p>
        <h2 className="text-xl font-semibold text-white sm:text-2xl">Lịch sử quét</h2>
      </div>
      <span className="rounded-full border border-white/8 bg-white/5 px-3 py-1 text-xs uppercase tracking-[0.18em] text-slate-200/75">
        {records.length} entries
      </span>
    </div>

    <div className="mt-5 grid gap-3">
      {records.length > 0 ? (
        records.slice(0, 6).map((record) => (
          <article
            className={`flex flex-col gap-3 rounded-[1.5rem] border px-4 py-4 sm:flex-row sm:items-start sm:justify-between ${record.outcome === 'success' ? 'border-emerald-400/20 bg-emerald-400/8' : record.outcome === 'duplicate' ? 'border-amber-300/20 bg-amber-300/8' : 'border-rose-400/20 bg-rose-400/8'}`}
            key={record.id}
          >
            <div>
              <strong className="block text-base text-white">{record.title}</strong>
              <p className="mt-1 text-sm leading-6 text-slate-200/75">{record.message}</p>
            </div>
            <div className="grid gap-1 text-sm text-slate-300/70 sm:text-right">
              <span>{record.ticketId}</span>
              <span>{record.seatKey || 'No seat'}</span>
              <span>{record.time}</span>
            </div>
          </article>
        ))
      ) : (
        <div className="rounded-[1.5rem] border border-dashed border-white/10 bg-white/3 p-5 text-sm text-slate-300/70">
          Chưa có lịch sử quét.
        </div>
      )}
    </div>
  </section>
)