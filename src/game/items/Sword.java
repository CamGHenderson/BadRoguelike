package game.items;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import game.Game;
import game.player.Player;

public abstract class Sword 
{
	private BufferedImage icon;
	private BufferedImage top;
	private BufferedImage bottom;
	
	private BufferedImage iconBuffer;
	private BufferedImage topBuffer;
	private BufferedImage bottomBuffer;
	
	private float x, y;
	private int axisX = 2, axisY = 2;
	private float rotation;
	
	private AffineTransform transform;
	private AffineTransform topTransform;
	private AffineTransform bottomTransform;
	
	private int damage;
	private float speed;
	
	private String name;
	
	protected Game game;
	
	public Sword(BufferedImage icon, BufferedImage top, BufferedImage bottom, Game game)
	{
		this.icon = icon;
		this.top = top;
		this.bottom = bottom;
		this.game = game;
	}
	
	public abstract void onSwing();
	
	public void renderIcon(Graphics2D graphics)
	{
		transform = new AffineTransform();
		transform.translate(x, y);
		transform.scale(2, 2);
		transform.rotate(Math.toRadians(rotation), axisX, axisY);
		iconBuffer = icon;
		graphics.drawImage(iconBuffer, transform, null);
	}
	
	public void renderInHand(Graphics2D graphics, Player player)
	{
		transform = new AffineTransform();
		if(player.getHorizontalFlip())
			transform.translate(player.getX() - player.getHandX(), player.getY() + player.getHandY());
		else
			transform.translate(player.getX() + player.getHandX(), player.getY() + player.getHandY());
		transform.scale(2, 2);
		topBuffer = top;
		bottomBuffer = bottom;
		topTransform = new AffineTransform(transform);
		bottomTransform = new AffineTransform(transform);
		if(player.getHorizontalFlip())
		{
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-top.getWidth(), 0);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			topBuffer = op.filter(top, null);
			tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-bottom.getWidth(), 0);
			op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			bottomBuffer = op.filter(bottom, null);
			topTransform.translate(-top.getWidth() + 11, -top.getHeight() + 20);
			topTransform.rotate(Math.toRadians(-rotation), top.getWidth() + axisX, top.getHeight() + axisY);
			bottomTransform.translate(-bottom.getWidth() + 17, 23);
			bottomTransform.rotate(Math.toRadians(-rotation), bottom.getWidth() - axisX, -axisY);
		}
		else
		{
			topTransform.translate(20, -top.getHeight() + 20);
			topTransform.rotate(Math.toRadians(rotation), -axisX, top.getHeight() + axisY);
			bottomTransform.translate(-bottom.getWidth() + 19, 23);
			bottomTransform.rotate(Math.toRadians(rotation), bottom.getWidth() + axisX, -axisY);
		}
		graphics.drawImage(topBuffer, topTransform, null);
		graphics.drawImage(bottomBuffer, bottomTransform, null);
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
	
	public int getWidth()
	{
		return icon.getWidth() * 2;
	}
	
	public int getHeight()
	{
		return icon.getHeight() * 2;
	}
	
	public void setRotation(float rotation)
	{
		this.rotation = rotation;
	}
	
	public float getRotation()
	{
		return rotation;
	}
	
	public void setDamage(int damage)
	{
		this.damage = damage;
	}
	
	public int getDamage()
	{
		return damage;
	}
	
	public void setSpeed(float speed)
	{
		this.speed = speed;
	}
	
	public float getSpeed()
	{
		return speed;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
}
