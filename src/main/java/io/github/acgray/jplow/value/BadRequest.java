package io.github.acgray.jplow.value;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import io.github.acgray.jplow.snowplow.CollectorPayload;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.immutables.gson.Gson;
import org.immutables.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * Value class holding the error format produced by the Snowplow Enrich process
 *
 * @link https://github.com/snowplow/snowplow/wiki/Hadoop-Event-Recovery
 */
@Gson.TypeAdapters
@Value.Immutable
public abstract class BadRequest implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(BadRequest.class);

  public static BadRequest fromString(String s) {
    com.google.gson.Gson gson =
        new GsonBuilder()
            .registerTypeAdapterFactory(new GsonAdaptersBadRequest())
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();

    return gson.fromJson(s, BadRequest.class);
  }

  public abstract List<BadRequestError> errors();

  public abstract String line();

  @SerializedName("failure_tstamp")
  public abstract Date failureTstamp();

  public CollectorPayload deserializePayload() throws InvalidThriftRecord {
    CollectorPayload payload = new CollectorPayload();

    byte[] binaryLine = Base64.getDecoder().decode(this.line());

    try {
      TDeserializer deserializer = new TDeserializer(new TBinaryProtocol.Factory());
      deserializer.deserialize(payload, binaryLine);
      return payload;
    } catch (TException exc) {
      throw new InvalidThriftRecord();
    }
  }

  /**
   * Parse one or more events from this record's request payload and return them
   *
   * @return a list of TrackerProtocol objects
   * @throws NoEventsFound when no events could be parsed from the request
   */
  public List<TrackerProtocol> getRawEvents() throws NoEventsFound {

    CollectorPayload cp;
    com.google.gson.Gson gson = new com.google.gson.Gson();

    try {
      cp = this.deserializePayload();
    } catch (InvalidThriftRecord e) {
      throw new NoEventsFound();
    }

    List<TrackerProtocol> rawEvents = new ArrayList<>();

    if (cp.getBody() != null) {
      try {
        JsonObject payloadBody = gson.fromJson(cp.getBody(), JsonObject.class);
        payloadBody
            .getAsJsonArray("data")
            .forEach((o) -> rawEvents.add(TrackerProtocol.fromJson(o.getAsJsonObject())));
      } catch (JsonSyntaxException | NullPointerException e) {
        throw new NoEventsFound();
      }
    } else if (cp.getQuerystring() != null && !cp.getQuerystring().equals("")) {
      rawEvents.add(TrackerProtocol.fromQueryString(cp.getQuerystring()));
    } else {
      throw new NoEventsFound();
    }

    return rawEvents;
  }

  /**
   * Thrown when the base64-encoded `line` parameter does not contain a valid CollectorPayload
   * thrift record.
   */
  public static class InvalidThriftRecord extends Exception {}

  /** Thrown when no events could be parsed from the request body */
  public static class NoEventsFound extends Exception {}

  /** The error format contained in the BadRequest `errors` field */
  @Value.Immutable
  @Gson.TypeAdapters
  public abstract static class BadRequestError implements Serializable {
    public abstract String level();

    public abstract String message();
  }
}
