import { useCallback, useMemo, useState } from 'react'
import { AuthContext, type User } from '../contexts/AuthContext'

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(() => {
    const storedUser = localStorage.getItem('@App:user')
    return storedUser ? JSON.parse(storedUser) : null
  })

  const login = useCallback(async (email: string, password: string) => {
    try {
      // Here you would make your API call to authenticate
      const response = await fetch('your-api-url/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
      })

      if (!response.ok) {
        throw new Error('Authentication failed')
      }

      const data = await response.json()
      
      // Store the token
      localStorage.setItem('@App:token', data.token)
      
      // Store the user
      localStorage.setItem('@App:user', JSON.stringify(data.user))
      
      setUser(data.user)
    } catch (error) {
      console.error('Login error:', error)
      throw error
    }
  }, [])

  const logout = useCallback(() => {
    localStorage.removeItem('@App:token')
    localStorage.removeItem('@App:user')
    setUser(null)
  }, [])

  const value = useMemo(
    () => ({
      user,
      isAuthenticated: !!user,
      login,
      logout
    }),
    [user, login, logout]
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}