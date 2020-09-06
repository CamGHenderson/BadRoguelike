package game.audio;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.openal.AL;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.openal.SoundStore;

public class AudioPlayer {
	
	private static Map<String, Sound> soundMap;
	private static Map<String, Music> musicMap;
	
	private static boolean alive = false;
	
	public static void init() {
		soundMap = new HashMap<String, Sound>();
		musicMap = new HashMap<String, Music>();
		alive = true;
	}
	
	public static void destroy() {
		alive = false;
		SoundStore.get().clear();
		AL.destroy();
	}
	
	public static void loadMusic(String key, String path) {
		try {
			musicMap.put(key, new Music(path));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public static Music getMusic(String key) {
		if(alive)
			return musicMap.get(key);
		return null;
	}
	
	public static void loadSound(String key, String path) {
		try {
			soundMap.put(key, new Sound(path));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public static Sound getSound(String key) {
		if(alive)
			return soundMap.get(key);
		return null;
	}
}
