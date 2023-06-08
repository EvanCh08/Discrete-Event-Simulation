import java.util.function.Supplier;

public interface Event {

    public String toString();

    public double getTime();


    public Customer getCustomer();

    public int getServeID();

    public Pair<Pair<Event, ImList<Server>>, ImList<Counters>> getNewEvent(ImList<Integer> numb,
                                                                           ImList<Server> servers,
                                                                           ImList<Integer> 
                                                                           numb2,
                                                                           ImList<Counters> 
                                                                           counters,
                                                                           Supplier<Double> 
                                                                           s,
                                                                           Supplier<Double> 
                                                                           rest,
                                                                           ImList<Customer> 
                                                                           customers);

    public ImList<Customer> getNewCustomers(ImList<Customer> customers,
                                            int numb, double arriveT, 
                                            double serviceT, Supplier<Double> s);

    public int getCode();

    public int queueLength();

    public double plusWaitTime();

    public boolean canAdd();

    public boolean canUpdate();

    public int plusCountA();

    public int plusCountB();

    public double getInitialArrivalTime();

    public int plusCount();
}



