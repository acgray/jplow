package io.github.acgray.jplow.value;

import com.google.gson.*;
import io.github.acgray.jplow.selfdesc.ImmutableSelfDescribing;
import io.github.acgray.jplow.selfdesc.SchemaKey;
import io.github.acgray.jplow.selfdesc.SelfDescribing;
import org.apache.commons.codec.binary.Base64;
import org.immutables.gson.Gson;
import org.immutables.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implements the fields of the Snowplow Tracker Protocol
 *
 * <p>TODO annotate with field descriptions
 *
 * @link https://github.com/snowplow/snowplow/wiki/snowplow-tracker-protocol
 */
@Gson.TypeAdapters
@Value.Immutable
public abstract class TrackerProtocol implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(TrackerProtocol.class);
  private static final com.google.gson.Gson gson =
      new GsonBuilder()
          .registerTypeAdapter(SchemaKey.class, new SchemaKey.GsonTypeAdapter())
          .create();

  public static TrackerProtocol withContexts(List<SelfDescribing<JsonObject>> contexts) {

    try {
      SelfDescribing<List<SelfDescribing<JsonObject>>> cxWrapper =
          ImmutableSelfDescribing.<List<SelfDescribing<JsonObject>>>builder()
              .schema(
                  SchemaKey.fromString(
                      "iglu:com.snowplowanalytics.snowplow/contexts/jsonschema/1-0-1"))
              .data(contexts)
              .build();

      return ImmutableTrackerProtocol.builder()
          .cx(new String(Base64.encodeBase64(gson.toJson(cxWrapper).getBytes())))
          .build();
    } catch (SchemaKey.InvalidFormat exc) {
      throw new AssertionError(exc);
    }
  }

  public static TrackerProtocol withUnstructEventPayload(
      SelfDescribing<JsonObject> unstructEventPayload) {
    try {
      SelfDescribing<SelfDescribing<JsonObject>> wrapper =
          ImmutableSelfDescribing.<SelfDescribing<JsonObject>>builder()
              .schema(
                  SchemaKey.fromString(
                      "iglu:com.snowplowanalytics.snowplow/unstruct_event/jsonschema/1-0-0"))
              .data(unstructEventPayload)
              .build();

      return ImmutableTrackerProtocol.builder()
          .ue_px(new String(Base64.encodeBase64(gson.toJson(wrapper).getBytes())))
          .build();
    } catch (SchemaKey.InvalidFormat exc) {
      throw new AssertionError(exc);
    }
  }

  private static String urlDecode(final String encoded) {
    try {
      return encoded == null ? null : URLDecoder.decode(encoded, "UTF-8");
    } catch (final UnsupportedEncodingException e) {
      throw new RuntimeException("Impossible: UTF-8 is a required encoding", e);
    }
  }

  /**
   * Build a TrackerProtocol object from a json object, such as the format used in the body of
   * Snowplow collector POST request payloads
   *
   * @param object gson JsonObject of tracker protocol fields and values
   * @return created TrackerProtocol object
   */
  public static TrackerProtocol fromJson(JsonObject object) {
    com.google.gson.Gson gson =
        new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .registerTypeAdapterFactory(new GsonAdaptersTrackerProtocol())
            .create();

    return gson.fromJson(object, TrackerProtocol.class);
  }

  /**
   * Return a new TrackerProtocol instance from a GET querystring
   *
   * @param queryString the Snowplow querystring
   * @return the created TrackerProtocol instance
   */
  public static TrackerProtocol fromQueryString(String queryString) {

    Builder builder = ImmutableTrackerProtocol.builder();

    Arrays.stream(queryString.split("&"))
        .map((s) -> s.split("="))
        .filter((s) -> s.length == 2)
        .forEach(
            (s) -> {
              String key = urlDecode(s[0]);
              String val = urlDecode(s[1]);

              if (key == null || val == null) {
                return;
              }

              builder.setByName(key, val);
            });
    return builder.build();
  }

  public static Builder builder() {
    return ImmutableTrackerProtocol.builder();
  }

  @Nullable
  public abstract String tna();

  @Nullable
  public abstract String env();

  @Nullable
  public abstract String aid();

  @Nullable
  public abstract String p();

  @Nullable
  public abstract String dtm();

  @Nullable
  public abstract String stm();

  @Nullable
  public abstract String ttm();

  @Nullable
  public abstract String tz();

  @Nullable
  public abstract String e();

  @Nullable
  public abstract String tid();

  @Nullable
  public abstract String eid();

  @Nullable
  public abstract String tv();

  @Nullable
  public abstract String duid();

  @Nullable
  public abstract String nuid();

  @Nullable
  public abstract String tnuid();

  @Nullable
  public abstract String uid();

  @Nullable
  public abstract String vid();

  @Nullable
  public abstract String sid();

  @Nullable
  public abstract String ip();

  @Nullable
  public abstract String res();

  @Nullable
  public abstract String url();

  @Nullable
  public abstract String ua();

  @Nullable
  public abstract String page();

  @Nullable
  public abstract String refr();

  @Nullable
  public abstract String fp();

  @Nullable
  public abstract String ctype();

  @Nullable
  public abstract String cookie();

  @Nullable
  public abstract String lang();

  @Nullable
  public abstract String f_pdf();

  @Nullable
  public abstract String f_qt();

  @Nullable
  public abstract String f_realp();

  @Nullable
  public abstract String f_wma();

  @Nullable
  public abstract String f_dir();

  @Nullable
  public abstract String f_fla();

  @Nullable
  public abstract String f_java();

  @Nullable
  public abstract String f_gears();

  @Nullable
  public abstract String f_ag();

  @Nullable
  public abstract String cd();

  @Nullable
  public abstract String ds();

  @Nullable
  public abstract String cs();

  @Nullable
  public abstract String vp();

  @Nullable
  public abstract String mac();

  @Nullable
  public abstract String pp_mix();

  @Nullable
  public abstract String pp_max();

  @Nullable
  public abstract String pp_miy();

  @Nullable
  public abstract String pp_may();

  @Nullable
  public abstract String ad_ba();

  @Nullable
  public abstract String ad_ca();

  @Nullable
  public abstract String ad_ad();

  @Nullable
  public abstract String ad_uid();

  @Nullable
  public abstract String tr_id();

  @Nullable
  public abstract String tr_af();

  @Nullable
  public abstract String tr_tt();

  @Nullable
  public abstract String tr_tx();

  @Nullable
  public abstract String tr_sh();

  @Nullable
  public abstract String tr_ci();

  @Nullable
  public abstract String tr_st();

  @Nullable
  public abstract String tr_co();

  @Nullable
  public abstract String tr_cu();

  @Nullable
  public abstract String ti_id();

  @Nullable
  public abstract String ti_sk();

  @Nullable
  public abstract String ti_na();

  @Nullable
  public abstract String ti_ca();

  @Nullable
  public abstract String ti_pr();

  @Nullable
  public abstract String ti_qu();

  @Nullable
  public abstract String ti_cu();

  @Nullable
  public abstract String sa();

  @Nullable
  public abstract String sn();

  @Nullable
  public abstract String st();

  @Nullable
  public abstract String sp();

  @Nullable
  public abstract String se_ca();

  @Nullable
  public abstract String se_ac();

  @Nullable
  public abstract String se_la();

  @Nullable
  public abstract String se_pr();

  @Nullable
  public abstract String se_va();

  @Nullable
  public abstract String ue_pr();

  @Nullable
  public abstract String ue_px();

  @Nullable
  public abstract String cv();

  @Nullable
  public abstract String co();

  @Nullable
  public abstract String cx();

  @Nullable
  public abstract String u();

  public List<SelfDescribing<JsonObject>> getContextObjects() {
    String cx = cx();

    if (cx != null) {

      String cxJson;

      try {
        cxJson = new String(Base64.decodeBase64(cx.getBytes()));
      } catch (IllegalArgumentException exc) {
        exc.printStackTrace();
        LOG.info(cx);
        return null;
      }

      com.google.gson.Gson gson = new com.google.gson.Gson();

      List<SelfDescribing<JsonObject>> cxList = new ArrayList<>();

      if (cx() == null) {
        return new ArrayList<>();
      }

      JsonObject cxObjectWrapper;

      try {
        cxObjectWrapper = gson.fromJson(cxJson, JsonObject.class);
      } catch (JsonSyntaxException exc) {
        LOG.warn("Malformed context wrapper");
        return null;
      }

      JsonArray cxObjects = cxObjectWrapper.getAsJsonArray("data");

      cxObjects.forEach(
          (el) -> {
            try {
              cxList.add(SelfDescribing.<JsonObject>fromJsonObject(el.getAsJsonObject()));
            } catch (IllegalArgumentException exc) {
              LOG.warn("Skipping malformed context object {}", el);
            }
          });

      return cxList;
    } else {
      return null;
    }
  }

  public TrackerProtocol withContextObjects(List<SelfDescribing<JsonObject>> contextObjects) {

    try {
      SelfDescribing<List<SelfDescribing<JsonObject>>> correctedCtx =
          ImmutableSelfDescribing.<List<SelfDescribing<JsonObject>>>builder()
              .schema(
                  SchemaKey.fromString(
                      "iglu:com.snowplowanalytics.snowplow/contexts/jsonschema/1-0-1"))
              .data(contextObjects)
              .build();

      return ImmutableTrackerProtocol.builder()
          .from(this)
          .cx(
              new String(
                  Base64.encodeBase64(gson.toJson(correctedCtx).getBytes())))
          .build();
    } catch (SchemaKey.InvalidFormat exception) {
      throw new AssertionError(exception);
    }
  }

  public TrackerProtocol withUnstructEvent(SelfDescribing<JsonObject> unstructEvent) {
    try {
      SelfDescribing<SelfDescribing<JsonObject>> wrapped =
          ImmutableSelfDescribing.<SelfDescribing<JsonObject>>builder()
              .schema(
                  SchemaKey.fromString(
                      "iglu:com.snowplowanalytics.snowplow/payload_data/jsonschema/1-0-4"))
              .data(unstructEvent)
              .build();

      com.google.gson.Gson gson =
          new GsonBuilder()
              .registerTypeAdapter(SchemaKey.class, new SchemaKey.GsonTypeAdapter())
              .create();

      return ImmutableTrackerProtocol.builder()
          .from(this)
          .ue_px(Base64.encodeBase64String(gson.toJson(wrapped).getBytes()))
          .build();
    } catch (SchemaKey.InvalidFormat exc) {
      throw new AssertionError(exc);
    }
  }

  public SelfDescribing<JsonObject> getUnstructPayload() {
    String ue_px = ue_px();
    if (ue_px != null) {
      com.google.gson.Gson gson = new com.google.gson.Gson();

      String ueJson = new String(Base64.decodeBase64(ue_px));

      SelfDescribing<JsonObject> ueWrapper = SelfDescribing.fromJson(ueJson);

      return SelfDescribing.fromJsonObject(ueWrapper.data());
    } else {
      return null;
    }
  }

  abstract static class Builder {
    private Builder setByName(@Nonnull String key, String value) {
      Method builderMethod = null;
      try {
        builderMethod = ImmutableTrackerProtocol.Builder.class.getMethod(key, String.class);
      } catch (NoSuchMethodException | SecurityException exc) {
        LOG.warn(
            "Ignoring unknown Tracker Protocol field \"{}\". "
                + "This class definition may be out of date with the "
                + "latest Snowplow release",
            key);
      }

      if (builderMethod != null) {
        try {
          builderMethod.invoke(this, value);
        } catch (IllegalAccessException | InvocationTargetException exc) {
          // should never happen
          throw new RuntimeException(exc);
        }
      }

      return this;
    }

    abstract TrackerProtocol build();
  }
}
