package net.pyraetos.durian;

import java.awt.Image;

import net.pyraetos.util.Images;
import net.pyraetos.util.Sys;

public abstract class TileConstants{
	
	public static final byte WATER = 0;
	public static final byte SAND = 1;
	public static final byte GRASS = 2;
	public static final byte TREE = 3;
	public static final byte NULL = -128;
	
	public static Image imageFor(byte type){
		switch(type){
		case GRASS: return Images.retrieve("grass.png");
		case SAND: return Images.retrieve("sand.png");
		case WATER: return Images.retrieve("water.png");
		case TREE: return Images.retrieve("tree.png");
		case NULL: return Images.retrieve("null.png");
		default: return null;
		}
	}
	
	public static String describe(double x, double y){
		double tile = Tileset.tileGet((int)x, (int)y);
		byte t = (byte)tile;
		String type = "NULL";
		if(t <= 0) type = "WATER"; else
		if(t == 1) type = "SAND"; else
		if(t == 2) type = "GRASS"; else
		if(t >= 3) type = "TREE";
		return Sys.round(tile) + " (" + type + ")";
	}
}
