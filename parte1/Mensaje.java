package parte1;

import java.io.Serializable;

public class Mensaje implements Serializable{
	String msg;
	
	public Mensaje(String msg) {
		this.msg = msg;
	}
	
	public String toString() {
		return this.msg;
	}
}
