import React from 'react'
import { UseFormRegisterReturn } from 'react-hook-form'
import { FieldWrapper, FieldWrapperPassThroughProps } from './FieldWrapper'
import { Input } from './Input'

type InputFieldProps = FieldWrapperPassThroughProps & {
  type?: 'text' | 'email' | 'password'
  className?: string
  registration: Partial<UseFormRegisterReturn>
}

export const InputField: React.FC<InputFieldProps> = ({
  type = 'text',
  label,
  className,
  error,
  registration,
  ...rest
}) => (
  <FieldWrapper label={label} error={error} {...rest}>
    <Input type={type} className={className} {...registration} invalid={!!error} />
  </FieldWrapper>
)
