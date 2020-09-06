package game.player;

import java.awt.Color;
import java.awt.Graphics2D;

public class HealthBar {
	public void render(Graphics2D graphics, int hp)
	{
		graphics.setColor(Color.red);
		graphics.fillRect(15, 15, 400, 25);
		graphics.setColor(Color.green);
		graphics.fillRect(15, 15, hp * 4, 25);
		graphics.setColor(Color.white);
		graphics.drawRect(15, 15, 400, 25);
	}
}
