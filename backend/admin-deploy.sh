#!/bin/zsh -eu

gcloud run deploy \
  --project itomise-blog \
  --region asia-northeast1 \
  --platform managed \
  --memory=512Mi \
  --max-instances=4 \
  --allow-unauthenticated \
  --image gcr.io/itomise-blog/admin-api:latest \
  --add-cloudsql-instances $(gcloud secrets versions access latest --secret="instance-connection-name" --project="itomise-blog") \
  --vpc-connector cloudrun-vpc \
  --update-secrets=DB_PASSWORD=db-user-password:latest,DB_USER=db-user-name:latest,INSTANCE_UNIX_SOCKET=instance-unix-socket:latest,INSTANCE_CONNECTION_NAME=instance-connection-name:latest,DB_URL=db-url:latest,JWT_PRIVATE_KEY=jwt-private-key:latest,SENDGRID_API_KEY=sendgrid-api-key:latest,SESSION_SIGN_KEY=session-sign-key:latest,JWT_ENCRYPTION_KEY=jwt-encryption-key:latest,REDIS_ENDPOINT=redis-endpoint:latest,GOOGLE_OAUTH2_CLIENT_SECRET=google-oauth2-client-secret:latest \
  admin-api
