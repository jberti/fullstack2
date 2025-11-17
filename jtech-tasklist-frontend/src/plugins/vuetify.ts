import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import '@mdi/font/css/materialdesignicons.css'

export default createVuetify({
  components,
  directives,
  theme: {
    defaultTheme: 'light',
    themes: {
      light: {
        colors: {
          primary: '#6366F1', // Indigo moderno
          secondary: '#8B5CF6', // Purple
          accent: '#06B6D4', // Cyan
          error: '#EF4444', // Red moderno
          info: '#3B82F6', // Blue
          success: '#10B981', // Emerald
          warning: '#F59E0B', // Amber
          surface: '#FFFFFF',
          background: '#F8FAFC', // Slate-50
          'on-surface': '#1E293B', // Slate-800
          'surface-variant': '#F1F5F9', // Slate-100
        },
      },
      dark: {
        colors: {
          primary: '#818CF8', // Indigo-400
          secondary: '#A78BFA', // Purple-400
          accent: '#22D3EE', // Cyan-400
          error: '#F87171', // Red-400
          info: '#60A5FA', // Blue-400
          success: '#34D399', // Emerald-400
          warning: '#FBBF24', // Amber-400
          surface: '#1E293B', // Slate-800
          background: '#0F172A', // Slate-900
          'on-surface': '#F1F5F9', // Slate-100
          'surface-variant': '#334155', // Slate-700
        },
      },
    },
  },
  defaults: {
    VCard: {
      elevation: 2,
    },
    VBtn: {
      style: 'text-transform: none; font-weight: 500;',
    },
    VTextField: {
      variant: 'outlined',
      density: 'comfortable',
    },
    VSelect: {
      variant: 'outlined',
      density: 'comfortable',
    },
  },
})


