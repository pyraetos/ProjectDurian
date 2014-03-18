package net.pyraetos.util;

import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public abstract class Images{
	
	private static Map<String, Image> loadedImages = new HashMap<String, Image>();
	private static String prefix;
	
	static{
		fromLocal();
	}
	
	public static void fromPyraetosNet(){
		fromURL("http://www.pyraetos.net/images/");
	}
	
	public static void fromLocal(){
		fromDirectory("");
	}
	
	public static void fromLocalImages(){
		fromDirectory("images" + File.separator);
	}
	
	public static void fromDirectory(String dir){
		prefix = dir = dir.replace("\\", "/") + (dir.endsWith(File.separator) ? "" : File.separator);
	}
	
	/**
	 * remember to include protocol
	 */
	public static void fromURL(String url){
		prefix = "url-" + url + (url.endsWith("/") ? "" : "/");
	}
	
	/**
	 * Attempts by default to retrieve from the current working directory
	 */
	public static Image retrieve(String file){
		if(!loadedImages.containsKey(file)){
			try{
				Image image;
				if(prefix.startsWith("url-")){
					URL url = new URL(prefix.substring(4) + file);
					image = ImageIO.read(url);
				}else{
					File f = new File(prefix + file);
					image = ImageIO.read(f);
				}
				loadedImages.put(file, image);
			}catch(Exception e){
				Sys.debug(e.getMessage());
				Sys.debug("Path: " + prefix + file);
				return null;
			}
		}
		return loadedImages.get(file);
	}
}
