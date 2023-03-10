# Using SBT to generate gRPC models and Stubs

- https://scalapb.github.io/docs/sbt-settings/

## Publishing

To publish, run the command
```sh
sbt publish
```

For now, It is published to `contract-sdk-sbt:contract-sdk-sbt_2.13`

```sh
➜  contract-sdk-sbt git:(port-to-scala-akka-sbt) ✗ gcloud artifacts versions list --package=contract-sdk-sbt:contract-sdk-sbt_2.13
Listing items under project a-proj-to-be-deleted, location us-central1, repository java-repo, package contract-sdk-sbt:contract-sdk-sbt_2.13.

VERSION         DESCRIPTION       CREATE_TIME          UPDATE_TIME
0.1.0-SNAPSHOT  contract-sdk-sbt  2023-03-08T16:41:19  2023-03-08T16:41:19

```


