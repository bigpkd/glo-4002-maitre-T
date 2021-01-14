package ca.ulaval.glo4002.application;

import ca.ulaval.glo4002.reservation.ReservationServer;
import ca.ulaval.glo4002.warehouse.WarehouseServer;

public class ApplicationServer {
  public static void main(String[] args) throws InterruptedException {
    Thread warehouse = new Thread(new WarehouseServer(args));
    Thread reservation = new Thread(new ReservationServer());

    reservation.start();
    warehouse.start();

    reservation.join();
    warehouse.join();
  }
}
