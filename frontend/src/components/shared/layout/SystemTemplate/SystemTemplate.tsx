import { Center, Paper, useMantineTheme } from '@mantine/core'

type Props = {
  children: React.ReactNode
}

export const SystemTemplate: React.FC<Props> = ({ children }) => {
  const theme = useMantineTheme()

  return (
    <Center sx={{ width: '100%', height: '100vh', background: theme.colors.gray[2] }}>
      <Paper p={50} radius="md" sx={{ maxWidth: 400, width: '100%' }} shadow="lg">
        {children}
      </Paper>
    </Center>
  )
}
