package com.yet.spring.core;

import com.yet.spring.core.beans.Client;
import com.yet.spring.core.beans.Event;
import com.yet.spring.core.beans.EventType;
import com.yet.spring.core.loggers.EventLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.DateFormat;
import java.util.Map;

public class App {
  @Autowired
  private Client client;
/*  private ConsoleEventLogger eventLogger;
  private FileEventLogger fileEventLogger;*/
  private EventLogger defaultLogger;
  private Map<EventType, EventLogger> loggers;

  public App() {}

  public App(Client client,
             EventLogger eventLogger,
             Map<EventType, EventLogger> loggers) {
    this.client = client;
    this.defaultLogger = eventLogger;
    this.loggers = loggers;
    //this.eventLogger = eventLogger;
  }

  public static void main(String[] args) {
    ConfigurableApplicationContext ctx =
            new ClassPathXmlApplicationContext(
                    "spring.xml");
/*    ConfigurableApplicationContext parent =
            new ClassPathXmlApplicationContext(
                    "spring.xml");
    ConfigurableApplicationContext child =
            new ClassPathXmlApplicationContext(
                    "loggers.xml",
                    parent.toString());*/

    App app = ctx.getBean(App.class);
    app.logEvents(ctx);
    ctx.close();

/*    app.logEvent("Some event for User 1");
    app.logEvent("Some event for User 2");*/
  }

  public void logEvents(ApplicationContext ctx) {
    Event event = ctx.getBean(Event.class);
    logEvent(EventType.INFO, event, "Some event for User 1");
    event = ctx.getBean(Event.class);
    logEvent(EventType.INFO, event, "One more event for User 1");
    event = ctx.getBean(Event.class);
    logEvent(EventType.ERROR, event, "Some event for User 2");
  }

  private void logEvent(EventType type,
                        Event event,
                        String msg) {
    String message = msg.replaceAll(client.getId(),
            client.getFullName());
    event.setMsg(message);
    //eventLogger.logEvent(event);
    //fileEventLogger.logEvent(event);
    EventLogger logger = loggers.get(type);
    if (logger == null) {
      logger = defaultLogger;
    }
    logger.logEvent(event);
  }

  public EventLogger getDefaultLogger() {
    return defaultLogger;
  }

}
