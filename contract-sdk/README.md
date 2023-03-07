This project holds the code to be used to build an SDK
that defines models and interfaces that are the contract between services

## How to publish
```sh
./gradlew -Pversion=$BUILD_VERSION build publish
```

I'm going to store the sdk in the GAR
I followed this tutorial to learn
https://cloud.google.com/artifact-registry/docs/java/store-java

1. `gcloud config set artifacts/repository $REPO_NAME #in this case REPO_NAME=java-repo`
2. `gcloud config set artifacts/location us-central1`
3. `gcloud artifacts packages list --repository=$REPO_NAME`
4. `gcloud artifacts versions list --package=the.group.name:library`

```sh
➜  contract-sdk git:(main) ✗ gcloud artifacts packages list --repository=java-repo
Listing items under project a-proj-to-be-deleted, location us-central1, repository java-repo.

PACKAGE                      CREATE_TIME          UPDATE_TIME
tg-group:contract-sdk        2023-03-06T16:53:16  2023-03-06T16:53:16
the.group.name:contract-sdk  2023-03-06T16:43:11  2023-03-06T16:43:11
the.group.name:library       2023-03-06T16:48:16  2023-03-06T16:48:16

----------

➜  contract-sdk git:(main) ✗ gcloud artifacts versions list --package=tg-group:contract-sdk
Listing items under project a-proj-to-be-deleted, location us-central1, repository java-repo, package tg-group:contract-sdk.

VERSION  DESCRIPTION  CREATE_TIME          UPDATE_TIME
1.0.0                 2023-03-06T

