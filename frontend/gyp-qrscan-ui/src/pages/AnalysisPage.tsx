import { useMemo } from 'react'
import { MetricCard } from '../components/shared/MetricCard'
import { useScanContext } from '../context/ScanContext'
import { buildOutcomeBreakdown, buildReportInsights, buildSeatRows, buildTimeline } from '../services/reportService'

export const AnalysisPage = () => {
  const { records, acceptedCount, scanCount } = useScanContext()

  const insights = useMemo(() => buildReportInsights(records), [records])
  const breakdown = useMemo(() => buildOutcomeBreakdown(records), [records])
  const timeline = useMemo(() => buildTimeline(records), [records])
  const seatRows = useMemo(() => buildSeatRows(records), [records])

  const acceptanceRate = scanCount === 0 ? 0 : Math.round((acceptedCount / scanCount) * 100)

  return (
    <div className="space-y-6">
      <section className="rounded-[2.25rem] border border-white/8 bg-white/5 p-6 shadow-[0_30px_80px_rgba(0,0,0,0.26)] backdrop-blur-xl sm:p-8">
        <p className="text-[0.7rem] uppercase tracking-[0.35em] text-violet-200/70">Report / analysis</p>
        <h2 className="mt-4 text-3xl font-semibold tracking-tight text-white sm:text-5xl">Quản trị cổng, seat usage và lịch sử check-in.</h2>
        <p className="mt-4 max-w-3xl text-base leading-7 text-slate-300/80 sm:text-lg">
          Màn này dùng cho organizer theo dõi volume scan, tỷ lệ hợp lệ, lượt trùng vé và các seat đã được cập nhật sang USED.
        </p>

        <div className="mt-6 grid gap-3 sm:grid-cols-2 xl:grid-cols-4">
          {insights.map((insight) => (
            <MetricCard key={insight.label} label={insight.label} value={insight.value} note={insight.note} />
          ))}
        </div>
      </section>

      <section className="grid gap-6 xl:grid-cols-[minmax(0,1.1fr)_minmax(320px,0.9fr)]">
        <div className="rounded-[2rem] border border-white/8 bg-white/5 p-5 shadow-[0_22px_64px_rgba(0,0,0,0.22)] backdrop-blur-xl sm:p-6">
          <div className="flex items-start justify-between gap-3">
            <div>
              <p className="text-[0.7rem] uppercase tracking-[0.3em] text-violet-200/70">Outcome breakdown</p>
              <h3 className="text-xl font-semibold text-white sm:text-2xl">Phân phối kết quả quét</h3>
            </div>
            <span className="rounded-full border border-white/8 bg-white/5 px-3 py-1 text-xs uppercase tracking-[0.18em] text-slate-200/75">
              {acceptanceRate}% pass rate
            </span>
          </div>

          <div className="mt-5 space-y-4">
            {breakdown.map((bucket) => (
              <div key={bucket.key}>
                <div className="mb-2 flex items-center justify-between text-sm text-slate-300/80">
                  <span>{bucket.label}</span>
                  <span>{bucket.count}</span>
                </div>
                <div className="h-3 overflow-hidden rounded-full bg-white/8">
                  <div
                    className={`h-full rounded-full bg-gradient-to-r ${bucket.tone}`}
                    style={{ width: `${Math.max(bucket.count * 12, bucket.count > 0 ? 18 : 0)}%` }}
                  />
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="rounded-[2rem] border border-white/8 bg-white/5 p-5 shadow-[0_22px_64px_rgba(0,0,0,0.22)] backdrop-blur-xl sm:p-6">
          <p className="text-[0.7rem] uppercase tracking-[0.3em] text-violet-200/70">Timeline</p>
          <h3 className="text-xl font-semibold text-white sm:text-2xl">Lượt quét theo giờ</h3>

          <div className="mt-5 space-y-3">
            {timeline.length > 0 ? (
              timeline.map((point) => (
                <div key={point.label}>
                  <div className="mb-2 flex items-center justify-between text-sm text-slate-300/80">
                    <span>{point.label}</span>
                    <span>{point.count}</span>
                  </div>
                  <div className="h-3 rounded-full bg-white/8">
                    <div className="h-full rounded-full bg-gradient-to-r from-sky-300 to-violet-300" style={{ width: `${Math.min(100, point.count * 18)}%` }} />
                  </div>
                </div>
              ))
            ) : (
              <div className="rounded-[1.5rem] border border-dashed border-white/10 bg-white/3 p-5 text-sm text-slate-300/70">
                Chưa có timeline quét.
              </div>
            )}
          </div>
        </div>
      </section>

      <section className="grid gap-6 xl:grid-cols-[minmax(0,1.1fr)_minmax(320px,0.9fr)]">
        <div className="rounded-[2rem] border border-white/8 bg-white/5 p-5 shadow-[0_22px_64px_rgba(0,0,0,0.22)] backdrop-blur-xl sm:p-6">
          <p className="text-[0.7rem] uppercase tracking-[0.3em] text-violet-200/70">Seat usage</p>
          <h3 className="text-xl font-semibold text-white sm:text-2xl">Danh sách seat đã update</h3>

          <div className="mt-5 overflow-hidden rounded-[1.5rem] border border-white/8">
            <div className="grid grid-cols-[1.1fr_1.2fr_1fr_0.9fr] gap-2 border-b border-white/8 bg-white/5 px-4 py-3 text-xs uppercase tracking-[0.18em] text-slate-300/60">
              <span>Seat</span>
              <span>Attendee</span>
              <span>Ticket</span>
              <span>Time</span>
            </div>
            <div className="divide-y divide-white/8">
              {seatRows.length > 0 ? (
                seatRows.map((row) => (
                  <div key={`${row.seatKey}-${row.ticketId}`} className="grid grid-cols-[1.1fr_1.2fr_1fr_0.9fr] gap-2 px-4 py-3 text-sm text-slate-200/80">
                    <strong className="text-white">{row.seatKey}</strong>
                    <span>{row.attendeeName}</span>
                    <span>{row.ticketId}</span>
                    <span>{row.time}</span>
                  </div>
                ))
              ) : (
                <div className="px-4 py-5 text-sm text-slate-300/70">Chưa có seat nào được cập nhật.</div>
              )}
            </div>
          </div>
        </div>

        <div className="rounded-[2rem] border border-white/8 bg-white/5 p-5 shadow-[0_22px_64px_rgba(0,0,0,0.22)] backdrop-blur-xl sm:p-6">
          <p className="text-[0.7rem] uppercase tracking-[0.3em] text-violet-200/70">Recommendations</p>
          <h3 className="text-xl font-semibold text-white sm:text-2xl">Hệ thống scan thực tế nên có</h3>

          <ul className="mt-5 space-y-4 text-sm leading-7 text-slate-300/80">
            <li className="rounded-[1.25rem] border border-white/8 bg-white/4 p-4">Đăng nhập staff theo gate và phân quyền theo event để khóa cổng đúng khu vực.</li>
            <li className="rounded-[1.25rem] border border-white/8 bg-white/4 p-4">Chống double-scan ở backend bằng trạng thái ticket/seat và audit log theo từng lần quét.</li>
            <li className="rounded-[1.25rem] border border-white/8 bg-white/4 p-4">Offline queue hoặc retry nếu mạng ở cổng yếu, đặc biệt khi dùng mobile browser.</li>
            <li className="rounded-[1.25rem] border border-white/8 bg-white/4 p-4">Bộ report theo gate, theo giờ, theo seat và export CSV/PDF cho organizer sau sự kiện.</li>
            <li className="rounded-[1.25rem] border border-white/8 bg-white/4 p-4">Scan config theo event để mở đúng time window, đúng gate, đúng loại vé.</li>
          </ul>
        </div>
      </section>
    </div>
  )
}