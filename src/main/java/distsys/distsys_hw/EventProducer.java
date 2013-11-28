package distsys.distsys_hw;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.IdGenerator;

public class EventProducer {
	HazelcastInstance client;
	IdGenerator idGenerator;

	public void processEvents(Iterable<Event> events) {

	}

	public EventProducer(String clusterIP) {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.addAddress(clusterIP);
		client = HazelcastClient.newHazelcastClient(clientConfig);

		idGenerator = client.getIdGenerator("events");
	}

	public static void main(String[] args) {
		String clusterIP =args[0];// "192.168.1.11:5701";
		EventProducer consumer = new EventProducer(clusterIP);
		while (true) {
			consumer.addEvents();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void addEvents() {
		IMap<Long, Event> map = client.getMap("events");
		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			long id = idGenerator.newId();
			Event e = new Event(id, "name" + id, System.currentTimeMillis());
			map.put(id++, e);
		}
	}
}
