package io.github.acgray.jplow.selfdesc;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapterFactory;
import org.immutables.gson.Gson;
import org.immutables.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ServiceLoader;

/**
 * Wrapper around a value object holding its self-describing schema, allowing objects to be
 * serialized and deserialized
 *
 * @param <T> type of the wrapped object
 */
@Gson.TypeAdapters
@Value.Immutable
public abstract class SelfDescribing<T> implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(SelfDescribing.class);

  public static <T> ImmutableSelfDescribing.Builder<T> builder() {
    return ImmutableSelfDescribing.builder();
  }

  public static <T> SelfDescribing<T> fromJson(
      String json, Class<T> wrappedType, TypeAdapterFactory typeAdapterFactory) {
    GsonBuilder gsonBuilder = new GsonBuilder();

    for (TypeAdapterFactory factory : ServiceLoader.load(TypeAdapterFactory.class)) {
      gsonBuilder.registerTypeAdapterFactory(factory);
    }

    if (typeAdapterFactory != null) {
      gsonBuilder.registerTypeAdapterFactory(typeAdapterFactory);
    }

    com.google.gson.Gson gson =
        gsonBuilder.registerTypeAdapter(SchemaKey.class, new SchemaKey.GsonTypeAdapter()).create();

    try {

      JsonObject obj = gson.fromJson(json, JsonObject.class);

      SchemaKey schema = gson.fromJson(obj.get("schema"), SchemaKey.class);
      T data = gson.fromJson(obj.get("data"), wrappedType);

      return ImmutableSelfDescribing.<T>builder().schema(schema).data(data).build();
    } catch (JsonSyntaxException | NullPointerException | ClassCastException exc) {
      throw new IllegalArgumentException(String.format("Invalid self describing json: %s", json));
    }
  }

  public static <T> SelfDescribing<T> fromJson(String json, Class<T> wrappedType) {
    return fromJson(json, wrappedType, null);
  }

  public static SelfDescribing<JsonObject> fromJson(String json) {
    return fromJson(json, JsonObject.class);
  }

  public static <T> SelfDescribing<T> fromJsonObject(
      JsonObject object, Class<T> dataType, TypeAdapterFactory typeAdapterFactory) {

    GsonBuilder gsonBuilder = new GsonBuilder();

    if (typeAdapterFactory != null) {
      gsonBuilder.registerTypeAdapterFactory(typeAdapterFactory);
    }

    com.google.gson.Gson gson =
        gsonBuilder.registerTypeAdapter(SchemaKey.class, new SchemaKey.GsonTypeAdapter()).create();

    SchemaKey schema = gson.fromJson(object.get("schema"), SchemaKey.class);
    T data = gson.fromJson(object.get("data"), dataType);

    return ImmutableSelfDescribing.<T>builder().schema(schema).data(data).build();
  }

  public static SelfDescribing<JsonObject> fromJsonObject(JsonObject object) {
    return fromJsonObject(object, JsonObject.class, null);
  }

  public abstract SchemaKey schema();

  public abstract T data();

  public <U> SelfDescribing<U> as(Class<U> targetType, TypeAdapterFactory typeAdapterFactory) {
    return SelfDescribing.fromJsonObject(this.toJsonObject(), targetType, typeAdapterFactory);
  }

  private JsonObject toJsonObject() {
    com.google.gson.Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(SchemaKey.class, new SchemaKey.GsonTypeAdapter())
            .create();

    return gson.toJsonTree(this).getAsJsonObject();
  }
}
