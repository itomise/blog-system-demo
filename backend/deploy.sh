#!/bin/zsh

gcloud run deploy \
  --project itomise-blog \
  --region asia-northeast1 \
  --platform managed \
  --allow-unauthenticated \
  --image gcr.io/itomise-blog/backend \
  --args="-config=src/main/resources/application.prod.conf" \
  --update-secrets=DB_PASSWORD=db-user-password:latest,DB_USER=db-user-name:latest,INSTANCE_UNIX_SOCKET=instance-unix-socket:latest,INSTANCE_CONNECTION_NAME=instance-connection-name:latest,DB_URL=db-url:latest,JWT_PRIVATE_KEY=jwt-private-key:latest,SENDGRID_API_KEY=sendgrid-api-key:latest,SESSION_SIGN_KEY=session-sign-key:latest,JWT_ENCRYPTION_KEY=jwt-encryption-key:latest,REDIS_ENDPOINT=redis-endpoint:latest \
  backend
  
# 備忘録
#  --vpc-connector { redisに接続するための vpc コネクタ名 } \
#  --add-cloudsql-instances { cloud sql のインスタンスコネクション名 } \