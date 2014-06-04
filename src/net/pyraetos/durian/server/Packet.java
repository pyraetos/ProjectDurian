package net.pyraetos.durian.server;

import java.io.Serializable;

public interface Packet extends Serializable{

	void process();
	
}
