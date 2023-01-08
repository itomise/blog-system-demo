# blog system

色々使ってみるためのサンプルプロジェクト

## 技術構成

- backend
  - Kotlin / Ktor
  - PostgreSQL
  - HikariCP
  - Exposed
  - Jackson
  - SendGrid
  - Auth
    - session (ktor)
      - Redis
    - jwt (nimbus-jose-jwt / ktor-server-auth-jwt)
  - Test
    - Ktor
    - mockk
- frontend
  - Next.js
  - TypeScript
  - Mantine.dev
  - TanStack Query
  - Emotion
  - React Hook Form
  - zod
- cloud
  - GCP
  - Cloud Run
  - Cloud Storage
  - Secret Manager
  - Cloud CDN
  - Load Balancer
  - Cloud SQL
  - MemoryStore (Redis)
