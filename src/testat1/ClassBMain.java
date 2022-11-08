package testat1;

public class ClassBMain extends Thread{

    static ClassB1 Lok0 = new ClassB1(0);
    static ClassB2 Lok1 = new ClassB2(1);

    public static void main(String[] args) throws InterruptedException {

        Lok0.start();
        Lok1.start();
    }

}