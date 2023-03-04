import Link, { LinkProps } from 'next/link'
import { Anchor, AnchorProps, useMantineTheme } from '@mantine/core'

type Props = LinkProps & {
  children: React.ReactNode
  anchorProps?: AnchorProps
  unstyled?: boolean
}

export const InternalLink: React.FC<Props> = ({ children, anchorProps, unstyled, ...restLinkProps }) => {
  const theme = useMantineTheme()

  const anchorBase: AnchorProps = unstyled
    ? {
        size: 'md',
        sx: {
          color: theme.colors.dark[9],
          ':hover': { textDecoration: 'none' },
        },
      }
    : {}

  return (
    <Link {...restLinkProps} legacyBehavior passHref>
      <Anchor size="sm" {...anchorBase} {...anchorProps}>
        {children}
      </Anchor>
    </Link>
  )
}
