import Head from 'next/head'
import { Box, Button, Stack, Group } from '@mantine/core'
import { AdminTemplate } from '@/components/shared/layout/AdminTemplate'
import { AdminBreadcrumbs } from '@/components/shared/link/AdminBreadcrumbs'
import { PostRichTextEditor } from '@/services/posts/components/PostRichTextEditor'
import { Form } from '@/components/shared/form/Form'
import { InputField } from '@/components/shared/form/InputField'
import { z } from 'zod'

const schema = z.object({
  title: z.string().min(1).max(255),
  content: z.string(),
})

type FormType = z.infer<typeof schema>

export const PostsNewPage: React.FC = () => {
  return (
    <>
      <Head>
        <title>ポスト新規作成 | itomise admin</title>
      </Head>
      <AdminTemplate>
        <main>
          <Form<FormType>
            onSubmit={(data) => {
              console.log(data)
            }}
            schema={schema}
            defaultValues={{
              content: '',
            }}
          >
            {({ register, setValue, formState: { errors }, watch }) => (
              <>
                <Group position="apart" align="flex-start">
                  <AdminBreadcrumbs
                    links={[
                      { title: 'ポスト一覧', href: '/admin/posts' },
                      { title: 'ポスト新規作成', href: null },
                    ]}
                  />
                  <Box>
                    <Button color="blue" type="submit">
                      保存する
                    </Button>
                  </Box>
                </Group>
                <Stack spacing="sm" mt="sm">
                  <InputField
                    label="タイトル"
                    size="xs"
                    placeholder="タイトル"
                    error={errors.title}
                    registration={register('title')}
                    required
                  />
                </Stack>
                <Box mt="md">
                  <PostRichTextEditor value={watch('content')} onChange={(value) => setValue('content', value)} />
                </Box>
              </>
            )}
          </Form>
        </main>
      </AdminTemplate>
    </>
  )
}
