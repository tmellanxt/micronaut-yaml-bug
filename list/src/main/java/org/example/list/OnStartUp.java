package org.example.list;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.function.Consumer;

@Singleton
public class OnStartUp implements ApplicationEventListener<StartupEvent> {

  @Inject private ApplicationContext applicationContext;

  @Override
  public void onApplicationEvent(StartupEvent event) {
    applicationContext.getProperty("configuration.name", String.class).ifPresent(s -> System.out.println("Configuration value: " + s));
  }
}
