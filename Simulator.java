import java.util.function.Supplier;

class Simulator {

    private final int numOfServers;
    private final int numOfSelfChecks;
    private final int qmax;
    private final ImList<Double> arrivalTimes;
    private final Supplier<Double> serviceTimes;
    private final Supplier<Double> restTimes;


    public Simulator(int numOfServers, int numOfSelfChecks, int qmax,
                     ImList<Double> arrivalTimes,
                     Supplier<Double> serviceTimes, 
                     Supplier<Double> restTimes) {
        this.numOfServers = numOfServers;
        this.numOfSelfChecks = numOfSelfChecks;
        this.qmax = qmax;
        this.arrivalTimes = arrivalTimes;
        this.serviceTimes = serviceTimes;
        this.restTimes = restTimes;
    }


    public String simulate() {
        ImList<Event> waitCustomers = new ImList<Event>();
        ImList<Server> servers = createServer();
        PQ<Event> pq = createPQ();
        ImList<Customer> customers = createCustomer();
        int count = 0;
        String sentence = "";
        Pair<Event, PQ<Event>> pr = pq.poll();  //creating pair for first interation
        Event action = pr.first(); //creating an action for first iteration
        ImList<Integer> numb = new ImList<Integer>(); // for keep track loop
        int countA = 0; //added if there's serve
        int countB = 0; //added if there's leave
        Pair<Pair<Event, ImList<Server>>, ImList<Counters>> p2; // to be returned byy events
        boolean can = false;
        ImList<Integer> trueServers = new ImList<Integer>();
        double totalWaitTime = 0.0;
        int finalQueueLength = 0;
        boolean canUpdate = false;
        int numbWaitCustomer = 0;
        ImList<Counters> counters = createSelfChecks();
        int countNumb = 0;

        while (!pq.isEmpty()) {
            if (count < arrivalTimes.size()) {
                //assume at the front there's no done
                pr = pq.poll();
                pq = pr.second();
                action = pr.first();

                servers = changeServerState(servers, action.getTime(), restTimes);
                count = action.plusCount();


                countA = countA += action.plusCountA();
                countB = countB += action.plusCountB();

                totalWaitTime += action.plusWaitTime();
                finalQueueLength += action.queueLength();


                can = action.canAdd();
                numb = whichServer(servers, counters); // whichServer is available?
                sentence += action.toString();

                canUpdate = action.canUpdate();

                trueServers = trueServer(servers, this.qmax, counters, action);

                p2 = action.getNewEvent(numb, servers,
                        trueServers, counters,
                        this.serviceTimes, restTimes, customers);
                action = p2.first().first();
                servers = p2.first().second();
                counters = p2.second();

                if (can) { // can add or not
                    pq = pq.add(action);
                }

            }
        }
        if (finalQueueLength == 0.0) {
            sentence += "[" +
                    String.format("%.3f", 0.0) + " " + countA + " " + countB + "]";
        } else {
            totalWaitTime = totalWaitTime / finalQueueLength;
            sentence += "[" +
                    String.format("%.3f", totalWaitTime) + " " + countA + " " + countB + "]";
        }

        return sentence;

    }


    private int whichWaitingCustomer(ImList<Event> waitingCustomers, int numb) {

        int count = -1;

        for (int i = 0; i < waitingCustomers.size(); i++) {
            if (waitingCustomers.get(i).getServeID() == numb) {
                count = i;
                break;
            }
        }

        return count;
    }

    private ImList<Customer> findCustomers(ImList<Customer> customers, int numb, 
            Supplier<Double> s) {
        customers = customers.set(numb, new Customer(customers.get(numb).getArrival(), s,
                customers.get(numb).getID(), 0, customers.get(numb).getWaitingTime(), 1));
        int custID = customers.get(numb).getID();
        int queueNumb = customers.get(numb).getQueueNumb();
        return customers;
    }

    private ImList<Counters> createSelfChecks() {
        int id = numOfServers + 1;
        int num = numOfSelfChecks;
        ImList<Counters> s = new ImList<Counters>();
        while (num > 0) {
            s = s.add(new Counters(false, id, 0, this.qmax, restTimes));
            num = num - 1;
            id++;
        }

        return s;
    }

    private ImList<Server> changeServerState(ImList<Server> servers,
                                             double arrivalTime,
                                             Supplier<Double> rest) {
        for (Server s: servers) {
            if (s.hasOccupied() == true) {
                if (s.getRestTime2() <= arrivalTime) {
                    servers = servers.set(s.getID() - 1,
                            new Server(false, s.getCustID(), s.getID(),
                                    s.getLengthQueue(),
                                    s.getRestTime2(), s.getListCustomers(),
                                    rest, s.getRestTime2()));
                }
            }
        }

        return servers;

    }


