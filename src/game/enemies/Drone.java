package game.enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import game.Game;

public class Drone extends Enemy
{
	private float searchSpeed = 0.2f;
	private float attackSpeed = 0.525f;
	private float moveDirection = 90.0f;
	private float acceleration = 0.0f;
	private float searchTime = 0.0f;
	private float viewDistance = 400.0f;
	private boolean attacking = false;
	private float tilt = 0.0f;
	private boolean dead = false;
	
	public Drone(int x, int y, Game game) 
	{
		super("Drone", new Color(163, 73, 164), game);
		setScale(2, 2);
		setPosition(x * 64, y * 64);
		setHp(15);
		setDamage(20);
		setRotationAxis(11, 4);
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
				if(getCurrentAnimation() != "Attack")
				{
					setAnimation("Attack");
					getAnimation().play();
				}
				acceleration += 0.001f * game.getDeltaTime();
				if(acceleration > attackSpeed)
					acceleration = attackSpeed;
				moveDirection = (float)Math.toDegrees(Math.atan2((game.player.getX() + game.player.getWidth()/2) - (getX() + 11), (game.player.getY() + game.player.getHeight()/2) - (getY() + 4)));
			}
			else
			{
				if(getCurrentAnimation() != "Search")
				{
					setAnimation("Search");
					getAnimation().play();
				}
				searchTime += game.getDeltaTime();
				
				if(searchTime > 5000)
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
			}
			
			if(moveDirection > 0.0f && moveDirection < 180.0f)
				tilt = acceleration * 100;
			else
				tilt = -acceleration * 100;
			
			move((float)Math.sin(Math.toRadians(moveDirection)) * acceleration, (float)Math.cos(Math.toRadians(moveDirection)) * acceleration);
			setRotation(tilt);
			
			if(game.player.isAttacking() && game.player.getAttackHitbox().intersects(getHitbox()))
			{
				takeDamage(game.player.getDamage());
			}
			else if(game.player.getHitbox().intersects(getHitbox()))
			{
				game.player.takeDamage(getDamage());
				takeDamage(getHp());
				dead = true;
				remove();
			}
		}
		else
		{
			if(game.map.platformCollides((int)getX(), (int)getY(), getHitbox().width, getHitbox().height) == null)
				move(0.0f, 0.5f);
		}
	}
}
