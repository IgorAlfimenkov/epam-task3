package com.alfimenkov.task3;

import com.alfimenkov.task3.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Port {

    private List<Dock> docks;
    private Semaphore semaphore;
    private ReentrantLock lock = new ReentrantLock();

    private BlockingQueue<Container> containersForLoad = new LinkedBlockingQueue<>();
    private List<Container> storage = new ArrayList<>();
    private int maxCapacity;

    public Port(List<Dock> docks, int maxCapacity) {
        this.docks = docks;
        this.maxCapacity = maxCapacity;
        semaphore = new Semaphore(docks.size());
        docks.forEach(dock -> dock.setSemaphore(semaphore));
    }

    public Dock getDock() throws InterruptedException {

        semaphore.acquire();
        lock.lock();
        Dock dock = docks.stream().filter(d -> d.isFree()).findFirst().get();
        dock.setBusy();
        lock.unlock();
        return dock;
    }

    public Dock getDockByNum(int num) {

        return docks.get(num);
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
            System.out.printf("Контейнер %d был добавлен на склад. Общее количество контейнеров на складе: %d\n", container.getContainerNum(), storage.size());
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
            ship.add(storage.get(i));
            forLoad.add(storage.get(i));
            lock.unlock();
        }
        storage.removeAll(forLoad);
    }

}
