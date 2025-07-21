package com.gyp.notificationservice.verticles;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.internal.logging.Logger;
import io.vertx.core.internal.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;

public class CacheVerticle extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(CacheVerticle.class);
  private final Map<String, Object> cache = new ConcurrentHashMap<>();

  @Override
  public void start(Promise<Void> startPromise) {
    vertx.eventBus().consumer("cache.get", message -> {
      String key = (String)message.body();
      message.reply(cache.get(key));
    });

    vertx.eventBus().consumer("cache.put", message -> {
      JsonObject data = (JsonObject)message.body();
      cache.put(data.getString("key"), data.getValue("value"));
      message.reply("Cached");
    });

    logger.info("Cache Verticle started");
    startPromise.complete();
  }
}
