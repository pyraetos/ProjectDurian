package net.pyraetos.util;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Sys{
	
	public static final byte NORTH = 0;
	public static final byte SOUTH = 1;
	public static final byte EAST = 2;
	public static final byte WEST = 3;
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("########.##");
	private static final Random RANDOM = new Random();

	public static void thread(Runnable r){
		new Thread(r).start();
	}
	
	public static boolean equal(Object...objects){
		Object o = null;
		for(Object object : objects){
			if(o != null)
			if(!object.equals(o)) return false;
			o = object;
		}
		return true;
	}
	
	public static void sleep(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static <E> Set<E> concurrentSet(Class<E> type){
		return Collections.newSetFromMap(new ConcurrentHashMap<E, Boolean>());
	}
	
	public static Set<Integer> concurrentSetInteger(){
		return Collections.newSetFromMap(new ConcurrentHashMap<Integer, Boolean>());
	}
	
	public static Set<String> concurrentSetString(){
		return Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
	}
	
	public static void debug(Object o){
		System.out.println(o);
	}
	
	public static void debug(int i){
		System.out.println(i);
	}
	
	public static void debug(double d){
		System.out.println(d);
	}
	
	public static void debug(boolean b){
		System.out.println(b);
	}
	
	public static void debug(String s){
		System.out.println(s);
	}
	
	public static void debug(long l){
		System.out.println(l);
	}
	
	public static void debug(){
		System.out.println("debug");
	}

	public static boolean chance(double chance){
		return chance(chance, new Random());
	}
	
	public static boolean chance(double chance, Random r){
		double random = r.nextDouble();
		return random <= chance;
	}
	
	public static int distanceFrom(int x0, int y0, int x1, int y1){
		return (int)Math.round(Math.sqrt(Math.pow(Math.max(x0, x1) - Math.min(x0, x1), 2) + Math.pow(Math.max(y0, y1) - Math.min(y0, y1), 2)));
	}
	
	public static long randomSeed(){
		return Math.round(System.currentTimeMillis() * (1 - (2*RANDOM.nextDouble())));
	}
	
	public static double round(double d){
		return Double.parseDouble(DECIMAL_FORMAT.format(d));
	}
}
