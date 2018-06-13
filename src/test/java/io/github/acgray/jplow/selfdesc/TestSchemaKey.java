package io.github.acgray.jplow.selfdesc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSchemaKey {

  private static final Logger LOG = LoggerFactory.getLogger(TestSchemaKey.class);

  @Test
  public void testTableNameShouldReturnCorrectName() throws Exception {

    SchemaKey sk = ImmutableSchemaKey.builder()
        .vendor("foo.bar")
        .name("foo_context")
        .version(SchemaVer.fromString("2-0-1"))
        .format("jsonschema")
        .build();

    Assert.assertEquals(sk.tableName(), "foo_bar_foo_context_2");
  }

  @Test
  public void testFromStringShouldCreateSchemaKey() throws Exception {
    SchemaKey sk = SchemaKey.fromString(
        "iglu:com.acme/random_ctx/jsonschema/1-0-0");

    Assert.assertEquals(sk.vendor(), "com.acme");
    Assert.assertEquals(sk.name(), "random_ctx");
    Assert.assertEquals(sk.format(), "jsonschema");
    Assert.assertEquals(sk.version(), SchemaVer.fromString("1-0-0"));
  }

  @Test
  public void testFromStringShouldThrowErrorForInvalidFormat() {
    try {
      SchemaKey.fromString("blah");
    }
    catch (SchemaKey.InvalidFormat e) {
      // this is expected
      return;
    }
    Assert.fail("Expected an exception but none was thrown.");
  }

  @Test
  public void testFromStringShouldThrowErrorForInvalidSchemaVer() {
    try {
      SchemaKey sk = SchemaKey.fromString(
          "iglu:com.acme/random_ctx/jsonschema/1--");
    }
    catch (SchemaKey.InvalidFormat e) {
      // this is expected
      return;
    }
    Assert.fail("Expected an exception but none was thrown.");
  }

  @Test
  public void testIgluKey() throws Exception {
    String igluKey = "iglu:com.acme/foo_context/jsonschema/2-0-1";
    SchemaKey key = SchemaKey.fromString(igluKey);
    Assert.assertEquals(igluKey, key.igluKey());
  }

  @Test
  public void testGsonTypeAdapterRead() throws Exception {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(SchemaKey.class, new SchemaKey.GsonTypeAdapter())
        .create();

    SchemaKey key = gson.fromJson(
        "\"iglu:com.acme/foo_context/jsonschema/2-0-1\"",
        SchemaKey.class);

    Assert.assertEquals(key.vendor(), "com.acme");
    Assert.assertEquals(key.name(), "foo_context");
    Assert.assertEquals(key.format(), "jsonschema");
    Assert.assertEquals(key.version(), SchemaVer.fromString("2-0-1"));
  }

  @Test
  public void testGsonTypeAdapterWrite() throws Exception {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(SchemaKey.class, new SchemaKey.GsonTypeAdapter())
        .create();

    SchemaKey key = SchemaKey.builder()
        .vendor("com.acme")
        .name("blah_context")
        .version(SchemaVer.fromString("1-2-3"))
        .build();

    String json = gson.toJson(key, SchemaKey.class);

    Assert.assertEquals(
        "\"iglu:com.acme/blah_context/jsonschema/1-2-3\"",
        json);

  }

  @Test(expected = JsonParseException.class)
  public void testGsonTypeAdapterReadWithInvalidFormat() throws Exception {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(SchemaKey.class, new SchemaKey.GsonTypeAdapter())
        .create();

    gson.fromJson("\"blah\"", SchemaKey.class);
  }

  @Test
  public void testToString() throws Exception {
    SchemaKey key = SchemaKey.builder()
        .vendor("com.acme")
        .name("blah_context")
        .version(SchemaVer.fromString("1-2-3"))
        .build();

    Assert.assertEquals(
        "SchemaKey{iglu:com.acme/blah_context/jsonschema/1-2-3}",
        key.toString());
  }

}
