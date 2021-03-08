# Testerra AzureDevOps Connector

This module for Testerra provides a simple synchronization service for Microsoft AzureDevOps platform.

It will register automatically by using Testerra `ModuleHook`, but you have to provide a valid property file its usage.

---

## Releases

* Latest Release: `1.0-RC-2`


## Requirements

This module requires Testerra version `1-0-RC-30`.

## Usage

Include the following dependency in your project.

Gradle:
````groovy
implementation 'eu.tsystems.mms.tic.testerra:azure-devops-connector:1.0-RC-2'
````

Maven:
````xml
<dependency>
    <groupId>eu.tsystems.mms.tic.testerra</groupId>
    <artifactId>azure-devops-connector</artifactId>
    <version>1.0-RC-2</version>
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

_Publishing to local repo_
```shell
gradle publishToMavenLocal
```

_Publishing to remote repo_
```shell
gradle publish -DdeployUrl=<repo-url> -DdeployUsername=<repo-user> -DdeployPassword=<repo-password>
```

_Set a custom version_
```shell
gradle publish -DmoduleVersion=<version>
```
### ... to GitHub Packages

Some hints for using GitHub Packages as Maven repository

* Deploy URL is https://maven.pkg.github.com/OWNER/REPOSITRY
* As password generate an access token and grant permissions to ``write:packages`` (Settings -> Developer settings -> Personal access token)
