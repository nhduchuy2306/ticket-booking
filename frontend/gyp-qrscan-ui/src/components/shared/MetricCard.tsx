type MetricCardProps = {
  label: string
  value: string
  note: string
  accent?: string
}

export const MetricCard = ({ label, value, note, accent = 'from-violet-400 to-cyan-300' }: MetricCardProps) => (
  <div className="rounded-3xl border border-white/8 bg-white/5 p-4 shadow-[0_18px_50px_rgba(0,0,0,0.22)] backdrop-blur">
    <div className={`mb-4 h-1.5 w-16 rounded-full bg-gradient-to-r ${accent}`} />
    <p className="text-xs uppercase tracking-[0.22em] text-slate-300/70">{label}</p>
    <strong className="mt-2 block text-3xl font-semibold tracking-tight text-white">{value}</strong>
    <p className="mt-2 text-sm leading-6 text-slate-300/75">{note}</p>
  </div>
)