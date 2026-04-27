package com.gyp.notificationservice.verticles;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.gyp.notificationservice.configurations.Constants;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.internal.logging.Logger;
import io.vertx.core.internal.logging.LoggerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

public class NotificationVerticle extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(NotificationVerticle.class);

  private final Set<String> sentNewEventKeys = ConcurrentHashMap.newKeySet();
  private final Set<String> sentReminderKeys = ConcurrentHashMap.newKeySet();

  private WebClient webClient;
  private LocalDateTime lastCreatedEventCheck;

  @Override
  public void start(Promise<Void> startPromise) {
    webClient = WebClient.create(vertx, new WebClientOptions());
    lastCreatedEventCheck = LocalDateTime.now().minusMinutes(10);

    registerNotificationConsumer();
    registerTimers();
    pollNewEvents();
    pollTomorrowReminders();

    logger.info("Notification Verticle started");
    startPromise.complete();
  }

  private void registerNotificationConsumer() {
    vertx.eventBus().consumer("notification.send", message -> {
      JsonObject notificationData = message.body() instanceof JsonObject jsonObject ? jsonObject : new JsonObject();
      logger.info("Received notification payload: " + notificationData.encode());
      message.reply("Notification received");
    });
  }

  private void registerTimers() {
    vertx.setPeriodic(Constants.CREATED_EVENT_POLL_INTERVAL_MS, id -> pollNewEvents());
    vertx.setPeriodic(Constants.TOMORROW_REMINDER_POLL_INTERVAL_MS, id -> pollTomorrowReminders());
  }

  private void pollNewEvents() {
    fetchJsonArray(Constants.EVENT_SERVICE_BASE_URL + Constants.CREATED_EVENTS_PATH,
            new JsonObject().put("since", lastCreatedEventCheck.toString()))
        .onSuccess(events -> {
          if(events == null || events.isEmpty()) {
            return;
          }

          updateCreatedEventCheckpoint(events);
          fetchCustomerEmails().onSuccess(customers -> {
            List<JsonObject> pendingEvents = events.stream()
                .map(item -> item instanceof JsonObject jsonObject ? jsonObject : new JsonObject())
                .filter(event -> sentNewEventKeys.add(buildEventKey(event)))
                .toList();

            if(pendingEvents.isEmpty() || customers.isEmpty()) {
              return;
            }

            for(JsonObject event : pendingEvents) {
              for(String customerEmail : customers) {
                sendEmail(customerEmail, "New event coming soon: " + event.getString("name", "an upcoming event"),
                    buildNewEventBody(event))
                    .onFailure(error -> logger.error("Failed to send new-event email", error));
              }
            }
          }).onFailure(error -> logger.error("Failed to fetch customers for new event notifications", error));
        })
        .onFailure(error -> logger.error("Failed to poll newly created events", error));
  }

  private void pollTomorrowReminders() {
    fetchJsonArray(Constants.EVENT_SERVICE_BASE_URL + Constants.TOMORROW_EVENTS_PATH, null)
        .onSuccess(events -> {
          if(events == null || events.isEmpty()) {
            return;
          }

          for(Object item : events) {
            JsonObject event = item instanceof JsonObject jsonObject ? jsonObject : new JsonObject();
            if(!sentReminderKeys.add(buildReminderKey(event))) {
              continue;
            }

            String eventId = event.getString("id");
            if(eventId == null || eventId.isBlank()) {
              continue;
            }

            fetchJsonArray(Constants.TICKET_SERVICE_BASE_URL + Constants.TICKETS_BY_EVENT_PATH + "/" + eventId, null)
                .onSuccess(tickets -> {
                  Set<String> recipients = tickets.stream()
                      .map(ticket -> ticket instanceof JsonObject jsonObject ? jsonObject : new JsonObject())
                      .map(ticket -> ticket.getString("attendeeEmail"))
                      .filter(email -> email != null && !email.isBlank())
                      .collect(Collectors.toCollection(HashSet::new));

                  for(String recipient : recipients) {
                    sendEmail(recipient,
                        "Reminder: " + event.getString("name", "your event") + " is tomorrow",
                        buildTomorrowBody(event))
                        .onFailure(error -> logger.error("Failed to send reminder email", error));
                  }
                })
                .onFailure(error -> logger.error("Failed to fetch tickets for event reminder", error));
          }
        })
        .onFailure(error -> logger.error("Failed to poll tomorrow events", error));
  }

  private Future<List<String>> fetchCustomerEmails() {
    return fetchJsonArray(Constants.AUTH_SERVICE_BASE_URL + Constants.CUSTOMERS_PATH, null)
        .map(customers -> customers.stream()
            .map(item -> item instanceof JsonObject jsonObject ? jsonObject : new JsonObject())
            .map(customer -> customer.getString("email"))
            .filter(email -> email != null && !email.isBlank())
            .toList());
  }

  private Future<JsonArray> fetchJsonArray(String url, JsonObject queryParams) {
    var request = webClient.getAbs(url);
    if(queryParams != null) {
      for(String fieldName : queryParams.fieldNames()) {
        request.addQueryParam(fieldName, String.valueOf(queryParams.getValue(fieldName)));
      }
    }

    return request.send().compose(response -> {
      if(response.statusCode() < 200 || response.statusCode() >= 300) {
        return Future.failedFuture(new IllegalStateException(
            "Unexpected status " + response.statusCode() + " from " + url));
      }
      return Future.succeededFuture(response.bodyAsJsonArray());
    });
  }

  private Future<Void> sendEmail(String to, String subject, String text) {
    JsonObject payload = new JsonObject()
        .put("to", to)
        .put("subject", subject)
        .put("text", text);
    return vertx.eventBus().request(Constants.EMAIL_ADDRESS, payload).mapEmpty();
  }

  private String buildNewEventBody(JsonObject event) {
    String note = event.getString("note");
    String startTime = event.getString("startTime");
    return "A new event has just been created: " + event.getString("name", "Unknown event") +
        (startTime != null ? "\nStart time: " + startTime : "") +
        (note != null && !note.isBlank() ? "\nNote: " + note : "");
  }

  private String buildTomorrowBody(JsonObject event) {
    String note = event.getString("note");
    String startTime = event.getString("startTime");
    return "Your event is happening tomorrow: " + event.getString("name", "Unknown event") +
        (startTime != null ? "\nStart time: " + startTime : "") +
        (note != null && !note.isBlank() ? "\nReminder notes: " + note : "");
  }

  private String buildEventKey(JsonObject event) {
    return event.getString("id", "") + ":" + event.getString("createTimestamp", "");
  }

  private String buildReminderKey(JsonObject event) {
    return event.getString("id", "") + ":" + event.getString("startTime", "");
  }

  private void updateCreatedEventCheckpoint(JsonArray events) {
    LocalDateTime maxCreateTimestamp = events.stream()
        .map(item -> item instanceof JsonObject jsonObject ? jsonObject : new JsonObject())
        .map(item -> item.getString("createTimestamp"))
        .filter(value -> value != null && !value.isBlank())
        .map(LocalDateTime::parse)
        .max(LocalDateTime::compareTo)
        .orElse(lastCreatedEventCheck);

    if(maxCreateTimestamp.isAfter(lastCreatedEventCheck)) {
      lastCreatedEventCheck = maxCreateTimestamp;
    }
  }

  @Override
  public void stop(Promise<Void> stopPromise) {
    if(webClient != null) {
      webClient.close();
    }
    stopPromise.complete();
  }
}
