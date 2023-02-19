import Link, { LinkProps } from 'next/link'
import { Anchor, AnchorProps } from '@mantine/core'

type Props = LinkProps & {
  children: React.ReactNode
  anchorProps?: AnchorProps
}

export const InternalLink: React.FC<Props> = ({ children, anchorProps, ...restLinkProps }) => (
  <Link {...restLinkProps} legacyBehavior passHref>
    <Anchor size="sm" {...anchorProps}>
      {children}
    </Anchor>
  </Link>
)
