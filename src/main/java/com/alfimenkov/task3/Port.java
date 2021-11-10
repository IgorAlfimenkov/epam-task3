package com.alfimenkov.task3;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Port {

    private static int COUNT_DOCKS;
    protected static boolean[] DOCKS;
    private int  capacity;
    public ReentrantLock locker = new ReentrantLock();
    public static Semaphore SEMAPHORE;
    private ShipDispatcher dispatcher;
    public BlockingQueue<Ship> queue = new LinkedBlockingQueue<>();
    LinkedList<Ship> ships = new LinkedList<>();

    public Port(int COUNT_DOCKS) {

        this.COUNT_DOCKS = COUNT_DOCKS;
        DOCKS = new boolean[this.COUNT_DOCKS];
        setDOCKS();
        SEMAPHORE = new Semaphore(DOCKS.length, true);
        dispatcher = new ShipDispatcher(this);
        new Thread(dispatcher).start();
    }

    public void setDOCKS() {
        for (int i = 0; i < COUNT_DOCKS; i++) {

            DOCKS[i] = true;
        }
    }

    public int getCapacity() {
        return capacity;
    }

    public static int getCountDocks() {
        return COUNT_DOCKS;
    }

    public void acquire() throws InterruptedException {

        this.SEMAPHORE.acquire();
    }

    public void release() {

        this.SEMAPHORE.release();
    }

    public void lock() {

        this.locker.lock();
    }

    public void unlock() {

        this.locker.unlock();
    }

    public void addContainers(int value) {

        int capacity = this.capacity;

        capacity += value;
        this.capacity = capacity;
    }

    public void removeContainers(int value) {

        int capacity = this.capacity;
        capacity -= value;
        this.capacity = capacity;
    }

    public void addShip(Ship ship) {

        this.ships.add(ship);
    }

    public void removeShip(Ship ship) {

        this.ships.remove(ship);
    }

    public boolean isValid(Ship ship){
        if(ship.isForLoad() && ship.getNumOfContainers() > this.getCapacity()) return false;
        else return true;
    }

    public boolean hasEnoughContainers(Ship ship) {

        if(ship.isForLoad() && ship.getNumOfContainers() > this.getCapacity()) return false;
        else return true;
    }


}
