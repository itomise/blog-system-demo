#!/bin/zsh

gcloud builds submit --pack=image=gcr.io/itomise-blog/backend --project itomise-blog

