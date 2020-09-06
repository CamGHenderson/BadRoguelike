package game.items;

import java.awt.Color;

import game.Game;
import game.Projectile;
import game.util.Loader;

public class MagicSword extends Sword
{
	public static Color bg = new Color(163, 73, 164);
	
	public MagicSword(Game game) {
		super(Loader.loadImage("res/Swords/MagicSword/Icon.png", bg),
			  Loader.loadImage("res/Swords/MagicSword/Top.png", bg),
			  Loader.loadImage("res/Swords/MagicSword/Bottom.png", bg),
			  game);
			setDamage(20);
			setSpeed(1.6f);
			setName("Sword of Magic");
	}

	@Override
	public void onSwing() {
		new Projectile(game.player.getX() + game.player.getWidth()/2 + (game.player.getHorizontalFlip() ?  -50 : 30), game.player.getY() + game.player.getHeight()/4,
				game.player.getHorizontalFlip() ? 180 : 0, 1.0f, 10, true, Color.red, game);
	}
}
