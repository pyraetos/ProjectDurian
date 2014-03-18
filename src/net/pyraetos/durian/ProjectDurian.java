package net.pyraetos.durian;

import net.pyraetos.durian.server.Server;

public abstract class ProjectDurian{

	public static void main(String[] args){
		if(args.length == 0 || args[0].equalsIgnoreCase("client"))
			new DurianFrame();
		else
		if(args[0].equalsIgnoreCase("server"))
			Server.start();
	}

}
