import { ZodType, ZodTypeDef } from 'zod'
import { useForm, UseFormReturn, SubmitHandler, UseFormProps, FieldValues } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'

type FormProps<TFormValues extends FieldValues> = {
  onSubmit: SubmitHandler<TFormValues>
  children: (methods: UseFormReturn<TFormValues>) => React.ReactNode
  schema?: ZodType<unknown, ZodTypeDef, unknown>
  className?: string
  options?: UseFormProps<TFormValues>
  id?: string
  defaultValues?: TFormValues
}

export const Form = <TFormValues extends Record<string, unknown> = Record<string, unknown>>({
  onSubmit,
  children,
  className,
  options,
  id,
  schema,
  defaultValues,
}: FormProps<TFormValues>) => {
  const methods = useForm<TFormValues>({
    ...options,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    resolver: schema ? zodResolver(schema as any) : undefined,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    defaultValues: defaultValues as any,
  })
  return (
    <form className={className} onSubmit={methods.handleSubmit(onSubmit)} id={id} noValidate>
      {children(methods)}
    </form>
  )
}
