import Link, { LinkProps } from 'next/link'
import { Anchor } from '@mantine/core'

type Props = LinkProps & {
  children: React.ReactNode
}

export const InternalLink: React.FC<Props> = ({ children, ...restLinkProps }) => (
  <Link {...restLinkProps} legacyBehavior passHref>
    <Anchor
      sx={{
        ':hover': {
          textDecoration: 'none',
        },
      }}
    >
      {children}
    </Anchor>
  </Link>
)
