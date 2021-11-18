package com.alfimenkov.task3.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Ship extends Thread {

    private int shipNum;
    private List<Container> containers;
    private Port port;
    private ReentrantLock lock = new ReentrantLock();
    private int maxCapacity;
    private boolean getFromStorage;// If ship wants to get Containers from storage

    private static final Logger LOGGER = LoggerFactory.getLogger(Ship.class);

    public Ship(int shipNum, List<Container> containers, Port port, int capacity) {
        this.shipNum = shipNum;
        this.containers = containers;
        this.port = port;
        this.maxCapacity = capacity;
        containers.forEach(container -> container.setShipNum(shipNum));
        this.getFromStorage = false;
    }

    public Ship(int shipNum, List<Container> containers, Port port, int capacity, boolean getFromStorage) {
        this.shipNum = shipNum;
        this.containers = containers;
        this.port = port;
        this.maxCapacity = capacity;
        containers.forEach(container -> container.setShipNum(shipNum));
        this.getFromStorage = getFromStorage;
    }

    @Override
    public void run() {

        try {
            Dock dock = port.getDock();
            LOGGER.info("Ship {} enter dock {}.",shipNum,dock.getDockNum());
            releaseContainers(dock);

            prepareToLeavePort(dock);

            leavePort(dock);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getShipNum() {
        return shipNum;
    }

    public void releaseContainers(Dock dock) throws InterruptedException {

        containers.forEach(container -> container.setDockNum(dock.getDockNum()));
        containers.forEach(Thread::start);
        containers.clear();

        LOGGER.info("Ship {} leave containers in dock {}", shipNum,dock.getDockNum());
        sleep((long) (100 + Math.random() * 300));
    }

    public void prepareToLeavePort(Dock dock) throws InterruptedException {

        sleep(100);
        int num = 1 + (int)(Math.random()*(maxCapacity+1));
        if(getFromStorage == true){
            LOGGER.info("Ship {} wants to get {} containers from storage.",shipNum, num);
            if(!port.hasEnoughContainers(num))
                LOGGER.info("Ship {} waiting until there are enough containers in the storage",shipNum);
            port.loadShip(this,num);
            sleep(100);
        }
        else {
            for(int i = 0; i < num; i++)
            {

                if(!port.queueIsEmpty()){
                    lock.lock();
                    Container container = port.takeContainerFromQueue();
                    add(container);
                    LOGGER.info("Ship {} take container {}", shipNum,container.getContainerNum());
                    lock.unlock();
                }
            }
        }
        LOGGER.info("Ship {} take {} containers. Total number of containers in storage: {}",shipNum,containers.size(),port.getStorageSize());
    }

    public void leavePort(Dock dock) {
        dock.release();
        LOGGER.info("Ship {} leave port...", shipNum);
    }

    public void add(Container container) {


        containers.add(container);
    }

    public void setGetFromStorage(boolean getFromStorage) {
        this.getFromStorage = getFromStorage;
    }
}
