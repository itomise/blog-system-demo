import * as React from 'react'
import { useForm, UseFormReturn, SubmitHandler, UseFormProps, FieldValues } from 'react-hook-form'
import { ZodType, ZodTypeDef } from 'zod'
import { zodResolver } from '@hookform/resolvers/zod'

type FormProps<TFormValues extends FieldValues> = {
  onSubmit: SubmitHandler<TFormValues>
  children: (methods: UseFormReturn<TFormValues>) => React.ReactNode
  schema: ZodType<unknown, ZodTypeDef, unknown> | undefined
  className?: string
  options?: UseFormProps<TFormValues>
  id?: string
}

export const Form = <TFormValues extends Record<string, unknown> = Record<string, unknown>>({
  onSubmit,
  children,
  className,
  options,
  id,
  schema,
}: FormProps<TFormValues>) => {
  const methods = useForm<TFormValues>({ ...options, resolver: schema && zodResolver(schema) })
  return (
    <form className={className} onSubmit={methods.handleSubmit(onSubmit)} id={id}>
      {children(methods)}
    </form>
  )
}
