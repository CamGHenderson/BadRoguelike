package game.map;

import java.awt.image.BufferedImage;

public class Tile
{
	private BufferedImage image;
	private PlatformType type;
	
	public Tile(BufferedImage image, PlatformType type)
	{
		this.image = image;
		this.type = type;
	}
	
	public BufferedImage getImage()
	{
		return image;
	}
	
	public PlatformType getType()
	{
		return type;
	}
}
