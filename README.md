# Maya Server

## What is Maya Server ?
Maya server is application which generates mock data like billing, accounts and recommendation data and these data will used by
CGRO application to execute performance application testing. Maya server supports 3 cloud providers AWS,GCP and AZURE are supported 


## Release Notes
### Git Hub Link
https://github.houston.softwaregrp.net/DND/maya-server.git
```
git clone git@github.houston.softwaregrp.net:DND/maya-server.git
cd maya-server

build
mvn clean install
```

### CICD Link
https://cifarm-ore.itom.aws.swinfra.net/hcm/job/DND/job/maya-server/job/v1.0.x/

### Pre requisite to build the project and docker image
```
JDK
Maven
docker
```
## Java 17 Requirements
To build this via maven the following CLI params are additionally needed:
* --add-opens=java.base/java.util=ALL-UNNAMED and --add-opens=java.base/java.lang=ALL-UNNAMED for https://docs.oracle.com/en/java/javase/18/migrate/migrating-jdk-8-later-jdk-releases.html#GUID-12F945EB-71D6-46AF-8C3D-D354FD0B1781
``` 

### Build the project and create a docker image
To build the maya-server project and to create a docker image, please run below commands from projects root directory.
```
mvn clean install
mvn clean install docker:build
```
### How to build and run project locally
To build and run the project locally from Intellij, please use this file maya-server\src\main\resources\application.yml
To run from command use below command,
```
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.config.location=src/main/resources/application.yaml"
```

```
kubectl get pods --all-namespace | grep maya
### User Guide
Refer below user guide for AWS accounts and bill generation.

* https://rndwiki.houston.softwaregrp.net/confluence/display/BRDC/MAYA+Server+User+Guide


##                    Development is still in progress, Please stay tuned for more features.
