package game.enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

import game.Game;
import game.Projectile;
import game.util.Loader;

public class Sentry extends Enemy
{
	private static Color bg = new Color(163, 73, 164);
	private static Random random = new Random();
	private float viewDistance = 650.0f;
	private BufferedImage turret;
	private AffineTransform turretTransform;
	private float direction = random.nextInt(180)-90;
	private float playerDirection = 0.0f;
	private boolean dead = false;
	private boolean attackHold = false;
	private boolean lookLeft = false;
	private boolean attacking = false;
	private float shootTime = 0.0f;
	
	public Sentry(int x, int y, Game game) 
	{
		super("Sentry/Base", bg, game);
		turret = Loader.loadImage("res/Enemies/Sentry/Turret.png", bg);	
		setScale(2, 2);
		setPosition(x * 64, y * 64 - getAnimation().getFrame().getHeight() * 2 + 6);
		setHitbox(new Rectangle((int)Math.round(getX()), (int)Math.round(getY()), getAnimation().getFrame().getWidth() * 2, getAnimation().getFrame().getHeight() * 2));
		setHp(100);
	}

	@Override
	protected void onRender(Graphics2D graphics) 
	{
		if(game.player.getAttackHitbox().intersects(getHitbox()) && game.player.isAttacking())
		{
			if(!attackHold)
			{
				if(!game.player.onGround())
				{
					if(game.player.hasGrappled())
						takeDamage((int)(game.player.getDamage() * 2.5));
					else
						takeDamage((int)(game.player.getDamage() * 1.3));
				}
				else
					takeDamage((int)(game.player.getDamage()));
				attackHold = true;
			}
		}
		else
			attackHold = false;
		
		dead = (getHp() <= 0);
		
		if(!dead)
		{
			if(Math.sqrt((Math.pow(getX() + 16 * 2 - game.player.getX() + game.player.getWidth()/2, 2) +
					Math.pow(getY() + 20 * 2 - game.player.getY() + game.player.getHeight()/2, 2))) < viewDistance)
				attacking = true;
			else
			{
				attacking = false;
				shootTime = 0.0f;
			}
			
			if(attacking)
			{
				setAnimation("Attack");
				playerDirection = (float)-Math.toDegrees(Math.atan2((game.player.getX() + game.player.getWidth()/2) - (getX() + 32 * 2), (game.player.getY() + game.player.getHeight()/2) - (getY() + 20 * 2)))-90;
				if(direction+180 > playerDirection+180)
					direction -= 0.05 * game.getDeltaTime();
				else if(direction+180 < playerDirection+180)
					direction += 0.05 * game.getDeltaTime();
				shootTime += game.getDeltaTime();
				if(shootTime >= 1500)
				{
					if(game.player.getHitbox().intersects(getHitbox()) || getHitbox().intersects(game.player.getAttackHitbox()))
						game.player.takeDamage(30);
					new Projectile((int)Math.round((Math.sin(-Math.toRadians(direction + 90)) * 35 * 2) + (getX() + 37)),
						(int)Math.round((Math.cos(-Math.toRadians(direction + 90)) * 35 * 2) + (getY() + 18)),
						direction + 180, 1.0f, 30, false, Color.yellow, game);
					shootTime = 0;
				}
			}
			else
			{
				setAnimation("Search");
				if(lookLeft)
				{
					direction -= 0.01f * game.getDeltaTime();
					if(direction <= -20.0f)
						lookLeft = false;
				}
				else
				{
					direction += 0.01f * game.getDeltaTime();
					if(direction >= 200.0f)
						lookLeft = true;
				}
			}
		}
		
		turretTransform = new AffineTransform();
		turretTransform.translate(getX() - 40, getY() + 10);
		turretTransform.scale(2, 2);
		turretTransform.rotate(Math.toRadians(direction), 37, 4);
		graphics.drawImage(turret, turretTransform, null);
		graphics.setColor(Color.red);
		graphics.fillRect((int)getX() + getHitbox().width/2 - 25, (int)getY() - 20, 50, 5);
		if(!dead)
		{
			graphics.setColor(Color.green);
			graphics.fillRect((int)getX() + getHitbox().width/2 - 25, (int)getY() - 20, getHp()/2, 5);
		}
		graphics.setColor(Color.white);
		graphics.drawRect((int)getX() + getHitbox().width/2 - 25, (int)getY() - 20, 50, 5);
	}
	
	public float getTurretRotation()
	{
		return direction;
	}
}