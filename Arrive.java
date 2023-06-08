import java.util.function.Supplier;

class Arrive implements Event {

    private final Customer customer;
    private final int serveID;
    private  final int qmax;

    //declare number of qmax

    public Arrive(Customer customer, int serveID, int qmax) {
        this.customer = customer;
        this.serveID  = serveID;
        this.qmax  = qmax;
    }

    public Arrive(Customer customer, int qmax) {
        this(customer, 0,  qmax);
    }

    @Override
    public String toString() {
        return String.format("%.3f", this.customer.getArrival()) +
                " " + this.customer.getID() +
                " arrives" + "\n";
    }

    @Override
    public int plusCount() {
        return 0;
    }

    @Override
    public double getTime() {
        return customer.getArrival();
    }

    @Override
    public int plusCountA() {
        return 0;
    }


    @Override
    public int plusCountB() {
        return 0;
    }

    @Override
    public boolean canAdd() {
        return true;
    }


    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public int getServeID() {
        return serveID;
    }

    private ImList<Server> changeLengthQueue(Customer customer, ImList<Server> servers,
                                             int numb, Supplier<Double> rest) {
        servers = servers.set(numb,
                new Server(true, numb + 1, customer.getID(),
                        servers.get(numb).getLengthQueue() - 1,
                        servers.get(numb).getServiceUntil(),
                        servers.get(numb).getListCustomers().add(customer), rest,
                        servers.get(numb).getRestTime2()));
        return servers;
    }

    @Override
    public int queueLength() {
        return 0;
    }

    @Override
    public double plusWaitTime() {
        return 0;
    }

    @Override
    public double getInitialArrivalTime() {
        return this.customer.getArrival();
    }

    private ImList<Counters> changeLengthQueue2(Customer customer, ImList<Counters> counters,
                                             int numb, Supplier<Double> rest, int selfId) {
        counters = counters.set(numb,
                new Counters(true, selfId, customer.getID(),
                        counters.get(numb).getLengthQueue() - 1,
                        counters.get(numb).getServiceUntil(),
                        counters.get(numb).getListCustomers().add(customer), rest, 0,
                        counters.get(numb).getQueue()));
        return counters;
    }

    @Override
    public ImList<Customer> getNewCustomers(ImList<Customer> customers, int numb,
                                            double arriveT,
                                            double serviceT,
                                            Supplier<Double> serviceT2) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getID() == customer.getID()) {
                customers = customers.set(i,
                        new Customer(arriveT,
                                serviceT2,  customer.getID(),
                                serviceT, 0, 0));
            }
        }

        return customers;
    }

    @Override
    public int getCode() {
        return 0;
    }

    //method to check if its the firstCustomer in the queue or  not.
    //return 1 if its the first, -1 otherwise.

    private int checkForFirst(Counters counter) {
        if (counter.getLengthQueue() == this.qmax) {
            return 1;
        }

        return 0;
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
        if (numb.get(0) >= 0) {
            return new Pair<>(new Pair<>(new Serve(this.customer, numb.get(0) + 1, 0, servers,
                    this.customer.getArrival(), 0), servers), // update server
                    counters);

        } else if (numb.get(1) >= 0) {
            return new Pair<>(new Pair<>(new SelfCheckout(this.customer,
                    counters.get(numb.get(1)).getID(), 0, servers,
                    this.customer.getArrival(), 0, numb.get(1)), servers), // update server
                    counters);
        } else {
            if (numb2.get(0) >= 0) {
                return new Pair<>(new Pair<>(new Wait(customer,  numb2.get(0) + 1,
                        customer.getArrival(), -1, 1, 0),
                        changeLengthQueue(this.getCustomer(),
                                servers, numb2.get(0), rest)),
                        counters);
            } else if (numb2.get(1) >= 0) {
                return new Pair<>(new Pair<>(new WaitCheck(
                                new Customer(this.customer.getArrival(), s,
                        this.customer.getID(), 0, this.customer.getWaitingTime(), 
                        checkForFirst(counters.get(0))), 
                        counters.get(0).getID(),
                        customer.getArrival(), 1, 1, numb2.get(2), -1),
                        servers),
                        changeLengthQueue2(this.getCustomer(),
                                counters, 0, rest, counters.get(0).getID()));
            }

            return new Pair<>(new Pair<>(new Leave(customer, customer.getID()),
                    servers), counters);
        }
    }

}
