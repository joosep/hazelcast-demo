package distsys.distsys_hw;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.EntryObject;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;

public class EventConsumer implements Runnable {
	HazelcastInstance client;

	public EventConsumer(String clusterIP) {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.addAddress(clusterIP);
		client = HazelcastClient.newHazelcastClient(clientConfig);
	}

	public static void main(String[] args) throws InterruptedException {
		String clusterIP = args[0];// "0.0.0.1:5701";
		EventConsumer consumer = new EventConsumer(clusterIP);
		consumer.run();
	}

	private void removeOldestEvents(int i) {
		IMap<Integer, Event> map = client.getMap("events");
		System.out.println("before: " + map.size());
		EntryObject entryObject = new PredicateBuilder().getEntryObject();
		@SuppressWarnings("unchecked")
		Predicate<Integer, Event> predicate = entryObject.get("time").lessThan(
				System.currentTimeMillis() - 30000);
		Collection<Event> events = (Collection<Event>) map.values(predicate);
		for (Event e : events) {
			map.remove(e.getId(), e);
		}
		System.out.println("after: " + map.size());

	}

	SimpleDateFormat sdf = new SimpleDateFormat("YYYY.MM.dd - HH:mm:ss:SSS");

	private void printEvent() {
		IMap<Integer, Event> map = client.getMap("events");
		Event[] events = map.values().toArray(new Event[0]);
		Arrays.sort(events, new EventComparator());
		Event e = events[0];
		System.out.println("oldest event: id=" + e.getId() + ", name="
				+ e.getName() + ", time=" + sdf.format(new Date(e.getTime())));
	}

	public int sleepTime = 1000;

	public void run() {
		while (true) {
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			requestCount();
			requestOldest();
			// printEvent();
			// removeOldestEvents(1);
		}
	}

	SimpleDateFormat time = new SimpleDateFormat("mm:ss:SSS");

	private void requestCount() {
		IMap<Integer, Event> map = client.getMap("events");
		long start = System.currentTimeMillis();
		System.out.println();
		System.out.println(map.size() + " : "
				+ time.format(new Date(System.currentTimeMillis() - start)));
	}

	private void requestOldest() {
		IMap<Integer, Event> map = client.getMap("events");
		long start = System.currentTimeMillis();
		Event[] events = map.values().toArray(new Event[0]);
		Arrays.sort(events, new EventComparator());
		Event e = events[0];
		System.out.println("oldest event: id=" + e.getId() + ", name="
				+ e.getName() + ", time=" + sdf.format(new Date(e.getTime()))
				+ " : "
				+ time.format(new Date(System.currentTimeMillis() - start)));
		System.out.println();
	}
}
