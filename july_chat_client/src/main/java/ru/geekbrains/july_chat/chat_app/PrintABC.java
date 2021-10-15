package ru.geekbrains.july_chat.chat_app;

public class PrintABC {
    private final Object mon = new Object();
    private volatile char currentLetter = 'A';

    public static void main(String[] args) {
        PrintABC printABC = new PrintABC();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (printABC.mon) {
                    try {
                        for (int i = 0; i < 5; i++) {
                            while (printABC.currentLetter != 'A') {
                                printABC.mon.wait();
                            }
                            System.out.print("A");
                            printABC.currentLetter = 'B';
                            printABC.mon.notifyAll();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (printABC.mon) {
                    try {
                        for (int i = 0; i < 5; i++) {
                            while (printABC.currentLetter != 'B') {
                                printABC.mon.wait();
                            }
                            System.out.print("B");
                            printABC.currentLetter = 'C';
                            printABC.mon.notifyAll();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (printABC.mon) {
                    try {
                        for (int i = 0; i < 5; i++) {
                            while (printABC.currentLetter != 'C') {
                                printABC.mon.wait();
                            }
                            System.out.print("C");
                            printABC.currentLetter = 'A';
                            printABC.mon.notifyAll();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
