package game.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import game.Game;
import game.GameState;

public class Title 
{	
	private Font font = new Font("Courier", Font.PLAIN, 60);
	private String title = "Roguelike Without A Name";
	private int x = 0, y = 100;
	private Button play;
	
	private Game game;
	
	public Title(Game game)
	{
		this.game = game;
		play = new Button("Play", game.getWidth() / 2, 400);
	}
	
	public void render(Graphics2D graphics)
	{
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.setColor(Color.white);
		graphics.setFont(font);
		if(x == 0)
			x = game.getWidth()/2 - graphics.getFontMetrics(font).stringWidth(title)/2;
		graphics.drawString(title, x, y);
		play.render(graphics);
		if(play.getDown())
		{
			game.state = GameState.game;
			game.gameStart();
		}
	}
}
