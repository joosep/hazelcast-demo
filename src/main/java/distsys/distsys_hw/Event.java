package distsys.distsys_hw;

import java.io.Serializable;

public class Event implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private String name;
	private long time;
	private long pseudonym;

	public Event(long id, String name, long time, long pseudonym ) {
		this.setId(id);
		this.setName(name);
		this.setTime(time);
		this.setPseudonym(pseudonym);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPseudonym() {
		return pseudonym;
	}

	public void setPseudonym(long pseudonym) {
		this.pseudonym = pseudonym;
	}
}
