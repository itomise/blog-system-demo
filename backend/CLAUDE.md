# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Build
```bash
# Build all modules
./gradlew build

# Build specific module
./gradlew :adapters:adminApi:build
./gradlew :adapters:blogApi:build
./gradlew :core:build
```

### Run Tests
```bash
# Run all tests
./gradlew test

# Run tests for specific module
./gradlew :adapters:adminApi:test
./gradlew :adapters:blogApi:test
./gradlew :core:test

# Run checks (includes tests)
./gradlew check
```

### Run Applications
```bash
# Start local development environment (PostgreSQL + Redis)
docker-compose up -d db redis

# Run database migrations
./flyway.sh --local migrate

# Run specific application
./gradlew :adapters:adminApi:run
./gradlew :adapters:blogApi:run
```

### Testing Environment
```bash
# Start test databases
docker-compose up -d test-db test-redis

# Test database ports:
# - PostgreSQL: 55432
# - Redis: 56379
```

## Architecture Overview

This is a Kotlin/Ktor-based blog platform backend following Clean Architecture principles with a multi-module structure:

### Module Structure

1. **core/**: Domain logic, entities, and shared utilities
   - Domain entities (User, Post, Session)
   - Security services (JWT, hashing, encryption)
   - Shared exceptions and utilities

2. **adapters/**: Infrastructure implementations
   - **adminApi/**: Admin REST API (port 8080)
     - Authentication (JWT, Session, Google OAuth2)
     - CRUD operations for posts and users
   - **blogApi/**: Public blog API
     - Read-only post endpoints with CSRF protection
   - **blogDb/**: Database layer
     - Exposed ORM with PostgreSQL
     - Redis for session storage
     - Flyway migrations
   - **eventBus/**: Async event handling
     - SendGrid email integration

3. **test/**: Shared test utilities and factories

4. **tool/**: Utility scripts (e.g., data seeding)

### Key Technologies

- **Framework**: Ktor 2.1.0
- **Language**: Kotlin 1.7.10
- **Database**: PostgreSQL with Exposed ORM
- **Cache**: Redis (session storage)
- **Auth**: JWT (Nimbus JOSE) + Session-based + Google OAuth2
- **DI**: Koin
- **Migrations**: Flyway
- **Email**: SendGrid
- **Deployment**: Google Cloud Run + Cloud SQL

### Authentication Flow

The system supports three authentication methods:
1. **JWT Authentication**: For programmatic API access
2. **Session-based Authentication**: For web UI (cookies + Redis)
3. **Google OAuth2**: External user authentication

### Database Schema

Key tables:
- `users`: Core user information
- `user_internal_login_info`: Password-based authentication
- `user_external_login_info`: OAuth provider information
- `posts`: Blog posts with status management
- `sessions`: Active user sessions

### Environment Configuration

Each adapter has environment-specific configs:
- `application.local.conf`: Local development
- `application.prod.conf`: Production
- `application.test.conf`: Testing

Required environment variables:
- `DB_USER`, `DB_PASSWORD`, `DB_URL`
- `JWT_ENCRYPTION_KEY`, `JWT_PRIVATE_KEY`
- `SESSION_SIGN_KEY`
- `SENDGRID_API_KEY`
- `GOOGLE_OAUTH2_CLIENT_SECRET`

### Testing Strategy

- Unit tests for domain logic
- Integration tests for API endpoints
- Test-specific database instances
- Mock implementations for external services (SendGrid)

Tests use separate containers (test-db, test-redis) to avoid conflicts with development environment.