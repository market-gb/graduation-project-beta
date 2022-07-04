package ru.market_gb.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAsync
@ConditionalOnProperty(name="scheduler.enabled", matchIfMissing = true)
@SpringBootApplication
public class CoreSpringWebApplication {
	public static void main(String[] args) {
		SpringApplication.run(CoreSpringWebApplication.class, args);
	}
}
