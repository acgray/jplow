package io.github.acgray.jplow.selfdesc;

import org.immutables.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

/**
 * Partial value of a SchemaKey consisting of name, major version and optionally the minor version
 * and addition.
 */
@Value.Immutable
public abstract class SchemaPattern {

  private static final Logger LOG = LoggerFactory.getLogger(SchemaPattern.class);

  public static ImmutableSchemaPattern.Builder builder() {
    return ImmutableSchemaPattern.builder();
  }

  public static SchemaPattern of(SchemaKey key) {
    return builder()
        .vendor(key.vendor())
        .name(key.name())
        .major(key.version().major())
        .minor(key.version().minor())
        .addition(key.version().addition())
        .build();
  }

  public abstract String vendor();

  public abstract String name();

  public abstract Integer major();

  @Nullable
  public abstract Integer minor();

  @Nullable
  public abstract Integer addition();

  public boolean matches(SchemaKey schema) {
    return schema.vendor().equals(vendor())
        && schema.name().equals(name())
        && schema.version().major() == major()
        && (minor() == null || schema.version().minor() == minor())
        && (addition() == null || schema.version().addition() == addition());
  }
}
