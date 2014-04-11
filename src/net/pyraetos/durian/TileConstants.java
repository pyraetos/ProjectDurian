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
	public final static Image GRASS_IMAGE = Images.retrieve("grass.png");
	public final static Image WATER_IMAGE = Images.retrieve("water.png");
	public final static Image TREE_IMAGE = Images.retrieve("tree.png");
	public final static Image SAND_IMAGE = Images.retrieve("sand.png");
	public final static Image NULL_IMAGE = Images.retrieve("null.png");
	
	public static Image imageFor(byte type){
		switch(type){
		case GRASS: return GRASS_IMAGE;
		case SAND: return SAND_IMAGE;
		case WATER: return WATER_IMAGE;
		case TREE: return TREE_IMAGE;
		case NULL: return NULL_IMAGE;
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
