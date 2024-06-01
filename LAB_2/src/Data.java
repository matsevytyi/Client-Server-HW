public class Data {
    private int state=1;

    public int getState() {
        return state;
    }

    public synchronized void Tic(){
        while (state == 2) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.print("Tic-");
        state = 2;
        notifyAll();
    }
    public synchronized void Tak(){
        while (state == 1) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Tak");
        state = 1;
        notifyAll();
    }
}
