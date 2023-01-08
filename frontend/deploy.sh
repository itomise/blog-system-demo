#!/bin/zsh

gcloud run deploy \
  --project itomise-blog \
  --region asia-northeast1 \
  --platform managed \
  --allow-unauthenticated \
  --image gcr.io/itomise-blog/frontend \
  frontend
