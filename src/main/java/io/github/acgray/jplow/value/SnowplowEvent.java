package io.github.acgray.jplow.value;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import io.github.acgray.jplow.selfdesc.SchemaPattern;
import io.github.acgray.jplow.selfdesc.SelfDescribing;
import org.immutables.gson.Gson;
import org.immutables.value.Value;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a complete enriched Snowplow Event with context objects
 * and unstructured event payload.
 * <p>
 * Context and unstructured payload are deserialized when an instance
 * is created and cached as instances of SelfDescribingJson.
 * <p>
 * <h2>Example usage:</h2>
 * <p>
 * <code>
 * String tsvData = "...\t......";
 * <p>
 * try {
 * SnowplowEvent event = SnowplowEvent.fromTsv(tsvData);
 * <p>
 * SelfDescribingJson webPageCtx = event.getContextObject(
 * "iglu:com.snowplowanalytics.snowplow/web_page_context");
 * <p>
 * String pageId = webPageCtx.getStringProperty("web_page_id");
 * <p>
 * } catch (SnowplowEvent.InvalidFormat exc) {
 * // the tsv data was in the wrong format
 * } catch (SnowplowEvent.ContextNotFound exc) {
 * // the event did not have a a web_page_context object
 * }
 * </code>
 */
@Gson.TypeAdapters
@Value.Immutable
public abstract class SnowplowEvent implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(SnowplowEvent.class);

  /**
   * Thrown when an event is constructed from invalid input data.
   */
  public static class InvalidFormat extends Exception {
    public InvalidFormat(String message) {
      super(message);
    }
  }

  /**
   * Thrown when no matching context entity is found in the event.
   */
  public static class ContextNotPresent extends Exception {
    SchemaPattern pattern;

    private ContextNotPresent(SchemaPattern pattern) {
      this.pattern = pattern;
    }

    @Override
    public String getMessage() {
      return String.format(
          "Context %s not present in this event",
          this.pattern);
    }
  }

  public static ImmutableSnowplowEvent.Builder builder() {
    return ImmutableSnowplowEvent.builder();
  }


  @SerializedName("app_id")
  public abstract String appId();

  @SerializedName("platform")
  public abstract String platform();


  // Date/time
  @SerializedName("etl_tstamp")
  public abstract Instant etlTstamp();

  @SerializedName("collector_tstamp")
  public abstract Instant collectorTstamp();

  @Nullable
  @SerializedName("dvce_created_tstamp")
  public abstract Instant dvceCreatedTstamp();


  // Transaction (i.e. this logging event)
  @SerializedName("event")
  public abstract String event();

  @SerializedName("event_id")
  public abstract String eventId();

  @Nullable
  @SerializedName("txn_id")
  public abstract Integer txnId();


  // Versioning
  @Nullable
  @SerializedName("name_tracker")
  public abstract String nameTracker();

  @Nullable
  @SerializedName("v_tracker")
  public abstract String vTracker();

  @Nullable
  @SerializedName("v_collector")
  public abstract String vCollector();

  @Nullable
  @SerializedName("v_etl")
  public abstract String vEtl();


  // User and visit
  @Nullable
  @SerializedName("user_id")
  public abstract String userId();

  @Nullable
  @SerializedName("user_ipaddress")
  public abstract String userIpaddress();

  @Nullable
  @SerializedName("user_fingerprint")
  public abstract String userFingerprint();

  @Nullable
  @SerializedName("domain_userid")
  public abstract String domainUserid();

  @Nullable
  @SerializedName("domain_sessionidx")
  public abstract Integer domainSessionidx();

  @Nullable
  @SerializedName("network_userid")
  public abstract String networkUserid();


  // Location
  @Nullable
  @SerializedName("geo_country")
  public abstract String geoCountry();

  @Nullable
  @SerializedName("geo_region")
  public abstract String geoRegion();

  @Nullable
  @SerializedName("geo_city")
  public abstract String geoCity();

  @Nullable
  @SerializedName("geo_zipcode")
  public abstract String geoZipcode();

  @Nullable
  @SerializedName("geo_latitude")
  public abstract Float geoLatitude();

  @Nullable
  @SerializedName("geo_longitude")
  public abstract Float geoLongitude();

  @Nullable
  @SerializedName("geo_region_name")
  public abstract String geoRegionName();


  // Other IP lookups
  @Nullable
  @SerializedName("ip_isp")
  public abstract String ipIsp();

  @Nullable
  @SerializedName("ip_organization")
  public abstract String ipOrganization();

  @Nullable
  @SerializedName("ip_domain")
  public abstract String ipDomain();

  @Nullable
  @SerializedName("ip_netspeed")
  public abstract String ipNetspeed();


  // Page
  @Nullable
  @SerializedName("page_url")
  public abstract String pageUrl();

  @Nullable
  @SerializedName("page_title")
  public abstract String pageTitle();

  @Nullable
  @SerializedName("page_referrer")
  public abstract String pageReferrer();


  // Page URL components
  @Nullable
  @SerializedName("page_urlscheme")
  public abstract String pageUrlscheme();

  @Nullable
  @SerializedName("page_urlhost")
  public abstract String pageUrlhost();

  @Nullable
  @SerializedName("page_urlport")
  public abstract String pageUrlport();

  @Nullable
  @SerializedName("page_urlpath")
  public abstract String pageUrlpath();

  @Nullable
  @SerializedName("page_urlquery")
  public abstract String pageUrlquery();

  @Nullable
  @SerializedName("page_urlfragment")
  public abstract String pageUrlfragment();


  // Referrer URL components
  @Nullable
  @SerializedName("refr_urlscheme")
  public abstract String refrUrlscheme();

  @Nullable
  @SerializedName("refr_urlhost")
  public abstract String refrUrlhost();

  @Nullable
  @SerializedName("refr_urlport")
  public abstract String refrUrlport();

  @Nullable
  @SerializedName("refr_urlpath")
  public abstract String refrUrlpath();

  @Nullable
  @SerializedName("refr_urlquery")
  public abstract String refrUrlquery();

  @Nullable
  @SerializedName("refr_urlfragment")
  public abstract String refrUrlfragment();


  // Referrer details
  @Nullable
  @SerializedName("refr_medium")
  public abstract String refrMedium();

  @Nullable
  @SerializedName("refr_source")
  public abstract String refrSource();

  @Nullable
  @SerializedName("refr_term")
  public abstract String refrTerm();


  // Marketing
  @Nullable
  @SerializedName("mkt_medium")
  public abstract String mktMedium();

  @Nullable
  @SerializedName("mkt_source")
  public abstract String mktSource();

  @Nullable
  @SerializedName("mkt_term")
  public abstract String mktTerm();

  @Nullable
  @SerializedName("mkt_content")
  public abstract String mktContent();

  @Nullable
  @SerializedName("mkt_campaign")
  public abstract String mktCampaign();


  // Custom Contexts
  @Nullable
  @SerializedName("contexts")
  public abstract String contexts();


  // Structured Event
  @Nullable
  @SerializedName("se_category")
  public abstract String seCategory();

  @Nullable
  @SerializedName("se_action")
  public abstract String seAction();

  @Nullable
  @SerializedName("se_label")
  public abstract String seLabel();

  @Nullable
  @SerializedName("se_property")
  public abstract String seProperty();

  @Nullable
  @SerializedName("se_value")
  public abstract String seValue();


  // Unstructured Event
  @Nullable
  @SerializedName("unstruct_event")
  public abstract String unstructEvent();


  // Ecommerce transaction (from querystring)
  @Nullable
  @SerializedName("tr_orderid")
  public abstract String trOrderid();

  @Nullable
  @SerializedName("tr_affiliation")
  public abstract String trAffiliation();

  @Nullable
  @SerializedName("tr_total")
  public abstract String trTotal();

  @Nullable
  @SerializedName("tr_tax")
  public abstract String trTax();

  @Nullable
  @SerializedName("tr_shipping")
  public abstract String trShipping();

  @Nullable
  @SerializedName("tr_city")
  public abstract String trCity();

  @Nullable
  @SerializedName("tr_state")
  public abstract String trState();

  @Nullable
  @SerializedName("tr_country")
  public abstract String trCountry();


  // Ecommerce transaction item (from querystring)
  @Nullable
  @SerializedName("ti_orderid")
  public abstract String tiOrderid();

  @Nullable
  @SerializedName("ti_sku")
  public abstract String tiSku();

  @Nullable
  @SerializedName("ti_name")
  public abstract String tiName();

  @Nullable
  @SerializedName("ti_category")
  public abstract String tiCategory();

  @Nullable
  @SerializedName("ti_price")
  public abstract String tiPrice();

  @Nullable
  @SerializedName("ti_quantity")
  public abstract Integer tiQuantity();


  // Page Pings
  @Nullable
  @SerializedName("pp_xoffset_min")
  public abstract Integer ppXoffsetMin();

  @Nullable
  @SerializedName("pp_xoffset_max")
  public abstract Integer ppXoffsetMax();

  @Nullable
  @SerializedName("pp_yoffset_min")
  public abstract Integer ppYoffsetMin();

  @Nullable
  @SerializedName("pp_yoffset_max")
  public abstract Integer ppYoffsetMax();


  // User Agent
  @Nullable
  @SerializedName("useragent")
  public abstract String useragent();


  // Browser (from user-agent)
  @Nullable
  @SerializedName("br_name")
  public abstract String brName();

  @Nullable
  @SerializedName("br_family")
  public abstract String brFamily();

  @Nullable
  @SerializedName("br_version")
  public abstract String brVersion();

  @Nullable
  @SerializedName("br_type")
  public abstract String brType();

  @Nullable
  @SerializedName("br_renderengine")
  public abstract String brRenderengine();


  // Browser (from querystring)
  @Nullable
  @SerializedName("br_lang")
  public abstract String brLang();

  // Individual feature fields for non-Hive targets (e.g. Infobright)
  @Nullable
  @SerializedName("br_features_pdf")
  public abstract Boolean brFeaturesPdf();

  @Nullable
  @SerializedName("br_features_flash")
  public abstract Boolean brFeaturesFlash();

  @Nullable
  @SerializedName("br_features_java")
  public abstract Boolean brFeaturesJava();

  @Nullable
  @SerializedName("br_features_director")
  public abstract Boolean brFeaturesDirector();

  @Nullable
  @SerializedName("br_features_quicktime")
  public abstract Boolean brFeaturesQuicktime();

  @Nullable
  @SerializedName("br_features_realplayer")
  public abstract Boolean brFeaturesRealplayer();

  @Nullable
  @SerializedName("br_features_windowsmedia")
  public abstract Boolean brFeaturesWindowsmedia();

  @Nullable
  @SerializedName("br_features_gears")
  public abstract Boolean brFeaturesGears();

  @Nullable
  @SerializedName("br_features_silverlight")
  public abstract Boolean brFeaturesSilverlight();

  @Nullable
  @SerializedName("br_cookies")
  public abstract Boolean brCookies();

  @Nullable
  @SerializedName("br_colordepth")
  public abstract String brColordepth();

  @Nullable
  @SerializedName("br_viewwidth")
  public abstract Integer brViewwidth();

  @Nullable
  @SerializedName("br_viewheight")
  public abstract Integer brViewheight();


  // OS (from user-agent)
  @Nullable
  @SerializedName("os_name")
  public abstract String osName();

  @Nullable
  @SerializedName("os_family")
  public abstract String osFamily();

  @Nullable
  @SerializedName("os_manufacturer")
  public abstract String osManufacturer();

  @Nullable
  @SerializedName("os_timezone")
  public abstract String osTimezone();


  // Device/Hardware (from user-agent)
  @Nullable
  @SerializedName("dvce_type")
  public abstract String dvceType();

  @Nullable
  @SerializedName("dvce_ismobile")
  public abstract Boolean dvceIsmobile();


  // Device (from querystring)
  @Nullable
  @SerializedName("dvce_screenwidth")
  public abstract Integer dvceScreenwidth();

  @Nullable
  @SerializedName("dvce_screenheight")
  public abstract Integer dvceScreenheight();


  // Document
  @Nullable
  @SerializedName("doc_charset")
  public abstract String docCharset();

  @Nullable
  @SerializedName("doc_width")
  public abstract Integer docWidth();

  @Nullable
  @SerializedName("doc_height")
  public abstract Integer docHeight();


  // Currency
  @Nullable
  @SerializedName("tr_currency")
  public abstract String trCurrency();

  @Nullable
  @SerializedName("tr_total_base")
  public abstract String trTotalBase();

  @Nullable
  @SerializedName("tr_tax_base")
  public abstract String trTaxBase();

  @Nullable
  @SerializedName("tr_shipping_base")
  public abstract String trShippingBase();

  @Nullable
  @SerializedName("ti_currency")
  public abstract String tiCurrency();

  @Nullable
  @SerializedName("ti_price_base")
  public abstract String tiPriceBase();

  @Nullable
  @SerializedName("base_currency")
  public abstract String baseCurrency();


  // Geolocation
  @Nullable
  @SerializedName("geo_timezone")
  public abstract String geoTimezone();


  // Click ID
  @Nullable
  @SerializedName("mkt_clickid")
  public abstract String mktClickid();

  @Nullable
  @SerializedName("mkt_network")
  public abstract String mktNetwork();


  // ETL tags
  @Nullable
  @SerializedName("etl_tags")
  public abstract String etlTags();


  // Time event was sent
  @Nullable
  @SerializedName("dvce_sent_tstamp")
  public abstract Instant dvceSentTstamp();


  // Referer
  @Nullable
  @SerializedName("refr_domain_userid")
  public abstract String refrDomainUserid();

  @Nullable
  @SerializedName("refr_dvce_tstamp")
  public abstract String refrDvceTstamp();


  // Derived contexts
  @Nullable
  @SerializedName("derived_contexts")
  public abstract String derivedContexts();


  // Session ID
  @Nullable
  @SerializedName("domain_sessionid")
  public abstract String domainSessionid();


  // Derived timestamp
  @Nullable
  @SerializedName("derived_tstamp")
  public abstract Instant derivedTstamp();


  // Derived event vendor/name/format/version
  @SerializedName("event_vendor")
  public abstract String eventVendor();

  @SerializedName("event_name")
  public abstract String eventName();

  @SerializedName("event_format")
  public abstract String eventFormat();

  @SerializedName("event_version")
  public abstract String eventVersion();


  // Event fingerprint
  @Nullable
  @SerializedName("event_fingerprint")
  public abstract String eventFingerprint();


  // True timestamp
  @Nullable
  @SerializedName("true_tstamp")
  public abstract Instant trueTstamp();


  ///////////////////////////////////////////////////////////////////////


  public static SnowplowEvent fromTsv(String tsv) throws InvalidFormat {
    String[] bits = tsv.split("\t");

    if (bits.length != 130) {
      throw new InvalidFormat(
          String.format("Expected 130 fields but got %s", bits.length));
    }

    ImmutableSnowplowEvent.Builder b = builder()
        .appId(bits[0])
        .platform(bits[1])

        .etlTstamp(instantValue(bits[2]))
        .collectorTstamp(instantValue(bits[3]))
        .dvceCreatedTstamp(instantValue(bits[4]))

        .event(bits[5])
        .eventId(bits[6])
        .txnId(intValue(bits[7]))

        .nameTracker(bits[8])
        .vTracker(bits[9])
        .vCollector(bits[10])
        .vEtl(bits[11])

        .userId(stringValue(bits[12]))
        .userIpaddress(stringValue(bits[13]))
        .userFingerprint(stringValue(bits[14]))
        .domainUserid(stringValue(bits[15]))
        .domainSessionidx(intValue(bits[16]))
        .networkUserid(stringValue(bits[17]))

        .geoCountry(stringValue(bits[18]))
        .geoRegion(stringValue(bits[19]))
        .geoCity(stringValue(bits[20]))
        .geoZipcode(stringValue(bits[21]))
        .geoLatitude(floatValue(bits[22]))
        .geoLongitude(floatValue(bits[23]))
        .geoRegionName(stringValue(bits[24]))

        .ipIsp(stringValue(bits[25]))
        .ipOrganization(stringValue(bits[26]))
        .ipDomain(stringValue(bits[27]))
        .ipNetspeed(stringValue(bits[28]))

        .pageUrl(stringValue(bits[29]))
        .pageTitle(stringValue(bits[30]))
        .pageReferrer(stringValue(bits[31]))

        .pageUrlscheme(stringValue(bits[32]))
        .pageUrlhost(stringValue(bits[33]))
        .pageUrlport(stringValue(bits[34]))
        .pageUrlpath(stringValue(bits[35]))
        .pageUrlquery(stringValue(bits[36]))
        .pageUrlfragment(stringValue(bits[37]))

        .refrUrlscheme(stringValue(bits[38]))
        .refrUrlhost(stringValue(bits[39]))
        .refrUrlport(stringValue(bits[40]))
        .refrUrlpath(stringValue(bits[41]))
        .refrUrlquery(stringValue(bits[42]))
        .refrUrlfragment(stringValue(bits[43]))

        .refrMedium(stringValue(bits[44]))
        .refrSource(stringValue(bits[45]))
        .refrTerm(stringValue(bits[46]))

        .mktMedium(stringValue(bits[47]))
        .mktSource(stringValue(bits[48]))
        .mktTerm(stringValue(bits[49]))
        .mktContent(stringValue(bits[50]))
        .mktCampaign(stringValue(bits[51]))

        .contexts(stringValue(bits[52]))

        .seCategory(stringValue(bits[53]))
        .seAction(stringValue(bits[54]))
        .seLabel(stringValue(bits[55]))
        .seProperty(stringValue(bits[56]))
        .seValue(stringValue(bits[57]))

        .unstructEvent(stringValue(bits[58]))

        .trOrderid(stringValue(bits[59]))
        .trAffiliation(stringValue(bits[60]))
        .trTotal(stringValue(bits[61]))
        .trTax(stringValue(bits[62]))
        .trShipping(stringValue(bits[63]))
        .trCity(stringValue(bits[64]))
        .trState(stringValue(bits[65]))
        .trCountry(stringValue(bits[66]))

        .tiOrderid(stringValue(bits[67]))
        .tiSku(stringValue(bits[68]))
        .tiName(stringValue(bits[69]))
        .tiCategory(stringValue(bits[70]))
        .tiPrice(stringValue(bits[71]))
        .tiQuantity(intValue(bits[72]))

        .ppXoffsetMin(intValue(bits[73]))
        .ppXoffsetMax(intValue(bits[74]))
        .ppYoffsetMin(intValue(bits[75]))
        .ppYoffsetMax(intValue(bits[76]))

        .useragent(stringValue(bits[77]))

        .brName(stringValue(bits[78]))
        .brFamily(stringValue(bits[79]))
        .brVersion(stringValue(bits[80]))
        .brType(stringValue(bits[81]))
        .brRenderengine(stringValue(bits[82]))

        .brLang(stringValue(bits[83]))
        .brFeaturesPdf(boolValue(bits[84]))
        .brFeaturesFlash(boolValue(bits[85]))
        .brFeaturesJava(boolValue(bits[86]))
        .brFeaturesDirector(boolValue(bits[87]))
        .brFeaturesQuicktime(boolValue(bits[88]))
        .brFeaturesRealplayer(boolValue(bits[89]))
        .brFeaturesWindowsmedia(boolValue(bits[80]))
        .brFeaturesGears(boolValue(bits[91]))
        .brFeaturesSilverlight(boolValue(bits[92]))
        .brCookies(boolValue(bits[93]))
        .brColordepth(stringValue(bits[94]))
        .brViewwidth(intValue(bits[95]))
        .brViewheight(intValue(bits[96]))

        .osName(stringValue(bits[97]))
        .osFamily(stringValue(bits[98]))
        .osManufacturer(stringValue(bits[99]))
        .osTimezone(stringValue(bits[100]))

        .dvceType(stringValue(bits[101]))
        .dvceIsmobile(boolValue(bits[102]))

        .dvceScreenwidth(intValue(bits[103]))
        .dvceScreenheight(intValue(bits[104]))

        .docCharset(stringValue(bits[105]))
        .docWidth(intValue(bits[106]))
        .docHeight(intValue(bits[107]))

        .trCurrency(stringValue(bits[108]))
        .trTotalBase(stringValue(bits[109]))
        .trTaxBase(stringValue(bits[110]))
        .trShippingBase(stringValue(bits[111]))
        .tiCurrency(stringValue(bits[112]))
        .tiPriceBase(stringValue(bits[113]))
        .baseCurrency(stringValue(bits[114]))

        .geoTimezone(stringValue(bits[115]))

        .mktClickid(stringValue(bits[116]))
        .mktNetwork(stringValue(bits[117]))

        .etlTags(stringValue(bits[118]))

        .dvceSentTstamp(instantValue(bits[119]))

        .refrDomainUserid(stringValue(bits[120]))
        .refrDvceTstamp(stringValue(bits[121]))

        .derivedContexts(stringValue(bits[122]))

        .domainSessionid(stringValue(bits[123]))

        .derivedTstamp(instantValue(bits[124]))

        .eventVendor(stringValue(bits[125]))
        .eventName(stringValue(bits[126]))
        .eventFormat(stringValue(bits[127]))
        .eventVersion(stringValue(bits[128]))
        .eventFingerprint(stringValue(bits[129]));

    return b.build();

  }

  protected static final DateTimeFormatter DATE_FORMAT = DateTimeFormat
      .forPattern("yyyy-MM-dd HH:mm:ss.SSS")
      .withZone(DateTimeZone.UTC);

  @Value.Check
  protected void check() {
    // Initialize lazy objects early and catch validation errors
    this.unstructEventObject();
    this.contextObjects();
  }

  @Value.Lazy
  List<SelfDescribing<JsonObject>> contextObjects()
      throws IllegalArgumentException {
    // deserialize context objects from context and derivedContext fields

    com.google.gson.Gson gson = new com.google.gson.Gson();

    List<SelfDescribing<JsonObject>> selfDescribingJsons = new ArrayList<>();

    try {
      if (contexts() != null) {
        JsonArray ctx = gson.fromJson(contexts(), JsonObject.class)
            .getAsJsonArray("data");
        if (ctx == null) {
          throw new ClassCastException();
        }
        for (JsonElement e : ctx) {
          selfDescribingJsons.add(
              SelfDescribing.<JsonObject>fromJsonObject(e.getAsJsonObject()));
        }
      }
    }
    catch (JsonSyntaxException | ClassCastException | IllegalArgumentException exc) {
      throw new IllegalArgumentException(
          new InvalidFormat("Invalid format of contexts field: "
              + contexts()));
    }

    try {
      if (derivedContexts() != null) {
        JsonArray ctx = gson.fromJson(
            derivedContexts(),
            JsonObject.class)
            .getAsJsonArray("data");
        if (ctx == null) {
          throw new ClassCastException();
        }

        for (JsonElement e : ctx) {
          selfDescribingJsons.add(
              SelfDescribing.fromJsonObject(e.getAsJsonObject()));
        }
      }
    }
    catch (JsonSyntaxException | ClassCastException | IllegalArgumentException exc) {
      throw new IllegalArgumentException(
          new InvalidFormat("Invalid format of derived_contexts field: "
              + derivedContexts()));
    }

    return selfDescribingJsons;
  }

  @Nullable
  @Value.Lazy
  public SelfDescribing<JsonObject> unstructEventObject()
      throws IllegalArgumentException {
    if (unstructEvent() != null && !unstructEvent().isEmpty()) {
      try {
        return SelfDescribing.<JsonObject>fromJson(unstructEvent());
      }
      catch (IllegalArgumentException exc) {
        throw new IllegalArgumentException(
            new InvalidFormat("Invalid format of unstruct event payload: "
                + unstructEvent()));
      }
    } else {
      return null;
    }
  }

  /**
   * Look for a context entity in the event with the given schema
   * and returns it if it exists.
   * <p>
   * Both user and derive contexts are searched, in that order.
   *
   * @param pattern
   *     full or partial schema to match on
   *
   * @return SelfDescribingJson object representing the context object
   *
   * @throws ContextNotPresent
   *     when no context object in the event matches the schema
   */
  public <T> SelfDescribing<T> getContextForSchema(SchemaPattern pattern)
      throws ContextNotPresent {
    for (SelfDescribing sdj : contextObjects()) {
      if (pattern.matches(sdj.schema())) {
        return sdj;
      }
    }

    throw new ContextNotPresent(pattern);
  }

  @SuppressWarnings("unchecked")
  public <T> SelfDescribing<T> getContextForSchema(SchemaPattern pattern, Class<T> wrappedType)
    throws ContextNotPresent {
    for (SelfDescribing sdj : contextObjects()) {
      if (pattern.matches(sdj.schema())) {
        return sdj.<T>as(wrappedType, null);
      }
    }

    throw new ContextNotPresent(pattern);
  }

  /**
   * Check whether a context matching the provided schema is present
   * in the event.
   *
   * @param pattern
   *     full or partial schema key to match on
   *
   * @return true if a context matches the schema, otherwise false
   *
   * @see SnowplowEvent::getContextForSchema()
   */
  public Boolean hasContext(SchemaPattern pattern) {
    try {
      this.getContextForSchema(pattern);
      return true;
    }
    catch (ContextNotPresent e) {
      return false;
    }
  }

  private static String stringValue(String s) {
    return s.isEmpty() ? null : s;
  }

  private static Float floatValue(String s) {
    return s.isEmpty() ? null : Float.valueOf(s);
  }

  private static Integer intValue(String s) {
    return s.isEmpty() ? null : Integer.valueOf(s);
  }

  private static Boolean boolValue(String s) {
    return s.isEmpty() ? null : s.equals("1");
  }

  private static Instant instantValue(String s)
      throws InvalidFormat {

    return s.isEmpty() ? null : DATE_FORMAT.parseDateTime(s).toInstant();
  }

  private static String tsvRepr(Float f) {
    return f == null ? "" : f.toString();
  }

  private static String tsvRepr(Instant i) {
    return i == null ? "" : DATE_FORMAT.print(i);
  }

  private static String tsvRepr(Integer i) {
    return i == null ? "" : i.toString();
  }

  private static String tsvRepr(String s) {
    return s == null ? "" : s;
  }

  private static String tsvRepr(Boolean b) {
    return b == null ? "" : (b ? "1" : "0");
  }

  public String toTsv() {
    return String.join(
        "\t",
        tsvRepr(appId()),
        tsvRepr(platform()),
        tsvRepr(tsvRepr(etlTstamp())),
        tsvRepr(tsvRepr(collectorTstamp())),
        tsvRepr(tsvRepr(dvceCreatedTstamp())),
        tsvRepr(event()),
        tsvRepr(eventId()),
        tsvRepr(txnId()),
        tsvRepr(nameTracker()),
        tsvRepr(vTracker()),
        tsvRepr(vCollector()),
        tsvRepr(vEtl()),
        tsvRepr(userId()),
        tsvRepr(userIpaddress()),
        tsvRepr(userFingerprint()),
        tsvRepr(domainUserid()),
        tsvRepr(domainSessionidx()),
        tsvRepr(networkUserid()),
        tsvRepr(geoCountry()),
        tsvRepr(geoRegion()),
        tsvRepr(geoCity()),
        tsvRepr(geoZipcode()),
        tsvRepr(geoLatitude()),
        tsvRepr(geoLongitude()),
        tsvRepr(geoRegionName()),
        tsvRepr(ipIsp()),
        tsvRepr(ipOrganization()),
        tsvRepr(ipDomain()),
        tsvRepr(ipNetspeed()),
        tsvRepr(pageUrl()),
        tsvRepr(pageTitle()),
        tsvRepr(pageReferrer()),
        tsvRepr(pageUrlscheme()),
        tsvRepr(pageUrlhost()),
        tsvRepr(pageUrlport()),
        tsvRepr(pageUrlpath()),
        tsvRepr(pageUrlquery()),
        tsvRepr(pageUrlfragment()),
        tsvRepr(refrUrlscheme()),
        tsvRepr(refrUrlhost()),
        tsvRepr(refrUrlport()),
        tsvRepr(refrUrlpath()),
        tsvRepr(refrUrlquery()),
        tsvRepr(refrUrlfragment()),
        tsvRepr(refrMedium()),
        tsvRepr(refrSource()),
        tsvRepr(refrTerm()),
        tsvRepr(mktMedium()),
        tsvRepr(mktSource()),
        tsvRepr(mktTerm()),
        tsvRepr(mktContent()),
        tsvRepr(mktCampaign()),
        tsvRepr(contexts()),
        tsvRepr(seCategory()),
        tsvRepr(seAction()),
        tsvRepr(seLabel()),
        tsvRepr(seProperty()),
        tsvRepr(seValue()),
        tsvRepr(unstructEvent()),
        tsvRepr(trOrderid()),
        tsvRepr(trAffiliation()),
        tsvRepr(trTotal()),
        tsvRepr(trTax()),
        tsvRepr(trShipping()),
        tsvRepr(trCity()),
        tsvRepr(trState()),
        tsvRepr(trCountry()),
        tsvRepr(tiOrderid()),
        tsvRepr(tiSku()),
        tsvRepr(tiName()),
        tsvRepr(tiCategory()),
        tsvRepr(tiPrice()),
        tsvRepr(tiQuantity()),
        tsvRepr(ppXoffsetMin()),
        tsvRepr(ppXoffsetMax()),
        tsvRepr(ppYoffsetMin()),
        tsvRepr(ppYoffsetMax()),
        tsvRepr(useragent()),
        tsvRepr(brName()),
        tsvRepr(brFamily()),
        tsvRepr(brVersion()),
        tsvRepr(brType()),
        tsvRepr(brRenderengine()),
        tsvRepr(brLang()),
        tsvRepr(brFeaturesPdf()),
        tsvRepr(brFeaturesFlash()),
        tsvRepr(brFeaturesJava()),
        tsvRepr(brFeaturesDirector()),
        tsvRepr(brFeaturesQuicktime()),
        tsvRepr(brFeaturesRealplayer()),
        tsvRepr(brFeaturesWindowsmedia()),
        tsvRepr(brFeaturesGears()),
        tsvRepr(brFeaturesSilverlight()),
        tsvRepr(brCookies()),
        tsvRepr(brColordepth()),
        tsvRepr(brViewwidth()),
        tsvRepr(brViewheight()),
        tsvRepr(osName()),
        tsvRepr(osFamily()),
        tsvRepr(osManufacturer()),
        tsvRepr(osTimezone()),
        tsvRepr(dvceType()),
        tsvRepr(dvceIsmobile()),
        tsvRepr(dvceScreenwidth()),
        tsvRepr(dvceScreenheight()),
        tsvRepr(docCharset()),
        tsvRepr(docWidth()),
        tsvRepr(docHeight()),
        tsvRepr(trCurrency()),
        tsvRepr(trTotalBase()),
        tsvRepr(trTaxBase()),
        tsvRepr(trShippingBase()),
        tsvRepr(tiCurrency()),
        tsvRepr(tiPriceBase()),
        tsvRepr(baseCurrency()),
        tsvRepr(geoTimezone()),
        tsvRepr(mktClickid()),
        tsvRepr(mktNetwork()),
        tsvRepr(etlTags()),
        tsvRepr(dvceSentTstamp()),
        tsvRepr(refrDomainUserid()),
        tsvRepr(refrDvceTstamp()),
        tsvRepr(derivedContexts()),
        tsvRepr(domainSessionid()),
        tsvRepr(derivedTstamp()),
        tsvRepr(eventVendor()),
        tsvRepr(eventName()),
        tsvRepr(eventFormat()),
        tsvRepr(eventVersion()),
        tsvRepr(eventFingerprint()),
        tsvRepr(trueTstamp()));
  }
}
