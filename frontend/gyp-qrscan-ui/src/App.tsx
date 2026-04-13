import { Route, Routes } from 'react-router-dom'
import { AppShell } from './components/layout/AppShell'
import { AnalysisPage } from './pages/AnalysisPage'
import { HomePage } from './pages/HomePage'
import { ScannerPage } from './pages/ScannerPage'

function App() {
  return (
    <AppShell>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/scan" element={<ScannerPage />} />
        <Route path="/report" element={<AnalysisPage />} />
      </Routes>
    </AppShell>
  )
}

export default App