import { InternalLink } from '@/admin/components/shared/link/InternalLink'
import { Box, Container, Header, useMantineTheme, Text, Group, TextInput, ActionIcon } from '@mantine/core'
import { IconAlien, IconBrandGithub, IconSearch } from '@tabler/icons'

type Props = {
  children: React.ReactNode
}

export const BlogTemplate: React.FC<Props> = ({ children }) => {
  const theme = useMantineTheme()

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
              <TextInput placeholder="検索する" icon={<IconSearch size={16} />} />
              <ActionIcon
                variant="filled"
                size={36}
                component="a"
                href="https://github.com/itomise"
                target="_blank"
                rel="noopener noreferrer"
              >
                <IconBrandGithub />
              </ActionIcon>
            </Group>
          </Group>
        </Container>
      </Header>
      <Container py={24}>{children}</Container>
    </Box>
  )
}
