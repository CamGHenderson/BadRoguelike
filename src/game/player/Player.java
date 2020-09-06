package game.player;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import game.Game;
import game.GameState;
import game.input.KeyInput;
import game.input.MouseInput;
import game.items.Sword;
import game.map.Platform;
import game.object.AnimatedObject;
import game.util.Loader;

public class Player extends AnimatedObject
{
	private boolean moving = false;
	private boolean attacking = false;
	private boolean attackDown = true;
	private boolean forceLeft = false;
	private boolean forceRight = false;
	
	private float speed = 0.5f;
	private int width;
	private int height;
	
	private Sword sword;
	private int handX = 0;
	private int handY = 0;
	private boolean swinging = false;
	private boolean swingHold = false;
	private boolean returning = false;
	private boolean hasSwung = false;
	
	private float gravity = 0.0f;
	private boolean onGround = false;
	private boolean jumping = false;
	private boolean jumpHold = false;
	private float jumpAcceleration = 0.0f;
	private float jumpPower = 1.0f;
	private float lastX = 0.0f;
	private float lastY = 0.0f;
	private float lastCollisionX = 0.0f;
	private float lastCollisionY = 0.0f;
	
	private boolean grappling = false;
	private boolean grapplingHold = false;
	private boolean hooked = false;
	private float hookX = 0.0f, hookY = 0.0f;
	private float grappleAngle = 0.0f;
	private boolean doneAnimation = false;
	private float distance = 0.0f;
	private float maxDistance = 1000.0f;
	private Color ropeColor = new Color(140, 107, 14);
	private BasicStroke ropeStroke = new BasicStroke(2);
	private float grappleSpeed = 2.0f;
	private boolean grappled = false;
	private BufferedImage hook;
	private AffineTransform hookTransform;
	
	private boolean pickupHold = false;
	
	private int hp = 100;
	private Rectangle hitbox;
	private Rectangle attackHitbox;
	private Platform colliding;
	
	public Player(Game game) {
		super("Player", new Color(163, 73, 164), game);
		width = getAnimation("Idle").getFrame().getWidth() * 2;
		height = getAnimation("Idle").getFrame().getHeight() * 2;
		hook = Loader.loadImage("res/Hook.png", new Color(163, 73, 164));
		setScale(2, 2);
		getAnimation("Idle").setDelay(100);
		getAnimation("Attack").setDelay(100);
		getAnimation("Fall").setDelay(100);
		getAnimation("Walk").setDelay(40);
		setAnimation("Idle");
		getAnimation().play();
		hitbox = new Rectangle(0, 0);
		attackHitbox = new Rectangle(0, 0, width - 12, height - 12);
	}
	
