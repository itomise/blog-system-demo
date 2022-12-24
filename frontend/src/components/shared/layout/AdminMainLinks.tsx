import { FC } from 'react'
import { IconUsers } from '@tabler/icons'
import { UnstyledButton, Group, ThemeIcon, Text } from '@mantine/core'
import { InternalLink } from '../link/InternalLink'

type MainLinkProps = {
  icon: React.ReactNode
  color: string
  label: string
  href: string
}

const MainLink: FC<MainLinkProps> = ({ icon, color, label, href }) => (
  <InternalLink href={href}>
    <UnstyledButton
      sx={(theme) => ({
        display: 'block',
        width: '100%',
        padding: theme.spacing.xs,
        borderRadius: theme.radius.sm,
        color: theme.colorScheme === 'dark' ? theme.colors.dark[0] : theme.black,

        '&:hover': {
          backgroundColor: theme.colorScheme === 'dark' ? theme.colors.dark[6] : theme.colors.gray[0],
        },
      })}
    >
      <Group>
        <ThemeIcon color={color} variant="light">
          {icon}
        </ThemeIcon>

        <Text size="sm">{label}</Text>
      </Group>
    </UnstyledButton>
  </InternalLink>
)

const data: MainLinkProps[] = [{ icon: <IconUsers size={16} />, color: 'blue', label: 'ユーザー一覧', href: '/users' }]

export const AdminMainLinks: FC = () => (
  <div>
    {data.map((link) => (
      <MainLink {...link} key={link.label} />
    ))}
  </div>
)
