import { Form } from '@/components/shared/form/Form'
import { InputField } from '@/components/shared/form/InputField'
import { AdminTemplate } from '@/components/shared/layout/AdminTemplate'
import { AdminBreadcrumbs } from '@/components/shared/link/AdminBreadcrumbs'
import { queryClient } from '@/libs/react-query'
import { onMutateError } from '@/services/common/api/onMutateError'
import { useGetPost } from '@/services/posts/api/useGetPost'
import { usePublishPost } from '@/services/posts/api/usePublishPost'
import { useUnPublishPost } from '@/services/posts/api/useUnPublishPost'
import { useUpdatePost } from '@/services/posts/api/useUpdatePost'
import { PostRichTextEditor } from '@/services/posts/components/PostRichTextEditor'
import { PostStatus } from '@/services/posts/types'
import { Group, Box, Button, Stack, Tooltip } from '@mantine/core'
import { showNotification } from '@mantine/notifications'
import Head from 'next/head'
import { useRouter } from 'next/router'
import React from 'react'
import { z } from 'zod'
import { PublishOrUnPublishButton } from './_ui/PublishOrUnPublishButton'

const schema = z.object({
  title: z.string().min(1).max(255),
  content: z.string(),
})

type FormType = z.infer<typeof schema>

export const PostsDetailPage: React.FC = () => {
  const router = useRouter()
  const postId = router.query.id as string
  const post = useGetPost(postId)
  const { mutate, isLoading } = useUpdatePost({
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['/posts'] })
      router.push('/admin/posts')
      showNotification({
        color: 'green',
        message: 'ポストを更新しました。',
      })
    },
    onError: onMutateError('ポストの更新に失敗しました。'),
  })
  const { mutate: publishMutate, isLoading: publishing } = usePublishPost({
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['/posts'] })
      router.push('/admin/posts')
      showNotification({
        color: 'green',
        message: 'ポストを公開にしました。',
      })
    },
    onError: onMutateError('ポストの公開に失敗しました。'),
  })
  const { mutate: unPublishMutate, isLoading: unPublishing } = useUnPublishPost({
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['/posts'] })
      router.push('/admin/posts')
      showNotification({
        color: 'green',
        message: 'ポストを非公開にしました。',
      })
    },
    onError: onMutateError('ポストの非公開化に失敗しました。'),
  })

  return (
    <>
      <Head>
        <title>ポスト編集 | itomise admin</title>
      </Head>
      <AdminTemplate>
        <main>
          {post && (
            <Form<FormType>
              onSubmit={(data) =>
                mutate({
                  ...data,
                  id: postId,
                })
              }
              schema={schema}
              defaultValues={{
                title: post.title,
                content: post.content,
              }}
            >
              {({ register, setValue, formState: { errors }, watch }) => (
                <>
                  <Group position="apart" align="flex-start">
                    <AdminBreadcrumbs
                      links={[
                        { title: 'ポスト一覧', href: '/admin/posts' },
                        { title: 'ポスト編集', href: null },
                      ]}
                    />
                    <Group>
                      <PublishOrUnPublishButton
                        status={post.status}
                        otherLoading={isLoading}
                        publishLoading={publishing}
                        unPublishLoading={unPublishing}
                        onPublish={() =>
                          publishMutate({
                            ...watch(),
                            id: postId,
                          })
                        }
                        onUnPublish={() =>
                          unPublishMutate({
                            ...watch(),
                            id: postId,
                          })
                        }
                      />
                      <Button color="blue" type="submit" loading={isLoading} disabled={isLoading}>
                        保存する
                      </Button>
                    </Group>
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
          )}
        </main>
      </AdminTemplate>
    </>
  )
}