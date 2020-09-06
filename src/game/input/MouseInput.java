package game.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

public class MouseInput extends MouseAdapter{
	
	private static boolean[] button = new boolean[20];
	
	private static int x, y;
	
	private static int mouseWheelPosition = 0;
	
	public MouseInput() {
		for(int i = 0; i < 20; i++)
			button[i] = false;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		for(int i = 0; i < 20; i++)
			button[e.getButton()] = true;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		for(int i = 0; i < 20; i++)
			button[e.getButton()] = false;
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		mouseWheelPosition += e.getWheelRotation();
	}
	
	public static boolean getButtonDown(int buttonCode) {
		if(button[buttonCode]) {
			return true;
		}
		return false;
	}
	
	public static Point2D getMousePosition() {
		return new Point2D.Float(x, y);
	}
	
	public static int getMouseX() {
		return x;
	}
	
	public static int getMouseY() {
		return y;
	}
	
	public static int getMouseWheelPosition() {
		return mouseWheelPosition;
	}
}
