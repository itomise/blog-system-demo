import { forwardRef } from 'react'
import { PolymorphicComponentProps } from '@mantine/utils'
import { InputProps as MtInputProps, Input as MtInput } from '@mantine/core'

export type InputProps = PolymorphicComponentProps<'input', MtInputProps>

const Component: React.ForwardRefRenderFunction<HTMLInputElement, InputProps> = (props: InputProps, ref) => (
  <MtInput ref={ref} {...props} />
)

export const Input = forwardRef(Component)
