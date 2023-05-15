# Goal
Profide a simple project “Hello World” to compare classic Spring Boot 3 vs Quarkus (both JAR/native).

Disclamer:
* all native tests were done on Windows with WSL2 (it is known to be a bit low in IOcompare to a native GNU/Linux, native build times are affected by this);
* the code tested is very simple (single controller with a single service withou any intelligence).

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
Duration: 11.5s, around 3.8s per package. JAR size is 17.75Mio.

### Runtime
To launch the project you can use your favorite IDE or `java -jar target/helloworld-sb3-0.0.1-SNAPSHOT.jar`.

Average launch time is about 1.6s (by command line). Fingerprint memory reach 150Mio, then decrease to 120Mio.

### Bench
```
time parallel --jobs 20 curl http://localhost:8080/hello/S%C3%A9bastien{1} ::: $(seq 100000) &> /dev/null
```

Complete test lasted 560.978s.

## Tests (native)
### Build
To create a standard native image, `mvn native:compile -Pnative` can be used or `mvn spring-boot:build-image -Pnative` (this one will generate a complete Docker image).

Duration: 414s. Generated size is 67.6Mio.

### Runtime
To start the pplicaiton, `target/helloworld-sb3` is the best way.

Average launch time is about 1.28s. Fingerprint memory reach 95.3Mio.

### Bench
```
time parallel --jobs 20 curl http://localhost:8080/hello/S%C3%A9bastien{1} ::: $(seq 100000) &> /dev/null
```
Complete test lasted 228.948s


# Quarkus
You can use `mvnw quarkus:dev` to test the project. It will activated the “dev” mode (enable auto-reload for example).

## Tests (legacy JAR)
### Build
```
time for i in $(seq 3)
do
	mvn package
done
```
Duration: 18.439s, around 6.15s per package. Generated size is 15.8Mio (note: not a single JAR all dependencies are split).

### Runtime
To start the application, you can use `java -jar target/helloworld-q3-0.0.1-SNAPSHOT.jar`.

Average launch time is about 0.74s. Fingerprint memory reach 82Mio, then decrease to 69Mio.

### Bench
```
time parallel --jobs 20 curl http://localhost:8080/hello/S%C3%A9bastien{1} ::: $(seq 100000) &> /dev/null
```

Complete test lasted 428.521s in dev mode. Complete test lasted 379.553s in JAR package mode.

## Tests
### Build
The command is quite similar to compile in native: `mvn package -Pnative` (you just have to select the native profile).

Duration: 270s. Generated size is 43.3Mio.

### Runtime
Easy start can bu done with: `target/helloworld-q3-0.0.1-SNAPSHOT-runner`.

Average launch time is about 0.45s. Fingerprint memory reach 50.4Mio.

### Bench
```
time parallel --jobs 20 curl http://localhost:8080/hello/S%C3%A9bastien{1} ::: $(seq 100000) &> /dev/null
```

Complete test lasted 224.392s.

# Summary
## Build
Competitor | Build time | Build size
---|---|---
Spring Boot JAR | 3.8s | 17.75Mio
Quarkus JAR | 6.15s | 15.8Mio
Spring Boot native | 414s | 67.6Mio
Quarkus native | 270s | 43.3Mio

Quarkus produces always the slimmest build (especially in native). Spring Boot is the quickest to generate a JAR.

## Start
Competitor | Start time | Memory fingerprint
---|---|---
Spring Boot JAR | 1.6s | 120Mio
Quarkus JAR | 0.74s | 69Mio
Spring Boot native | 1.28s | 95.3Mio
Quarkus native | 0.45s | 50.4Mio

Quarkus is always the winner (start is at least half and fingerprint around 60% compare to Spring Boot).

## Bench
Competitor | Duration
---|---
Spring Boot JAR | 560.978s
Quarkus JAR | 379.553s
Spring Boot native | 228.948s
Quarkus native | 224.392s

Both natives are quicker.

## To summary…
… the summary:
* Quarkus is the best to start a new project;
* native may be a bit overkill for continuous integration, it may be quicker to deliver a simple JAR in your development environment;
* native is great when runtime prevailling and build time is not a mater.

## Support
Currently, you need to upgrade Quarkus each time a new version is bumped (almost one per month). It may be an issue on non-agile team. Quarkus will introduce a LTS starting from 3.2. More information to come…

Spring Boot did not change is policy: one version every 6 months, 1 year of (free) support on it.

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

Of course, env vars may be stored if you want to reuse them later…