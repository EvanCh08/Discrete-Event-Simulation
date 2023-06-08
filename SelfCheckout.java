import java.util.function.Supplier;

class SelfCheckout extends Serve {

    private final int countNumb;

    public SelfCheckout(Customer customer, int serveID, int code,
                        ImList<Server> servers,
                        double initialArrivalTime, double time, int countNumb) {
        super(customer, serveID, code, servers, initialArrivalTime, time);
        this.countNumb = countNumb;
    }

    @Override
    public String toString() {
        String var10000 = String.format("%.3f", super.getCustomer().getArrival());
        return var10000 + " " + super.getCustomer().getID() + " serves by self-check " + 
            this.getServeID() + "\n";
    }


    private ImList<Counters> assigns2(Customer customer, ImList<Counters> counters,
                                   int numb, int numb1, double time, 
                                   Supplier<Double> rest) {
        if (numb1 == 1) {
            counters = counters.set(numb, new Counters(true, this.getServeID(),
                    customer.getID(),
                    counters.get(numb).getLengthQueue(),
                    time, counters.get(numb).getListCustomers(),
                    rest, 0, 0));

            counters = counters.set(0, new Counters(counters.get(0).hasOccupied(), 
                        counters.get(0).getID(),
                    counters.get(0).getCustID(),
                    counters.get(0).getLengthQueue() + 1,
                    counters.get(0).getServiceUntil(),
                    counters.get(0).getListCustomers(), rest, 0, 0));
        } else {
            counters = counters.set(numb, new Counters(true, this.getServeID(),
                    customer.getID(),
                    counters.get(numb).getLengthQueue(),
                    time, counters.get(numb).getListCustomers(), rest, 0, 0));
        }

        return counters;
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
        double time = this.getCustomer().getService();
        int self = this.countNumb;
        return new Pair<>(new Pair<>(new Done(this.getCustomer(),
                this.getServeID(), time, this.getCode(), 1, this.countNumb),
                servers), assigns2(this.getCustomer(), counters,
                this.countNumb, this.getCode(),
                this.getCustomer().getArrival() + time, rest));

    }


}
