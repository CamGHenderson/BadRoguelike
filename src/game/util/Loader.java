package game.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import game.Game;
import game.items.BasicSword;
import game.items.DamageStaff;
import game.items.Katana;
import game.items.KnightSword;
import game.items.MagicSword;
import game.items.SpreadStaff;
import game.items.Sword;
import game.map.Map;
import game.map.Platform;
import game.map.PlatformType;
import game.map.Tile;

public class Loader {
	public static BufferedImage loadImage(String file)
	{
		BufferedImage image = null;
		try 
		{
			image = ImageIO.read(new File(file));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return image;
	}
	
	public static BufferedImage loadImage(String file, Color color)
	{
		BufferedImage image = loadImage(file);
		ImageFilter filter = new RGBImageFilter()
	    {
			public final int filterRGB(int x, int y, int rgb)
			{
		        int r = (rgb & 0xFF0000) >> 16;
		        int g = (rgb & 0xFF00) >> 8;
		        int b = rgb & 0xFF;
		        if (r == color.getRed() && g == color.getGreen() && b == color.getBlue())
		        	return rgb & 0xFFFFFF;
		        return rgb;
		    }
	    };
	    ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
	    Image transparent = Toolkit.getDefaultToolkit().createImage(ip);
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = newImage.createGraphics();
		graphics.drawImage(transparent, 0, 0, null);
		graphics.dispose();
		return newImage;
	}
	
	public static Map loadMap(String map, Color color, Game game)
	{
		ArrayList<Platform> platforms = new ArrayList<Platform>();
		HashMap<Character, Tile> tiles = new HashMap<Character, Tile>();
		HashMap<Character, String> swordTypes = new HashMap<Character, String>();
		char spawnMarker = 0;
		int spawnX = 0, spawnY = 0;
		boolean mapStart = false;
		int y = 0;
		PlatformType type = null;
		
		String input = null;
		try 
		{
			input = new String(Files.readAllBytes(new File(map).toPath()));
		}
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		
		StringBuilder mapData = new StringBuilder("");
	    for (int i = 0; i < input.length()-2; i += 2) {
	        String str = input.substring(i, i + 2);
	        mapData.append((char)Integer.parseInt(str, 16));
	    }
		
		String[] lines = mapData.toString().split("\\r?\\n");
		int count = 0;
		String line;
		
		while (count < lines.length) 
		{
			line = lines[count];
			count++;
			if(!mapStart)
		   	{
		   		if(!line.equals(""))
		   		{
		   			if(line.contains("="))
		   			{
		   				String data = line.substring(line.lastIndexOf("=") + 1).trim();
		   				String[] beforeSplit = line.split(" ");
		   				String[] afterSplit = data.split(" ");
		   				if(!data.contains(".png"))
		   				{
		   					if(beforeSplit[0].equals("PLAYER_WEAPON"))
		   					{
		   						if(afterSplit[0].equals("CLAYMORE"))
		   							game.player.setSword(new BasicSword(game));
		   						else if(afterSplit[0].equals("KATANA"))
		   							game.player.setSword(new Katana(game));
		   						else if(afterSplit[0].equals("KNIGHT_SWORD"))
		   							game.player.setSword(new KnightSword(game));
		   						else if(afterSplit[0].equals("MAGIC_SWORD"))
		   							game.player.setSword(new MagicSword(game));
		   						else if(afterSplit[0].equals("DAMAGE_STAFF"))
		   							game.player.setSword(new DamageStaff(game));
		   						else if(afterSplit[0].equals("SPREAD_STAFF"))
		   							game.player.setSword(new SpreadStaff(game));
		   					}
		   					else if(beforeSplit[0].equals("PLAYER_HEALTH"))
		   					{
		   						game.player.setHp(Integer.parseInt(afterSplit[0]));
		   					}
		   					else if(data.equals("SPAWN_POINT"))
		   						spawnMarker = line.charAt(0);
		   					else if(data.equals("CLAYMORE"))
		   						swordTypes.put(line.charAt(0), "CLAYMORE");
		   					else if(data.equals("KATANA"))
		   						swordTypes.put(line.charAt(0), "KATANA");
		   					else if(data.equals("KNIGHT_SWORD"))
		   						swordTypes.put(line.charAt(0), "KNIGHT_SWORD");
		   					else if(data.equals("MAGIC_SWORD"))
		   						swordTypes.put(line.charAt(0), "MAGIC_SWORD");
		   					else if(data.equals("DAMAGE_STAFF"))
		   						swordTypes.put(line.charAt(0), "DAMAGE_STAFF");
		   					else if(data.equals("SPREAD_STAFF"))
		   						swordTypes.put(line.charAt(0), "SPREAD_STAFF");
		   					else
		   					{
		    					Tile tile = tiles.get(afterSplit[0].charAt(0));
		    					boolean h = false;
		    					boolean v = false;
		    					
		    					if(afterSplit.length > 2)
		    					{
		    						h = afterSplit[1].equals("true");
		    						v = afterSplit[2].equals("true");
		    					}
		    					else if(afterSplit.length > 1)
		    					{
		    						h = afterSplit[1].equals("true");
		    					}
		    					
		    					if(h & v)
		    					{
		    						type = PlatformType.incline4;
		    					}
		    					else if(h)
		    					{
		    						type = PlatformType.incline2;
		    					}
		    					else if(v)
		    					{
		    						type = PlatformType.incline3;
		    					}
			    					
		    					tiles.put(beforeSplit[1].charAt(0), new Tile(tile.getImage(), type));
		   					}
		   				}
		   				else
		   				{
		   					if(beforeSplit[0].equals("Square"))
		   						type = PlatformType.square;
		   					else if(beforeSplit[0].equals("Incline"))
		    					type = PlatformType.incline1;
		   					tiles.put(beforeSplit[1].charAt(0), new Tile(loadImage("res/Tiles/"+data, color), type));
		   				}
		   			}	
		   			if(line.equals("done"))
		   				mapStart = true;
		   		}
		   	}
		   	else
		   	{
		   		int x = 0;
		   		try
		   		{
		   			while(true)
		    		{	
		   				if(line.charAt(x) != ' ')
		   				{
			   				if(line.charAt(x) == '\n')
			   					break;
			    			if(line.charAt(x) == spawnMarker)
			    			{
			    				spawnX = x;
			    				spawnY = y;	
			    			}
			    			else if(swordTypes.containsKey(line.charAt(x)))
			    			{
			    				String swordType = swordTypes.get(line.charAt(x));
			    				Sword sword;
			    				if(swordType == "CLAYMORE")
			    				{
			    					sword = new BasicSword(game);
			    					sword.setPosition(x * 64, y * 64 + 18);
			    					game.swords.add(sword);
			    				}
			    					else if(swordType == "KATANA")
				   				{
			    					sword = new Katana(game);
			    					sword.setPosition(x * 64, y * 64 + 18);
			    					game.swords.add(sword);
				   				}
				   				else if(swordType == "KNIGHT_SWORD")
				   				{
				   					sword = new KnightSword(game);
			    					sword.setPosition(x * 64, y * 64 + 18);
			    					game.swords.add(sword);
				   				}
				   				else if(swordType == "MAGIC_SWORD")
				   				{
				   					sword = new MagicSword(game);
			    					sword.setPosition(x * 64, y * 64 + 18);
			    					game.swords.add(sword);
				   				}
				   				else if(swordType == "DAMAGE_STAFF")
				    				{
				   					sword = new DamageStaff(game);
			    					sword.setPosition(x * 64, y * 64 + 18);
			    					game.swords.add(sword);
				   				}
				   				else if(swordType == "SPREAD_STAFF")
				   				{
				   					sword = new SpreadStaff(game);
			    					sword.setPosition(x * 64, y * 64 + 18);
			    					game.swords.add(sword);
				   				}
			    			}
			    			else
			    			{
			    				Tile tile = tiles.get(line.charAt(x));
			    				Platform platform = new Platform(tile.getImage(), tile.getType(), game);
				   				platform.setLocation(x, y);
				   				platforms.add(platform);
			    			}
		   				}
		   				x++;
		   			}
		   		}
		   		catch(StringIndexOutOfBoundsException e){}
		   		y++;
		  	}
		} 
		
		return new Map(platforms, spawnX, spawnY, game);
	}
}
