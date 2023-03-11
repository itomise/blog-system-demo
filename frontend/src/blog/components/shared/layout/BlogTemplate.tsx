import { SubmitHandler, useForm } from 'react-hook-form'
import { useCallback } from 'react'
import { useRouter } from 'next/router'
import { IconAlien, IconBrandGithub, IconBrandTwitter, IconSearch } from '@tabler/icons'
import { Box, Container, Header, useMantineTheme, Text, Group, TextInput, ActionIcon } from '@mantine/core'
import { InternalLink } from '@/admin/components/shared/link/InternalLink'

type FormType = {
  query: string
}

type Props = {
  children: React.ReactNode
}

export const BlogTemplate: React.FC<Props> = ({ children }) => {
  const { handleSubmit, register } = useForm<FormType>()
  const router = useRouter()

  const theme = useMantineTheme()

  const onSubmit: SubmitHandler<FormType> = useCallback(
    ({ query }) => {
      const href = `/posts/search?query=${query}`
      router.push(href, href, { shallow: true })
    },
    [router]
  )

  return (
    <Box component="main" sx={{ background: theme.colors.gray[1], minHeight: '100vh' }}>
      <Header height={60}>
        <Container sx={{ height: '100%' }}>
          <Group position="apart" align="center" sx={{ height: '100%' }}>
            <InternalLink href="/" unstyled>
              <Group align="center" spacing="xs">
                <IconAlien size={24} />
                <Text weight="bold" size="md">
                  itomise
                </Text>
              </Group>
            </InternalLink>
            <Group>
              <form onSubmit={handleSubmit(onSubmit)}>
                <TextInput
                  placeholder="検索する"
                  icon={<IconSearch size={16} />}
                  type="search"
                  {...register('query')}
                />
              </form>
              <ActionIcon
                variant="filled"
                size={36}
                component="a"
                color="gray.7"
                href="https://github.com/itomise/blog-system-demo"
                target="_blank"
                rel="noopener noreferrer"
              >
                <IconBrandGithub />
              </ActionIcon>
              <ActionIcon
                variant="filled"
                size={36}
                color="blue"
                component="a"
                href="https://twitter.com/itomise0f"
                target="_blank"
                rel="noopener noreferrer"
              >
                <IconBrandTwitter />
              </ActionIcon>
            </Group>
          </Group>
        </Container>
      </Header>
      <Container py={24}>{children}</Container>
    </Box>
  )
}
