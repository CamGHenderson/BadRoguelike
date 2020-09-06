package game.object;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import game.util.Loader;

public class Animation 
{
	
	int currentFrame = 0;
	private ArrayList<BufferedImage> frames;
	private double delay = 0;
	private double frameTime = 0;
	boolean playing = false;
	
	public Animation(String folder, Color color)
	{
		frames = new ArrayList<BufferedImage>();
		HashMap<Integer, BufferedImage> unordered = new HashMap<Integer, BufferedImage>();
		File file = new File(folder);
		for(File frame : file.listFiles())
		{
			try
			{
				unordered.put(Integer.parseInt(frame.getName().replaceAll("\\D+","")) - 1,
						Loader.loadImage(folder+"/"+frame.getName(), color));
			}
			catch(NumberFormatException e)
			{
				frames.add(Loader.loadImage(folder+"/"+frame.getName(), color));
			}
		}
		
		for(int i = 0; i < unordered.size(); i++)
		{
			frames.add(unordered.get(i));
		}
	}
	
	public void update(double deltaTime) 
	{
		if(playing)
		{
			frameTime += deltaTime;
			if(frameTime >= delay)
			{
				if(currentFrame + 1 == frames.size())
				{
					
					playing = false;
				}
				else
				{
					currentFrame++;
				}
				frameTime = 0;
			}
		}
	}
	
	public BufferedImage getFrame()
	{
		return frames.get(currentFrame);
	}
	
	public int getCurrentFrame()
	{
		return currentFrame;
	}
	
	public void setDelay(double delay)
	{
		this.delay = delay;
	}
	
	public boolean isPlaying()
	{
		return playing;
	}
	
	public void play()
	{
		currentFrame = 0;
		playing = true;
	}
	
	public void pause()
	{
		playing = false;
	}
	
	public void stop()
	{
		currentFrame = 0;
		playing = false;
	}
}
