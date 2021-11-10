package com.alfimenkov.task3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

        try{

            checkFreeDock();
            int dockNum = -1;

            for (int i = 0; i < port.getCountDocks(); i++) {

                if (port.DOCKS[i]) {

                    dockNum = i;
                    enterDock(i);
                    break;
                }

            }

            releaseDock(dockNum);
        }catch (Exception e) { }
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isForLoad() {
        return isForLoad;
    }

    public int getNumOfContainers() {
        return numOfContainers;
    }

    public int getShipNum() {
        return shipNum;
    }

    private void checkFreeDock() throws InterruptedException {
        port.acquire();
        System.out.printf("Корабль %d проверяет есть ли свободный причал...\n", shipNum);
    }

    private void enterDock(int i) throws InterruptedException {
        port.DOCKS[i] = false;
        System.out.printf("Корабль %d подошел к причалу %d\n", shipNum, i);
        if(port.locker.isLocked()) System.out.printf("Корабль %d ожидает доступа к общему ресурсу\n", shipNum);
        getAccessToSharedResource();
    }

    private void getAccessToSharedResource() throws InterruptedException {
        port.lock();
        if(!port.hasEnoughContainers(this)){
            System.out.printf("Корабль %d ждет пока появятся контейнеры. Общее число контейнеров: %d\n", this.getShipNum(), port.getCapacity());
            port.condition.await();
        }
        port.condition.signal();
        port.queue.put(this);
        ((Thread)this).sleep(1000);
        port.unlock();
    }

    private void releaseDock( int dockNum) {
        port.DOCKS[dockNum] = true;
        port.release();
        System.out.printf("Корабль %d покинул порт\n", shipNum);
    }

}
