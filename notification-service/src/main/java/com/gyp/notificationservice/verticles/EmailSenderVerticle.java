package com.gyp.notificationservice.verticles;

import com.gyp.notificationservice.configurations.Constants;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.internal.logging.Logger;
import io.vertx.core.internal.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.StartTLSOptions;

public class EmailSenderVerticle extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(EmailSenderVerticle.class);
  private MailClient mailClient;

  @Override
  public void start(Promise<Void> startPromise) {
    initMailClient();
    registerEventBusConsumer();

    logger.info("Email Sender Verticle started");
    startPromise.complete();
  }

  private void initMailClient() {
    MailConfig config = new MailConfig()
      .setHostname(Constants.SMTP_HOST)
      .setPort(Constants.SMTP_PORT)
      .setUsername(Constants.SMTP_USERNAME)
      .setPassword(Constants.SMTP_PASSWORD)
      .setStarttls(StartTLSOptions.REQUIRED);

    this.mailClient = MailClient.create(vertx, config);
  }

  private void registerEventBusConsumer() {
    vertx.eventBus().consumer(Constants.EMAIL_ADDRESS, message -> {
      JsonObject emailData = (JsonObject)message.body();
      sendEmail(emailData)
        .onSuccess(result -> {
          message.reply("Email sent successfully");
          logger.info("Email sent to: " + emailData.getString("to"));
        })
        .onFailure(error -> {
          message.fail(500, "Failed to send email: " + error.getMessage());
          logger.error("Failed to send email", error);
        });
    });
  }

  private Future<Void> sendEmail(JsonObject emailData) {
    MailMessage message = new MailMessage()
      .setFrom(Constants.SMTP_USERNAME)
      .setTo(emailData.getString("to", "target@example.com"))
      .setSubject(emailData.getString("subject", "Notification from Vert.x"))
      .setText(emailData.getString("text", "This is a test notification email!"));

    return mailClient.sendMail(message).mapEmpty();
  }

  @Override
  public void stop(Promise<Void> stopPromise) {
    if(mailClient != null) {
      mailClient.close();
    }
    stopPromise.complete();
  }
}
