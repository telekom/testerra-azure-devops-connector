# Testerra AzureDevOps Connector

This module for Testerra provides a simple synchronization service for Microsoft AzureDevOps platform.

It will register automatically by using Testerra `ModuleHook`, but you have to provide a valid property file its usage.

## Usage

### Dependencies

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

### Add property file

Add the property file ``azuredevops.properties`` and add project specific settings.

### Add test method mapping
