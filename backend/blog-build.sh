#!/bin/zsh -eu

./gradlew :adapters:blogApi:build -x test

gcloud builds submit --project itomise-blog --config=adapters/blogApi/cloudbuild.yaml adapters/blogApi
