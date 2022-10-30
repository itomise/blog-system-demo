---
name: 'component'
root: 'src/components/pages'
output: '**/*'
questions:
  name: 'Please enter a page name.'
---

# `{{ inputs.name | pascal }}/index.ts`

```typescript
export { {{ inputs.name | pascal }} } from './{{ inputs.name }}'
```

# `{{ inputs.name | pascal }}/{{ inputs.name | pascal }}.tsx`

```typescript
type Props = {
  children: React.ReactNode
}

export const {{ inputs.name | pascal }}: React.FC<Props> = ({ children }) => <div>{children}</div>
```
