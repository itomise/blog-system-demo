import axios from 'axios'

export const appAxios = axios.create({
  baseURL: process.env.NODE_ENV === 'development' ? 'http://localhost:8080/api' : '/api',
  headers: {
    'X-Requested-With': 'HttpRequest',
    Accept: 'application/json',
    'Content-Type': 'application/json',
  },
  withCredentials: true,
})
