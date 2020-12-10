[![Build Status](https://travis-ci.org/bmiller1009/sql-utils.svg?branch=master)](https://travis-ci.org/bmiller1009/sql-utils)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.bradfordmiller/sqlutils/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.bradfordmiller/sqlutils)
[![github: bmiller1009/sql-utils](https://img.shields.io/badge/github%3A-issues-blue.svg?style=flat-square)](https://github.com/bmiller1009/sql-utils/issues)

# sql-utils
Utilities library for JDBC sql operations

API docs were generated using [dokka](https://github.com/Kotlin/dokka) and are hosted [here](https://bmiller1009.github.io/sql-utils/).

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install the software and how to install them

* Gradle 5.4.1 or greater if you want to build from source
* JVM 8+

### Installing

#### Running with Maven

If you're using [Maven](maven.apache.org) simply specify the GAV coordinate below and Maven will do the rest

```xml
<dependency>
  <groupId>org.bradfordmiller</groupId>
  <artifactId>sqlutils</artifactId>
  <version>0.0.2</version>
</dependency>
```

#### Running with SBT

Add this GAV coordinate to your SBT dependency list

```sbt
libraryDependencies += "org.bradfordmiller" %% "sqlutils" % "0.0.2"
```

#### Running with Gradle

Add this GAV coordinate to your Gradle dependencies section

```gradle
dependencies {
    ...
    ...
    implementation 'org.bradfordmiller:sqlutils:0.0.2'
}
```
