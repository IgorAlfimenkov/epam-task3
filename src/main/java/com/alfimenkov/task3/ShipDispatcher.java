package com.alfimenkov.task3;

import java.util.concurrent.TimeUnit;

public class ShipDispatcher implements Runnable {

    private static ShipDispatcher instance;
    private Port port;


    public ShipDispatcher(Port port) {
        this.port = port;
    }

    public static ShipDispatcher getInstance(Port port) {
        if(instance == null) {
            instance = new ShipDispatcher(port);
        }
        return instance;
    }

    @Override
    public void run() {

        try {
            while(true){
                Ship ship = port.queue.take();
                if(ship.isForLoad()){
                    port.removeContainers(ship.getNumOfContainers());
                    System.out.printf("Корабль %d забрал %d контейнеров. Общее число контейнеров в порту: %d \n", ship.getShipNum(), ship.getNumOfContainers(), this.port.getCapacity());
                }
                else{
                    port.addContainers(ship.getNumOfContainers());
                    System.out.printf("Корабль %d выгрузил %d контейнеров. Общее число контейнеров в порту: %d \n", ship.getShipNum(), ship.getNumOfContainers(), this.port.getCapacity());
                }

            }

        }catch (InterruptedException e) {}
    }

}
