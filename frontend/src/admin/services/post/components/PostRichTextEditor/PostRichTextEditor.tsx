import { useState } from 'react'
import { RichTextEditor } from '@mantine/rte'
import { useMantineTheme } from '@mantine/core'

type Props = {
  value: string
  onChange: (value: string) => void
}

export default function PostRichTextEditor({ value, onChange }: Props) {
  const theme = useMantineTheme()

  return (
    <RichTextEditor
      value={value}
      onChange={onChange}
      sx={{
        '.ql-editor a': {
          color: theme.colors.blue,
          textDecoration: 'underline',
        },
      }}
      controls={[
        ['bold', 'strike', 'italic', 'underline', 'link', 'clean'],
        ['h1', 'h2', 'h3', 'h4', 'h5', 'h6'],
        ['sup', 'sub', 'blockquote'],
        ['video', 'image', 'orderedList', 'unorderedList'],
        ['alignLeft', 'alignCenter', 'alignRight'],
        ['code', 'codeBlock'],
      ]}
    />
  )
}
