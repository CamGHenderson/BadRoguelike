package game.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter{
	
	public static boolean key[] = new boolean[526];
	
	public KeyInput() {
		for(int keys = 0; keys < 526; keys++) {
			key[keys] = false;
		}
	}
	
	public void keyPressed(KeyEvent e) {
		for(int keys = 0; keys < 526; keys++) {
			if(e.getKeyCode() == keys) key[keys] = true;
		}
	}
	
	
	public void keyReleased(KeyEvent e) {
		for(int keys = 0; keys < 526; keys++) {
			if(e.getKeyCode() == keys) key[keys] = false;
		}
	}
}