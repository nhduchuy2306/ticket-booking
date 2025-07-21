package com.gyp.notificationservice.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.internal.logging.Logger;
import io.vertx.core.internal.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;

public class NotificationVerticle extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(NotificationVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) {
    vertx.eventBus().consumer("notification.send", message -> {
      // Handle notifications
      JsonObject notificationData = (JsonObject)message.body();
      logger.info("Sending notification: " + notificationData.getString("message"));
      message.reply("Notification sent");
    });

    logger.info("Notification Verticle started");
    startPromise.complete();
  }
}
