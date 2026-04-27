package com.gyp.notificationservice.verticles;

import com.gyp.notificationservice.configurations.Constants;
import com.gyp.notificationservice.registry.RouterRegistry;
import com.gyp.notificationservice.registry.VerticleRegistry;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.internal.logging.Logger;
import io.vertx.core.internal.logging.LoggerFactory;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) {
    deployAllVerticles()
      .compose(v -> createHttpServer())
      .onSuccess(v -> {
        logger.info("Main Verticle started successfully on port: " + Constants.PORT);
        startPromise.complete();
      })
      .onFailure(startPromise::fail);
  }

  private Future<Void> deployAllVerticles() {
    VerticleRegistry registry = new VerticleRegistry(vertx);
    return registry.deployAll();
  }

  private Future<Void> undeployVerticles() {
    VerticleRegistry registry = new VerticleRegistry(vertx);
    return registry.undeployAll();
  }

  private Future<HttpServer> createHttpServer() {
    Router router = createRouter();

    return vertx.createHttpServer()
      .requestHandler(router)
      .listen(Constants.PORT);
  }

  private Router createRouter() {
    Router router = Router.router(vertx);

    RouterRegistry routerRegistry = new RouterRegistry(router, vertx);
    routerRegistry.createAllRouter();

    return router;
  }

  @Override
  public void stop(Promise<Void> stopPromise) {
    logger.info("Stopping Main Verticle...");
    stopPromise.complete();
    undeployVerticles().onComplete(ar -> {
      if(ar.succeeded()) {
        logger.info("All verticles undeployed successfully.");
      } else {
        logger.error("Failed to undeploy verticles", ar.cause());
      }
    });
  }
}

