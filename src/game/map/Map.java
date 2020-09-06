package game.map;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

import game.Game;
import game.items.BasicSword;
import game.items.DamageStaff;
import game.items.Katana;
import game.items.KnightSword;
import game.items.MagicSword;
import game.items.SpreadStaff;
import game.items.Sword;

public class Map 
{	
	private static String header = 
			"Square # = tile_square.png\r\n" + 
			"\r\n" + 
			"% = SPAWN_POINT\r\n" + 
			"\r\n" + 
			"c = CLAYMORE\r\n" + 
			"k = KATANA\r\n" + 
			"n = KNIGHT_SWORD\r\n" + 
			"m = MAGIC_SWORD\r\n" + 
			"d = DAMAGE_STAFF\r\n" + 
			"s = SPREAD_STAFF\r\n" +
			"\r\n";
	
	private static String done = 
			"\r\n" + 
			"done\r\n" +
			"\r\n";
	
	private static Random random = new Random();
	
	private ArrayList<Platform> platforms;
	private int spawnX, spawnY;
	private Game game;
	
	public Map(ArrayList<Platform> platforms, int spawnX, int spawnY, Game game)
	{
		this.platforms = platforms;
		this.spawnX = spawnX;
		this.spawnY = spawnY;
		this.game = game;
	}
	
	public Map(Game game)
	{
		this.platforms = new ArrayList<Platform>();
		this.game = game;
	}
	
	public ArrayList<Platform> getPlatforms()
	{
		return platforms;
	}
	
	public Platform platformCollides(int x, int y, int width, int height)
	{
		for(Platform platform : platforms)
		{
			switch(platform.getPlatformType())
			{
			case square:
				if(x + width >= platform.getX() * 64 && y + height >= platform.getY() * 64 &&
				   x <= (platform.getX() + 1) * 64 && y <= (platform.getY() + 1) * 64)
				{
					return platform;
				}
				break;
			case incline1:
				if(x + width >= platform.getX() * 64 && y + height >= platform.getY() * 64 - 64 &&
				   x <= (platform.getX() + 1) * 64 && y <= (platform.getY() + 1) * 64)
				{
					return platform;
				}
				break;
			case incline2:
				
				break;
			case incline3:
				
				break;
			case incline4:
				
				break;
			default:
				break;
			}
		}
		return null;
	}
	
	public int getSpawnX()
	{
		return spawnX;
	}
	
	public int getSpawnY()
	{
		return spawnY;
	}
	
