package game.window;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.Timer;

import game.input.KeyInput;
import game.input.MouseInput;
import game.object.Camera;

public abstract class Renderer extends JFrame implements Runnable
{
	private static final long serialVersionUID = 1L;
	
	private static int frameCap = 300;
	private int fps = 0;
	private int fpsCount = 0;
	
	private volatile boolean running = false;
	private Canvas canvas;
	private BufferStrategy bufferStrategy;
	private Graphics2D graphics;

	private double deltaTime = 0;
	private AffineTransform perspective;
	
	public Renderer(String title, int width, int height)
	{	
		setTitle(title);
		setSize(width, height);
		setResizable(false);
		setLocationRelativeTo(null);
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				running = false;
			}
		});
	}
	
	public int getFPS()
	{
		return fps;
	}
	
	public Canvas getCanvas()
	{
		return canvas;
	}
	
	public void init()
	{
		running = true;
		new Thread(this).start();
	}
	
	public void destroy()
	{
		running = false;
	}
	
	public double getDeltaTime()
	{
		return deltaTime;
	}
	
	public void renderFromCamera(Camera camera)
	{
		perspective = new AffineTransform();
		perspective.translate(-camera.getX(), -camera.getY());
		graphics.setTransform(perspective);
	}
	
	public void startUI()
	{
		graphics.setTransform(new AffineTransform());
	}
	
	protected abstract void onStart();
	protected abstract void onRender(Graphics2D graphics); 
	protected abstract void onExit();
	
	@Override
	public void run() 
	{
		
		new Timer(1000, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				fps = fpsCount;
				fpsCount = 0;
			}
		}).start();
		onStart();
		requestFocus();
		setVisible(true);
		
		add(canvas = new Canvas());
		canvas.createBufferStrategy(3);
		canvas.addKeyListener(new KeyInput());
		MouseInput mouseInput = new MouseInput();
		canvas.addMouseListener(mouseInput);
		canvas.addMouseMotionListener(mouseInput);
		canvas.addMouseWheelListener(mouseInput);
		canvas.setFocusable(true);
		canvas.requestFocus();
		
		double currentTime = System.nanoTime() / 1000000;
		double lastTime = currentTime;
		
		long frameDelay = (long)(((double)1000 / (double)frameCap) * 1e+6);
		long currentFrameTime = System.nanoTime();
		long lastFrameTime = currentFrameTime;
		
		while(running)
		{
			currentFrameTime = System.nanoTime();
			if(currentFrameTime - lastFrameTime >= frameDelay)
			{
				bufferStrategy = canvas.getBufferStrategy();
				graphics = (Graphics2D)bufferStrategy.getDrawGraphics();
				canvas.paint(graphics);
				graphics.setColor(Color.black);
				graphics.fillRect(0, 0, getWidth(), getHeight());
				currentTime = System.nanoTime() / 1000000;
				deltaTime = currentTime - lastTime;
				onRender(graphics);
				lastTime = currentTime;
				bufferStrategy.show();
				graphics.dispose();
				lastFrameTime = currentFrameTime;
				fpsCount++;
			} 
		}
		
		onExit();
		System.exit(0);
	}
}
