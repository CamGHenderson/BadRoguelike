package game.items;

import java.awt.Color;

import game.Game;
import game.Projectile;
import game.util.Loader;

public class SpreadStaff extends Sword
{
	public static Color bg = new Color(163, 73, 164);
	public static Color color = new Color(128, 0, 255);
	
	public SpreadStaff(Game game) 
	{
		super(Loader.loadImage("res/Swords/SpreadStaff/Icon.png", bg),
			  Loader.loadImage("res/Swords/SpreadStaff/Top.png", bg),
			  Loader.loadImage("res/Swords/SpreadStaff/Bottom.png", bg),
			  game);
			setDamage(15);
			setSpeed(1.0f);
			setName("Staff of Many Projectiles");
	}

	@Override
	public void onSwing() 
	{
		new Projectile(game.player.getX() + game.player.getWidth()/2 + (game.player.getHorizontalFlip() ?  -50 : 30), game.player.getY() + game.player.getHeight()/4,
				game.player.getHorizontalFlip() ? 180 : 0, 1.0f, 15, true, color, game);
		new Projectile(game.player.getX() + game.player.getWidth()/2 + (game.player.getHorizontalFlip() ?  -50 : 30), game.player.getY() + game.player.getHeight()/4,
				(game.player.getHorizontalFlip() ? 180 : 0) + 15, 1.0f, 15, true, color, game);
		new Projectile(game.player.getX() + game.player.getWidth()/2 + (game.player.getHorizontalFlip() ?  -50 : 30), game.player.getY() + game.player.getHeight()/4,
				(game.player.getHorizontalFlip() ? 180 : 0) - 15, 1.0f, 15, true, color, game);
	}
}
