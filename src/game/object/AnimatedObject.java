package game.object;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import game.Game;

public abstract class AnimatedObject 
{	
	private float x = 0, y = 0;
	private float scaleX = 1, scaleY = 1;
	private int axisX, axisY;
	private double rotation = 0;
	boolean flipH = false, flipV = false;
	private	HashMap<String, Animation> animations;
	private String currentAnimation;
	private AffineTransform affineTransform;
	private BufferedImage buffer;
	protected Game game;
	
	public AnimatedObject(String path, Color color, Game game)
	{
		this.game = game;
		animations = new HashMap<String, Animation>();
		File file = null;
		file = new File("res/"+path);
		for(File folder : file.listFiles())
		{
			if(folder.isDirectory())
			{
				animations.put(folder.getName(), new Animation("res/"+path+"/"+folder.getName(), color));
				if(currentAnimation == null)
					currentAnimation = folder.getName();
			}
		}
	}
	
	protected abstract void onRender(Graphics2D graphics);
	
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
	
	public float getScaleX() {
		return scaleX;
	}

	public void setScale(float scaleX, float scaleY)
	{
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	public float getScaleY() {
		return scaleY;
	}

	public void setRotationAxis(int axisX, int axisY)
	{
		this.axisX = axisX;
		this.axisY = axisY;
	}

	public int getAxisY() {
		return axisY;
	}

	public void setAxisY(int axisY) {
		this.axisY = axisY;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public void flip(boolean flipH, boolean flipV)
	{
		this.flipH = flipH;
		this.flipV = flipV;
	}
	
	public boolean getHorizontalFlip()
	{
		return flipH;
	}
	
	public boolean getVerticalFlip()
	{
		return flipV;
	}
	
	public HashMap<String, Animation> getAnimations()
	{
		return animations;
	}
	
	public Animation getAnimation()
	{
		return animations.get(currentAnimation);
	}
	
	public Animation getAnimation(String animation)
	{
		return animations.get(animation);
	}
	
	public void setAnimation(String currentAnimation)
	{
		this.currentAnimation = currentAnimation;
	}
	
	public String getCurrentAnimation()
	{
		return currentAnimation;
	}
	
	public void render(Graphics2D graphics)
	{
		animations.get(currentAnimation).update(game.getDeltaTime());
		affineTransform = new AffineTransform();
		affineTransform.translate(x, y);
		affineTransform.scale(scaleX, scaleY);
		affineTransform.rotate(Math.toRadians(rotation), axisX, axisY);
		buffer = animations.get(currentAnimation).getFrame();
		if(flipH && flipV)
		{
			AffineTransform tx = AffineTransform.getScaleInstance(-1, -1);
			tx.translate(-animations.get(currentAnimation).getFrame().getWidth(),
					-animations.get(currentAnimation).getFrame().getHeight());
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			buffer = op.filter(animations.get(currentAnimation).getFrame(), null);
		}
		else if(flipH)
		{
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-animations.get(currentAnimation).getFrame().getWidth(), 0);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			buffer = op.filter(animations.get(currentAnimation).getFrame(), null);
		}
		else if(flipV)
		{
			AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
			tx.translate(0, -animations.get(currentAnimation).getFrame().getHeight());
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			buffer = op.filter(animations.get(currentAnimation).getFrame(), null);
		}
		graphics.drawImage(buffer, affineTransform, null);
		onRender(graphics);
	}
}
