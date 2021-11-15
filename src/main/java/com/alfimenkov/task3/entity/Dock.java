package com.alfimenkov.task3.entity;

import java.util.concurrent.Semaphore;

public class Dock {

    private int dockNum;
    private Semaphore semaphore;
    private boolean free = true;


    public Dock(int dockNum) {
        this.dockNum = dockNum;
    }

    public void setSemaphore(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public boolean isFree() {
        return free;
    }


    public void setBusy() {

        free = false;
    }

    public void release() {

        free = true;
        semaphore.release();
    }

    public int getDockNum () {

        return dockNum;
    }

}
