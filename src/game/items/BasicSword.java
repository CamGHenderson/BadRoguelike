package game.items;

import java.awt.Color;

import game.Game;
import game.util.Loader;

public class BasicSword extends Sword
{
	public static Color bg = new Color(163, 73, 164);
	
	public BasicSword(Game game) 
	{
		super(Loader.loadImage("res/Swords/BasicSword/Icon.png", bg),
			  Loader.loadImage("res/Swords/BasicSword/Top.png", bg),
			  Loader.loadImage("res/Swords/BasicSword/Bottom.png", bg),
			  game);
		setDamage(30);
		setSpeed(2.0f);
		setName("Claymore");
	}

	@Override
	public void onSwing() {}
}
