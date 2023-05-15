# Goal
Profide a simple project “Hello World” to compare classic Spring Boot 3 vs native

# Prerequisite
To execute this project, you can use your favorite IDE or `maven` command.
Java 17 is required.

Native mode for Quarkus needs docker. Spring Boot native mode needs GraalVM: https://github.com/graalvm/graalvm-ce-builds/releases (see bellow for example of installation on GNU/LInux).

For the bench, simple `parallel` and `time` commands were used.

# Spring Boot 3
## Tests (standard JAR)
### Build
```
time for i in $(seq 3)
do
	mvn package
done
```
Duration: 11.5s, around 3.8s per package.

JAR size is 17.75Mio.

### Runtime
`java -jar target/helloworld-sb3-0.0.1-SNAPSHOT.jar` to launch the project (or your favorite IDE).
Average launch time is about 1.6s. Fingerprint memory reach 150Mio, then decrease to 120Mio.

### Bench
`time parallel --jobs 20 curl http://localhost:8080/hello/S%C3%A9bastien{1} ::: $(seq 100000) &> /dev/null`
Complete test lasted 560.978s

## Tests (native mode; limitated a bit by my WSL2)
### Build
`mvn native:compile -Pnative` to create a standard native image or `mvn spring-boot:build-image -Pnative` to generate docker image.
Duration: 414s

Generated size is 67.6Mio

### Runtime
`target/helloworld-sb3` to launch the project.
Average launch time is about 1.28s. Fingerprint memory reach 95.3Mio

### Bench
`time parallel --jobs 20 curl http://localhost:8080/hello/S%C3%A9bastien{1} ::: $(seq 100000) &> /dev/null`
Complete test lasted 228.948s


# Quarkus
You can use `mvnw quarkus:dev` to test the project.

## Tests (leacy JAR)
### Build
```
time for i in $(seq 3)
do
	mvn package
done
```
Duration: 18.439s, around 6.15s per package.

Generated size is 15.8Mio (note: not a single JAR all dependencies are splited).

### Runtime
`java -jar target/helloworld-q3-0.0.1-SNAPSHOT.jar` to launch the project.
Average launch time is about 0.74s. Fingerprint memory reach 82Mio, then decrease to 69Mio.

### Bench
`time parallel --jobs 20 curl http://localhost:8080/hello/S%C3%A9bastien{1} ::: $(seq 100000) &> /dev/null`
Complete test lasted 428.521s in dev mode
Complete test lasted 379.553s in JAR package mode

## Tests (native mode; limitated a bit by my WSL2)
### Build
```
mvn package -Pnative
```
Duration: 270s

Generated size is 43.3Mio

### Runtime
Simple use can be `target/helloworld-q3-0.0.1-SNAPSHOT-runner`.
Average launch time is about 0.45s. Fingerprint memory reach 50.4Mio

### Bench
`time parallel --jobs 20 curl http://localhost:8080/hello/S%C3%A9bastien{1} ::: $(seq 100000) &> /dev/null`
Complete test lasted 224.392s


# Installation example of GraalVM for GNU/Linux
```
# To be adapted with latest version
curl --location https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-22.3.2/graalvm-ce-java17-linux-amd64-22.3.2.tar.gz -O
tar -xf graalvm-ce-java17-linux-amd64-22.3.2.tar.gz
export PATH=/home/sebastienp/graalvm-ce-java17-22.3.2/bin:${PATH}
export GRAALVM_HOME=/home/sebastienp/graalvm-ce-java17-22.3.2
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64 # Not sure this one is needed
gu install native-image
```