# Testerra AzureDevOps Connector

This module for Testerra provides a simple synchronization service for Microsoft AzureDevOps platform.

It will register automatically by using Testerra `ModuleHook`, but you have to provide a valid property file its usage.

---

## Usage

### Dependencies

* Latest version: `1-SNAPSHOT`
* Required Testerra version: `1-0-RC-16`

Include the following dependency in your project.

Gradle:
````groovy
implementation 'eu.tsystems.mms.tic.testerra:azure-devops-connector:1-SNAPSHOT'
````

Maven:
````xml
<dependency>
    <groupId>eu.tsystems.mms.tic.testerra</groupId>
    <artifactId>azure-devops-connector</artifactId>
    <version>1-SNAPSHOT</version>
</dependency>
````

### Test method mapping

````java
@Test
@AzureTest(id = 2257)
public void test_case_01() {
    ...
}
````

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

---


## Publication

### ... to a Maven repo

```sh
gradle publishToMavenLocal
```

or pass then properties via. CLI

```sh
gradle publish -DdeployUrl=<repo-url> -DdeployUsername=<repo-user> -DdeployPassword=<repo-password>
```

Set a custom version

```shell script
gradle publish -DmoduleVersion=<version>
```

### ... to Bintray

Upload and publish this module to Bintray:

````sh
gradle bintrayUpload -DmoduleVersion=<version> -DBINTRAY_USER=<bintray-user> -DBINTRAY_API_KEY=<bintray-api-key>
```` 
