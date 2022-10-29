import { InputProps, Input as MtInput } from '@mantine/core'
import { PolymorphicComponentProps } from '@mantine/utils'
import React from 'react'

type Props = PolymorphicComponentProps<'input', InputProps>

const Component: React.ForwardRefRenderFunction<HTMLInputElement, Props> = (props: Props, ref) => (
  <MtInput ref={ref} {...props} />
)

export const Input = React.forwardRef(Component)
