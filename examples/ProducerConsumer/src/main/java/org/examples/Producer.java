package org.examples;

import java.util.Queue;

public class Producer implements Runnable {

    private Queue<Integer> queue;
    private Object queueMonitor;

    public Producer(Queue<Integer> queue, Object queueMonitor) {
        this.queue = queue;
        this.queueMonitor = queueMonitor;
    }

    @Override
    public void run() {
        for(int i=0;i<100;i++) {
            //synchronized (queue) {
                System.out.println(String.format("Trying to enqueue: %d", i));

                while(!queue.offer(i)) {
                    synchronized (queueMonitor) {
                        try {
                            queueMonitor.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                boolean success = true;
                System.out.println(String.format("Enqueue Successful: %d, %s", i, success));

//            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
