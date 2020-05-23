# Minsk Compiler

Based on https://github.com/terrajobst/minsk

Video tutorials (C#) https://www.youtube.com/playlist?list=PLRAdsfhKI4OWNOSfS7EUu5GRAVmze1t2y

## Building with Maven

### Run The REPL
```sh
mvn exec:java
```

### Build Executable Jar

Creates an executable jar file that will launch the REPL.

```sh
mvn package

java -jar target/minsk-$VERSION.jar
```

### Project Reports

```sh
mvn site
```

Will generate the following [project reports](./target/site/project-reports.html):
* Dependencies with Updates
* Code coverage
* Spot Bugs

### Spot Bugs GUI

```sh
mvn spotbugs:spotbugs spotbugs:gui
```

### View Dependencies with Updates

```sh
mvn versions:display-plugin-updates versions:display-dependency-updates versions:display-property-updates
```
