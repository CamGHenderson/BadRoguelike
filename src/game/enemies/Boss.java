package game.enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import game.Game;
import game.Projectile;
import game.util.Loader;

public class Boss extends Enemy{

	private float searchSpeed = 0.2f;
	private float moveDirection = 90.0f;
	private float acceleration = 0.0f;
	private float searchTime = 0.0f;
	private float viewDistance = 800.0f;
	private boolean attacking = false;
	private float tilt = 0.0f;
	private boolean dead = false;
	private float direction = 0.0f;
	private float chargeTime = 0.0f;
	private float shootTime = 0.0f;
	private int burstCount = 0;
	private boolean attackHold = false;
	private BufferedImage turret;
	private AffineTransform turretTransform;
	
	public Boss(int x, int y, Game game) 
	{
		super("Boss", new Color(163, 73, 164), game);
		turret = Loader.loadImage("res/Enemies/Boss/Turret.png", new Color(163, 73, 164));
		setScale(2, 2);
		setPosition(x * 64, y * 64);
		setHp(1000);
		setRotationAxis(61/2, 23/2);
	}

	@Override
	protected void onRender(Graphics2D graphics) 
	{
		setHitbox(new Rectangle((int)Math.round(getX()), (int)Math.round(getY() - 10), 23 * 2, 9 * 2 + 5));
		setAttackHitbox(getHitbox());
		
		if(getHp() <= 0)
			dead = true;
		
		if(!dead)
		{
			if(attacking)
			{
				direction = (float)-Math.toDegrees(Math.atan2((game.player.getX() + game.player.getWidth()/2) - (getX() + 50 * 2), (game.player.getY() + game.player.getHeight()/2) - (getY() + 20 * 2)))-90;
				shootTime += game.getDeltaTime();
				chargeTime += game.getDeltaTime();
				if(chargeTime >= 3000)
				{
					if(shootTime >= 50)
					{
						new Projectile((int)Math.round((Math.sin(-Math.toRadians(direction + 90)) * 35 * 2) + (getX() + 60)),
								(int)Math.round((Math.cos(-Math.toRadians(direction + 90)) * 35 * 2) + (getY() + 18)),
								direction + 180, 1.0f, 5, false, Color.yellow, game);
						burstCount++;
						shootTime = 0;
					}
					
					if(burstCount == 3)
					{
						burstCount = 0;
						chargeTime = 0;
					}
				}
			}

			searchTime += game.getDeltaTime();
			
			if(searchTime > 2000)
			{
				if(moveDirection == 270)
					moveDirection = 90;
				else
					moveDirection = 270;
				acceleration = -0.2f;
				searchTime = 0;
			}
			acceleration += 0.001f * game.getDeltaTime();
			if(acceleration > searchSpeed)
				acceleration = searchSpeed;
			
			if(Math.sqrt((Math.pow(getX() + 23 - game.player.getX() + game.player.getWidth()/2, 2) +
					Math.pow(getY() + 9 - game.player.getY() + game.player.getHeight()/2, 2))) < viewDistance)
				attacking = true;
			else
				attacking = false;
			
			if(moveDirection > 0.0f && moveDirection < 180.0f)
				tilt = acceleration * 100;
			else
				tilt = -acceleration * 100;
			
			move((float)Math.sin(Math.toRadians(moveDirection)) * acceleration, (float)Math.cos(Math.toRadians(moveDirection)) * acceleration);
			setRotation(tilt);
			
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
		}
		else
		{
			if(game.map.platformCollides((int)getX(), (int)getY(), getHitbox().width, getHitbox().height) == null)
				move(0.0f, 0.5f);
		}
		turretTransform = new AffineTransform();
		turretTransform.translate(getX()-10, getY()-5);
		turretTransform.scale(2, 2);
		turretTransform.rotate(Math.toRadians(direction), 35, 11);
		graphics.drawImage(turret, turretTransform, null);
		graphics.setColor(Color.red);
		graphics.fillRect((int)getX() + (getHitbox().width/2 - 12), (int)getY() - 30, 100, 5);
		if(!dead)
		{
			graphics.setColor(Color.green);
			graphics.fillRect((int)getX() + (getHitbox().width/2 - 12), (int)getY() - 30, getHp()/10, 5);
		}
		graphics.setColor(Color.white);
		graphics.drawRect((int)getX() + (getHitbox().width/2 - 12), (int)getY() - 30, 100, 5);
	}

}
