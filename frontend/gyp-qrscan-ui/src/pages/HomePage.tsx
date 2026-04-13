import { useNavigate } from 'react-router-dom'
import heroImg from '../assets/hero.png'

type HomeFeatureCardProps = {
  title: string
  description: string
  badge: string
  tone: string
  onClick: () => void
}

const HomeFeatureCard = ({ title, description, badge, tone, onClick }: HomeFeatureCardProps) => (
  <button
    className={`group relative overflow-hidden rounded-[2rem] border border-white/8 bg-white/5 p-5 text-left shadow-[0_22px_64px_rgba(0,0,0,0.22)] backdrop-blur-xl transition hover:-translate-y-1 active:scale-[0.99] ${tone}`}
    onClick={onClick}
    type="button"
  >
    <div className="absolute inset-x-0 top-0 h-1 bg-gradient-to-r from-transparent via-white/50 to-transparent opacity-70" />
    <div className="flex items-start justify-between gap-3">
      <div>
        <p className="text-[0.68rem] uppercase tracking-[0.32em] text-violet-100/70">{badge}</p>
        <h3 className="mt-3 text-2xl font-semibold tracking-tight text-white">{title}</h3>
      </div>
      <span className="rounded-full border border-white/10 bg-white/6 px-3 py-1 text-xs uppercase tracking-[0.18em] text-slate-200/75">
        Open
      </span>
    </div>

    <p className="mt-4 max-w-md text-sm leading-6 text-slate-300/80">{description}</p>
    <div className="mt-5 flex items-center gap-3 text-sm font-medium text-white/90">
      <span>Tap to open</span>
      <span className="transition group-hover:translate-x-1">→</span>
    </div>
  </button>
)

export const HomePage = () => {
  const navigate = useNavigate()

  return (
    <div className="space-y-6 pb-24">
      <section className="grid gap-4 lg:grid-cols-[minmax(0,1.15fr)_minmax(320px,0.85fr)]">
        <div className="overflow-hidden rounded-[2.25rem] border border-white/8 bg-white/5 p-6 shadow-[0_30px_80px_rgba(0,0,0,0.26)] backdrop-blur-xl sm:p-8">
          <p className="text-[0.7rem] uppercase tracking-[0.35em] text-violet-200/70">Mobile-first organizer app</p>
          <h2 className="mt-4 max-w-[12ch] text-4xl font-semibold tracking-tight text-white sm:text-6xl">
            Một app, nhiều card, tối ưu cho cổng check-in.
          </h2>
          <p className="mt-4 max-w-3xl text-base leading-7 text-slate-300/80 sm:text-lg">
            Màn hình chính kiểu mobile app: chạm vào card để vào scan, report, sync, hoặc cấu hình. Mục tiêu là dùng nhanh trên điện thoại nhưng vẫn đủ rõ trên desktop.
          </p>

          <div className="mt-6 flex flex-wrap gap-2">
            {['PWA ready', 'Offline friendly', 'Camera scan', 'Report dashboard'].map((item) => (
              <span key={item} className="rounded-full border border-white/10 bg-white/6 px-3 py-2 text-sm text-slate-100/90">
                {item}
              </span>
            ))}
          </div>
        </div>

        <div className="overflow-hidden rounded-[2.25rem] border border-white/8 bg-white/5 p-4 shadow-[0_30px_80px_rgba(0,0,0,0.26)] backdrop-blur-xl sm:p-6">
          <div className="rounded-[2rem] border border-white/8 bg-slate-950/50 p-4 sm:p-5">
            <img src={heroImg} alt="QR scanner console illustration" className="mx-auto w-full max-w-[360px] object-contain" />
          </div>
          <div className="mt-4 grid grid-cols-3 gap-3 text-center">
            <div className="rounded-3xl border border-white/8 bg-white/5 p-3">
              <p className="text-xs uppercase tracking-[0.2em] text-slate-300/60">Scan</p>
              <strong className="mt-2 block text-xl text-white">Live</strong>
            </div>
            <div className="rounded-3xl border border-white/8 bg-white/5 p-3">
              <p className="text-xs uppercase tracking-[0.2em] text-slate-300/60">Mode</p>
              <strong className="mt-2 block text-xl text-white">PWA</strong>
            </div>
            <div className="rounded-3xl border border-white/8 bg-white/5 p-3">
              <p className="text-xs uppercase tracking-[0.2em] text-slate-300/60">UX</p>
              <strong className="mt-2 block text-xl text-white">Mobile</strong>
            </div>
          </div>
        </div>
      </section>

      <section className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
        <HomeFeatureCard
          title="Scan QR"
          description="Mở camera, quét vé, xác thực seat và đẩy trạng thái lên backend trong một màn hình tối giản."
          badge="Core action"
          tone="from-violet-500/15"
          onClick={() => navigate('/scan')}
        />
        <HomeFeatureCard
          title="Report"
          description="Xem tổng lượt quét, tỷ lệ hợp lệ, seat đã update và các insight vận hành."
          badge="Analytics"
          tone="from-cyan-500/15"
          onClick={() => navigate('/report')}
        />
        <HomeFeatureCard
          title="Sync queue"
          description="Chế độ nền cho scan queue, hữu ích khi mạng yếu ở cổng hoặc cần đồng bộ sau."
          badge="PWA"
          tone="from-emerald-500/15"
          onClick={() => navigate('/scan')}
        />
        <HomeFeatureCard
          title="Settings"
          description="Kết nối API, gate config, camera chọn thiết bị và các tham số vận hành."
          badge="Config"
          tone="from-amber-500/15"
          onClick={() => navigate('/scan')}
        />
      </section>

      <section className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
        <button
          className="rounded-[2rem] border border-white/8 bg-white/5 p-5 text-left shadow-[0_22px_64px_rgba(0,0,0,0.22)] backdrop-blur-xl transition hover:-translate-y-1"
          onClick={() => navigate('/scan')}
          type="button"
        >
          <p className="text-xs uppercase tracking-[0.22em] text-slate-300/60">Quick action</p>
          <strong className="mt-3 block text-2xl text-white">Bật camera</strong>
          <p className="mt-2 text-sm leading-6 text-slate-300/75">Đi thẳng vào màn scan, tối ưu cho check-in tại cổng.</p>
        </button>
        <button
          className="rounded-[2rem] border border-white/8 bg-white/5 p-5 text-left shadow-[0_22px_64px_rgba(0,0,0,0.22)] backdrop-blur-xl transition hover:-translate-y-1"
          onClick={() => navigate('/report')}
          type="button"
        >
          <p className="text-xs uppercase tracking-[0.22em] text-slate-300/60">Quick action</p>
          <strong className="mt-3 block text-2xl text-white">Xem report</strong>
          <p className="mt-2 text-sm leading-6 text-slate-300/75">Mở dashboard phân tích lượt quét và seat usage.</p>
        </button>
        <div className="rounded-[2rem] border border-white/8 bg-white/5 p-5 shadow-[0_22px_64px_rgba(0,0,0,0.22)] backdrop-blur-xl md:col-span-2 xl:col-span-1">
          <p className="text-xs uppercase tracking-[0.22em] text-slate-300/60">Recommended flow</p>
          <ul className="mt-3 space-y-3 text-sm leading-6 text-slate-300/80">
            <li>1. Mở app như mobile app từ màn hình chính.</li>
            <li>2. Chạm Scan QR ở card lớn.</li>
            <li>3. Check report sau sự kiện.</li>
            <li>4. Khi mạng yếu, dùng queue/offline sync.</li>
          </ul>
        </div>
      </section>
    </div>
  )
}