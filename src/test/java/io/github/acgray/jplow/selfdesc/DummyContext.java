package io.github.acgray.jplow.selfdesc;

import org.immutables.gson.Gson;
import org.immutables.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Gson.TypeAdapters
@Value.Immutable
public abstract class DummyContext {

  private static final Logger LOG = LoggerFactory.getLogger(DummyContext.class);

  @Value.Parameter
  abstract String lorem();

  @Value.Parameter
  abstract Integer dolor();

  @Value.Parameter
  abstract Boolean sit();

}
