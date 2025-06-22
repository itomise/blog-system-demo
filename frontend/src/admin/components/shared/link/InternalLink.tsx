import Link, { LinkProps } from 'next/link'
import { AnchorProps, useMantineTheme } from '@mantine/core'

type Props = LinkProps & {
  children: React.ReactNode
  anchorProps?: AnchorProps
  unstyled?: boolean
}

// eslint-disable-next-line @typescript-eslint/no-unused-vars
export const InternalLink: React.FC<Props> = ({ children, anchorProps, unstyled, ...restLinkProps }) => {
  const theme = useMantineTheme()

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const anchorBase: AnchorProps = unstyled
    ? {
        size: 'md',
        style: {
          color: theme.colors.dark[9],
          ':hover': { textDecoration: 'none' },
        },
      }
    : {}

  return <Link {...restLinkProps}>{children}</Link>
}
