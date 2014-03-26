package net.pyraetos.durian;

import java.awt.Image;

import net.pyraetos.util.Images;

public abstract class TileConstants{
	
	public static final byte NULL = -128;
	public static final byte WATER = 0;
	public static final byte SAND = 1;
	public static final byte GRASS = 2;
	public static final byte TREE = 3;
	public final static Image GRASS_IMAGE = Images.retrieve("grass.png");
	public final static Image WATER_IMAGE = Images.retrieve("water.png");
	public final static Image TREE_IMAGE = Images.retrieve("tree.png");
	public final static Image SAND_IMAGE = Images.retrieve("sand.png");

	public static Image imageFor(byte tile){
		switch(tile){
		case(0): return WATER_IMAGE;
		case(1): return SAND_IMAGE;
		case(2): return GRASS_IMAGE;
		case(3): return TREE_IMAGE;
		}
		return tile < 0 ? WATER_IMAGE : TREE_IMAGE;
	}
}
