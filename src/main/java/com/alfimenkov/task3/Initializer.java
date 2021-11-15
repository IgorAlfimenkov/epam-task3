package com.alfimenkov.task3;

import java.util.ArrayList;
import java.util.List;

public class Initializer {


    private static int  containerNum = 0;
    private static int  shipNum = 0;




    public static List<Ship> getShips(Port port) {

        List<Ship> ships = new ArrayList<>();
        ships.add(getShip(port));
        ships.add(getShip(port));
        ships.add(getShip(port));
        ships.add(getShip(port));
        ships.add(getShip(port));

        ships.get(1).setGetFromStorage(true);
        ships.get(3).setGetFromStorage(true);

        return ships;
    }

    public static Ship getShip(Port port) {
        int capacity = 1 + (int)(Math.random()*6);
        Ship ship = new Ship(shipNum,containers(port),port,capacity);
        shipNum +=1;
        return ship;
    }

    public static List<Container> containers(Port port) {
        List<Container> containers = new ArrayList<>();
        containers.add(getContainer(port));
        containers.add(getContainer(port));
        containers.add(getContainer(port));
        containers.add(getContainer(port));
        return containers;
    }

    public static Container getContainer(Port port) {
        Container container = new Container(port, containerNum);
        containerNum +=1;
        return container;
    }

    public static List<Dock> docks() {
        List<Dock> docks = new ArrayList<>();
        docks.add(new Dock(0));
        docks.add(new Dock(1));
        return docks;
    }
}
