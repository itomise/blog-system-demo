import { UseFormRegisterReturn } from 'react-hook-form'
import { Input, InputProps } from './Input'
import { FieldWrapper, FieldWrapperPassThroughProps } from './FieldWrapper'

type InputFieldProps = FieldWrapperPassThroughProps & {
  type?: 'text' | 'email' | 'password'
  className?: string
  registration: Partial<UseFormRegisterReturn>
  inputProps?: InputProps
}

export const InputField: React.FC<InputFieldProps> = ({
  type = 'text',
  label,
  className,
  error,
  registration,
  placeholder,
  inputProps,
  ...rest
}) => (
  <FieldWrapper label={label} error={error} {...rest}>
    <Input
      type={type}
      className={className}
      {...registration}
      {...inputProps}
      placeholder={placeholder}
      invalid={!!error}
    />
  </FieldWrapper>
)
