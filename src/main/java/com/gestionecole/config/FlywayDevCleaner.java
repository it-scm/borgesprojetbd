package com.gestionecole.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FlywayDevCleaner {

    private final Flyway flyway;

    public FlywayDevCleaner(Flyway flyway) {
        this.flyway = flyway;
    }

    @EventListener(org.springframework.boot.context.event.ApplicationReadyEvent.class)
    public void onApplicationReady() {
        String activeProfile = System.getProperty("spring.profiles.active", "default");
        if ("dev".equals(activeProfile)) {
            flyway.clean();
            flyway.migrate();
        }
    }
}

