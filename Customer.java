import java.util.function.Supplier;

class Customer {

    private final double arrivalTimes;
    private final Supplier<Double> serviceTime;
    private final int id;
    private final double time;
    private final double waitingTime;
    private final int queueNumb;

    public Customer(double arrivalTimes, Supplier<Double> serviceTime, int id, double time, 
            double waitingTime, int queueNumb) {
        this.arrivalTimes = arrivalTimes;
        this.serviceTime = serviceTime;
        this.id = id;
        this.time = time;
        this.waitingTime = waitingTime;
        this.queueNumb = queueNumb;
    }

    public Customer(double arrivalTimes, Supplier<Double> serviceTime, int id) {
        this(arrivalTimes, serviceTime, id, 0, 0, 0);
    }


    @Override
    public String toString() {
        String var10000 = String.format("%.3f", this.arrivalTimes);
        return var10000 + " customer " + this.id;
    }

    public double getWaitingTime() {
        return this.waitingTime;
    }

    public int getQueueNumb() {
        return this.queueNumb;
    }

    public int getID() {
        return this.id;
    }


    public double getArrival() {
        return this.arrivalTimes;
    }


    public int getCode() {
        return 0;
    }

    public double getService() {
        return this.serviceTime.get();
    }

    public double getTime2() {
        return time;
    }


}
