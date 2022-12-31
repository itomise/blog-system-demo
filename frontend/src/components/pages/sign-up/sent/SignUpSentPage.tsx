import { Button, Center, Container, Group, Paper, Text, useMantineTheme } from '@mantine/core'
import { InternalLink } from '@/components/shared/link/InternalLink'

export const SignUpSentPage: React.FC = () => {
  const theme = useMantineTheme()

  return (
    <Container sx={{ background: theme.colors.gray[2] }}>
      <main>
        <Center sx={{ width: '100%', height: '100vh' }}>
          <Paper p={80} radius="md" sx={{ maxWidth: 500, width: '100%' }}>
            <Text>本登録のメールを送信しました。 メールを確認し、登録を完了してください。</Text>
            <Group mt="lg" position="center">
              <InternalLink href="/">
                <Button>トップへ戻る</Button>
              </InternalLink>
            </Group>
          </Paper>
        </Center>
      </main>
    </Container>
  )
}