    //the real one




    private ImList<Server> createServer() {
        int id = 1;
        int num = numOfServers;
        ImList<Server> s = new ImList<Server>();
        while (num > 0) {
            s = s.add(new Server(false, id, 0, this.qmax, restTimes));
            num = num - 1;
            id++;
        }

        return s;
    }

    private ImList<Integer> trueServer(ImList<Server> servers, int lengthQueue,
                                       ImList<Counters> counters, Event e) {
        ImList<Server> newServers = new ImList<Server>();
        ImList<Counters> newCounters = new ImList<Counters>();
        int numb = -1;
        int numb2 = -1;
        int custID = -1;
        int firstWait = -1;
        ImList<Integer> integers = new ImList<Integer>();

        for (int i = 0; i < servers.size(); i++) {
            if (servers.get(i).hasOccupied()) {
                newServers = newServers.add(servers.get(i));
            }
        }

        for (int j = 0; j < newServers.size(); j++) {
            if (newServers.get(j).getLengthQueue() <= lengthQueue &&
                    newServers.get(j).getLengthQueue() > 0) {
                numb = j;
                break;
            }
        }

        if (counters.size() > 0) {
            if (counters.get(0).getLengthQueue() <= lengthQueue &&
                    counters.get(0).getLengthQueue() > 0) {
                if (serverOne(counters) < 0) {
                    numb2 = 0;
                    firstWait = e.getCustomer().getID() - 1; // indicating first queue or not
                } else {
                    numb2 = 0;
                }
            }
        }

        return integers.add(numb).add(numb2).add(firstWait);
    }

    private int serverOne(ImList<Counters> counters) {
        int numb = -1;
        for (Counters counter: counters) {
            if (counter.getQueue() == 1) {
                numb = 0;
                break;
            }
        }

        return numb;
    }


    private ImList<Event> createAction() {
        ImList<Event> newActions = new ImList<Event>();
        for (int i = 0; i < createCustomer().size(); i++) {
            newActions = newActions.add(new Arrive(createCustomer().get(i), this.qmax));
        }

        return newActions;
    }


    private ImList<Integer> whichServer(ImList<Server> s, ImList<Counters> counters) {
        ImList<Integer> integers = new ImList<Integer>();
        int index = -1;
        int indexLol = -1;
        int index2 = -1;

        if (s.size() != 0 && counters.size() != 0) {
            for (int i = 0; i < s.size(); i++) {
                if (s.get(i).hasOccupied() == false) {
                    if (s.get(i).getRestTime2() == s.get(i).getServiceUntil()) {
                        index = i;
                        break;
                    }
                }
            }

            for (int i = 0; i < counters.size(); i++) {
                if (counters.get(i).hasOccupied() == false) {
                    if (counters.get(i).getRestTime2() == 0.0) {
                        indexLol = i;
                        break;
                    }
                }
            }

            return integers.add(index).add(indexLol);

        } else if (s.size() == 0 && counters.size() != 0) {
            for (int i = 0; i < counters.size(); i++) {
                if (counters.get(i).hasOccupied() == false) {
                    indexLol = i;
                    break;
                }
            }

            return integers.add(-1).add(indexLol);
        } else if (s.size() != 0 && counters.size() == 0) {
            for (int i = 0; i < s.size(); i++) {
                if (s.get(i).hasOccupied() == false) {
                    if (s.get(i).getRestTime2() == s.get(i).getServiceUntil()) {
                        index = i;
                        break;
                    }
                }
            }

            return integers.add(index).add(-1);
        } else {
            return integers.add(-1).add(-1);
        }

    }

    private ImList<Customer> createCustomer() {
        int counter = 0;
        ImList<Customer> c = new ImList<Customer>();
        while (counter < arrivalTimes.size()) {
            c = c.add(new Customer(arrivalTimes.get(counter),
                    this.serviceTimes,
                    counter + 1));
            counter++;
        }

        return c;

    }

    private PQ<Event> createPQ() {
        PQ<Event> pq = new PQ<Event>(new TimeComp());
        ImList<Customer> customers = createCustomer();
        for (int i = 0; i < customers.size(); i++) {
            pq = pq.add(new Arrive(customers.get(i), this.qmax));
        }

        return pq;
    }
}
