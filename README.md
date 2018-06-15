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

## Features

### Self-describing objects

The central feature of the library is the `SelfDescribing<T>` class, which
wraps a data type instance with the metadata of the Iglu self-describing JSON system.
It can be used with any type that Gson knows how to (de)serialize.

A quick and clean pattern is to use the excellent [Immutables](https://immutables.org)
library with its [Gson Type Adapters generation](http://immutables.github.io/json.html#generating-type-adapters)
to represent the wrapped type.

#### Self-describing example

Self-describing JSON object:

```json
{
    "schema": "iglu:com.acme/product_view/jsonschema/1-0-0",
    "data": {
        "product_id": 111,
        "product_name": "Beer pong set"
    }
}
```

Usage with `JsonObject`:

```java

SelfDescribing<JsonObject> productView = SelfDescribing.fromString(jsonString);

SchemaKey schema = productView.schema();
// SchemaKey{vendor=com.acme,name=product_view,format=jsonschema,version=1-0-0}

String productTitle = productView.data().get("product_name").getAsString();
// Beer pong set
```

Usage with custom value class

```java

// Define value class
@Value.Immutable
@Gson.TypeAdapters
abstract class ProductViewV1 {
    @SerializedName("product_id")
    abstract Integer productId();

    @SerializedName("product_name")
    abstract String productName();
}

// then...

SelfDescribing<ProductViewV1> productView =
    SelfDescribing.fromString(jsonString, GsonAdaptersProductViewV1())
    .as(ProductViewV1.class);

String productName = productView.data().productName()
```

### Snowplow events

The `SnowplowEvent` class represents the [Snowplow Canonical Event Format](https://github.com/snowplow/snowplow/wiki/canonical-event-model).
It can instantiate events from the Snowplow Enriched TSV format as follows:

```java
String input = "some_app_id\tweb\t....";

SnowplowEvent event = SnowplowEvent.fromTsv(input);

String appId = event.appId();

List<SelfDescribing<JsonObject>> contexts = event.getContextObjects();

try {
    SelfDescribing<PageContextV1> pageContext = event
        .<PageContextV1>getContextForSchema(SchemaPattern.builder()
            .vendor("com.acme")
            .name("page_context")
            .major(1)
            .build());
catch (SnowplowEvent.ContextNotPresent exc) {
    // there was no page_context in this event
}
```

### Bad events

The `BadRequest`, `CollectorPayload` and `TrackerProtocol` classes provide support for working
with events rejected by the Snowplow Enrich process.

Example:

```java
String badLine = "{\n" +
                "    \"line\": \"....\",\n" +
                "    \"failureTstamp\": \"2018-01-01T00:00:00Z\",\n" +
                "    \"errors\": [..] \n" +
                "}";

BadRequest badRequest = BadRequest.fromString(badLine);

CollectorPayload payload = badRequest.deserializePayload();
List<TrackerProtocol> failedEvents = badRequest.getRawEvents();
List<BadRequest.BadRequestError> errors = badRequest.errors();
```

