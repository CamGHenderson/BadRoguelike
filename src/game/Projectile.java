package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Projectile {
	
	private float x, y;
	private Rectangle hitbox;
	private float direction;
	private float speed;
	private int damage;
	private boolean player = false;
	private Color color;
	private Game game;
	
	public Projectile(float x, float y, float direction, float speed, int damage, boolean player, Color color, Game game)
	{
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.speed = speed;
		this.damage = damage;
		this.player = player;
		this.color = color;
		this.game = game;
		hitbox = new Rectangle((int)Math.round(x), (int)Math.round(y), 8, 8);
		game.projectiles.add(this);
	}
	
	public void render(Graphics2D graphics)
	{
		x += (float)Math.cos(Math.toRadians(direction)) * speed * game.getDeltaTime();
		y += (float)Math.sin(Math.toRadians(direction)) * speed * game.getDeltaTime();
		hitbox.x = (int)Math.round(x);
		hitbox.y = (int)Math.round(y);
		graphics.setColor(color);
		graphics.fillOval(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
		if(player)
		{
			for(int i = 0; i < game.enemies.size(); i++)
				if(game.enemies.get(i).getHitbox().intersects(hitbox))
				{
					game.enemies.get(i).takeDamage(damage);
					game.projectiles.remove(this);
				}
		}
		else
			if(game.player.getHitbox().intersects(hitbox))
			{
				game.player.takeDamage(damage);
				game.projectiles.remove(this);
			}
				
		if(game.map.platformCollides(hitbox.x, hitbox.y, hitbox.width, hitbox.height) != null)
		{
			game.projectiles.remove(this);
		}
	}
}
