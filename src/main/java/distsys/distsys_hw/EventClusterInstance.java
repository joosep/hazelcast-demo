package distsys.distsys_hw;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class EventClusterInstance {
	
	public static void main(String[] args) {
		 Config cfg = new Config();
	     HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
	}
}
