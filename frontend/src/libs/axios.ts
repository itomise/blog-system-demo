import axios from 'axios'

export const appAxios = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'X-Requested-With': 'HttpRequest1',
    Accept: 'application/json',
    'Content-Type': 'application/json',
  },
  withCredentials: true,
})
