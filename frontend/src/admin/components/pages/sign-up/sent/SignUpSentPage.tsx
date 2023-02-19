import { Center, Text } from '@mantine/core'
import { InternalLink } from '@/admin/components/shared/link/InternalLink'
import { SystemTemplate } from '@/admin/components/shared/layout/SystemTemplate'

export const SignUpSentPage: React.FC = () => {
  return (
    <main>
      <SystemTemplate>
        <Text>
          本登録のメールを送信しました。
          <br />
          メールを確認し、登録を完了してください。
        </Text>
        <Center mt="lg">
          <InternalLink href="/admin/login">ログインへ戻る</InternalLink>
        </Center>
      </SystemTemplate>
    </main>
  )
}
