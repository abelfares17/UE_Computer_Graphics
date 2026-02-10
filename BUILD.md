# Building

The program can be built using either a Makefile or an Ant build file.
Makefile requires `make` to be installed on your system and it is usually
available by default on Linux and MacOS systems. Ant is instead cross-platform and can be used on Windows as well.

## MakeFile

Use the following command to build the project:

```bash
make
```

or

```bash
make all
```

This will compile all the sources and place the compiled classes in the `build` folder.

To run the project you can use the following command:

```bash
make run
```

To remove and clean all the compiled files do:

```bash
make clean
```

cleans the `build` folder.

### Generate JavaDoc

To generate the JavaDoc, do :

```bash
make doc
```

To run the tests use the following command:

```bash
make tests
```

## Ant

You can compile the project using Ant version 1.10.14 and later
([download here](https://dlcdn.apache.org//ant/binaries/apache-ant-1.10.15-bin.tar.gz)).

You can also install ant package as any other package by the command on your
**personal computer**:

```bash
sudo snap install ant --stable --classic
```

### Build

To a small description of the available task in ant, do:

```bash
ant -projecthelp
```

Use the following command to build the project:

```bash
ant
```

or

```bash
ant compile
```

To run the project you can use the following command:

```bash
ant run
```

To generate JavaDoc of the project do:

```bash
ant doc
```

To clean JavaDoc

```bash
ant clean-doc
```

To compile the tests do:

```bash
ant compile-test
```

```bash
ant clean
```

cleans the `build` folder and the tests reports directory   .

### Tests

To run all tests use the following command:

```bash
ant test
```

The test reports will be written in the dir `test/reports/`.

To run functional Tests:

```bash
ant func-tests
```

To run unit Tests:

```bash
ant unit-tests
```

To verify checkstyle on the project if you have checkstyle in lib:

```bash
ant checkstyle
```

To download the lib:

```bash
ant dl-checkstyle
```

To make a jar of the project do:

```bash
ant jar
```
