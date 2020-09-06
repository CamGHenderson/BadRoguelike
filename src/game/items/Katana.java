package game.items;

import java.awt.Color;

import game.Game;
import game.util.Loader;

public class Katana extends Sword{

	public static Color bg = new Color(163, 73, 164);
	
	public Katana(Game game) {
		super(Loader.loadImage("res/Swords/Katana/Icon.png", bg),
			  Loader.loadImage("res/Swords/Katana/Top.png", bg),
			  Loader.loadImage("res/Swords/Katana/Bottom.png", bg),
			  game);
			setDamage(15);
			setSpeed(3.0f);
			setName("Katana");
	}

	@Override
	public void onSwing() {}
}
