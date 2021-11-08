package com.alfimenkov.task3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantLock;

public class Ship implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Ship.class);

    private int numOfContainers;
    private int capacity;
    private int shipNum;
    private Port port;
    private boolean isForLoad;


    public Ship(int num, Port port, int numOfContainers) {
        this.shipNum = num;
        this.port = port;
        this.numOfContainers = numOfContainers;
        this.isForLoad = false;
    }

    public Ship(int num, Port port, int capacity, boolean isForLoad) {
        this.shipNum = num;
        this.port = port;
        this.numOfContainers = numOfContainers;
        this.isForLoad = isForLoad;
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

            for (int i = 0; i < port.getCountDocks(); i++) {

                if (port.DOCKS[i]) {

                    port.DOCKS[i] = false;
                    dockNum = i;
                    System.out.printf("Корабль %d подошел к причалу %d\n", shipNum, i);
                    if(port.locker.isLocked()) System.out.printf("Корабль %d ожидает доступа к общему ресурсу\n", shipNum);
                    port.locker.lock();
                    if(this.isForLoad){
                        port.removeContainers(this.capacity);
                        System.out.printf("Корабль %d забрал %d контейнеров, общаая вместительность %d\n", shipNum, this.capacity, this.port.getCapacity());

                    }
                    else{
                        port.addContainers(this.capacity);
                        System.out.printf("Корабль %d выгрузил %d контейнеров, общаая вместительность %d\n", shipNum, this.capacity, this.port.getCapacity());
                    }

                    break;
                }
            }

            Port.DOCKS[dockNum] = true;
            Port.SEMAPHORE.release();
            System.out.printf("Корабль %d покинул порт\n", shipNum);

        }catch (InterruptedException e) {

        }finally {
            port.locker.unlock();
        }
    }

}
