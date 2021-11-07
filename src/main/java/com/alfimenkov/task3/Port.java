package com.alfimenkov.task3;

import java.util.concurrent.Semaphore;

public class Port {

    protected static int COUNT_DOCKS = 3;
    protected static boolean[] DOCKS = new boolean[COUNT_DOCKS];
    private int  capacity;

    protected static Semaphore SEMAPHORE = new Semaphore(DOCKS.length,
            true);

    public Port() {


    }

    public void setDOCKS() {
        for (int i = 0; i < COUNT_DOCKS; i++) {

            DOCKS[i] = true;
        }
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void changeCapacity(int value) {

        int capacity = this.capacity;

        capacity += value;
         this.capacity = capacity;
    }
}
