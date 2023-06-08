import java.util.function.Supplier;

class WaitCheck extends Wait {
    
    private final int firstWait;
    private final int countNumb;

    public WaitCheck(Customer customer, int serveId,  double waitUntil,
                int serveOrNot, int isToString, int countNumb, int firstWait) {
        super(customer, serveId, waitUntil, serveOrNot, isToString, countNumb);
        this.firstWait = firstWait;
        this.countNumb = countNumb;
    }


    @Override
    public String toString() {
        if (this.getIsToString() == 1) {
            return String.format("%.3f", this.getCustomer().getArrival()) +
                    " " + this.getCustomer().getID() +
                    " waits at self-check " + super.getServeID() + "\n";
        }

        return "";
    }

    private int minimumService(ImList<Counters> counters) {
        double minimumService = counters.get(0).getServiceUntil();
        int numbSelf = 0;

        for (int i = 1; i < counters.size(); i++) {
            if (counters.get(i).getServiceUntil() < minimumService) {
                minimumService = counters.get(i).getServiceUntil();
            }
        }

        for (int i = 0; i < counters.size(); i++) {
            if (minimumService == counters.get(i).getServiceUntil()) {
                numbSelf = i;
                break;
            }
        }

        return numbSelf;
    }

    private final int findCustomer(ImList<Counters> counters) {
        int numb = -1;
        for (int i = 0; i < counters.size(); i++) {
            if (counters.get(i).getQueue() == 1) {
                numb = i;
                break;
            }
        }

        return numb;
    }

    @Override
    public int getServeID() {
        return this.firstWait;
    }

    private ImList<Counters> changeCounters(ImList<Counters> counters, int numb, 
            Supplier<Double> restTime) {

        counters = counters.set(numb, new Counters(counters.get(numb).isOccupied(),
                counters.get(numb).getID(),
                counters.get(numb).getCustID(), counters.get(numb).getLengthQueue(),
                counters.get(numb).getServiceUntil(), counters.get(numb).getListCustomers(),
                restTime, counters.get(numb).getRestTime2(), 1));

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
                                                                           Supplier<Double> rest,
                                                                           ImList<Customer> 
                                                                           customers) {


        int custID = this.getCustomer().getID();
        int queue = this.getCustomer().getQueueNumb();
        if (numb2.get(2) >= 0) { // first in queue
            if (firstWait < 0) { // lomb dapet server
                int numbSelf = minimumService(counters);
                return new Pair<>(new Pair<>(new WaitCheck(
                                new Customer(this.getCustomer().getArrival(),
                        s,
                        super.getCustomer().getID(),
                        0, counters.get(numbSelf).getServiceUntil(), 1), 
                                counters.get(0).getID(),
                        counters.get(numbSelf).getServiceUntil(), 0,
                        0, 0, numbSelf), 
                            servers), changeCounters(counters, numbSelf, rest));
            } else {
                if (!counters.get(this.firstWait).hasOccupied()) {
                    return new Pair<>(new Pair<>(new SelfCheckout(
                            new Customer(counters.get(firstWait).getServiceUntil(),
                                    s,
                                    super.getCustomer().getID(),
                                    0, 0, 0), counters.get(firstWait).getID(), 1, servers,
                            super.getCustomer().getArrival(), 0,
                            firstWait), servers), counters);
                }
            }
        } else {
            if (this.getCustomer().getQueueNumb() > 0) {
                if (firstWait >= 0) {
                    if (!counters.get(this.firstWait).hasOccupied()) {
                        return new Pair<>(new Pair<>(new SelfCheckout(
                                new Customer(counters.get(firstWait).getServiceUntil(),
                                        s,
                                        super.getCustomer().getID(),
                                        0, 0, 0), counters.get(firstWait).getID(), 1, 
                                servers,
                                super.getCustomer().getArrival(), 0,
                                firstWait), servers), counters);
                    }
                } else {
                    int numbSelf = minimumService(counters);
                    return new Pair<>(new Pair<>(new WaitCheck(
                                new Customer(this.getCustomer().getArrival(),
                            s,
                            super.getCustomer().getID(),
                            0, counters.get(numbSelf).getServiceUntil(), 1), 
                                    counters.get(0).getID(),
                            counters.get(numbSelf).getServiceUntil(), 0,
                            0, 0, numbSelf), 
                                servers), changeCounters(counters, numbSelf, 
                                    rest));
                }
            } else {
                return new Pair<>(new Pair<>(new WaitCheck(this.getCustomer(), 
                                counters.get(0).getID(),
                        counters.get(findCustomer(counters)).getServiceUntil(), 0,
                        0, 0, -1), 
                            servers), counters);
            }
        }

        return new Pair<>(new Pair<>(this, servers), counters);
    }
}
