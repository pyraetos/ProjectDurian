package net.pyraetos.util;

import java.awt.Image;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public abstract class Images{
	
	private static Map<String, Image> loadedImages = new HashMap<String, Image>();

	/**
	 * Returns null if image was not found or if String argument was bad
	 */
	public static Image retrieve(String file){
		if(!loadedImages.containsKey(file)){
			try {
				URL url = new URL("http://www.pyraetos.net/images/" + file);
				Image image = ImageIO.read(url.openStream());
				loadedImages.put(file, image);
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
		return loadedImages.get(file);
	}
}
