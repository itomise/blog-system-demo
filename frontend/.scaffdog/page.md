---
name: 'page'
root: 'src/components/pages'
output: '**/*'
questions:
  name: 'Please enter a page name. (The word "Page" is automatically entered)'
---

# `{{ inputs.name | kebab }}/index.ts`

```typescript
export { {{ inputs.name | pascal }}Page } from './{{ inputs.name | pascal }}Page'
```

# `{{ inputs.name | kebab }}/{{ inputs.name | pascal }}Page.tsx`

```typescript
import Head from 'next/head'
import { Container } from '@mantine/core'

export const {{ inputs.name | pascal }}Page: React.FC = () => {
  const template = 'template'
  return (
    <>
      <Head>
        <title>template</title>
      </Head>
      <Container>
        <main>
          <p>{template}</p>
        </main>
      </Container>
    </>
  )
}

```
