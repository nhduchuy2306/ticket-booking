package com.gyp.orderservice.controllers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequestMapping(OrderController.ORDER_CONTROLLER_RESOURCE_PATH)
public class OrderController {
	public static final String ORDER_CONTROLLER_RESOURCE_PATH = "/orders";

	@GetMapping("/sse/countdown")
	public SseEmitter countdown() {
		// Countdown 15 minutes
		SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

		AtomicInteger remainingTime = new AtomicInteger(15 * 60); // 15 minutes in seconds

		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

		ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(() -> {
			try {
				int currentTime = remainingTime.get();
				log.info("Countdown: {} seconds remaining", currentTime);

				// Send current time
				emitter.send(SseEmitter.event()
						.name("time")
						.data(currentTime));

				// Decrement time
				remainingTime.decrementAndGet();

				// Complete when countdown reaches 0
				if(currentTime <= 0) {
					emitter.send(SseEmitter.event()
							.name("expired")
							.data("Time expired"));
					emitter.complete();
					executor.shutdown();
				}

			} catch(Exception e) {
				emitter.completeWithError(e);
				executor.shutdown();
			}
		}, 0, 1, TimeUnit.SECONDS);

		// Handle client disconnect or timeout
		emitter.onCompletion(() -> {
			scheduledFuture.cancel(false);
			executor.shutdown();
		});

		emitter.onTimeout(() -> {
			scheduledFuture.cancel(false);
			executor.shutdown();
			emitter.complete();
		});

		emitter.onError((ex) -> {
			scheduledFuture.cancel(false);
			executor.shutdown();
		});

		return emitter;
	}
}