	public void update()
	{	
		handX = 0;
		handY = 0;
		moving = false;
		if(!attacking && !hooked || !onGround)
		{
			if(KeyInput.key[KeyEvent.VK_A] && !KeyInput.key[KeyEvent.VK_D] || forceLeft)
			{
				moving = true;
				move(-speed, 0.0f);
				if(!grappling)
					flip(true, false);
				colliding = game.map.platformCollides(Math.round(getX() + 12), Math.round(getY() + 8), 6, height - 16);					
				if(colliding != null)
					setPosition(colliding.getX() * 64 + 46, getY());
			}
			
			if(KeyInput.key[KeyEvent.VK_D] && !KeyInput.key[KeyEvent.VK_A] || forceRight)
			{
				moving = true;
				if(!grappling)
					flip(false, false);
				move(speed, 0.0f);
				colliding = game.map.platformCollides(Math.round(getX() + 44), Math.round(getY() + 8), 6, height - 16);
				if(colliding != null)
					setPosition(colliding.getX() * 64 - 46, getY());
			}
			
			if(KeyInput.key[KeyEvent.VK_SPACE])
			{
				if(!jumpHold && onGround)
				{
					jumping = true;
					jumpAcceleration = jumpPower;
					jumpHold = true;
				}
			}
			else
				jumpHold = false;
			
			if(moving)
				if(getAnimation().getCurrentFrame() > 2 && getAnimation().getCurrentFrame() < 6)
					handY-=2;
		}
		
		if((colliding = game.map.platformCollides(Math.round(getX() + 20), Math.round(getY() + height - 4), 24, 4)) != null && !jumping)
		{
			onGround = true;
			setPosition(getX(), colliding.getY() * 64 - getHeight() + 5);
			lastX = getX();
			lastY = getY();
		}
		else
			onGround = false;
		
		if(MouseInput.getButtonDown(3))
		{
			if(!grapplingHold && !attacking)
			{
				grappling = true;
				grapplingHold = true;
				hooked = false;
				distance = 0.0f;
				setAnimation("Attack");
				getAnimation().play();
				hookX = (getX() + 46 - ((getHorizontalFlip()) ? 28 : 0));
				hookY = (getY() + 36);
				if(MouseInput.getMouseX() < game.getWidth() / 2)
					flip(true, false);
				else
					flip(false, false);
				grappleAngle = (float)Math.atan2((MouseInput.getMouseY() + getY() - game.getHeight()/2 + height/2) - hookY, 
						(MouseInput.getMouseX() + getX() - game.getWidth()/2 + width/2) - hookX);
			}
		}
		else
			grapplingHold = false;
		
		if(grappled && onGround)
			grappled = false;
		
		if(grappling)
		{
			if(onGround && moving)
				grappling = false;
			else
			{
				doneAnimation = (getAnimation().getCurrentFrame() == 2);
				if(doneAnimation)
				{
					if(!hooked)
					{
						hookX += Math.cos(grappleAngle) * game.getDeltaTime() * grappleSpeed;
						hookY += Math.sin(grappleAngle) * game.getDeltaTime() * grappleSpeed;
						distance += game.getDeltaTime() * grappleSpeed;
						if(distance > maxDistance)
						{
							grappling = false;
						}
						if(game.map.platformCollides((int)Math.round(hookX), (int)Math.round(hookY), 2, 2) != null)
						{
							hooked = true;
							move(0.0f, -2);
							if(game.map.platformCollides((int)Math.round(getX() + 12), (int)Math.round(getY()), width - 12, height) != null)
							{
								move(-8.0f, 0.0f);
								if(game.map.platformCollides((int)Math.round(getX() + 12), (int)Math.round(getY()), width - 12, height) != null)
									move(16.0f, 0.0f);
							}
							grappled = true;
						}
					}
					else 
					{
						if(getY() < hookY)
						{
							grappling = false;
							hooked = false;
						} 
						else if(game.map.platformCollides((int)Math.round(getX() + 12), (int)Math.round(getY()), width - 12, height) != null)
						{
							if(game.map.platformCollides((int)Math.round(getX() + 12), (int)Math.round(getY()), width - 12, height) != null)
							{
								move(-1.0f, 0.0f);
								if(game.map.platformCollides((int)Math.round(getX() + 12), (int)Math.round(getY()), width - 12, height) != null)
									move(2.0f, 0.0f);
								if(game.map.platformCollides((int)Math.round(getX() + 12), (int)Math.round(getY()), width - 12, height) != null)
								{
									move(-1.0f, 0.0f);
									grappling = false;
									hooked = false;
								}
							}
						}
						else
							setPosition(getX() + (float)Math.cos(grappleAngle) * (float)game.getDeltaTime() * grappleSpeed,
									getY() +(float)Math.sin(grappleAngle) * (float)game.getDeltaTime() * grappleSpeed);
					}
				}
			}
		}
		
		if(game.map.platformCollides((int)Math.round(getX()), (int)Math.round(getY()), width, height) == null)
		{
			lastCollisionX = getX();
			lastCollisionY = getY();
		}
		
		if(game.map.platformCollides((int)Math.round(getX() + 32), (int)Math.round(getY()), width/2, height/2) != null &&
		   game.map.platformCollides((int)Math.round(getX() - 32), (int)Math.round(getY()), width/2, height/2) != null &&
		   game.map.platformCollides((int)Math.round(getX()), (int)Math.round(getY() + 32), width/2, height/32) != null &&
		   game.map.platformCollides((int)Math.round(getX()), (int)Math.round(getY() - 32), width/2, height/32) != null)
		{
			setPosition(lastCollisionX, lastCollisionY);
		}
			
		if(MouseInput.getButtonDown(1))
		{
			if(!attackDown && !moving && !attacking)
			{
				setAnimation("Attack");
				getAnimation().play();
				attacking = true;
				attackDown = true;
				if(grappling)
				{
					grappling = false;
					hooked = false;
					if(Math.toDegrees(grappleAngle) > -90 && Math.toDegrees(grappleAngle) < 90)
						forceRight = true;
					else 
						forceLeft = true;
				}
			}
		}
		else
			attackDown = false;
		
		if(attacking)
		{
			switch(getAnimation().getCurrentFrame())
			{
				case 1:
					handX += 6;
					break;
				case 2:
					handX += 6;
					handY -= 4;
					if(!swingHold)
						swinging = true;
					swingHold = true;
					break;
				default:
					break;
			}
		}
		
		if(swinging)
			if(sword.getRotation() < 90)
				sword.setRotation(sword.getRotation() + (float)(sword.getSpeed() * game.getDeltaTime()));
			else
			{
				if(onGround)
				{
					swinging = false;
					returning = true;
				}
				
				if(!hasSwung)
				{
					sword.onSwing();
					hasSwung = true;
				}
			}
		
		if(returning)
			if(sword.getRotation() > 5)
			{
				sword.setRotation(sword.getRotation() - (float)(sword.getSpeed()/2 * game.getDeltaTime()));
			}
			else
			{
				sword.setRotation(0.0f);
				returning = false;
				swingHold = false;
				attacking = false;
				forceRight = false;
				forceLeft = false;
				hasSwung = false;
			}
			
		if(moving && onGround)
		{
			if(getCurrentAnimation() != "Walk")
				setAnimation("Walk");
			if(!getAnimation().isPlaying())
				getAnimation().play();
		}
		
		if(getCurrentAnimation() == "Walk" && !moving)
			getAnimation().stop();
		
		if(!attacking && !grappling && !moving && onGround)
		{
			setAnimation("Idle");
			getAnimation().play();
		}
		
		if(!onGround)
		{
			if(!grappling && !attacking)
			{
				setAnimation("Fall");
				getAnimation().play();
			}
			if(!jumping)
			{
				gravity += 0.002f * game.getDeltaTime();
				if(gravity > 1.5f)
					gravity = 1.5f;
			}
		}
		else
		{
			gravity = 0.0f;
		}
		
		if(jumping)
		{
			gravity = -jumpAcceleration;
			jumpAcceleration -= 0.0025f * game.getDeltaTime();
			if(jumpAcceleration < 0)
				jumping = false;
			if((colliding = game.map.platformCollides(Math.round(getX() + 20), (int)Math.round(getY() - gravity * game.getDeltaTime()), 24, 2)) != null)
			{
				jumping = false;
				gravity = 0.0f;
				jumpAcceleration = 0.0f;
			}
		}
		
		if(hooked)
			gravity = 0.0f;
		
		for(int i = 0; i < game.swords.size(); i++)
		{
			Sword sword = game.swords.get(i);
			if(Math.sqrt((Math.pow(getX() - sword.getX() + sword.getWidth()/2, 2) +
					Math.pow(getY() - sword.getY() + sword.getHeight()/2, 2))) < 100)
			{
				if(KeyInput.key[KeyEvent.VK_E])
				{
					if(!pickupHold)
					{
						game.swords.remove(sword);
						this.sword.setPosition(sword.getX(), sword.getY());
						this.sword.setRotation(0.0f);
						game.swords.add(this.sword);
						this.sword = sword;
						pickupHold = true;
					}
				}
				else
					pickupHold = false;
			}
		}
		
		move(0.0f, gravity);
		hitbox = new Rectangle((int)Math.round(getX() + 20), (int)Math.round(getY()), width - 40, height);
		if(getHorizontalFlip())
		{
			attackHitbox.x = (int)getX() - width + 24;
			attackHitbox.y = (int)getY();
		}
		else
		{
			attackHitbox.x = (int)getX() + width - 12;
			attackHitbox.y = (int)getY();
		}
		
		if(hp != 100)
			for(int i = 0; i < game.medkits.size(); i++)
				if(hitbox.intersects(game.medkits.get(i).getHitbox()))
				{
					hp += game.medkits.get(i).getHp();
					if(hp > 100)
						hp = 100;
					game.medkits.remove(i);
				}
	}
	
