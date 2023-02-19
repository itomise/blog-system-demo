import { ADMIN_BACKEND_ENDPOINT } from '@/shared/api/constants'

export const onGoogleOAuth2Request = () => {
  window.location.href = `${ADMIN_BACKEND_ENDPOINT}/auth/google_oauth2`
}
