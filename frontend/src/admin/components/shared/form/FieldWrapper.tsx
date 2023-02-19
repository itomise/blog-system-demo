import { FieldError } from 'react-hook-form'
import { Input, InputWrapperProps } from '@mantine/core'

type Props = Omit<InputWrapperProps, 'error'> & {
  error: FieldError | undefined
}

export type FieldWrapperPassThroughProps = Omit<Props, 'className' | 'children'>

export const FieldWrapper: React.FC<Props> = ({ children, error, ...rest }) => (
  <Input.Wrapper error={error?.message} {...rest}>
    {children}
  </Input.Wrapper>
)
