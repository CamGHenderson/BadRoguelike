package game.items;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import game.util.Loader;

public class Medkit {
	
	private static Color bg = new Color(163, 73, 164);
	
	private BufferedImage image;
	private Rectangle hitbox;
	private int hp = 20;
	private AffineTransform transform;
	
	public Medkit(int x, int y)
	{
		image = Loader.loadImage("res/Medkit.png", bg);
		hitbox = new Rectangle(x * 64 + (32 - image.getWidth()), y * 64 - image.getHeight() * 2 + 2, image.getWidth() * 2, image.getHeight() * 2);
		transform = new AffineTransform();
		transform.translate(hitbox.x, hitbox.y);
		transform.scale(2, 2);
	}
	
	public void render(Graphics2D graphics)
	{	
		graphics.drawImage(image, transform, null);
	}
	
	public Rectangle getHitbox()
	{
		return hitbox;
	}
	
	public int getHp()
	{
		return hp;
	}
}
