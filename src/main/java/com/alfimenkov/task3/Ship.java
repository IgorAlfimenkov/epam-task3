package com.alfimenkov.task3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Ship extends Thread {

    private int shipNum;
    private List<Container> containers;
    private Port port;
    private ReentrantLock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    private int maxCapacity;
    private boolean getFromStorage;

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
            System.out.printf("Корабль %d подошел к причалу %d\n", shipNum, dock.getDockNum());
            releaseContainers(dock);

            prepareToLeavePort(dock);

            leavePort(dock);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void releaseContainers(Dock dock) throws InterruptedException {

        containers.forEach(container -> container.setDockNum(dock.getDockNum()));
        containers.forEach(Thread::start);
        containers.clear();
        System.out.printf("Корабль %d выгрузил контейнеры на причале %d\n", shipNum, dock.getDockNum());
        sleep((long) (100 + Math.random() * 300));
    }

    public void prepareToLeavePort(Dock dock) throws InterruptedException {

        this.sleep(100);
        int num = 1 + (int)(Math.random()*(maxCapacity+1));
        if(getFromStorage == true){
            System.out.printf("Корабль %d хочет забрать %d контейнеров со склада\n",shipNum, num);
            if(!port.hasEnoughContainers(num))  System.out.printf("Корабль %d ждет пока появится достаточное количество контейнеров на складе\n",getShipNum());
            port.loadShip(this,num);
            sleep(100);
        }
        else if(!port.queueIsEmpty()) {
            for(int i = 0; i < num; i++)
            {
                lock.lock();
                Container container = port.takeContainerFromQueue();
                if(container != null){
                    add(container);
                    System.out.printf("Корабль %d забрал контейнер %d\n", shipNum, container.getContainerNum());
                }
                lock.unlock();

            }
        }
        System.out.printf("Корабль %d забрал %d контейнеров. Общее количество контейнеров на складе: %d\n", shipNum,containers.size(), port.getStorageSize());
    }

    public void leavePort(Dock dock) {

        /*containers.forEach(container -> {
            container.setOnShip(true);
        });*/
        dock.release();
        System.out.printf("Корабль %d покинул порт\n",shipNum);
    }

    public void add(Container container) {


        containers.add(container);
    }

    public void addAll(List<Container> containers) {


        containers.addAll(containers);

    }

    public int getShipNum() {
        return shipNum;
    }
}
