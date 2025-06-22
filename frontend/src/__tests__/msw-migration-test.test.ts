import { describe, it, expect } from 'vitest'
import { http, HttpResponse } from 'msw'
import { server } from './testDouble/server'

describe('MSW v2 Migration Test', () => {
  it('should handle HTTP requests with MSW v2 syntax', async () => {
    // Override handler for this specific test
    server.use(
      http.get('http://localhost:8080/api/test', () => HttpResponse.json({ message: 'MSW v2 is working!' }, { status: 200 }))
    )

    // Make a request to the mocked endpoint
    const response = await fetch('http://localhost:8080/api/test')
    const data = await response.json()

    expect(response.status).toBe(200)
    expect(data).toEqual({ message: 'MSW v2 is working!' })
  })

  it('should handle text responses with MSW v2 syntax', async () => {
    server.use(
      http.get('http://localhost:8081/api/test-text', () => new HttpResponse('Text response from MSW v2', {
          status: 200,
          headers: {
            'Content-Type': 'text/plain',
          },
        }))
    )

    const response = await fetch('http://localhost:8081/api/test-text')
    const text = await response.text()

    expect(response.status).toBe(200)
    expect(text).toBe('Text response from MSW v2')
  })
})