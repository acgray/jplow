package io.github.acgray.jplow.selfdesc;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class TestSchemaVer {

  private static final Logger LOG = LoggerFactory.getLogger(TestSchemaVer.class);

  @Test
  public void testCompareTo() throws Exception {
    SchemaVer sv1 = SchemaVer.fromString("1-0-0");
    SchemaVer sv2 = SchemaVer.fromString("2-0-0");

    Assert.assertTrue(sv1.lessThan(sv2));
    Assert.assertTrue(sv2.greaterThan(sv1));
    Assert.assertFalse(sv1.greaterThan(sv2));
    Assert.assertFalse(sv2.lessThan(sv1));

    SchemaVer sv3 = SchemaVer.fromString("1-0-0");
    SchemaVer sv4 = SchemaVer.fromString("1-1-0");

    Assert.assertTrue(sv3.lessThan(sv4));
    Assert.assertTrue(sv4.greaterThan(sv3));
    Assert.assertFalse(sv3.greaterThan(sv4));
    Assert.assertFalse(sv4.lessThan(sv3));

    SchemaVer sv5 = SchemaVer.fromString("2-0-0");
    SchemaVer sv6 = SchemaVer.fromString("2-0-1");

    Assert.assertTrue(sv5.lessThan(sv6));
    Assert.assertTrue(sv6.greaterThan(sv5));
    Assert.assertFalse(sv5.greaterThan(sv6));
    Assert.assertFalse(sv6.lessThan(sv5));
  }

  @Test
  public void testFromStringReturnsAnObject() throws Exception {
    SchemaVer sv = SchemaVer.fromString("2-1-3");
    Assert.assertEquals(sv.major(), 2);
    Assert.assertEquals(sv.minor(), 1);
    Assert.assertEquals(sv.addition(), 3);
  }

  @Test
  public void testFromStringThrowsExceptionOnInvalidFormat() throws Exception {
    Arrays.asList("blah", "1-a-b", "1.0.1", "1-0-1 ")
        .forEach(s -> {
          try {
            SchemaVer.fromString(s);
          }
          catch (SchemaVer.InvalidFormat e) {
            return;
          }
          Assert.fail(String.format(
              "Expected InvalidFormat exception for %s but none was thrown",
              s));
        });
  }

  @Test
  public void testToString() throws Exception {
    List<String> cases = Arrays.asList("1-0-0", "1-0-1", "2-1-0", "2-1-3");

    for (String s : cases) {
      Assert.assertEquals(SchemaVer.fromString(s).toString(), s);
    }
  }

}
