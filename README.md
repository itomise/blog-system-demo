# Blog System Demo

Currently not open to the public

- https://blog-demo.itomise.com (blog top)
- https://blog-demo.itomise.com/admin/login (blog admin)

## Architecture

- backend
  - Kotlin / Ktor
  - PostgreSQL
  - HikariCP
  - Exposed
  - Jackson
  - SendGrid
  - Auth
    - session
      - Redis
    - jwt
  - Test
    - Ktor
    - JUnit
    - mockk
- frontend
  - Next.js
  - TypeScript
  - Mantine.dev
  - TanStack Query
  - Emotion
  - React Hook Form
  - zod
  - Vitest
- cloud (GCP)
  - Cloud Run
  - Cloud Storage
  - Secret Manager
  - Cloud CDN
  - Load Balancer
  - Cloud SQL
  - MemoryStore (Redis)
- ci/cd
  - test
    - Github Actions
  - build - deploy
    - Cloud build
