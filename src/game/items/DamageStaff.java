package game.items;

import java.awt.Color;

import game.Game;
import game.Projectile;
import game.util.Loader;

public class DamageStaff extends Sword
{
	public static Color bg = new Color(163, 73, 164);
	
	public DamageStaff(Game game) 
	{
		super(Loader.loadImage("res/Swords/DamageStaff/Icon.png", bg),
			  Loader.loadImage("res/Swords/DamageStaff/Top.png", bg),
			  Loader.loadImage("res/Swords/DamageStaff/Bottom.png", bg),
			  game);
			setDamage(10);
			setSpeed(1.0f);
			setName("Staff of Destruction");
	}

	@Override
	public void onSwing() 
	{
		new Projectile(game.player.getX() + game.player.getWidth()/2 + (game.player.getHorizontalFlip() ?  -50 : 30), game.player.getY() + game.player.getHeight()/4,
				game.player.getHorizontalFlip() ? 180 : 0, 1.0f, 35, true, Color.green, game);
	}
}
