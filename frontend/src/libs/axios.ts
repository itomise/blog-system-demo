import axios, { AxiosError } from 'axios'
import { ADMIN_BACKEND_ENDPOINT, BLOG_BACKEND_ENDPOINT } from '@/shared/api/constants'

const adminAppAxios = axios.create({
  baseURL: ADMIN_BACKEND_ENDPOINT,
  headers: {
    'X-Requested-With': 'HttpRequest',
    Accept: 'application/json',
    'Content-Type': 'application/json',
  },
  withCredentials: true,
})

const blogAppAxios = axios.create({
  baseURL: BLOG_BACKEND_ENDPOINT,
  headers: {
    'X-Requested-With': 'HttpRequest',
    Accept: 'application/json',
    'Content-Type': 'application/json',
  },
  withCredentials: true,
})

const onErrorInterceptor = (error: AxiosError) => {
  // eslint-disable-next-line no-console
  console.error('=== FETCH_CUSTOM_ERROR ===')
  // eslint-disable-next-line no-console
  console.error(error)

  throw error
}

// Add a response interceptor
adminAppAxios.interceptors.response.use((response) => response, onErrorInterceptor)
blogAppAxios.interceptors.response.use((response) => response, onErrorInterceptor)

export { adminAppAxios, blogAppAxios }
