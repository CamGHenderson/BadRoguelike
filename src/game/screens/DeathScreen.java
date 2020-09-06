package game.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import game.Game;
import game.GameState;

public class DeathScreen {
	private Font font = new Font("Courier", Font.PLAIN, 80);
	private String title = "You Died";
	private int x = 0, y = 350;
	private double deathTimer = 0;
	
	private Game game;
	
	public DeathScreen(Game game)
	{
		this.game = game;
	}
	
	public void render(Graphics2D graphics)
	{
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.setColor(Color.red);
		graphics.setFont(font);
		if(x == 0)
			x = game.getWidth()/2 - graphics.getFontMetrics(font).stringWidth(title)/2;
		graphics.drawString(title, x, y);
		deathTimer += game.getDeltaTime();
		if(deathTimer > 2000)
		{
			game.state = GameState.title;
			deathTimer = 0;
		}
	}
}
