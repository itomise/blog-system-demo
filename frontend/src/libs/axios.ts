import axios from 'axios'
import { BACKEND_ENDPOINT } from '@/services/common/api/constants'

export const appAxios = axios.create({
  baseURL: BACKEND_ENDPOINT,
  headers: {
    'X-Requested-With': 'HttpRequest',
    Accept: 'application/json',
    'Content-Type': 'application/json',
  },
  withCredentials: true,
})
