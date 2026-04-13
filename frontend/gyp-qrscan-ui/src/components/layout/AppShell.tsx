import type { ReactNode } from 'react'
import { NavLink } from 'react-router-dom'

const linkClass = ({ isActive }: { isActive: boolean }) =>
  [
    'rounded-full px-4 py-2 text-sm font-medium transition',
    isActive
      ? 'bg-white text-slate-950 shadow-[0_10px_24px_rgba(255,255,255,0.16)]'
      : 'text-slate-200/80 hover:bg-white/8 hover:text-white',
  ].join(' ')

export const AppShell = ({ children }: { children: ReactNode }) => (
  <div className="min-h-screen bg-[radial-gradient(circle_at_top_left,_rgba(164,93,255,0.24),_transparent_28%),radial-gradient(circle_at_top_right,_rgba(77,166,255,0.14),_transparent_25%),linear-gradient(180deg,_#090b12_0%,_#0c0f18_100%)] text-slate-100">
    <header className="sticky top-0 z-40 border-b border-white/8 bg-slate-950/70 backdrop-blur-xl">
      <div className="mx-auto flex w-full max-w-7xl items-center justify-between gap-4 px-4 py-4 sm:px-6 lg:px-8">
        <div>
          <p className="text-[0.7rem] uppercase tracking-[0.35em] text-violet-200/70">GYP Scanner</p>
          <h1 className="text-lg font-semibold text-white sm:text-xl">Organizer QR console</h1>
        </div>

        <nav className="hidden items-center gap-2 rounded-full border border-white/8 bg-white/5 p-1 md:flex">
          <NavLink to="/" className={linkClass} end>
            Home
          </NavLink>
          <NavLink to="/scan" className={linkClass} end>
            Scanner
          </NavLink>
          <NavLink to="/report" className={linkClass}>
            Report
          </NavLink>
        </nav>
      </div>

      <div className="mx-auto flex w-full max-w-7xl gap-2 px-4 pb-4 sm:px-6 lg:px-8 md:hidden">
        <NavLink to="/" className={linkClass} end>
          Home
        </NavLink>
        <NavLink to="/scan" className={linkClass} end>
          Scanner
        </NavLink>
        <NavLink to="/report" className={linkClass}>
          Report
        </NavLink>
      </div>
    </header>

    <main className="mx-auto w-full max-w-7xl px-4 py-5 sm:px-6 sm:py-6 lg:px-8">{children}</main>
  </div>
)