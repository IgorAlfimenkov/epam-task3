package com.alfimenkov.task3;



import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Container extends Thread {


    private Port port;
    private int containerNum;
    private int dockNum;
    private int shipNum;
    public boolean isOnShip = false;
    public boolean isLoaded = false;
    ReentrantLock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

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
            System.out.printf("Контейнер %d стоит в очереди на загрузку на причале %d \n", containerNum, dockNum);

            sleep(1000);

           /* if(!isOnShip){
                dock.leave(this);
                //lock.lock();
                *//*port.putContainerInQueue(this);*//*
             *//*if (!isLoaded) {
                    condition.await();
                }
                condition.signal();
                lock.unlock();*//*
            }*/


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

    public boolean isOnShip() {
        return isOnShip;
    }

    public void setOnShip(boolean onShip) {
        isOnShip = onShip;
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
