package testat1;


import java.util.concurrent.Semaphore;

public class ClassB1 extends Thread{

    public static final int FAHREND = 0;
    public static final int WARTEND = 1;
    public static final int FAHRENDTEIL = 2;

    int id;
    int ctr0 = 0;

    static Semaphore privsem[] = new Semaphore[2];
    static Semaphore mutex = new Semaphore(1, true) ;
    static int state[] = new int[2];

    public ClassB1(int id) {
        this.id = id;
        this.ctr0 = ctr0;
        state[id] = FAHREND;
        privsem[id] = new Semaphore(0, true);
    }


    public void run() {
        while(true){
            try {
                System.out.println("Zug 0 fährt auf seiner Strecke");
                sleep(1000);
                enterLok0();
                System.out.println("Zug 0 fährt durch den gemeinsamen Teil");
                sleep(700);
                exitLok0();
                ctr0++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void enterLok0() throws InterruptedException {
        ClassBMain.Lok0.state[0] = FAHREND;
        System.out.println("Zug 0 kommt an der Weiche an");
        mutex.acquire();
        if (ClassBMain.Lok1.state[1] != FAHRENDTEIL && ctr0 == ClassBMain.Lok1.ctr1) {
            ClassBMain.Lok0.state[0] = FAHRENDTEIL;
            ClassBMain.Lok0.privsem[0].release();
        } else {
            ClassBMain.Lok0.state[0] = WARTEND;
            System.out.println("Zug 0 wartet");
        }
        mutex.release();
        ClassBMain.Lok0.privsem[0].acquire();
    }
    public void exitLok0() throws InterruptedException {
        mutex.acquire();
        System.out.println("Zug 0 ist durch den gemeinsamen Teil");
        ClassBMain.Lok0.state[0] = FAHREND;
        if(ClassBMain.Lok1.state[1] == WARTEND && ClassBMain.Lok0.state[0] != FAHRENDTEIL){
            System.out.println("           Zug " + id + " weckt Zug " + (id+1)%2);
            ClassBMain.Lok1.state[1] = FAHRENDTEIL;
            ClassBMain.Lok1.privsem[1].release();
        }
        mutex.release();
    }
}

