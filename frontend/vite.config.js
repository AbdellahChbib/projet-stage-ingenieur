import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
    server: {
    host: '0.0.0.0',     // Nécessaire pour que Vite soit accessible depuis Docker
    port: 5173,
    strictPort: true
  }
})
 
