import { z } from 'zod'
import React from 'react'
import { useRouter } from 'next/router'
import Head from 'next/head'
import { showNotification } from '@mantine/notifications'
import { Group, Box, Button, Stack } from '@mantine/core'
import { PublishOrUnPublishButton } from './_ui/PublishOrUnPublishButton'
import { onMutateError } from '@/shared/api/onMutateError'
import { queryClient } from '@/libs/react-query'
import { PostRichTextEditor } from '@/admin/services/post/components/PostRichTextEditor'
import { useUpdatePost } from '@/admin/services/post/api/useUpdatePost'
import { useUnPublishPost } from '@/admin/services/post/api/useUnPublishPost'
import { usePublishPost } from '@/admin/services/post/api/usePublishPost'
import { useGetPost } from '@/admin/services/post/api/useGetPost'
import { AdminBreadcrumbs } from '@/admin/components/shared/link/AdminBreadcrumbs'
import { AdminTemplate } from '@/admin/components/shared/layout/AdminTemplate'
import { InputField } from '@/admin/components/shared/form/InputField'
import { Form } from '@/admin/components/shared/form/Form'

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
      queryClient.invalidateQueries({ queryKey: ['admin', 'posts'] })
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
      queryClient.invalidateQueries({ queryKey: ['admin', 'posts'] })
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
      queryClient.invalidateQueries({ queryKey: ['admin', '/posts'] })
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
        {post !== undefined && (
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
      </AdminTemplate>
    </>
  )
}
