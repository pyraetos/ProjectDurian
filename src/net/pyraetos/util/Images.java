package net.pyraetos.util;

import java.awt.Image;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public abstract class Images{
	
	private static Map<String, Image> loadedImages = new HashMap<String, Image>();
	private static String prefix;
	
	static{
		try{
			fromLocal();
		}catch(Exception e){}
	}
	
	public static void fromPyraetosNet(){
		fromURL("http://www.pyraetos.net/images/");
	}
	
	public static void fromLocal(){
		fromURL("file:///" + System.getProperty("user.dir").replace("\\", "/"));
	}
	
	public static void fromLocalImages(){
		fromURL("file:///" + System.getProperty("user.dir").replace("\\", "/") + "/images/");
	}
	
	public static void fromURL(String url){
		prefix = url + (url.endsWith("/") ? "" : "/");
	}

	public static Image retrieve(String file){
		if(!loadedImages.containsKey(file)){
			try{
				URL url = new URL(prefix + file);
				Image image = ImageIO.read(url);
				loadedImages.put(file, image);
			}catch(Exception e){
				Sys.debug("Path: " + prefix + file);
				e.printStackTrace();
				return null;
			}
		}
		return loadedImages.get(file);
	}
}
