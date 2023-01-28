import 'dayjs/locale/ja'
import Head from 'next/head'
import { Box, Button, Stack, Group } from '@mantine/core'
import { AdminTemplate } from '@/components/shared/layout/AdminTemplate'
import { AdminBreadcrumbs } from '@/components/shared/link/AdminBreadcrumbs'
import { PostRichTextEditor } from '@/services/posts/components/PostRichTextEditor'
import { Form } from '@/components/shared/form/Form'
import { InputField } from '@/components/shared/form/InputField'
import { z } from 'zod'
import { TimeField } from '@/components/shared/form/TimeField'
import { DateField } from '@/components/shared/form/DateField'

const schema = z.object({
  title: z.string().min(1).max(255),
  date: z.date().nullable(),
  time: z.date().nullable(),
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
              date: new Date(),
              time: new Date(),
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
                  <Group>
                    <DateField
                      label="作成した日付"
                      size="xs"
                      placeholder="2023.01.01"
                      locale="ja"
                      required
                      clearable={false}
                      value={watch('date')}
                      onChange={(value) => setValue('date', value)}
                    />
                    <TimeField
                      label="作成した時間"
                      size="xs"
                      required
                      value={watch('time')}
                      clearable={false}
                      onChange={(value) => setValue('time', value)}
                    />
                  </Group>
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
