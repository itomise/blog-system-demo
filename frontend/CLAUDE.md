# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Development Commands

```bash
# Development
pnpm dev                 # Start Next.js dev server on http://localhost:3000

# Testing
pnpm test               # Run tests once
pnpm test:watch         # Run tests in watch mode

# Build and Production
pnpm build              # Build for production
pnpm start              # Start production server

# Code Quality
pnpm lint               # Lint and auto-fix code in src/

# Code Generation
pnpm scaffdog           # Generate components/pages from templates
```

## Architecture Overview

This is a Next.js 13 blog application with separate admin and public interfaces. The codebase follows a feature-based organization pattern:

```
src/
├── admin/           # Admin panel (CRUD operations for posts/users)
│   ├── components/
│   │   ├── pages/   # Page components for routing
│   │   └── shared/  # Shared admin UI components
│   └── services/    # API calls and business logic
├── blog/            # Public blog interface
├── pages/           # Next.js file-based routing
├── libs/            # External library configurations
└── shared/          # Cross-feature utilities and types
```

### Key Architectural Decisions

1. **Dual API Structure**: 
   - Admin API: `NEXT_PUBLIC_ADMIN_BACKEND_ENDPOINT`
   - Blog API: `NEXT_PUBLIC_BLOG_BACKEND_ENDPOINT`

2. **Data Fetching**: React Query (TanStack Query) for all API interactions
   - Hooks in `*/services/api/` directories
   - Custom configuration: no refetch on window focus, no retry

3. **Forms**: React Hook Form + Zod validation
   - Form schemas in `*/services/types/`
   - Consistent validation patterns across features

4. **Testing**: Vitest + React Testing Library + MSW
   - Tests mirror source structure in `__tests__/`
   - API mocks in `testDouble/handler/`
   - Use `TestApp` wrapper for providers

5. **Component Structure**:
   - Page components handle routing logic
   - UI components in `_ui/` folders
   - Arrow function components (ESLint enforced)

## Technology Stack

- **Framework**: Next.js 13.0.0 with React 18.2.0
- **Language**: TypeScript 4.8.4 (strict mode)
- **UI**: Mantine v5 + Emotion CSS-in-JS
- **State**: React Query for server state
- **Testing**: Vitest with MSW for API mocking
- **Node**: v16.x, pnpm 8.1.0

## Development Guidelines

1. **Imports**: Use `@/` alias for src imports
2. **Components**: Must be arrow functions per ESLint rules
3. **API Calls**: Always use React Query hooks, never direct axios
4. **Testing**: Write tests for new features using existing patterns
5. **Code Generation**: Use `pnpm scaffdog` for new components/pages