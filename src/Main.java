import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition a = lock.newCondition();
        Condition b = lock.newCondition();
        Condition c = lock.newCondition();
        Thread ct = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    lock.lock();
                    try {
                        c.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.print("c");
                    a.signal();
                } finally {
                    lock.unlock();
                }
            }
        }, "c");

        Thread bt = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    lock.lock();
                    try {
                        b.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.print("b");
                    c.signal();
                } finally {
                    lock.unlock();
                }
            }
        }, "b");

        Thread at = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    lock.lock();
                    System.out.print("a");
                    b.signal();
                    a.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        }, "a");

        ct.start();
        bt.start();
        at.start();
    }
}

















































































































