package ca.ulaval.glo4002.reservation;

import ca.ulaval.glo4002.reservation.context.IngredientContext;
import ca.ulaval.glo4002.reservation.context.ReservationContext;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

public class ReservationServer implements Runnable {
  private static final int PORT = 8181;

  public static void main(String[] args) {
    new ReservationServer().run();
  }

  public void run() {
    Server server = new Server(PORT);
    ServletContextHandler contextHandler = new ServletContextHandler(server, "/");

    AbstractBinder rootBinder = getRootBinder();
    AbstractBinder ingredientBinder = getIngredientBinder();
    ResourceConfig packageConfig = new ResourceConfig()
        .packages("ca.ulaval.glo4002.reservation")
        .packages("ca.ulaval.glo4002.ingredient")
        .property(ServerProperties.LOCATION_HEADER_RELATIVE_URI_RESOLUTION_DISABLED, true)
        .property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true)
        .register(JacksonJaxbJsonProvider.class)
        .register(ingredientBinder)
        .register(rootBinder);

    ServletContainer container = new ServletContainer(packageConfig);
    ServletHolder servletHolder = new ServletHolder(container);

    contextHandler.addServlet(servletHolder, "/*");

    try {
      server.start();
      server.join();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (server.isRunning()) {
        server.destroy();
      }
    }
  }

  private AbstractBinder getIngredientBinder() {
    return new AbstractBinder() {
      @Override
      protected void configure() {
        install(new IngredientContext());
      }
    };
  }

  private AbstractBinder getRootBinder() {
    return new AbstractBinder() {
      @Override
      protected void configure() {
        install(new ReservationContext());
      }
    };
  }
}
