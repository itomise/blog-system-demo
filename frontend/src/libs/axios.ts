import axios from 'axios'

export const appAxios = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'X-Requested-With': 'HttpRequest',
    Accept: 'application/json',
    'Content-Type': 'application/json',
  },
  withCredentials: true,
})
