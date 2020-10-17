## What is SARIF?

The Static Analysis Results Interchange Format (SARIF)
is an industry standard format for the output of static analysis tools.

[Website](https://sarifweb.azurewebsites.net/)
[Schema](https://docs.oasis-open.org/sarif/sarif/v2.0/csprd02/schemas/sarif-schema-2.1.0.json)

### Build

```shell script
./gradlew build
```

The build consists of three steps:
1. Getting the schema file via `getSchema` task
2. Building the models via `generateJsonSchema2Pojo` task
3. Compiling the `SarifJsonWriter` helpers
