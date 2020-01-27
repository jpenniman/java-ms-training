package org.examples;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Queue<Integer> queue = new ArrayBlockingQueue<>(5);

        Object queueMonitor = new Object();

        Producer p = new Producer(queue, queueMonitor);
        Consumer c = new Consumer(queue, queueMonitor);

        Thread producerThread = new Thread(p);
        Thread consumerThread = new Thread(c);

        producerThread.start();
        consumerThread.start();

        //Thread.sleep(5);
        //c.stop();
    }
}
