package org.examples;

import java.util.Queue;

public class Consumer implements Runnable{

    private Queue<Integer> queue;
    private Object queueMonitor;
    private boolean isRunning = true;

    public Consumer(Queue<Integer> queue, Object queueMonitor) {
        this.queue = queue;
        this.queueMonitor = queueMonitor;
    }

    @Override
    public void run() {
        while(true) {
            //synchronized (queue) {
                if (!queue.isEmpty()) {
                    Integer i = queue.poll();
                    try {
                        Thread.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(String.format("Consumed: %d", i));
                    synchronized (queueMonitor) {
                        queueMonitor.notifyAll();
                    }
                }
                 if (!isRunning && queue.isEmpty())
                     break;
           // }
        }
    }

    public void stop() {
        isRunning = false;
    }

}
