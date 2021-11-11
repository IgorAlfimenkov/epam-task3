package com.alfimenkov.task3;

import com.alfimenkov.task3.Container;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Dock {

    private int dockNum;
    private Semaphore semaphore;
    private ReentrantLock lock = new ReentrantLock();
    private boolean free = true;
    private List<Container> containers = new ArrayList<>();

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

    public List<Container> getContainers() {

        return containers;
    }

    public void addForLoad(Container container) {

        lock.lock();
        containers.add(container);
        lock.unlock();
    }

    public int getDockNum () {

        return dockNum;
    }

    public void leave(Container container) {

        lock.lock();
        containers.remove(container);
        lock.unlock();
    }
}
