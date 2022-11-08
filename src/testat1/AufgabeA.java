package testat1;


import java.util.concurrent.Semaphore;

public class AufgabeA extends Thread{
    private int id;
    private static Semaphore mutex = new Semaphore(1, true);
    private static Semaphore full = new Semaphore(0,true);
    private static Semaphore empty = new Semaphore(1,true);;

    public AufgabeA (int id){
        this.id = id;
    }

    public static void main(String[] args) {
        AufgabeA cons = new AufgabeA(0);
        AufgabeA prod = new AufgabeA(1);
        cons.start();
        prod.start();
    }

    @Override
    public void run() {
        if(id == 0){
            while (true) {
                try {
                    System.out.println("Zug 0 fährt auf seiner Strecke");
                    sleep(1000);

                    System.out.println("Zug 0 will Weiche durchqueren");
                    enterLok0();

                    System.out.println("Zug 0 fährt durch gemeinsamen Abschnitt");
                    sleep(1000);

                    exitLok0();
                    System.out.println("Zug 0 hat den gemeinsamen Abschnitt verlassen");
                    sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }}else if(id == 1){

            while (true) {
                try {
                    System.out.println("Zug 1 fährt auf seiner Strecke");
                    sleep(1500);
                    System.out.println("Zug 1 will Weiche durchqueren");
                    enterLok1();

                    System.out.println("Zug 1 fährt durch gemeinsamen Abschnitt");
                    sleep(1000);

                    exitLok1();
                    System.out.println("Zug 1 hat den gemeinsamen Abschnitt verlassen");
                    sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }}


    public void enterLok0() throws InterruptedException{
        System.out.println("betritt enterLok0");
        empty.acquire();
        mutex.acquire();


    }

    public void exitLok0(){
        System.out.println("betritt exitLok0");
        mutex.release();
        full.release();

    }

    public void enterLok1() throws InterruptedException{
        System.out.println("betritt enterLok1");
        full.acquire();
        mutex.acquire();
        System.out.println("nach mutex");


    }

    public void exitLok1(){
        System.out.println("betritt exitLok1");
        mutex.release();
        empty.release();

    }

}
