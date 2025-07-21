package com.gyp.notificationservice.registry;

import com.gyp.notificationservice.configurations.Constants;
import io.vertx.ext.web.Router;

public class RouterRegistry {
  private Router router;

  public RouterRegistry(Router router) {
    this.router = router;
  }

  public void createAllRouter() {
    createBaseRouter();
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
