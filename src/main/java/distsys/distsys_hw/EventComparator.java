package distsys.distsys_hw;

import java.util.Comparator;

public class EventComparator implements Comparator<Event> {

	public int compare(Event e1, Event e2) {
		return Long.compare(e1.getTime(), e2.getTime());
	}

}
