# Testerra AzureDevOps Connector

<p align="center">
    <a href="https://mvnrepository.com/artifact/io.testerra/azure-devops-connector" title="MavenCentral"><img src="https://img.shields.io/maven-central/v/io.testerra/azure-devops-connector?label=Maven%20Central"></a>
    <a href="/../../commits/" title="Last Commit"><img src="https://img.shields.io/github/last-commit/telekom/testerra-azure-devops-connector?style=flat"></a>
    <a href="/../../issues" title="Open Issues"><img src="https://img.shields.io/github/issues/telekom/testerra-azure-devops-connector?style=flat"></a>
    <a href="./LICENSE" title="License"><img src="https://img.shields.io/badge/License-Apache%202.0-green.svg?style=flat"></a>
</p>

<p align="center">
  <a href="#installation">Installation</a> •
  <a href="#documentation">Documentation</a> •
  <a href="#support-and-feedback">Support</a> •
  <a href="#how-to-contribute">Contribute</a> •
  <a href="#contributors">Contributors</a> •
  <a href="#licensing">Licensing</a>
</p>

## About this module

This module provides additional features for [Testerra Framework](https://github.com/telekom/testerra) for automated tests.

This module provides a simple synchronization service for Microsoft AzureDevOps platform.

It will register automatically by using Testerra `ModuleHook`, but you have to provide a valid property file its usage.

## Setup

### Requirements

![Maven Central](https://img.shields.io/maven-central/v/io.testerra/core/1.0-RC-32?label=Testerra)

### Usage

Include the following dependency in your project.

Gradle:
````groovy
implementation 'io.testerra:azure-devops-connector:1.0'
````

Maven:
````xml
<dependency>
    <groupId>io.testerra</groupId>
    <artifactId>azure-devops-connector</artifactId>
    <version>1.0</version>
</dependency>
````

***Important note:*** This modul is not compatible with the Testerra legacy report (`report` module). Please always use `report-ng`.

## Documentation

### Properties

Add the property file ``azuredevops.properties`` and add project specific settings.

| Property | Default value | Description |
|----------| --------------| ------------|
| azure.url | na. | URL to Azure DevOps server |
| azure.user | na. | User with permissions to execute test cases (create/update test runs, create test results) |
| azure.token | na. | Token must generated in Azure DevOps in Profile -> Security -> Personal Access Token |
| azure.sync.enabled | false | Flag, if test result sync is activated |
| azure.project.root | na. | Root of REST API of the project, e.g. `agile/<project>/_apis` |
| azure.api.version | na. | Current API version of your Azure DevOps system, e.g. `5.1` |
| azure.api.version.get.points | azure.api.version | In some cases the endpoint for getting the test points differs from general API version, e.g. `5.1-preview.2` |
| azure.testplan.id | na. | Define the test plan where your test cases are added. |
| azure.run.name | Current timestamp | Define a custom name for the test run. |

### Test method mapping

Map your tests with the corresponding AzureDevops ticket id.

````java
@Test
@AzureTest(id = 2257)
public void test_case_01() {
    ...
}
````

---

## Publication

This module is deployed and published to Maven Central. All JAR files are signed via Gradle signing plugin.

The following properties have to be set via command line or ``~/.gradle/gradle.properties``

| Property                      | Description                                         |
| ----------------------------- | --------------------------------------------------- |
| `moduleVersion`               | Version of deployed module, default is `1-SNAPSHOT` |
| `deployUrl`                   | Maven repository URL                                |
| `deployUsername`              | Maven repository username                           |
| `deployPassword`              | Maven repository password                           |
| `signing.keyId`               | GPG private key ID (short form)                     |
| `signing.password`            | GPG private key password                            |
| `signing.secretKeyRingFile`   | Path to GPG private key                             |

If all properties are set, call the following to build, deploy and release this module:
````shell
gradle publish closeAndReleaseRepository
````

## Code of Conduct

This project has adopted the [Contributor Covenant](https://www.contributor-covenant.org/) in version 2.0 as our code of conduct. Please see the details in our [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md). All contributors must abide by the code of conduct.

## Working Language

We decided to apply _English_ as the primary project language.  

Consequently, all content will be made available primarily in English. We also ask all interested people to use English as language to create issues, in their code (comments, documentation etc.) and when you send requests to us. The application itself and all end-user faing content will be made available in other languages as needed.


## Support and Feedback

The following channels are available for discussions, feedback, and support requests:

| Type                     | Channel                                                |
| ------------------------ | ------------------------------------------------------ |
| **Issues**   | <a href="/../../issues/new/choose" title="Issues"><img src="https://img.shields.io/github/issues/telekom/testerra-azure-devops-connector?style=flat"></a> |
| **Other Requests**    | <a href="mailto:testerra@t-systems-mms.com" title="Email us"><img src="https://img.shields.io/badge/email-CWA%20team-green?logo=mail.ru&style=flat-square&logoColor=white"></a>   |

## How to Contribute

Contribution and feedback is encouraged and always welcome. For more information about how to contribute, the project structure, as well as additional contribution information, see our [Contribution Guidelines](./CONTRIBUTING.md). By participating in this project, you agree to abide by its [Code of Conduct](./CODE_OF_CONDUCT.md) at all times.

## Contributors

At the same time our commitment to open source means that we are enabling -in fact encouraging- all interested parties to contribute and become part of its developer community.

## Licensing

Copyright (c) 2021 Deutsche Telekom AG.

Licensed under the **Apache License, Version 2.0** (the "License"); you may not use this file except in compliance with the License.

You may obtain a copy of the License at https://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the [LICENSE](./LICENSE) for the specific language governing permissions and limitations under the License.
