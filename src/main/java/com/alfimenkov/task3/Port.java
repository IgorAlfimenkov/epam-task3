package com.alfimenkov.task3;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Port {

    private static int COUNT_DOCKS;
    protected static boolean[] DOCKS;
    private int  capacity;
    public ReentrantLock locker = new ReentrantLock();
    public Condition condition = locker.newCondition();
    public static Semaphore SEMAPHORE;
    private static ShipDispatcher dispatcher;
    public BlockingQueue<Ship> queue = new LinkedBlockingQueue<>();

    
    public Port(int COUNT_DOCKS) {

        this.COUNT_DOCKS = COUNT_DOCKS;
        DOCKS = new boolean[this.COUNT_DOCKS];
        setDOCKS();
        SEMAPHORE = new Semaphore(DOCKS.length, true);
        dispatcher = ShipDispatcher.getInstance(this);
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

    protected void acquire() throws InterruptedException {

        this.SEMAPHORE.acquire();
    }

    protected void release() {

        this.SEMAPHORE.release();
    }

    protected void lock() {

        this.locker.lock();
    }

    protected void unlock() {

        this.locker.unlock();
    }

    protected void addContainers(int value) {

        int capacity = this.capacity;

        capacity += value;
        this.capacity = capacity;
    }

    protected void removeContainers(int value) {

        int capacity = this.capacity;
        capacity -= value;
        this.capacity = capacity;
    }

    protected Ship take() throws InterruptedException {

        return this.queue.take();
    }

    protected boolean hasEnoughContainers(Ship ship) {

        if(ship.isForLoad() && ship.getNumOfContainers() > this.getCapacity()) return false;
        else return true;
    }


}
