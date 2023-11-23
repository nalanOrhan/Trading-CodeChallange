# Simple Trading Application

This is a simple trading application.

Being a first draft, it uses an in-memory H2 database. Neither the schema, nor the data are explicitly created.

## Required tools and libraries

1. [JDK 17](https://www.oracle.com/java/technologies/downloads/#JDK17)
2. At least [Maven 3.6.3](https://maven.apache.org/download.cgi)

Install both the JDK (define the environment variable `JAVA_HOME` appropriately), then
Maven (define the environment variable `M2_HOME` appropriately).

## Build the code

Execute in a shell

```shell
git clone https://github.com/nalanOrhan/Trading-CodeChallange.git
cd trading
mvn -U clean verify -DskipTests
```

In this way, a new jar file will be created in the `target` folder, e.g.
`target/trading-0.0.2-SNAPSHOT.jar`.

## Run the code

```shell
java -jar target/trading-0.0.2-SNAPSHOT.jar
```

## Test the code

Once you launched the main application (see the previous Chapter), you can test it as

```shell
mvn test
```

Optionally, you can remove the comment to annotation `@SpringBootTest` in file
[`CucumberTest.java`](src/test/java/name/lattuada/trading/tests/CucumberTest.java), so you can run the test without
explicitly having the application running.

In this way, by invoking:

```shell
mvn -U clean verify
```

you can build and test directly the code.

## API

Once the application is started, you can check all the available APIs and models by using
the [Swagger interface](http://localhost:8080/swagger-ui/).

## Test Reports

Once the tests run a pdf report and a html report will be created in test-output package
   
open the HTML report located to `test-output/SparkReport/Spark.html`

open the pdf report located to `test-output/PdfReport/SparkPdf.pdf`
