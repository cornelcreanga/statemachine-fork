package com.example.cornel.demo.statemachine;

public class Util {

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) { }
    }

    public static void work(States s) {
        System.err.println("working " + s + " on " + Thread.currentThread().getName());
        sleep((long) (2500 + 1500 * Math.random()));
    }
}
