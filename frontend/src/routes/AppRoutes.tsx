import { Navigate, useRoutes, type RouteObject } from 'react-router-dom'
import { authRoutes } from '../pages/auth/AuthRoutes'
import { useAuth } from '../hooks/useAuth'
import { dashboardRoutes } from '../pages/dashboard/DashboardRoutes'
import { DefaultLayout } from '../utils/DefaultLayout'

export const AppRoutes = () => {
  const { isAuthenticated } = useAuth()

  const authenticatedRoutes: RouteObject[] = [
    {
      element: <DefaultLayout />,
      children: [
        ...dashboardRoutes,
        {
          path: '*',
          element: <Navigate to="/dashboard" />
        }
        // ...candidaturasRoutes,
        // ...avaliacoesRoutes,
        // ...horariosRoutes
        // Add more feature routes here
      ]
    }
  ]

  const unauthenticatedRoutes: RouteObject[] = [
    {
    //   element: <DefaultLayout />,
      children: [
        ...authRoutes,
        {
          path: '*',
          element: <Navigate to="/login" />
        }
        // Add more public routes here
      ]
    }
  ]

  const routes = isAuthenticated ? authenticatedRoutes : unauthenticatedRoutes
  // const routes = isAuthenticated ? unauthenticatedRoutes : authenticatedRoutes // TOGGLE FOR TESTING PURPOSES


  return useRoutes(routes)
}