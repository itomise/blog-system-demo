import { PostStatus } from "@/services/posts/types"
import { Tooltip, Button } from "@mantine/core"

type Props = { 
  status: PostStatus
  unPublishLoading: boolean
  publishLoading: boolean
  otherLoading: boolean
  onPublish: () => void
  onUnPublish: () => void 
}

export const PublishOrUnPublishButton: React.FC<Props> = ({ status, unPublishLoading, otherLoading, publishLoading, onPublish, onUnPublish }) => {
  switch(status) {
    case PostStatus.PUBLISHED:
      return (
        <Tooltip label="非公開にする" position="bottom">
          <Button color="green" type="button" loading={unPublishLoading} onClick={onUnPublish} disabled={otherLoading || publishLoading}>
            公開中
          </Button>
        </Tooltip>
      )
    case PostStatus.UN_PUBLISHED:
      return (
        <Tooltip label="公開にする" position="bottom">
          <Button color="gray" type="button" loading={publishLoading} onClick={onPublish} disabled={otherLoading || unPublishLoading}>
            非公開
          </Button>
        </Tooltip>
      )
    default:
      throw new Error(status satisfies never)
  }
  
}
