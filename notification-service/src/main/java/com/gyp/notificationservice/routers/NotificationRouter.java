package com.gyp.notificationservice.routers;

import com.gyp.notificationservice.services.NotificationService;
import com.gyp.notificationservice.services.impl.NotificationServiceImpl;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class NotificationRouter implements RouterService {
  private Router router;
  private NotificationService notificationService;

  public NotificationRouter(Router router) {
    this.router = router;
    notificationService = new NotificationServiceImpl();
  }

  @Override
  public Router createRouter() {
    router.route().handler(BodyHandler.create());

    return router;
  }
}
