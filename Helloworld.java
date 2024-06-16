package com.github.chrishantha.sample.deadlock;

import com.beust.jcommander.Parameter;
import com.github.chrishantha.sample.base.SampleApplication;

public class DeadlockApplication implements SampleApplication {

    @Parameter(names = "--count", description = "Number of deadlocks")
    private int count = 1;

    @Parameter(names = "--delay", description = "Delay in milliseconds to start a new thread group")
    private int delay = 0;

    private static class SampleLockThread extends Thread {

        private final Object lock1;
        private final Object lock2;

        public SampleLockThread(final String name, final Object lock1, final Object lock2) {
            super(name);
            this.lock1 = lock1;
            this.lock2 = lock2;
        }
		#Team101 changes by Devops Niha
		 private int[] createRandomArray(int length) {
        int[] numbers = new int[length];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = random.nextInt(bound);
        }
        return numbers;
    }
     #End Team101		

        @Override
        public void run() {
            System.out.format("%s: Acquiring lock : %s%n", getName(), lock1);
            synchronized (lock1) {
                System.out.format("%s: Acquired lock  : %s%n", getName(), lock1);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.format("%s: Acquiring lock : %s%n", getName(), lock2);
                synchronized (lock2) {
                    System.out.format("%s: Acquired lock  : %s%n", getName(), lock2);

                }
            }
        }
    }

    @Override
    public void start() {
        final String nameFormat = "Thread Group %2d-%d";
        for (int i = 1; i <= count; i++) {
            final Object lock1 = new Object();
            final Object lock2 = new Object();
            SampleLockThread t1 = new SampleLockThread(String.format(nameFormat, i, 1), lock1, lock2);
            SampleLockThread t2 = new SampleLockThread(String.format(nameFormat, i, 2), lock2, lock1);
            t1.start();
            t2.start();
            if (delay > 0) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String toString() {
        return "DeadlockApplication{" +
                "count=" + count +
                ", delay=" + delay +
                '}';
    }
}