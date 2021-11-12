package com.alfimenkov.task3;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Container extends Thread {


    private Port port;
    private int containerNum;
    private int dockNum;
    private int shipNum;
    public boolean isLoaded = false;

    private static final Logger LOGGER = LoggerFactory.getLogger(Container.class);

    public Container(Port port, int containerNum) {
        this.port = port;
        this.containerNum = containerNum;
    }


    @Override
    public void run(){

        try {
            sleep(100);

            Dock dock = port.getDockByNum(dockNum);
            port.putContainerInQueue(this);
            LOGGER.info("Container {} stay in queue in the dock {}",containerNum,dockNum);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public int getContainerNum() {
        return containerNum;
    }

    public void setDockNum(int dockNum) {
        this.dockNum = dockNum;
    }

    public int getShipNum() {
        return shipNum;
    }

    public void setShipNum(int shipNum) {
        this.shipNum = shipNum;
    }

    public void setLoaded() {

        isLoaded = true;
    }
}
