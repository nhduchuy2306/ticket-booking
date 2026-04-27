package com.gyp.notificationservice.registry;

import com.gyp.notificationservice.configurations.Constants;
import com.gyp.notificationservice.routers.NotificationRouter;
import io.vertx.ext.web.Router;
import io.vertx.core.Vertx;

public class RouterRegistry {
  private Router router;
  private final Vertx vertx;

  public RouterRegistry(Router router, Vertx vertx) {
    this.router = router;
    this.vertx = vertx;
  }

  public void createAllRouter() {
    createBaseRouter();
    new NotificationRouter(router, vertx).createRouter();
  }

  private void createBaseRouter() {
    // Health check endpoint
    router.get("/health").handler(ctx -> {
      ctx.response()
        .putHeader("content-type", "application/json")
        .end("{\"status\":\"UP\",\"port\":" + Constants.PORT + "}");
    });

    // 404 handler
    router.route().last().handler(ctx -> {
      ctx.response().setStatusCode(404).end("Not Found");
    });
  }
}
