package net.pyraetos.durian;

import java.util.LinkedList;
import java.util.Random;

import net.pyraetos.durian.entity.Bandit;
import net.pyraetos.durian.entity.Entity;
import net.pyraetos.util.Sys;

public abstract class Tileset extends TileConstants{

	private static LinkedList<LinkedList<Double>> tr = new LinkedList<LinkedList<Double>>();
	private static long seed = Sys.randomSeed();
	private static int offsetX;
	private static int offsetY;
	private static double s = 1.0d;
	
	public static void setSeed(long seed){
		Tileset.seed = seed;
	}
	
	public static long getSeed(){
		return seed;
	}

	public static void setEntropy(double s){
		Tileset.s = s;
	}

	/**
	 * Generates a single tile using the Pyraetos algorithm.
	 * @author Pyraetos
	 */
	public static void pgenerate(int x, int y){
		double value = 0;
		for(int i = x - 1; i <= x + 1; i++)
			for(int j = y - 1; j <= y + 1; j++)
				value += pnoise(i, j);
		tileSet(x, y, value / 9d);
		if(Sys.chance(.0005d)) Entity.addEntity(new Bandit(x, y));
	}
	
	private static double pnoise(int x, int y){
		Random random = new Random();
		double value = 2d;
		for(int i = x - 4; i <= x + 4; i++){
			for(int j = y - 4; j <= y + 4; j++){
				random.setSeed(seed * 17717171L + i * 22222223L + j * 111181111L);
				double h = (x == i && y == j) ? 1 : Math.sqrt(Math.pow(x - i, 2) + Math.pow(y - j, 2));
				value += (random.nextGaussian() * s) / (3d * h);
			}
		}
		return value;
	}
	
	public static byte getTile(int x, int y){
		byte b = (byte)Math.floor(tileGet(x, y));
		if(b == NULL) return NULL;
		if(b >= TREE) return TREE;
		if(b <= WATER) return WATER;
		return b;
	}

	public static double tileGet(int x, int y){
		try{
			return tr.get(x + offsetX).get(y + offsetY);
		}catch(Exception e){
			return NULL;
		}
	}

	public static void setTile(int x, int y, byte type){
		tileSet(x, y, (double)type);
	}
	
	private static void tileSet(int x, int y, double d){
		int dox = -x > offsetX ? -x - offsetX : 0;
		int doy = -y > offsetY ? -y - offsetY : 0;
		int xx = x + (offsetX += dox);
		int yy = y + (offsetY += doy);
		for(int i = 0; i < dox; i++){
			tr.addFirst(new LinkedList<Double>());
		}
		for(int i = 0; i < tr.size(); i++){
			for(int j = 0; j < doy; j++){
				tr.get(i).addFirst(null);
			}
		}
		while(xx >= tr.size()){
			tr.addLast(new LinkedList<Double>());
		}
		for(int i = 0; i < tr.size(); i++){
			while(yy >= tr.get(i).size()){
				tr.get(i).addLast(null);
			}
		}
		tr.get(xx).set(yy, d);
	}
	
	public static void setAdjacentTile(int x, int y, byte direction, byte type){
		switch(direction){
		case Sys.NORTH: setTile(x, y - 1, type); break;
		case Sys.WEST: setTile(x - 1, y, type); break;
		case Sys.SOUTH: setTile(x, y + 1, type); break;
		case Sys.EAST: setTile(x + 1, y, type); break;
		}
	}
	
	public static void setAdjacentTile(Entity entity, byte direction, byte type){
		setAdjacentTile((int)entity.getX(), (int)entity.getY(), direction, type);
	}
	
	public static byte getAdjacentTile(int x, int y, byte direction){
		switch(direction){
		case Sys.NORTH: return getTile(x, y - 1);
		case Sys.WEST: return getTile(x - 1, y);
		case Sys.SOUTH: return getTile(x, y + 1);
		case Sys.EAST: return getTile(x + 1, y);
		}
		return 0;
	}
	
	public static byte getAdjacentTile(Entity entity, byte direction){
		return getAdjacentTile((int)entity.getX(), (int)entity.getY(), direction);
	}
}	
