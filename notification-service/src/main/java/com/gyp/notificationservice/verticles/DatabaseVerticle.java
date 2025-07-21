package com.gyp.notificationservice.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.internal.logging.Logger;
import io.vertx.core.internal.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;

public class DatabaseVerticle extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(DatabaseVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) {
    vertx.eventBus().consumer("db.query", message -> {
      // Handle database operations
      JsonObject query = (JsonObject)message.body();
      logger.info("Executing query: " + query.getString("sql"));
      message.reply(new JsonObject().put("result", "Query executed"));
    });

    logger.info("Database Verticle started");
    startPromise.complete();
  }
}
