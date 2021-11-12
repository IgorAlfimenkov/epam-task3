package com.alfimenkov.task3;

import java.util.concurrent.locks.ReentrantLock;

public class ContainerDispatcher extends Thread {

    private Port port;
    private ReentrantLock lock = new ReentrantLock();
    private static ContainerDispatcher instance;
    private boolean isStarted = false;

    public ContainerDispatcher(Port port) {
        this.port = port;
    }

    public static ContainerDispatcher getInstance(Port port) {

        if(instance == null){

            instance = new ContainerDispatcher(port);
        }
        return instance;
    }

    @Override
    public void run(){

        try {

            sleep(100);
            while (!port.allDockFree()){
                //sleep(100);
                if(port.getStorageSize() == port.getMaxCapacity()) System.out.printf("Недастаточно места на складе. Невозможно добавить контейнеры\n");
                while (port.getStorageSize() == port.getMaxCapacity() && !port.allDockFree()) sleep(10);

                if(!port.queueIsEmpty()){
                    lock.lock();
                    Container container = port.takeContainerFromQueue();
                    port.addContainerInStorage(container);
                    lock.unlock();
                }

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            System.out.printf("Диспетчер закончил работу\n");
        }
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted() {
        isStarted = true;
    }
}
