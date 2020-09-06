package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import game.enemies.Boss;
import game.enemies.Drone;
import game.enemies.Enemy;
import game.enemies.Sentry;
import game.items.Medkit;
import game.items.Sword;
import game.map.Map;
import game.map.Platform;
import game.object.Camera;
import game.player.HealthBar;
import game.player.Player;
import game.screens.DeathScreen;
import game.screens.Title;
import game.util.Loader;
import game.window.Renderer;

public class Game extends Renderer
{
	private static final long serialVersionUID = 1L;
	
	public GameState state;
	public Title title;
	public DeathScreen deathScreen;
	public Map map;
	public Camera camera;
	public Player player;
	public HealthBar bar;
	public ArrayList<Sword> swords;
	public ArrayList<Projectile> projectiles;
	public ArrayList<Enemy> enemies;
	public ArrayList<Medkit> medkits;
	
	private Font font = new Font("Courier", Font.BOLD, 20);
	private boolean save = false;
	
	private Color color = new Color(0, 57, 94);
	
	public Game()
	{
		super("Roguelike Game", 1280, 720);
		init();
	}
	
	public void gameStart()
	{
		camera = new Camera(this);
		player = new Player(this);
		swords = new ArrayList<Sword>();
		bar = new HealthBar();
		projectiles = new ArrayList<Projectile>();
		enemies = new ArrayList<Enemy>();
		medkits = new ArrayList<Medkit>();
		Random random = new Random();
		if(!new File("Map.dat").exists())
			Map.generateMap();
		map = Loader.loadMap("Map.dat", new Color(163, 73, 164), this);
		int amount = random.nextInt(50) + 100;
		int x = 0, y = 0;
		int count = 0;
		while(count != amount)
		{
			x = random.nextInt(550);
			y = random.nextInt(100-2)+2;
			if(map.platformCollides(x*64, y*64, 1, 1) != null &&
			   map.platformCollides((x+1)*64, y*64, 1, 1) != null && 
			   map.platformCollides((x-1)*64, y*64, 1, 1) != null)
			{
				if(map.platformCollides(x*64, (y-1)*64, 1, 1) == null &&
				   map.platformCollides((x+1)*64, (y-1)*64, 1, 1) == null && 
				   map.platformCollides((x-1)*64, (y-1)*64, 1, 1) == null)
				{
					medkits.add(new Medkit(x, y));
					count++;
				}
			}
		}
		
		amount = random.nextInt(50) + 100;
		count = 0;
		while(count != amount)
		{
			x = random.nextInt(550);
			y = random.nextInt(100-11)+11;
			if(x < player.getX()/64-40 || x > player.getX()/64+40)
				if(map.platformCollides(x*64, y*64, 1, 1) != null &&
				   map.platformCollides((x+1)*64, y*64, 1, 1) != null && 
				   map.platformCollides((x-1)*64, y*64, 1, 1) != null)
				{
					if(map.platformCollides(x*64, (y-1)*64, 1, 1) == null &&
					   map.platformCollides((x+1)*64, (y-1)*64, 1, 1) == null && 
					   map.platformCollides((x-1)*64, (y-1)*64, 1, 1) == null)
					{
						enemies.add(new Sentry(x, y,this));
						count++;
					}
				}
		}
		
		
		amount = random.nextInt(200) + 300;
		count = 0;
		boolean vaild;
		while(count != amount)
		{
			x = random.nextInt(550);
			y = random.nextInt(100-11)+11;
			vaild = true;
			
			if(x < player.getX()/64-40 || x > player.getX()/64+40)
				vaild = true;
			else
				vaild = false;
			
			if(vaild)
				for(int x1 = x-3; x1 < x+3; x1++)
					if(map.platformCollides(x1*64, y*64, 1, 1) != null)
					{
						vaild = false;
						break;
					}
			
			if(vaild)
			{
				enemies.add(new Drone(x, y-1, this));
				count++;	
			}
		}
		
		count = 0;
		while(true)
		{
			x = random.nextInt(550);
			y = random.nextInt(100-11)+11;
			vaild = true;
			if(x < player.getX()/64-20 || x > player.getX()/64+20)
				vaild = true;
			else
				vaild = false;
			for(int x1 = x-10; x1 < x+10; x1++)
				for(int y1 = y-10; y1 < y+10; y1++)
				{
					if(map.platformCollides(x1*64, y1*64, 1, 1) != null)
					{
						vaild = false;
						break;
					}
				}
			if(vaild)
			{
				enemies.add(new Boss(x, y, this));
				break;
			}
		}
		player.setPosition(map.getSpawnX() * 64, map.getSpawnY() * 64 - player.getHeight()/4);
	}
	
	public void gameRender(Graphics2D graphics)
	{	graphics.setColor(color);
		graphics.fillRect(0, 0, getWidth(), getHeight());
		player.update();
		camera.setPosition(player.getX() - (getWidth() / 2 - player.getWidth() / 2),
				player.getY() - (getHeight() / 2 - player.getHeight() / 2));
		renderFromCamera(camera);
		for(Platform platform : map.getPlatforms())
			platform.render(graphics);
		player.render(graphics);
		for(int i = 0; i < projectiles.size(); i++)
			projectiles.get(i).render(graphics);
		for(int i = 0; i < enemies.size(); i++)
			enemies.get(i).render(graphics);
		for(int i = 0; i < medkits.size(); i++)
			medkits.get(i).render(graphics);;
		for(int i = 0; i < swords.size(); i++)
			swords.get(i).renderIcon(graphics);
		startUI();
		bar.render(graphics, player.getHp());
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.setFont(font);
		graphics.drawString(player.getSword().getName(), 15, 60);
		graphics.drawString("FPS: " + getFPS(), 1150, 20);
	}
	
	@Override
	protected void onStart() 
	{	
		state = GameState.title;
		title = new Title(this);
		deathScreen = new DeathScreen(this);
	}

	@Override
	protected void onRender(Graphics2D graphics) 
	{
		switch(state)
		{
		case title:
			title.render(graphics);
			save = false;
			break;
		case death:
			if(!save)
			{
				new File("Map.dat").delete();
				map = null;
				save = true;
			}
			deathScreen.render(graphics);
			break;
		case game:
			gameRender(graphics);
			break;
		}
	}

	@Override
	protected void onExit() 
	{
		if(map != null)
			map.save("Map.dat");
	}
	
	public static void main(String[] args)
	{
		new Game();
	}
}
