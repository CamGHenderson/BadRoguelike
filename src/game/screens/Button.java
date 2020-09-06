package game.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import game.input.MouseInput;

public class Button {
	
	private static Font font = new Font("Courier", Font.PLAIN, 30);
	private String label;
	private int x, y, width, height;
	private boolean down = false;
	
	public Button(String label, int x, int y)
	{
		this.label = label;
		this.x = x;
		this.y = y;
	}
	
	public void render(Graphics2D graphics)
	{
		graphics.setColor(Color.white);
		graphics.setFont(font);
		if(width == 0)
		{
			width = graphics.getFontMetrics(font).stringWidth(label);
			height = 30;
		}
		graphics.drawRect(x - width / 2 - 40, y, width + 80, height + 10);
		graphics.drawString(label, x - width / 2, y + height);
		
		down = false;
		if(MouseInput.getMouseX() > x - width / 2 - 40 && MouseInput.getMouseY() > y && MouseInput.getMouseX() < x - width / 2 + width + 40 && MouseInput.getMouseY() < y + height + 10)
		{
			graphics.fillRect(x - width / 2 - 40, y, width + 80, height + 10);
			if(MouseInput.getButtonDown(1))
				down = true;
		}
	}
	
	public boolean getDown()
	{
		return down;
	}
}
