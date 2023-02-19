import axios from 'axios'
import { ADMIN_BACKEND_ENDPOINT, BLOG_BACKEND_ENDPOINT } from '@/shared/api/constants'

export const adminAppAxios = axios.create({
  baseURL: ADMIN_BACKEND_ENDPOINT,
  headers: {
    'X-Requested-With': 'HttpRequest',
    Accept: 'application/json',
    'Content-Type': 'application/json',
  },
  withCredentials: true,
})

export const blogAppAxios = axios.create({
  baseURL: BLOG_BACKEND_ENDPOINT,
  headers: {
    'X-Requested-With': 'HttpRequest',
    Accept: 'application/json',
    'Content-Type': 'application/json',
  },
  withCredentials: true,
})
