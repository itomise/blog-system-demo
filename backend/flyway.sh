#!/bin/zsh

while true; do
  case "$1" in
    --local )
      export USER=im
      export SCHEMA=main
      export PASSWORD=password
      export URL=jdbc:postgresql://db:5432/local
      export CLEAN_DISABLED=false
    shift ;;
    --prod )
      export USER=$(gcloud secrets versions access latest --secret="db-user-name" --project="itomise-blog")
      export SCHEMA=main
      export PASSWORD=$(gcloud secrets versions access latest --secret="db-user-password" --project="itomise-blog")
      export URL=jdbc:postgresql://gateway.docker.internal:25432/itomise
      export CLEAN_DISABLED=true
    shift ;;
    -- ) shift; break ;;
    * ) break ;;
  esac
done

if [ -z "$URL" ]; then
  echo "環境変数が正しく設定されていません。"
  exit 1
fi

docker-compose run --rm flyway \
  -url=$URL \
  -user=$USER \
  -password=$PASSWORD \
  -schemas=$SCHEMA \
  -cleanDisabled=$CLEAN_DISABLED \
  $1