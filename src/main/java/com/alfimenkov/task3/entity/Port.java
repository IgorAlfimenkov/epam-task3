package com.alfimenkov.task3.entity;

import com.alfimenkov.task3.dispatcher.ContainerDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Port {

    private int maxCapacity;
    private List<Dock> docks;
    private Queue<Container> storage =  new ArrayDeque<>();
    private BlockingQueue<Container> containersForLoad = new LinkedBlockingQueue<>();
    private ReentrantLock lock = new ReentrantLock();
    private ContainerDispatcher dispatcher;
    private Semaphore semaphore;

    private static final Logger LOGGER = LoggerFactory.getLogger(Port.class);

    public Port(List<Dock> docks, int maxCapacity) {
        this.docks = docks;
        this.maxCapacity = maxCapacity;
        semaphore = new Semaphore(docks.size());
        docks.forEach(dock -> dock.setSemaphore(semaphore));
        dispatcher = ContainerDispatcher.getInstance(this);
    }

    public Dock getDock() throws InterruptedException {

        semaphore.acquire();
        lock.lock();
        Dock dock = docks.stream().filter(d -> d.isFree()).findFirst().get();
        dock.setBusy();
        lock.unlock();
        startDispatcher();
        return dock;
    }

    public Dock getDockByNum(int num) {

        return docks.get(num );
    }

    public ContainerDispatcher getDispatcher() {
        return dispatcher;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getStorageSize() {

        return storage.size();
    }

    public void putContainerInQueue(Container container) throws InterruptedException {
        containersForLoad.put(container);
    }

    public Container takeContainerFromQueue() throws InterruptedException {

        return containersForLoad.take();
    }

    public void addContainerInStorage(Container container) {

        if(storage.size() != maxCapacity) {

            storage.add(container);
            container.setLoaded();
            LOGGER.info("Container {} was added in storage. Total number of containers in storage: {}.", container.getContainerNum(),storage.size());
        }
    }

    public boolean queueIsEmpty() {

        return containersForLoad.isEmpty();
    }

    public boolean hasEnoughContainers(double num) {

        if(storage.size() > num) return true;
        else return false;
    }

    public void loadShip(Ship ship, double num) throws InterruptedException {

        while (storage.size() < num) {

            ship.sleep(10);

        }
        List<Container> forLoad = new ArrayList<>();
        for(int i = 0; i < num; i++) {

            lock.lock();
            Container container = storage.poll();
            ship.add(container);
            LOGGER.info("Container {} was added on ship {}", container.getContainerNum(),ship.getShipNum());
            lock.unlock();
        }
    }

    public void startDispatcher() {
        if(!dispatcher.isStarted()){
            dispatcher.setStarted();
            dispatcher.start();
            LOGGER.info("Dispatcher started...");
        }
    }

    public boolean allDockFree() {
        return docks.stream().allMatch(Dock::isFree);
    }

}

