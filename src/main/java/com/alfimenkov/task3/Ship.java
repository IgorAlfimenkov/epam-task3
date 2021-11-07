package com.alfimenkov.task3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ship implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Ship.class);

    private int numOfContainers;
    private int capacity;
    private int shipNum;
    private Port port;


    public Ship(int num, Port port, int capacity) {
        this.shipNum = num;
        this.port = port;
        this.capacity = capacity;
    }

    public Ship(int shipNum, int numOfContainers, int capacity) {

        this.shipNum = shipNum;
        this.numOfContainers = numOfContainers;
        this.capacity = capacity;
    }

    @Override
    public void run() {

        System.out.printf("Корабль %d подошел к порту.\n", shipNum);
        try{

            port.SEMAPHORE.acquire();
            System.out.printf("Корабль %d проверяет есть ли свободный причал...\n", shipNum);
            int dockNum = -1;
            synchronized (port.DOCKS){

                for (int i = 0; i < port.COUNT_DOCKS; i++) {

                    if (port.DOCKS[i]) {

                        port.DOCKS[i] = false;
                        dockNum = i;
                        System.out.printf("Корабль %d подошел к причалу %d\n", shipNum, i);
                        port.changeCapacity(this.capacity);
                        System.out.printf("Корабль %d выгрузил %d контейнеров\n", shipNum, this.capacity);
                        break;
                    }
                }
            }

            //Thread.sleep(300);

            synchronized (port.DOCKS) {
                Port.DOCKS[dockNum] = true;
                Port.SEMAPHORE.release();
                System.out.printf("Корабль %d покинул порт\n", shipNum);
            }
        }catch (InterruptedException e) {}
    }
}
