package com.alfimenkov.task3;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Currency;
import java.util.logging.Handler;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

public class testPort {

    @Test
    public void testPort() throws InterruptedException {


        ///GIVEN
        List<Dock> docks = Initializer.docks();
        Port port = new Port(docks,6);
        List<Ship> ships = Initializer.getShips(port);

        ///WHEN
        ships.forEach(ship -> ship.start());
        ships.forEach(ship -> {
            try {
                ship.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        ///THEN
        Thread.currentThread().sleep(1000);
        System.out.printf("Total containers in port storage: %d\n",port.getStorageSize());
        Assertions.assertNotEquals(true,port.getDispatcher().isStarted());

    }
}
