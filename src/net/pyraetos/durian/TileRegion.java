package net.pyraetos.durian;

public class TileRegion extends TileConstants{

	private double[][] tiles;
	
	public TileRegion(){

		tiles = new double[9][9];
		for(int i = 0; i < 9; i++)
			for(int j = 0; j < 9; j++)
				tiles[i][j] = NULL;
	}
	
	public double get(int x, int y){
		try{
			return tiles[x][y];
		}catch(Exception e){
			return NULL;
		}
	}
	
	public void set(int x, int y, double d){
		try{
			tiles[x][y] = d;
		}catch(Exception e){}
	}
}
