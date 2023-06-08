import java.util.function.Supplier;

class Wait implements Event {
    private final Customer customer;
    private final int serveID;
    private final double waitUntil;
    private final int serveOrNot;
    private final int isToString;
    private final int countNumb;

    public Wait(Customer customer, int serveId,  double waitUntil,
                int serveOrNot, int isToString, int countNumb) {
        this.customer = customer;
        this.serveID = serveId;
        this.waitUntil = waitUntil;
        this.serveOrNot = serveOrNot;
        this.isToString = isToString;
        this.countNumb = countNumb;
    }

    @Override
    public String toString() {
        if (isToString == 1) {
            return String.format("%.3f", this.customer.getArrival()) +
                    " " + this.customer.getID() +
                    " waits at " + this.serveID + "\n";
        } else {
            return "";
        }

    }

    @Override
    public double getTime() {
        return this.waitUntil;
    }

    @Override
    public int plusCountA() {
        return 0;
    }

    @Override
    public int plusCountB() {
        return 0;
    }

    public int plusCount() {
        return 0;
    }


    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public boolean canAdd() {
        return true;
    }

    @Override
    public int getServeID() {
        return serveID;
    }

    @Override
    public int getCode() {
        return 1;
    }

    public int getServeOrNot() {
        return serveOrNot;
    }

    @Override
    public double plusWaitTime() {
        return 0;
    }

    @Override
    public ImList<Customer> getNewCustomers(ImList<Customer> customers, int numb,
                                            double arriveT,
                                            double serviceT,
                                            Supplier<Double> serviceT2) {
        return customers;
    }

    @Override
    public int queueLength() {
        return 0;
    }

    private int searchCustomer(Server s, Customer customer) {
        int numb = 0;

        for (int i = 0; i < s.getListCustomers().size(); i++) {
            if (s.getListCustomers().get(i).getID() == customer.getID()) {
                numb = i;
                break;
            }
        }

        return numb;
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public double getInitialArrivalTime() {
        return this.waitUntil;
    }

    @Override
    public Pair<Pair<Event, ImList<Server>>, ImList<Counters>> getNewEvent(ImList<Integer> numb,
                                                                           ImList<Server>
                                                                           servers,
                                                                           ImList<Integer>
                                                                           numb2,
                                                                           ImList<Counters> 
                                                                           counters,
                                                                           Supplier<Double> s,
                                                                           Supplier<Double> rest,
                                                                           ImList<Customer> 
                                                                           customers) {
        ImList<Double> doubles = new ImList<Double>();
        double minimumService = 0.0;
        int numbSelf = 0;


        if (!servers.get(serveID - 1).hasOccupied()) {
            return new Pair<>(new Pair<>(new Serve(
                    new Customer(servers.get(serveID - 1).getRestTime2(),
                            s,
                            this.customer.getID(),
                            0, 0, 0), this.serveID, 1, servers,
                    this.customer.getArrival(), 0), servers), counters);
        }

        return new Pair<>(new Pair<>(new Wait(this.customer, this.serveID,
                servers.get(serveID - 1).getRestTime2(), 0, 0,
                0), servers), counters);

    }

    public int getIsToString() {
        return this.isToString;
    }

    public int getCountNumb()  {
        return this.countNumb;
    }

}
