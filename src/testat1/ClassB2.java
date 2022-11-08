package testat1;

import java.util.concurrent.Semaphore;

public class ClassB2 extends Thread {

    public static final int FAHREND = 0;
    public static final int WARTEND = 1;
    public static final int FAHRENDTEIL = 2;

    int id;
    int ctr1 = 0;

    static Semaphore privsem[] = new Semaphore[2];
    static Semaphore mutex = new Semaphore(1, true);
    static int state[] = new int[2];

    public ClassB2(int id) {
        this.id = id;
        this.ctr1 = ctr1;
        state[id] = FAHREND;
        privsem[id] = new Semaphore(0, true);
    }


    public void run() {
        while (true) {
            try {
                System.out.println("Zug 1 fährt auf seiner Strecke");
                sleep(400);
                enterLok1();
                System.out.println("Zug 1 fährt durch den gemeinsamen Teil");
                sleep(3000);
                exitLok1();
                ctr1++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void enterLok1() throws InterruptedException {
        ClassBMain.Lok0.state[1] = FAHREND;
        System.out.println("Zug 1 kommt an der Weiche an");
        mutex.acquire();
        if (ClassBMain.Lok0.state[0] != FAHRENDTEIL && ctr1 < ClassBMain.Lok0.ctr0) {
            ClassBMain.Lok1.state[1] = FAHRENDTEIL;
            ClassBMain.Lok1.privsem[1].release();
        } else {
            ClassBMain.Lok1.state[1] = WARTEND;
            System.out.println("Zug 1 wartet");
        }
        mutex.release();
        ClassBMain.Lok1.privsem[1].acquire();
    }

    public void exitLok1() throws InterruptedException {
        mutex.acquire();
        System.out.println("Zug 1 ist durch den gemeinsamen Teil");
        ClassBMain.Lok1.state[1] = FAHREND;
        if (ClassBMain.Lok0.state[0] == WARTEND && ClassBMain.Lok1.state[1] != FAHRENDTEIL) {
            System.out.println("           Zug " + id + " weckt Zug " + (id + 1) % 2);
            ClassBMain.Lok0.state[0] = FAHRENDTEIL;
            ClassBMain.Lok0.privsem[0].release();
        }
        mutex.release();
    }
}