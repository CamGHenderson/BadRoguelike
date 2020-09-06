package game.enemies;

import java.awt.Color;
import java.awt.Rectangle;

import game.Game;
import game.object.AnimatedObject;

public abstract class Enemy extends AnimatedObject
{
	private int hp;
	private int damage;
	private Rectangle hitbox;
	private Rectangle attackHitbox;
	
	public Enemy(String path, Color color, Game game) 
	{
		super("Enemies/"+path, color, game);
	}

	public void remove()
	{
		game.enemies.remove(this);
	}
	
	public int getHp() 
	{
		return hp;
	}

	public void setHp(int hp) 
	{
		this.hp = hp;
	}
	
	public void takeDamage(int damage)
	{
		hp -= damage;
		setDamage(0);
	}

	public int getDamage() 
	{
		return damage;
	}

	public void setDamage(int damage) 
	{
		this.damage = damage;
	}

	public Rectangle getHitbox() 
	{
		return hitbox;
	}

	public void setHitbox(Rectangle hitbox) 
	{
		this.hitbox = hitbox;
	}
	
	public Rectangle getAttackHitbox() 
	{
		return attackHitbox;
	}

	public void setAttackHitbox(Rectangle attackHitbox) 
	{
		this.attackHitbox = attackHitbox;
	}
}
