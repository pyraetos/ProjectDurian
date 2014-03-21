package net.pyraetos.durian;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import net.pyraetos.durian.entity.Entity;
import net.pyraetos.util.Point;
import net.pyraetos.util.Sys;

public abstract class Tileset extends TileConstants{

	private static LinkedList<LinkedList<TileRegion>> tr = new LinkedList<LinkedList<TileRegion>>();
	private static long seed = Sys.randomSeed();
	private static int offsetX;
	private static int offsetY;
	private static float s = 1.0f;
	
	public static void setSeed(long seed){
		Tileset.seed = seed;
	}
	
	public static long getSeed(){
		return seed;
	}

	public static void setEntropy(float s){
		Tileset.s = s;
	}

	public static Set<Point> nodes = new HashSet<Point>();

	public static void generate(int rx, int ry){
		int tx = rx * 8;
		int ty = ry * 8;
		nodes.add(new Point(tx, ty));
		initGen(tx, ty);
		for(int side = 8; side >= 2; side /= 2){
			int res = 8 / side;
			float scale = (float)(s / Math.pow(res, 3/s));
			for(int i = 0; i < res; i++){
				for(int j = 0; j < res; j++){
					int a = (int)(tx + side * ((1d + 2d * i) / 2d));
					int b = (int)(ty + side * ((1d + 2d * j) / 2d));
					diamondStep(a, b, side, scale);
				}
			}
			for(int i = 0; i < res; i++){
				for(int j = 0; j < res; j++){
					int a = (int)(tx + side * ((1d + 2d * i) / 2d));
					int b = (int)(ty + side * ((1d + 2d * j) / 2d));
					squareStep(a, b, side, scale);
				}
			}
		}
	}

	private static void initGen(int tx, int ty){
		tileSet(tx, ty, gen(tx, ty));
		tileSet(tx + 8, ty, gen(tx + 8, ty));
		tileSet(tx + 8, ty + 8, gen(tx + 8, ty + 8));
		tileSet(tx, ty + 8, gen(tx, ty + 8));
	}

	private static void diamondStep(int tx, int ty, int side, float scale){
		if(hasTile(tx, ty)) return;
		float a = tileGet(tx + side / 2, ty - side / 2);
		float b = tileGet(tx + side / 2, ty + side / 2);
		float c = tileGet(tx - side / 2, ty - side / 2);
		float d = tileGet(tx - side / 2, ty + side / 2);
		float average = (a + b + c + d) / 4;
		float value = average + scale * coeff(tx, ty);
		tileSet(tx, ty, value);
	}

	private static void squareStep(int tx, int ty, int side, float scale){
		int half = side / 2;
		if(!hasTile(tx + half, ty))
			tileSet(tx + half, ty, squareCalculation(tx + half, ty, half, scale));
		if(!hasTile(tx, ty + half))
			tileSet(tx, ty + half, squareCalculation(tx, ty + half, half, scale));
		if(!hasTile(tx - half, ty))
			tileSet(tx - half, ty, squareCalculation(tx - half, ty, half, scale));
		if(!hasTile(tx, ty - half))
			tileSet(tx, ty - half, squareCalculation(tx, ty - half, half, scale));
	}

	private static float squareCalculation(int tx, int ty, int half, float scale){
		float average = 0;
		float num = 0;
		float val = tileGet(tx - half, ty);
		if(val != NULL){
			average += val;
			num++;
		}
		val = tileGet(tx, ty - half);
		if(val != NULL){
			average += val;
			num++;
		}
		val = tileGet(tx + half, ty);
		if(val != NULL){
			average += val;
			num++;
		}
		val = tileGet(tx, ty + half);
		if(val != NULL){
			average += val;
			num++;
		}
		average /= num;
		return average + scale * coeff(tx, ty);
	}

	private static float coeff(int tx, int ty){
		Random random = new Random(seed * 17717171L + tx * 22222223L + ty * 111181111L);
		return -1  + 2 * random.nextFloat();
	}
	
	private static float gen(int tx, int ty){
		Random random = new Random(seed * 17717171L + tx * 22222223L + ty * 111181111L);
		return 4 * random.nextFloat();
	}
	
	public static TileRegion getRegion(int rx, int ry){
		try{
			TileRegion r = tr.get(rx + offsetX).get(ry + offsetY);
			if(r != null)
				return r;
		}catch(Exception e){}
		setRegion(rx, ry, new TileRegion());
		return tr.get(rx + offsetX).get(ry + offsetY);
	}
	
	public static void setRegion(int rx, int ry, TileRegion r){
		int dox = -rx > offsetX ? -rx - offsetX : 0;
		int doy = -ry > offsetY ? -ry - offsetY : 0;
		int xx = rx + (offsetX += dox);
		int yy = ry + (offsetY += doy);
		for(int i = 0; i < dox; i++){
			tr.addFirst(new LinkedList<TileRegion>());
		}
		for(int i = 0; i < tr.size(); i++){
			for(int j = 0; j < doy; j++){
				tr.get(i).addFirst(new TileRegion());
			}
		}
		while(xx >= tr.size()){
			tr.addLast(new LinkedList<TileRegion>());
		}
		for(int i = 0; i < tr.size(); i++){
			while(yy >= tr.get(i).size()){
				tr.get(i).addLast(new TileRegion());
			}
		}
		tr.get(xx).set(yy, r);
	}
	
	public static byte getTile(int tx, int ty){
		byte b = (byte)Math.round(tileGet(tx, ty));
		if(b >= TREE) return TREE;
		if(b <= WATER && b != NULL) return WATER;
		return b;
	}

	public static float tileGet(int tx, int ty){
		TileRegion r = getRegion(toRegionCoordinate(tx), toRegionCoordinate(ty));
		return (float)r.get(Math.abs(tx) % 8, Math.abs(ty) % 8);
	}

	public static void setTile(int tx, int ty, byte b){
		tileSet(tx, ty, (float)b);
	}
	
	private static void tileSet(int tx, int ty, float f){
		TileRegion r = getRegion(toRegionCoordinate(tx), toRegionCoordinate(ty));
		r.set(Math.abs(tx) % 8, Math.abs(ty) % 8, f);
	}
	
	public static boolean hasTile(int tx, int ty){
		return getTile(tx, ty) != NULL;
	}
	
	public static byte getAdjacentTile(int tx, int ty, byte direction){
		switch(direction){
		case Sys.NORTH: return getTile(tx, ty - 1);
		case Sys.WEST: return getTile(tx - 1, ty);
		case Sys.SOUTH: return getTile(tx, ty + 1);
		case Sys.EAST: return getTile(tx + 1, ty);
		}
		return 0;
	}
	
	public static byte getAdjacentTile(Entity entity, byte direction){
		return getAdjacentTile((int)entity.getX(), (int)entity.getY(), direction);
	}
	
	public static LinkedList<LinkedList<TileRegion>> getAllRegions(){
		return tr;
	}
	
	public static void setTileset(LinkedList<LinkedList<TileRegion>> tr){
		Tileset.tr = tr;
	}
	
	public static boolean ready(){
		return !tr.isEmpty();
	}
	
	public static int toRegionCoordinate(int t){
		return t < 0 ? t / 8 - 1 : t / 8;
	}
}
