package io.github.acgray.jplow.selfdesc;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.immutables.value.Value;

import java.io.IOException;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Value representation of an iglu schema reference. Iglu schema references have the format:
 *
 * <pre>
 * iglu:com.acme/foo_context/jsonschema/1-0-0
 * ^------------^ ^--------^ ^--------^ ^---^
 *    name          format    vendor     version
 * </pre>
 */
@Value.Immutable
public abstract class SchemaKey implements Serializable {

  public static ImmutableSchemaKey.Builder builder() {
    return ImmutableSchemaKey.builder();
  }

  public static SchemaKey fromString(String s) throws InvalidFormat {
    Pattern p = Pattern.compile("iglu:(.*)/([a-zA-Z0-9_\\-]+)/([a-z]+)/([\\d\\-]+)$");

    Matcher m = p.matcher(s);

    if (!m.matches()) {
      throw new InvalidFormat();
    }

    try {
      return ImmutableSchemaKey.builder()
          .vendor(m.group(1))
          .name(m.group(2))
          .format(m.group(3))
          .version(SchemaVer.fromString(m.group(4)))
          .build();
    } catch (SchemaVer.InvalidFormat exc) {
      throw new InvalidFormat();
    }
  }

  public abstract String vendor();;

  public abstract SchemaVer version();

  @Value.Default
  public String format() {
    return "jsonschema";
  }

  public abstract String name();

  public String toString() {
    return String.format("SchemaKey{%s}", igluKey());
  }

  public String igluKey() {
    return String.format(
      "iglu:%s/%s/%s/%s",
      vendor(),
      name(),
      format(),
      version());
  }

  public String tableName() {
    return String.format(
      "%s_%s_%d",
      vendor().replace(".", "_"),
      name(),
      version().major());
  }

  public static class InvalidFormat extends Exception {}

  public static class GsonTypeAdapter extends TypeAdapter<SchemaKey> {
    @Override
    public SchemaKey read(JsonReader in) throws IOException {
      String key = in.nextString();
      try {
        return SchemaKey.fromString(key);
      } catch (SchemaKey.InvalidFormat exc) {
        throw new JsonParseException(exc);
      }
    }

    @Override
    public void write(JsonWriter out, SchemaKey value) throws IOException {
      out.value(value.igluKey());
    }
  }
}
