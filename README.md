# jplow

Java tools and data structures for working with [Snowplow](https://www.snowplowanalytics.com/snowplow)
events and [self-describing json](https://github.com/snowplow/iglu/wiki/Self-describing-JSON-Schemas)
objects.

## Installation

Artifacts for this project are currently hosted on bintray.  To include this
projects as a maven dependency, add the bintray repo to your pom.xml repositories:

```xml
<repositories>
    <repository>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
        <id>bintray-acgray-maven-repo</id>
        <name>bintray</name>
        <url>https://dl.bintray.com/acgray/maven-repo</url>
    </repository>
</repositories>
```

Then include the dependency as normal:

```xml
<dependency>
  <groupId>io.github.acgray</groupId>
  <artifactId>jplow</artifactId>
  <version>0.1</version>
  <type>pom</type>
</dependency>
```