package game.object;

import game.Game;

public class Camera 
{
	
	private float x, y;
	private Game game;
	
	public Camera(Game game)
	{
		this.game = game;
	}
	
	public void move(float x, float y)
	{
		this.x += x * game.getDeltaTime();
		this.y += y * game.getDeltaTime();
	}
	
	public void setPosition(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public float getX()
	{
		return x; 
	}
	
	public float getY()
	{
		return y;
	}
}
