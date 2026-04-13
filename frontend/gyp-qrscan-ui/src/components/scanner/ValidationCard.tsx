import type { ValidationSummary } from '../../types/scan'

type ValidationCardProps = {
  statusMessage: string
  summary: ValidationSummary | null
}

export const ValidationCard = ({ statusMessage, summary }: ValidationCardProps) => (
  <section className="rounded-[2rem] border border-white/8 bg-white/5 p-5 shadow-[0_22px_64px_rgba(0,0,0,0.22)] backdrop-blur-xl sm:p-6">
    <div className="flex items-start justify-between gap-3">
      <div>
        <p className="text-[0.7rem] uppercase tracking-[0.3em] text-violet-200/70">Latest result</p>
        <h2 className="text-xl font-semibold text-white sm:text-2xl">Thông tin vé sau quét</h2>
      </div>
      <span className="rounded-full border border-white/8 bg-white/5 px-3 py-1 text-xs uppercase tracking-[0.18em] text-slate-200/75">{summary ? summary.outcome : 'idle'}</span>
    </div>

    {summary ? (
      <div className="mt-5 rounded-[1.5rem] border border-white/8 bg-slate-950/55 p-4 sm:p-5">
        <div className="grid gap-4 sm:grid-cols-2">
          <div>
            <p className="text-xs uppercase tracking-[0.2em] text-slate-300/60">Ticket ID</p>
            <strong className="mt-1 block break-all text-lg text-white">{summary.ticketId}</strong>
          </div>
          <div>
            <p className="text-xs uppercase tracking-[0.2em] text-slate-300/60">Event</p>
            <strong className="mt-1 block break-all text-lg text-white">{summary.eventId || 'N/A'}</strong>
          </div>
          <div>
            <p className="text-xs uppercase tracking-[0.2em] text-slate-300/60">Seat</p>
            <strong className="mt-1 block text-lg text-white">{summary.seatKey || summary.seatLabel || 'N/A'}</strong>
          </div>
          <div>
            <p className="text-xs uppercase tracking-[0.2em] text-slate-300/60">Attendee</p>
            <strong className="mt-1 block text-lg text-white">{summary.attendeeName || 'Unknown'}</strong>
          </div>
        </div>

        <div className="mt-4 rounded-2xl border border-white/8 bg-white/5 px-4 py-3 text-sm text-slate-200/80">
          <span className="mr-2 inline-flex rounded-full bg-violet-300/15 px-3 py-1 text-xs uppercase tracking-[0.2em] text-violet-100">
            {summary.outcome === 'success' ? 'Accepted' : summary.outcome}
          </span>
          {statusMessage}
        </div>
      </div>
    ) : (
      <div className="mt-5 rounded-[1.5rem] border border-dashed border-white/10 bg-white/3 p-5 text-sm leading-6 text-slate-300/70">
        Chưa có kết quả quét. Bật camera hoặc dán payload để bắt đầu.
      </div>
    )}
  </section>
)