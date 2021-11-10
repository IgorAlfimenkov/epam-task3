package com.alfimenkov.task3;

import org.omg.PortableServer.POA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantLock;

public class Ship extends Thread {

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

    public Ship(int num, Port port, int numOfContainers, boolean isForLoad) {
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
        port.addShip(this);
        try{

            port.acquire();
            System.out.printf("Корабль %d проверяет есть ли свободный причал...\n", shipNum);
            int dockNum = -1;

            for (int i = 0; i < port.getCountDocks(); i++) {

                if (port.DOCKS[i]) {

                    port.DOCKS[i] = false;
                    dockNum = i;
                    System.out.printf("Корабль %d подошел к причалу %d\n", shipNum, i);
                    while(!port.hasEnoughContainers(this)){
                        System.out.printf("Корабль %d ждет пока появятся контейнеры. Общее число контейнеров: %d\n", this.getShipNum(), port.getCapacity());
                        ((Thread)this).sleep(1000);
                    }
                    if(port.locker.isLocked()) System.out.printf("Корабль %d ожидает доступа к общему ресурсу\n", shipNum);

                    port.lock();
                    port.queue.put(this);
                    break;
                }
            }

            port.DOCKS[dockNum] = true;
            port.release();
            System.out.printf("Корабль %d покинул порт\n", shipNum);

        }catch (Exception e) {

        }finally {
            port.unlock();
        }
        port.removeShip(this);
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isForLoad() {
        return isForLoad;
    }

    public int getNumOfContainers() {
        return numOfContainers;
    }

    public void setNumOfContainers(int numOfContainers) {
        this.numOfContainers += numOfContainers;
    }

    public int getShipNum() {
        return shipNum;
    }

}
