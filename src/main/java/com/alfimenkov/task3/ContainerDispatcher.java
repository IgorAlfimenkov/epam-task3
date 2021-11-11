package com.alfimenkov.task3;

import java.util.concurrent.locks.ReentrantLock;

public class ContainerDispatcher extends Thread {

    private Port port;
    private ReentrantLock lock = new ReentrantLock();

    public ContainerDispatcher(Port port) {
        this.port = port;
    }

    @Override
    public void run(){

        try {

            sleep(100);
            while (true){
                //sleep(100);
                if(port.getStorageSize() == port.getMaxCapacity()) System.out.printf("Недастаточно места на складе. Невозможно добавить контейнеры\n");
                while (port.getStorageSize() == port.getMaxCapacity()) sleep(10);
                lock.lock();
                Container container = port.takeContainerFromQueue();
                port.addContainerInStorage(container);
                lock.unlock();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
