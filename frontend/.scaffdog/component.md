---
name: 'component'
root: 'src'
output: '**/*'
questions:
  name: 'Please enter a page name.'
---

# `{{ inputs.name | pascal }}.tsx`

```typescript
type Props = {
  children: React.ReactNode
}

export const {{ inputs.name | pascal }}: React.FC<Props> = ({ children }) => <div>{children}</div>
```