	public void save(String map)
	{
		int width = 0;
		int height = 0;
		for(int i = 0; i < platforms.size(); i++)
		{	
			if(platforms.get(i).getX()+1 > width)
				width = platforms.get(i).getX()+1;
				
			if(platforms.get(i).getY()+1 > height)
				height = platforms.get(i).getY()+1;
		}
		
		StringBuilder[] lines = new StringBuilder[height];
		for(int i = 0; i < lines.length; i++)
		{
			lines[i] = new StringBuilder();
			for(int x = 0; x < width; x++)
				lines[i].append(' ');
		}
		
		String data = "";
		
		for(int i = 0; i < platforms.size(); i++)
			lines[platforms.get(i).getY()-1].setCharAt(platforms.get(i).getX(), '#');
		
		for(int i = 0; i < game.swords.size(); i++)
		{
			char type = ' ';
			Sword sword = game.swords.get(i);
			if(sword instanceof BasicSword)
				type = 'c';
			else if(sword instanceof Katana)
				type = 'k';
			else if(sword instanceof KnightSword)
				type = 'n';
			else if(sword instanceof MagicSword)
				type = 'm';
			else if(sword instanceof DamageStaff)
				type = 'd';
			else if(sword instanceof SpreadStaff)
				type = 's';
			lines[((int)(sword.getY())/64)-1].setCharAt((int)Math.round(sword.getX() / 64), type);
		}
		
		try
		{
			if(lines[(int)(game.player.getLastY()/64)].toString().charAt((int)(game.player.getLastX()/64+1)) == ' ')
				lines[(int)(game.player.getLastY()/64)].setCharAt((int)(game.player.getLastX()/64+1), '%');
			else
				lines[(int)(game.player.getLastY()/64)].setCharAt((int)(game.player.getLastX()/64), '%');
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			if(platforms.size() > 0)
				lines[0].setCharAt(0, '%');
		}
		
		for(int i = 0; i < lines.length; i++)
		{
			data += lines[i].toString();
			data += '\n';
		}
		
		String type = null;
		Sword sword = game.player.getSword();
		if(sword instanceof BasicSword)
			type = "CLAYMORE";
		else if(sword instanceof Katana)
			type = "KATANA";
		else if(sword instanceof KnightSword)
			type = "KNIGHT_SWORD";
		else if(sword instanceof MagicSword)
			type = "MAGIC_SWORD";
		else if(sword instanceof DamageStaff)
			type = "DAMAGE_STAFF";
		else if(sword instanceof SpreadStaff)
			type = "SPREAD_STAFF";
		
		data = header + "PLAYER_WEAPON = " + type + "\nPLAYER_HEALTH = " + game.player.getHp() + done + data;
		File file = new File(map);
		file.delete();
		try 
		{
			file.createNewFile();
			DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
			out.write(String.format("%040x", new BigInteger(1, data.getBytes())).getBytes());
			out.close();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void generateMap()
	{	
		// draw out platforms
		StringBuilder map = new StringBuilder("");
		int width = 430;
		int height = 85;
		
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
				map.append('#');
			map.append('\n');
		}
		width += 1;
		
		// carve out details
		for(int y = 0; y < 5; y++)
		{
			for(int x = 0; x < 15; x++)
			{
				map.setCharAt((y + 3) * width + (x + 10), ' ');
			}
			map.append('\n');
		}
		map.setCharAt(7 * width + 11, '%');
		
		for(int y = 0; y < 5; y++)
		{
			map.setCharAt((6 + y) * width + (25), ' ');
			map.setCharAt((6 + y) * width + (26), ' ');
		}
		
		// start generation
		int length = random.nextInt(10) + 5;
		int lx = 0;
		int ly = 0;
		int rw = 0;
		int rh = 0;
		int modY = 0;
		
		for(int x = 0; x < length; x++)
		{
			map.setCharAt(9 * width + (25 + x), ' ');
			map.setCharAt(10 * width + (25 + x), ' ');
		}
		
		lx = length + 25;
		ly = 9;
		int highY = 0;
		
		//generate rooms
		for(int l = 0; l < 8; l++)
		{
			for(int i = 0; i < 10; i++)
			{
				rw = random.nextInt(30) + 10;
				rh = random.nextInt(5) + 5;
				modY = random.nextInt(rh-1);
				for(int x = 0; x < rw; x++)
				{
					for(int y = 0; y < rh; y++)
					{
						map.setCharAt((ly + y - modY) * width + (lx + x), ' ');
					}
					if(rh > highY)
						highY = rh;
				}
				lx += rw;
				length = random.nextInt(10) + 5;
				for(int x = 0; x < length; x++)
				{
					map.setCharAt((ly) * width + (lx + x), ' ');
					map.setCharAt((ly+1) * width + (lx + x), ' ');
				}
				lx += length;
			}
			ly += highY;
			lx = random.nextInt(10) + 10;
			highY = 0;
		}
		
		int count = 0;
		while(count != 6)
		{
			int x1 = random.nextInt(width);
			int y1 = random.nextInt(height-2-10)+2+10;
			if(map.charAt(y1 * width + x1) == '#')
				if(map.charAt((y1 - 1) * width + x1) == ' ')
				{
					map.setCharAt((y1 - 1) * width + x1, 'c');
					count++;
				}
		}
		
		count = 0;
		while(count != 5)
		{
			int x1 = random.nextInt(width);
			int y1 = random.nextInt(height-2-15)+2+15;
			if(map.charAt(y1 * width + x1) == '#')
				if(map.charAt((y1 - 1) * width + x1) == ' ')
				{
					map.setCharAt((y1 - 1) * width + x1, 'k');
					count++;
				}
		}
		
		count = 0;
		while(count != 4)
		{
			int x1 = random.nextInt(width);
			int y1 = random.nextInt(height-2-40)+2+40;
			if(map.charAt(y1 * width + x1) == '#')
				if(map.charAt((y1 - 1) * width + x1) == ' ')
				{
					map.setCharAt((y1 - 1) * width + x1, 'n');
					count++;
				}
		}
		
		count = 0;
		while(count != 4)
		{
			int x1 = random.nextInt(width);
			int y1 = random.nextInt(height-2-15)+2+15;
			if(map.charAt(y1 * width + x1) == '#')
				if(map.charAt((y1 - 1) * width + x1) == ' ')
				{
					map.setCharAt((y1 - 1) * width + x1, 'm');
					count++;
				}
		}
		
		count = 0;
		while(count != 3)
		{
			int x1 = random.nextInt(width);
			int y1 = random.nextInt(height-2-40)+2+40;
			if(map.charAt(y1 * width + x1) == '#')
				if(map.charAt((y1 - 1) * width + x1) == ' ')
				{
					map.setCharAt((y1 - 1) * width + x1, 'd');
					count++;
				}
		}
		
		count = 0;
		while(count != 2)
		{
			int x1 = random.nextInt(width);
			int y1 = random.nextInt(height-2-50)+2+50;
			if(map.charAt(y1 * width + x1) == '#')
				if(map.charAt((y1 - 1) * width + x1) == ' ')
				{
					map.setCharAt((y1 - 1) * width + x1, 's');
					count++;
				}
		}
		
		// write to save
		String data = header + "PLAYER_WEAPON = CLAYMORE" + "\nPLAYER_HEALTH = 100" + done + map.toString();
		File file = new File("Map.dat");
		file.delete();
		try 
		{
			file.createNewFile();
			DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
			out.write(String.format("%040x", new BigInteger(1, data.getBytes())).getBytes());
			out.close();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
