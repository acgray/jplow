package io.github.acgray.jplow.value;

import io.github.acgray.jplow.snowplow.CollectorPayload;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

public class TestBadRequest {

  @Test(expected = BadRequest.InvalidThriftRecord.class)
  public void testDeserializeShouldThrowExceptionForInvalidThriftPayload()
      throws Exception {
    BadRequest record = ImmutableBadRequest.builder()
        .failureTstamp(Date.from(Instant.now()))
        .line("blahblahblah")
        .build();

    record.deserializePayload();
  }

  @Test
  public void testDeserializeShouldReturnDeserializedRecord()
      throws Exception {
    CollectorPayload cp = new CollectorPayload();
    cp.setBody("body");

    TSerializer serializer = new TSerializer(new TBinaryProtocol.Factory());
    byte[] bytes = serializer.serialize(cp);
    String line = Base64.getEncoder().encodeToString(bytes);

    BadRequest record = ImmutableBadRequest.builder()
        .line(line)
        .errors(Collections.emptyList())
        .failureTstamp(Date.from(Instant.now()))
        .build();

    CollectorPayload cp2 = record.deserializePayload();

    Assert.assertTrue(cp.equals(cp2));
  }
}
