package com.gyp.notificationservice.routers;

import com.gyp.notificationservice.configurations.Constants;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class NotificationRouter implements RouterService {
  private Router router;
  private final Vertx vertx;

  public NotificationRouter(Router router, Vertx vertx) {
    this.router = router;
    this.vertx = vertx;
  }

  @Override
  public Router createRouter() {
    router.post(Constants.SEND_EMAIL_PATH)
      .handler(BodyHandler.create())
      .handler(ctx -> {
        JsonObject payload = ctx.body().asJsonObject();
        if(payload == null || payload.getString("to") == null || payload.getString("to").isBlank()) {
          ctx.response().setStatusCode(400).end("Missing recipient email");
          return;
        }

        vertx.eventBus().request(Constants.EMAIL_ADDRESS, payload)
          .onSuccess(result -> ctx.response().setStatusCode(200).end("Email sent successfully"))
          .onFailure(error -> ctx.response().setStatusCode(500)
            .end("Failed to send email: " + error.getMessage()));
      });

    return router;
  }
}
