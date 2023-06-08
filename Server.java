import java.util.function.Supplier;

class Server {

    private final boolean isOccupied;
    private final int id;
    private final int custID;
    private final int lengthQueue;
    private final double serviceUntil;
    private final  ImList<Customer> listCustomers;
    private final Supplier<Double> restTime;
    private final double restTime2;

    public Server(boolean isOccupied, int id, int custID, int lengthQueue, double serviceUntil,
                  ImList<Customer> customers, Supplier<Double> restTime, 
                  double restTime2) {
        this.isOccupied = isOccupied;
        this.id = id;
        this.custID = custID;
        this.lengthQueue = lengthQueue;
        this.serviceUntil = serviceUntil;
        this.listCustomers = customers;
        this.restTime = restTime;
        this.restTime2 = restTime2;
    }

    public Server(boolean isOccupied, int id, int custID, int lengthQueue,
                  Supplier<Double> restTime) {
        this(isOccupied, id, custID, lengthQueue, 0, new ImList<Customer>(),
                restTime, 0);
    }

    public boolean hasOccupied() {
        return this.isOccupied;
    }

    public int getID() {
        return this.id;
    }

    public int getCustID() {
        return this.custID;
    }

    public double getRestTime() {
        return this.restTime.get();
    }

    public boolean isOccupied() {
        return this.isOccupied;
    }

    public boolean checkCustomer(Customer c) {
        return c.getID() == this.custID;
    }

    public int getLengthQueue() {
        return this.lengthQueue;
    }

    public ImList<Customer> getListCustomers() {
        return this.listCustomers;
    }

    public double getRestTime2() {
        return this.restTime2;
    }

    public double getServiceUntil() {
        return this.serviceUntil;
    }

    public double totalServiceRestTime() {
        return this.getRestTime2();
    }
}
