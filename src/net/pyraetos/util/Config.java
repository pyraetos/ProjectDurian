package net.pyraetos.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Config{

	private Map<String, String> map;
	private File file;
	
	public Config(String dir){
		map = new HashMap<String, String>();
		file = new File(dir);
		try{
			if(!file.exists())
				file.createNewFile();
		}catch(IOException e){
			e.printStackTrace();
		}
		reload();
	}

	public void reload(){
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String raw = in.readLine();
			while(raw != null){
				String[] split = raw.split(":");
				map.put(split[0].trim(), split[1].trim());
				raw = in.readLine();
			}
			in.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	public void save(){
		try{
			file.delete();
			file.createNewFile();
			PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
			for(String key : map.keySet())
				out.println(key + ": " + map.get(key));
			out.flush();
			out.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public String getString(String key, String defaultValue){
		if(!map.containsKey(key)){
			map.put(key, defaultValue);
			save();
		}
		return map.get(key);
	}

	public boolean getBoolean(String key, boolean defaultValue){
		if(!map.containsKey(key)){
			map.put(key, String.valueOf(defaultValue));
			save();
		}
		return Boolean.parseBoolean(map.get(key));
	}
		
	public byte getByte(String key, byte defaultValue){
		if(!map.containsKey(key)){
			map.put(key, String.valueOf(defaultValue));
			save();
		}
		return Byte.parseByte(map.get(key));
	}
	
	public short getShort(String key, short defaultValue){
		if(!map.containsKey(key)){
			map.put(key, String.valueOf(defaultValue));
			save();
		}
		return Short.parseShort(map.get(key));
	}

	public char getChar(String key, char defaultValue){
		if(!map.containsKey(key)){
			map.put(key, String.valueOf(defaultValue));
			save();
		}
		return map.get(key).charAt(0);
	}

	public int getInt(String key, int defaultValue){
		if(!map.containsKey(key)){
			map.put(key, String.valueOf(defaultValue));
			save();
		}
		return Integer.parseInt(map.get(key));
	}

	public float getFloat(String key, float defaultValue){
		if(!map.containsKey(key)){
			map.put(key, String.valueOf(defaultValue));
			save();
		}
		return Float.parseFloat(map.get(key));
	}

	public long getLong(String key, long defaultValue){
		if(!map.containsKey(key)){
			map.put(key, String.valueOf(defaultValue));
			save();
		}	
		return Long.parseLong(map.get(key));
	}

	public double getDouble(String key, double defaultValue){
		if(!map.containsKey(key)){
			map.put(key, String.valueOf(defaultValue));
			save();
		}
		return Double.parseDouble(map.get(key));
	}
}