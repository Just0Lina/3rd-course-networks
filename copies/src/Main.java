// rm -rf bin
//javac -d bin ./*
//java -classpath ./bin Main 230.100.12.12

public class Main {

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            System.out.println("Args");
            System.exit(0);
        }
        Copies copies = new Copies();
        Sender sender = new Sender(args[0]);
        Receiver receiver = new Receiver(args[0], copies);

        Thread sendThread = new Thread(sender);
        Thread receiverThread = new Thread(receiver);

        sendThread.start();
        receiverThread.start();


        while (!Thread.interrupted()) {
            try {
                Thread.sleep(5000);
                copies.deleteCopies();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            sendThread.interrupt();
            sendThread.join();
            receiverThread.interrupt();
            receiverThread.join();
            System.exit(0);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}