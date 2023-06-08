import java.util.function.Supplier;

class Serve implements Event {
    
    private final Customer customer;
    private final int serveID;
    private final int code;
    private final ImList<Server> servers;
    private final double initialArrivalTime;
    private final double time;



    public Serve(Customer customer, int serveID, int code,
                 ImList<Server> servers,
                 double initialArrivalTime, double time) {
        this.customer = customer;
        this.serveID = serveID;
        this.code = code;
        this.servers = servers;
        this.initialArrivalTime = initialArrivalTime;
        this.time = time; // time of previous customer getArrival() + serviceTime;
    }

    public int plusCount() {
        return 1;
    }

    public String toString() {
        String var10000 = String.format("%.3f", this.customer.getArrival());
        return var10000 + " " + this.customer.getID() + " serves by " + this.serveID + "\n";
    }



    @Override
    public boolean canAdd() {
        return true;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    @Override
    public int plusCountA() {
        return 1;
    }

    @Override
    public int plusCountB() {
        return 0;
    }

    public int getServeID() {
        return this.serveID;
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public double getInitialArrivalTime() {
        return 0;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public double getTime() {
        return customer.getArrival();
    }

    public ImList<Customer> getNewCustomers(ImList<Customer> customers,
                                            int numb, double arriveT,
                                            double serviceT,
                                            Supplier<Double> 
                                            serviceT2) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getID() == customer.getID()) {
                customers = customers.set(i,
                        new Customer(arriveT, serviceT2,
                                customer.getID(), serviceT, 0, 0));
            }
        }

        return customers;
    }



    @Override
    public int queueLength() {
        return 1;
    }

    private ImList<Server> assigns(Customer customer, ImList<Server> servers,
                                   int numb, int numb1, double time, 
                                   Supplier<Double> rest) {
        if (numb1 == 1) {
            servers = servers.set(numb, new Server(true, numb + 1,
                    customer.getID(),
                    servers.get(numb).getLengthQueue() + 1,
                    time, servers.get(numb).getListCustomers(), rest, time));
        } else {
            servers = servers.set(numb, new Server(true, numb + 1,
                    customer.getID(),
                    servers.get(numb).getLengthQueue(),
                    time, servers.get(numb).getListCustomers(), rest, time));
        }

        return servers;
    }


    @Override
    public double plusWaitTime() {
        return this.customer.getArrival() - this.initialArrivalTime;
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
        double time = this.customer.getService();
        return new Pair<>(new Pair<>(new Done(this.customer,
                this.getServeID(), time, this.code, 0, 0),
                assigns(this.customer, servers,
                        this.serveID - 1, this.code,
                         this.customer.getArrival() + time, rest)), counters);

    }


}
