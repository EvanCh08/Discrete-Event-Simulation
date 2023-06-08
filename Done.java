import java.util.function.Supplier;

class Done implements Event {
    private final Customer customer;
    private final int serveID;
    private final double serviceT;
    private final int code;
    private final int  serveOrSelf;
    private final int countNumb;


    public Done(Customer customer, int serveID, double serviceT, int code,
                int serveOrSelf, int countNumb) {
        this.customer = customer;
        this.serveID = serveID;
        this.serviceT = serviceT;
        this.code = code;
        this.serveOrSelf = serveOrSelf;
        this.countNumb = countNumb;
    }


    public int plusCount() {
        return 0;
    }

    @Override
    public String toString() {
        if (serveOrSelf == 0) {
            String var10000 = String.format("%.3f", this.getTime());
            return var10000 + " " + this.customer.getID() +
                    " done serving by " + this.serveID + "\n";
        }  else {
            String var10000 = String.format("%.3f", this.getTime());
            return var10000 + " " + this.customer.getID() +
                    " done serving by self-check " + this.serveID + "\n";
        }



    }

    @Override
    public double getTime() {
        return this.customer.getArrival() + this.serviceT;
    }

    @Override
    public double getInitialArrivalTime() {
        return 0;
    }


    @Override
    public Customer getCustomer() {
        return this.customer;
    }

    @Override
    public int getServeID() {
        return this.serveID;
    }

    @Override
    public int getCode() {
        return code;
    }

    private ImList<Server> assigns(Customer customer, ImList<Server> servers,
                                   int numb, Supplier<Double> rest) {
        servers = servers.set(numb,
                new Server(true, numb + 1, customer.getID(),
                        servers.get(numb).getLengthQueue(), rest));
        return servers;
    }

    @Override
    public ImList<Customer> getNewCustomers(ImList<Customer> customers, int numb,
                                            double arriveT,
                                            double serviceT,
                                            Supplier<Double> serviceT2) {
        return customers;
    }

    @Override
    public double plusWaitTime() {
        return 0;
    }

    @Override
    public int queueLength() {
        return 0;
    }

    private ImList<Server> updateServers(ImList<Server> servers, Customer c, int numb,
                                         Supplier<Double> rest) {

        for (int i = 0; i < servers.size(); i++) {
            if (i == numb) {
                servers = servers.set(i, new Server(false, i + 1, c.getID(),
                        servers.get(i).getLengthQueue(),
                        servers.get(i).getServiceUntil(),
                        servers.get(i).getListCustomers().remove(0), rest, 0));
            }
        }

        return servers;
    }

    private ImList<Server> updateServers2(ImList<Server> servers, Customer c, int numb,
                                          Supplier<Double> rest, double restTime) {
        for (int i  = 0; i < servers.size(); i++) {
            if (i == numb) {
                if (restTime == 0.0) {
                    servers = servers.set(i, new Server(false, i + 1, c.getID(),
                            servers.get(i).getLengthQueue(),
                            servers.get(i).getServiceUntil(),
                            servers.get(i).getListCustomers(), rest,
                            servers.get(i).getServiceUntil() + restTime));
                } else {
                    servers = servers.set(i, new Server(true, i + 1, c.getID(),
                            servers.get(i).getLengthQueue(),
                            servers.get(i).getServiceUntil(),
                            servers.get(i).getListCustomers(), rest,
                            servers.get(i).getServiceUntil() + restTime));
                }
            }

        }
        return servers;
    }

    private ImList<Counters> updateServers3(ImList<Counters> counters, Customer c, int numb,
                                          Supplier<Double> rest, double restTime) {
        for (int i = 0; i < counters.size(); i++) {
            if (i == numb) {
                counters = counters.set(i, new Counters(false,
                        counters.get(i).getID(), c.getID(),
                        counters.get(i).getLengthQueue(),
                        counters.get(i).getServiceUntil() + restTime,
                        counters.get(i).getListCustomers(), rest, restTime, 0));


            }

        }

        return counters;
    }





    @Override
    public boolean canAdd() {
        return false;
    }

    @Override
    public boolean canUpdate() {
        return true;
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


        double restTime = 0.0;
        if (serveOrSelf == 0) {
            restTime = servers.get(serveID - 1).getRestTime();
            return new Pair<>(new Pair<>(this,
                    updateServers2(servers,
                            this.customer, serveID - 1, rest, restTime)), counters);
        } else {
            return new Pair<>(new Pair<>(this,
                    servers), updateServers3(counters,
                    this.customer, this.countNumb, rest, restTime));

        }



    }

}
