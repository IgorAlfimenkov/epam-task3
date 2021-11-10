package com.alfimenkov.task3;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestPort {

    @Test
    public void getCapacityTest() throws InterruptedException {


        //GIVEN

        Port port = new Port(3);
        Thread thread = new Thread(new Ship(1,port,5,true));
        Thread thread1 = new Thread(new Ship(2,port,10));
        Thread thread2 = new Thread(new Ship(3,port,15,true));
        Thread thread3 = new Thread(new Ship(4,port,20));

        int expectedResult = 10;

        //WHEN
        thread.start();
        thread1.start();
        thread2.start();
        thread3.start();
        thread.join();
        thread1.join();
        thread2.join();
        thread3.join();
        int result = port.getCapacity();

        //THEN
        System.out.println(result);
        Assertions.assertEquals(expectedResult,result);
    }

}
