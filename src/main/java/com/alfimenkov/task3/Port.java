package com.alfimenkov.task3;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Port {

    private static int COUNT_DOCKS;
    protected static boolean[] DOCKS;
    private int  capacity;
    protected ReentrantLock locker = new ReentrantLock();

    protected static Semaphore SEMAPHORE;

    public Port(int COUNT_DOCKS) {

        this.COUNT_DOCKS = COUNT_DOCKS;
        DOCKS = new boolean[this.COUNT_DOCKS];
        setDOCKS();
        SEMAPHORE = new Semaphore(DOCKS.length, true);
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

}