	@Override
	protected void onRender(Graphics2D graphics) 
	{
		if(onGround && !grappling || attacking)
			sword.renderInHand(graphics, this);
		if(grappling && doneAnimation)
		{
			graphics.setColor(ropeColor);
			graphics.setStroke(ropeStroke);
			graphics.drawLine((int)Math.round(getX() + 46 - ((getHorizontalFlip()) ? 28 : 0)), (int)Math.round(getY() + 36), (int)Math.round(hookX), (int)Math.round(hookY));
			graphics.setStroke(new BasicStroke(1));
			hookTransform = new AffineTransform();
			hookTransform.translate(hookX - Math.cos(Math.toRadians(0)) * 12, hookY - Math.sin(Math.toRadians(0)) * 12);
			hookTransform.scale(2, 2);
			hookTransform.rotate(grappleAngle + Math.toRadians(90), 6, 0);
			graphics.drawImage(hook, hookTransform, null);
		}
	}
	
	public boolean isAttacking()
	{
		return swinging;
	}
	
	public float getLastX()
	{
		return lastX;
	}
	
	public float getLastY()
	{
		return lastY;
	}
	
	public boolean onGround()
	{
		return onGround;
	}
	
	public boolean hasGrappled()
	{
		return grappled;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void setSword(Sword sword)
	{
		this.sword = sword;
	}
	
	public Sword getSword()
	{
		return sword;
	}
	
	public int getHandX()
	{
		return handX;
	}
	
	public int getHandY()
	{
		return handY;
	}
	
	public int getDamage()
	{
		return sword.getDamage();
	}
	
	public void setHp(int hp)
	{
		this.hp = hp;
	}
	
	public void takeDamage(int damage)
	{
		hp -= damage;
		if(hp <= 0)
		{
			hp = 0;
			game.state = GameState.death;
		}
	}
	
	public int getHp()
	{
		return hp;
	}
	
	public Rectangle getHitbox()
	{
		return hitbox;
	}
	
	public Rectangle getAttackHitbox()
	{
		return attackHitbox;
	}
}
