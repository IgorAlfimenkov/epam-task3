package com.alfimenkov.task3.dispatcher;

import com.alfimenkov.task3.entity.Container;
import com.alfimenkov.task3.entity.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantLock;

public class ContainerDispatcher extends Thread {

    private Port port;
    private ReentrantLock lock = new ReentrantLock();
    private static ContainerDispatcher instance;
    private boolean isStarted = false;
    private static final Logger LOGGER = LoggerFactory.getLogger(Container.class);

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

            while (!port.allDockFree()){
                if(port.getStorageSize() == port.getMaxCapacity()) LOGGER.info("Not enough free space in storage. Unable to add containers.");
                while (port.getStorageSize() == port.getMaxCapacity() && !port.allDockFree()) sleep(10);

                if(!port.queueIsEmpty()){
                    lock.lock();
                    Container container = port.takeContainerFromQueue();
                    port.addContainerInStorage(container);
                    lock.unlock();
                }

                sleep(100);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            finishDispatcher();
            LOGGER.info("Dispatcher finished work...");
        }
    }

    public void finishDispatcher() {

        isStarted = false;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted() {
        isStarted = true;
    }
}
