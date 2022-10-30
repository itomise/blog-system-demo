import { forwardRef } from 'react'
import { PolymorphicComponentProps } from '@mantine/utils'
import { InputProps, Input as MtInput } from '@mantine/core'

type Props = PolymorphicComponentProps<'input', InputProps>

const Component: React.ForwardRefRenderFunction<HTMLInputElement, Props> = (props: Props, ref) => (
  <MtInput ref={ref} {...props} />
)

export const Input = forwardRef(Component)
