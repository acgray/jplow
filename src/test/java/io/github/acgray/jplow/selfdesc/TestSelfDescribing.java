package io.github.acgray.jplow.selfdesc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class TestSelfDescribing {

  @Test
  public void testFromJson_JsonObject() throws Exception {
    String json = "{\n" +
        "    \"schema\": \"iglu:com.foo/bar_context/jsonschema/1-0-0\",\n" +
        "    \"data\": {\n" +
        "        \"lorem\": \"ipsum\",\n" +
        "        \"dolor\": 1,\n" +
        "        \"sit\": true\n" +
        "    }\n" +
        "}";
    SelfDescribing<JsonObject> obj = SelfDescribing.fromJson(json);

    Assert.assertTrue(obj.schema().equals(
        SchemaKey.builder()
            .vendor("com.foo")
            .name("bar_context")
            .version(SchemaVer.fromString("1-0-0"))
            .build()));

    Assert.assertEquals(
        "ipsum", obj.data().get("lorem").getAsString());

    Assert.assertEquals(
        1, obj.data().get("dolor").getAsInt());

    Assert.assertEquals(
        true, obj.data().get("sit").getAsBoolean());
  }

  @Test
  public void testFromJson_CustomObject() throws Exception {
    String json = "{\n" +
        "    \"schema\": \"iglu:com.foo/bar_context/jsonschema/1-0-0\",\n" +
        "    \"data\": {\n" +
        "        \"lorem\": \"ipsum\",\n" +
        "        \"dolor\": 1,\n" +
        "        \"sit\": true\n" +
        "    }\n" +
        "}";
    SelfDescribing<DummyContext> obj = SelfDescribing.fromJson(
        json,
        DummyContext.class);

    DummyContext context = obj.data();

    Assert.assertEquals("ipsum", context.lorem());
    Assert.assertEquals(Integer.valueOf(1), context.dolor());
    Assert.assertEquals(true, context.sit());
  }

  @Test
  public void testFromJson_CustomTypeAdapter() throws Exception {
    String json = "{\n" +
        "    \"schema\": \"iglu:com.foo/bar_context/jsonschema/1-0-0\",\n" +
        "    \"data\": [\"a\", \"b\"]\n" +
        "}";

    SelfDescribing<TestCustomType> obj = SelfDescribing.fromJson(
        json,
        TestCustomType.class,
        new TestTypeAdaptorFactory());
  }

  /**
   * Custom type with protected initializer - using a custom TypeAdapter to
   * (de)serialize this as [arg1, arg2].
   */
  private static class TestCustomType {
    String arg1;
    String arg2;

    protected TestCustomType(String arg1, String arg2) {
      this.arg1 = arg1;
      this.arg2 = arg2;
    }
  }

  private static class TestTypeAdaptorFactory implements TypeAdapterFactory {
    static class TestCustomTypeAdapter extends TypeAdapter<TestCustomType> {
      public void write(JsonWriter out, TestCustomType value)
          throws IOException {
        out.beginArray();
        out.value(value.arg1);
        out.value(value.arg2);
        out.endArray();
      }

      public TestCustomType read(JsonReader in)
          throws IOException {
        in.beginArray();
        String arg1 = in.nextString();
        String arg2 = in.nextString();
        in.endArray();

        return new TestCustomType(arg1, arg2);
      }
    }

    @SuppressWarnings({"unchecked", "raw"})
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
      if (type.getType() == TestCustomType.class) {
        return (TypeAdapter<T>) new TestCustomTypeAdapter();
      }
      return null;
    }
  }

}
