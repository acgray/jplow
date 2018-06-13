package io.github.acgray.jplow.value;

import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.Test;

public class TestTrackerProtocol {

  @Test
  public void testFromJsonCreatesObjectWithProvidedFields() {
    JsonObject input = new JsonObject();
    input.addProperty("e", "ue");
    input.addProperty("aid", "myapp");
    input.addProperty("ue_px", "abcde");

    TrackerProtocol tp = TrackerProtocol.fromJson(input);

    Assert.assertEquals(tp.e(), "ue");
    Assert.assertEquals(tp.aid(), "myapp");
    Assert.assertEquals(tp.ue_px(), "abcde");
  }

  @Test
  public void testFromQueryStringShouldCreateAnObject() {
    String input = "e=ue&aid=myapp&ue_px=1234";
    TrackerProtocol tp = TrackerProtocol.fromQueryString(input);

    Assert.assertEquals(tp.e(), "ue");
    Assert.assertEquals(tp.aid(), "myapp");
    Assert.assertEquals(tp.ue_px(), "1234");

    // Other fields should be null
    Assert.assertNull(tp.tna());
    Assert.assertNull(tp.env());
    Assert.assertNull(tp.p());
    Assert.assertNull(tp.dtm());
    Assert.assertNull(tp.stm());
    Assert.assertNull(tp.ttm());
    Assert.assertNull(tp.tz());
    Assert.assertNull(tp.tid());
    Assert.assertNull(tp.eid());
    Assert.assertNull(tp.tv());
    Assert.assertNull(tp.duid());
    Assert.assertNull(tp.nuid());
    Assert.assertNull(tp.tnuid());
    Assert.assertNull(tp.uid());
    Assert.assertNull(tp.vid());
    Assert.assertNull(tp.sid());
    Assert.assertNull(tp.ip());
    Assert.assertNull(tp.res());
    Assert.assertNull(tp.url());
    Assert.assertNull(tp.ua());
    Assert.assertNull(tp.page());
    Assert.assertNull(tp.refr());
    Assert.assertNull(tp.fp());
    Assert.assertNull(tp.ctype());
    Assert.assertNull(tp.cookie());
    Assert.assertNull(tp.lang());
    Assert.assertNull(tp.f_pdf());
    Assert.assertNull(tp.f_qt());
    Assert.assertNull(tp.f_realp());
    Assert.assertNull(tp.f_wma());
    Assert.assertNull(tp.f_dir());
    Assert.assertNull(tp.f_fla());
    Assert.assertNull(tp.f_java());
    Assert.assertNull(tp.f_gears());
    Assert.assertNull(tp.f_ag());
    Assert.assertNull(tp.cd());
    Assert.assertNull(tp.ds());
    Assert.assertNull(tp.cs());
    Assert.assertNull(tp.vp());
    Assert.assertNull(tp.mac());
    Assert.assertNull(tp.pp_mix());
    Assert.assertNull(tp.pp_max());
    Assert.assertNull(tp.pp_miy());
    Assert.assertNull(tp.pp_may());
    Assert.assertNull(tp.ad_ba());
    Assert.assertNull(tp.ad_ca());
    Assert.assertNull(tp.ad_ad());
    Assert.assertNull(tp.ad_uid());
    Assert.assertNull(tp.tr_id());
    Assert.assertNull(tp.tr_af());
    Assert.assertNull(tp.tr_tt());
    Assert.assertNull(tp.tr_tx());
    Assert.assertNull(tp.tr_sh());
    Assert.assertNull(tp.tr_ci());
    Assert.assertNull(tp.tr_st());
    Assert.assertNull(tp.tr_co());
    Assert.assertNull(tp.tr_cu());
    Assert.assertNull(tp.ti_id());
    Assert.assertNull(tp.ti_sk());
    Assert.assertNull(tp.ti_na());
    Assert.assertNull(tp.ti_ca());
    Assert.assertNull(tp.ti_pr());
    Assert.assertNull(tp.ti_qu());
    Assert.assertNull(tp.ti_cu());
    Assert.assertNull(tp.sa());
    Assert.assertNull(tp.sn());
    Assert.assertNull(tp.st());
    Assert.assertNull(tp.sp());
    Assert.assertNull(tp.se_ca());
    Assert.assertNull(tp.se_ac());
    Assert.assertNull(tp.se_la());
    Assert.assertNull(tp.se_pr());
    Assert.assertNull(tp.se_va());
    Assert.assertNull(tp.ue_pr());
    Assert.assertNull(tp.cv());
    Assert.assertNull(tp.co());
    Assert.assertNull(tp.cx());
    Assert.assertNull(tp.u());
  }

  @Test
  public void testFromQueryStringShouldDiscardUnknownFields() {
    TrackerProtocol tp = TrackerProtocol.fromQueryString("e=ue&random=123");

    Assert.assertEquals(tp.e(), "ue");
  }

  @Test
  public void testFromQueryStringShouldIgnoreMalformedQueryString() {
    TrackerProtocol tp = TrackerProtocol.fromQueryString(
        "the quickbrown fox jumped over the malformed query string");
  }

}
