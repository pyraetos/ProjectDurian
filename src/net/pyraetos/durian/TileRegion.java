package net.pyraetos.durian;

public class TileRegion extends TileConstants{

	private float[][] tiles;
	
	public TileRegion(){
		tiles = new float[9][9];
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
	
	public void set(int x, int y, float f){
		try{
			tiles[x][y] = f;
		}catch(Exception e){}
	}
}
