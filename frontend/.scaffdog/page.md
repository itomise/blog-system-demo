---
name: 'page'
root: 'src/components/pages'
output: '**/*'
questions:
  name: 'Please enter a page name.'
---

# `{{ inputs.name | kebab }}/index.ts`

```typescript
export { {{ inputs.name | pascal }} } from './{{ inputs.name }}'
```

# `{{ inputs.name | kebab }}/{{ inputs.name | pascal }}.tsx`

```typescript
import Head from 'next/head'
import { Container } from '@mantine/core'

export const {{ inputs.name | pascal }}: React.FC = () => {
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
