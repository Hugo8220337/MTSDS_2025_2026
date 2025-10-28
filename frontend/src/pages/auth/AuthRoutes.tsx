import type { RouteObject } from 'react-router-dom'
import { LoginPage } from './components/LoginPage'
import { RegisterPage } from './components/RegisterPage'

// Lazy load route components
// const LoginPage = lazy(() => import('./LoginPage'))
// const RegisterPage = lazy(() => import('./RegisterPage'))

export const authRoutes: RouteObject[] = [
  {
    path: 'login',
    element: <LoginPage />
  },
  {
    path: 'register',
    element: <RegisterPage />
  }
]
