import { RichTextEditor } from '@mantine/rte'

type Props = {
  value: string
  onChange: (value: string) => void
}

// eslint-disable-next-line react/function-component-definition
export default function PostRichTextEditor({ value, onChange }: Props) {

  return (
    <RichTextEditor
      value={value}
      onChange={onChange}
      className="rich-text-editor"
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
