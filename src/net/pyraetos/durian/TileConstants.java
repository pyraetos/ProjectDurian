package net.pyraetos.durian;

import java.awt.Image;

import net.pyraetos.util.Images;

public abstract class TileConstants{
	
	public static final byte WATER = -1;
	public static final byte GRASS = 0;
	public static final byte TREE = 1;
	public static final byte NULL = -128;
	public final static Image GRASS_IMAGE = Images.retrieve("grass.png");
	public final static Image WATER_IMAGE = Images.retrieve("water.png");
	public final static Image TREE_IMAGE = Images.retrieve("tree.png");
	
	public static Image imageFor(byte type){
		switch(type){
		case GRASS: return GRASS_IMAGE;
		case WATER: return WATER_IMAGE;
		case TREE: return TREE_IMAGE;
		default: return null;
		}
	}
}
