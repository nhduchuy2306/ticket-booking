package com.gyp.notificationservice.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.gyp.notificationservice.verticles.CacheVerticle;
import com.gyp.notificationservice.verticles.DatabaseVerticle;
import com.gyp.notificationservice.verticles.EmailSenderVerticle;
import com.gyp.notificationservice.verticles.FileProcessorVerticle;
import com.gyp.notificationservice.verticles.NotificationVerticle;
import com.gyp.notificationservice.verticles.WebSocketVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.internal.logging.Logger;
import io.vertx.core.internal.logging.LoggerFactory;

public class VerticleRegistry {
  private final Vertx vertx;
  private final List<String> deploymentIds = new ArrayList<>();
  private static final Logger logger = LoggerFactory.getLogger(VerticleRegistry.class);

  public VerticleRegistry(Vertx vertx) {
    this.vertx = vertx;
  }

  public Future<Void> deployAll() {
    List<VerticleInfo> verticles = Arrays.asList(
      new VerticleInfo(EmailSenderVerticle::new,
        new DeploymentOptions().setHa(true).setInstances(2)),
      new VerticleInfo(NotificationVerticle::new,
        new DeploymentOptions().setHa(true)),
      new VerticleInfo(FileProcessorVerticle::new,
        new DeploymentOptions().setHa(true)),

      new VerticleInfo(DatabaseVerticle::new,
        new DeploymentOptions().setInstances(1)),
      new VerticleInfo(CacheVerticle::new,
        new DeploymentOptions()),
      new VerticleInfo(WebSocketVerticle::new,
        new DeploymentOptions())
    );

    List<Future<String>> deploymentFutures = verticles.stream()
      .map(this::deployVerticle)
      .collect(Collectors.toList());

    return Future.all(deploymentFutures)
      .onSuccess(composite -> {
        deploymentIds.addAll(composite.list());
        logger.info("All verticles deployed successfully.");
      })
      .mapEmpty();
  }

  private Future<String> deployVerticle(VerticleInfo info) {
    return vertx.deployVerticle(info.getVerticleSupplier(), info.getOptions())
      .onSuccess(id -> {
        deploymentIds.add(id);
        logger.info("Deployed verticle: " + info.getVerticleSupplier().getClass().getSimpleName());
      })
      .onFailure(err -> {
        deploymentIds.removeIf(id -> id.equals(err.getMessage()));
        logger.error("Failed to deploy verticle: " + info.getVerticleSupplier().getClass().getSimpleName() + " - "
          + err.getMessage());
      });
  }

  public Future<Void> undeployAll() {
    List<Future<Void>> undeployFutures = deploymentIds.stream()
      .map(vertx::undeploy)
      .collect(Collectors.toList());

    return Future.all(undeployFutures).mapEmpty();
  }

  public static class VerticleInfo {
    private final Supplier<AbstractVerticle> verticleSupplier;
    private final DeploymentOptions options;

    public VerticleInfo(Supplier<AbstractVerticle> verticleSupplier, DeploymentOptions options) {
      this.verticleSupplier = verticleSupplier;
      this.options = options;
    }

    public AbstractVerticle getVerticleSupplier() {
      return verticleSupplier.get();
    }

    public DeploymentOptions getOptions() {
      return options;
    }
  }
}
