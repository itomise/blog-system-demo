import { Center, Text } from '@mantine/core'
import { InternalLink } from '@/components/shared/link/InternalLink'
import { SystemTemplate } from '@/components/shared/layout/SystemTemplate'

export const SignUpSentPage: React.FC = () => {
  return (
    <main>
      <SystemTemplate>
        <Text>
          An email for this registration has been sent. <br /> Please check your email to complete your registration.
        </Text>
        <Center mt="lg">
          <InternalLink href="/">Back To Top</InternalLink>
        </Center>
      </SystemTemplate>
    </main>
  )
}
