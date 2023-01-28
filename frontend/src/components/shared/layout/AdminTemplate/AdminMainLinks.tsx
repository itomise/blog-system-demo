import { FC } from 'react'
import { IconUsers } from '@tabler/icons'
import { UnstyledButton, Group, ThemeIcon, Text } from '@mantine/core'
import Link from 'next/link'
import { useRouter } from 'next/router'

type MainLinkProps = {
  icon: React.ReactNode
  color: string
  label: string
  href: string
}

const MainLink: FC<MainLinkProps & { current: boolean }> = ({ icon, color, label, href, current }) => (
  <Link href={href} legacyBehavior passHref>
    <UnstyledButton
      sx={(theme) => ({
        display: 'block',
        width: '100%',
        padding: theme.spacing.xs,
        borderRadius: theme.radius.sm,
        color: theme.colorScheme === 'dark' ? theme.colors.dark[0] : theme.black,
        background: current ? theme.colors.blue[0] : undefined,

        '&:hover': {
          backgroundColor: current ? undefined : theme.colors.gray[0],
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
  </Link>
)

const data: MainLinkProps[] = [
  { icon: <IconUsers size={16} />, color: 'blue', label: 'User List', href: '/admin/users' },
]

export const AdminMainLinks: FC = () => {
  const router = useRouter()

  return (
    <div>
      {data.map((link) => (
        <MainLink {...link} key={link.label} current={link.href === router.route} />
      ))}
    </div>
  )
}
