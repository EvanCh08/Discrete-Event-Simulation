import java.util.function.Supplier;

class Leave implements Event {

    private final Customer customer;
    private final int serveID;

    public Leave(Customer customer, int serveID) {
        this.customer = customer;
        this.serveID  = serveID;
    }



    public Leave(Customer customer) {
        this(customer, 0);
    }

    @Override
    public String toString() {
        return String.format("%.3f", customer.getArrival()) +
                " " + customer.getID() + " leaves" + "\n";
    }

    @Override
    public double getTime() {
        return customer.getArrival();
    }

    @Override
    public boolean canAdd() {
        return false;
    }

    @Override
    public boolean canUpdate() {
        return false;
    }


    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public double getInitialArrivalTime() {
        return 0;
    }

    public int getServeID() {
        return serveID;
    }

    public int getCode() {
        return 0;
    }

    @Override
    public int queueLength() {
        return 0;
    }

    @Override
    public double plusWaitTime() {
        return 0;
    }

    public int plusCount() {
        return 0;
    }

    @Override
    public ImList<Customer> getNewCustomers(ImList<Customer> customers, int numb,
                                            double arriveT,
                                            double serviceT,
                                            Supplier<Double> 
                                            serviceT2) {
        return customers;
    }

    @Override
    public int plusCountA() {
        return 0;
    }

    @Override
    public int plusCountB() {
        return 1;
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
                                                                           Supplier<Double> 
                                                                           rest,
                                                                           ImList<Customer> 
                                                                           customers) {
        return new Pair<>(new Pair<>(this, servers), counters);
    }

}
