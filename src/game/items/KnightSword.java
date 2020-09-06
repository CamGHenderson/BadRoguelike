package game.items;

import java.awt.Color;

import game.Game;
import game.util.Loader;

public class KnightSword extends Sword
{
	public static Color bg = new Color(163, 73, 164);
	
	public KnightSword(Game game) 
	{
		super(Loader.loadImage("res/Swords/KnightSword/Icon.png", bg),
			  Loader.loadImage("res/Swords/KnightSword/Top.png", bg),
			  Loader.loadImage("res/Swords/KnightSword/Bottom.png", bg),
			  game);
		setDamage(80);
		setSpeed(1.8f);
		setName("Knight's Sword");
	}

	@Override
	public void onSwing() {}
}
