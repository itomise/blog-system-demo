import Link, { LinkProps } from 'next/link'
import { Anchor, AnchorProps, Box, Breadcrumbs, Text, Title } from '@mantine/core'
import { InternalLink } from '../InternalLink'

type Props = {
  links: {
    title: string
    href: string | null
  }[]
}

export const AdminBreadcrumbs: React.FC<Props> = ({ links }) => {
  return (
    <Breadcrumbs>
      {links.map((l) => {
        if (l.href !== null) {
          return (
            <InternalLink href={l.href} key={l.href} anchorProps={{ color: 'blue' }}>
              {l.title}
            </InternalLink>
          )
        } else {
          return (
            <Title order={1} size="h6" key={l.href}>
              {l.title}
            </Title>
          )
        }
      })}
    </Breadcrumbs>
  )
}
