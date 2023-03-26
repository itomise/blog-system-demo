#!/bin/zsh -eu

./gradlew :adapters:adminApi:build -x test

gcloud builds submit --project itomise-blog --config=adapters/adminApi/cloudbuild.yaml adapters/adminApi
