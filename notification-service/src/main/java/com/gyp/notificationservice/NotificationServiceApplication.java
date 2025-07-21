package com.gyp.notificationservice;

import com.gyp.notificationservice.configurations.Constants;
import com.gyp.notificationservice.verticles.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.internal.logging.Logger;
import io.vertx.core.internal.logging.LoggerFactory;

public class NotificationServiceApplication {
	private static final Logger logger = LoggerFactory.getLogger(NotificationServiceApplication.class);

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new MainVerticle())
				.onSuccess(deploymentId -> {
					logger.info("Application started successfully!");
					logger.info("Server running on http://localhost:" + Constants.PORT);
					logger.info("Health check: http://localhost:" + Constants.PORT + "/health");
					logger.info("Send email: POST http://localhost:" + Constants.PORT + Constants.SEND_EMAIL_PATH);
				})
				.onFailure(Throwable::printStackTrace);
	}
}
