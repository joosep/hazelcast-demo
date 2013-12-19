package distsys.distsys_hw;

import com.hazelcast.config.Config;
import com.hazelcast.config.ListenerConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import junit.framework.TestCase;

/*
 * 
 * Make calculation request for data and middle of the request destroy one node. Will calculations in that node go missing?
 * How fast can service take in data from EventProducer and how does it scale when there are more nodes.
 * How fast can EventConsumer make request from service and how does it scale when there are more nodes. How does it scale when there are more
 * data in the services. Does the system use caching?
 * How can service serve EventConsumer when EventProducer keep sending new data?
 */
public class InstanceTest extends TestCase {
	public static void main(String[] args) {
		runInstanceWithProducers();
	}

	private static void runInstanceWithProducers() {
		
		Config cfg = new Config();
		cfg.setProperty("hazelcast.logging.type", "none");
		HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
		instance.getMap("events").addIndex("time", true);
//		String address = instance.getCluster().getLocalMember()
//				.getInetSocketAddress().getAddress().getHostAddress()
//				+ ":"
//				+ instance.getCluster().getLocalMember().getInetSocketAddress()
//						.getPort();
		String address = "localhost:5701";
		int maxProducers = 30;
		for (int i = 0; i < maxProducers; i++)
			startProducer(address);
		
	}

	private static long startProducer(String address) {
		Thread t = null;
		try {
			EventProducer producer = new EventProducer(address);
			producer.sleepTime=0;
			t = new Thread(producer);
			t.start();
		} catch (Exception err) {
			err.printStackTrace();
		}
		return t.getId();
	}

	private static long startConsumer(String address) {
		Thread t = null;
		try {
			EventConsumer producer = new EventConsumer(address);
			t = new Thread(producer);
			t.start();
		} catch (Exception err) {
			err.printStackTrace();
		}
		return t.getId();

	}
}
