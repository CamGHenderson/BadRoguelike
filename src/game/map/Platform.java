package game.map;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import game.Game;

public class Platform 
{	
	private int x = 0, y = 0;
	private int width = 64, height = 64;
	private PlatformType platformType;
	private BufferedImage buffer;
	private AffineTransform transform;
	
	private Game game;
	
	public Platform(BufferedImage tile, PlatformType platformType, Game game)
	{
		this.platformType = platformType;
		this.game = game;
		transform = new AffineTransform();
		transform.scale(2, 2);
		buffer = tile;
		if(platformType == PlatformType.incline2)
		{
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-tile.getWidth(), 0);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			buffer = op.filter(tile, null);
		}
		else if(platformType == PlatformType.incline3)
		{
			AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
			tx.translate(0, -tile.getHeight());
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			buffer = op.filter(tile, null);
		}
		else if(platformType == PlatformType.incline4)
		{
			AffineTransform tx = AffineTransform.getScaleInstance(-1, -1);
			tx.translate(-tile.getWidth(), -tile.getHeight());
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			buffer = op.filter(tile, null);
		}
	}
	
	public void setLocation(int x, int y)
	{
		this.x = x;
		this.y = y;
		transform.translate(x * width/2, y * height/2);
	}
	
	public void render(Graphics2D graphics)
	{
		if((x + 1) * width > game.camera.getX() && (y + 1) * height > game.camera.getY() && 
			x * width < game.camera.getX() + game.getWidth() && y * height < game.camera.getY() + game.getHeight())
			graphics.drawImage(buffer, transform, null);
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public PlatformType getPlatformType()
	{
		return platformType;
	}
}
