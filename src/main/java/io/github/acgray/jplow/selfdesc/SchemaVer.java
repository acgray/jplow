package io.github.acgray.jplow.selfdesc;

import org.immutables.value.Value;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Value representation of a SchemaVer value
 *
 * <p>For more information on the SchemaVer versioning system, see <a
 * href="https://github.com/snowplow/iglu/wiki/SchemaVer">the SchemaVer</a> section of the Iglu
 * documentation.
 */
@Value.Immutable
public abstract class SchemaVer implements Comparable<SchemaVer>, Serializable {

  /**
   * Create a SchemaVer instance from its representation as a string.
   *
   * <p>The string should conform to the Iglu SchemaVer syntax, i.e.: <code>
   * 1-0-0
   * ^-------major version
   *   ^-----minor version
   *     ^---addition
   * </code>
   *
   * @param schemaVer the string SchemaVer representation
   * @return an instance representing the provided value
   * @throws InvalidFormat when the input does not match the expected format
   */
  public static SchemaVer fromString(String schemaVer) throws InvalidFormat {
    Pattern p = Pattern.compile("^(\\d+)-(\\d+)-(\\d+)$");
    Matcher m = p.matcher(schemaVer);
    if (m.matches()) {
      return ImmutableSchemaVer.builder()
          .major(Integer.valueOf(m.group(1)))
          .minor(Integer.valueOf(m.group(2)))
          .addition(Integer.valueOf(m.group(3)))
          .build();
    } else {
      throw new InvalidFormat();
    }
  }

  /** @return the major version number l */
  public abstract int major();

  /** @return the minor version number */
  public abstract int minor();

  /** @return the addition number */
  public abstract int addition();

  public String toString() {
    return String.format("%d-%d-%d", major(), minor(), addition());
  }

  private long numericRepr() {
    return this.major() * 100 + this.minor() * 10 + this.addition();
  }

  /**
   * Compare one SchemaVer instance to another.
   *
   * <p>The result will be > 0 if this instance is greater, or 0 if it is less.
   *
   * <p>SchemaVer values are compared in the same way as SemVer. For more information on SemVer
   * comparison see <a href="https://semver.org/">the SemVer documentation.</a>
   *
   * @param other
   * @return
   */
  @Override
  public int compareTo(SchemaVer other) {
    return (int) Math.ceil(this.numericRepr() - other.numericRepr());
  }

  /**
   * Determine whether this SchemaVer instance is greater than the other passed instance.
   *
   * @param other SchemaVer instance to compare
   * @return true if this version is greater, otherwise false
   */
  public boolean greaterThan(SchemaVer other) {
    return this.compareTo(other) > 0;
  }

  /**
   * Determine whether this SchemaVer instance is less than than the other passed instance.
   *
   * @param other SchemaVer instance to compare
   * @return true if this version value is lower, otherwise false
   */
  public boolean lessThan(SchemaVer other) {
    return this.compareTo(other) < 0;
  }

  public static class InvalidFormat extends Exception {}
}
