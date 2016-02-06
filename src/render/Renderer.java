package render;

import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JPanel;

import main.GameEngine;
//import javax.swing.Timer;

public class Renderer implements Runnable{
	private Graphics graphics;
	private ArrayList<RenderObject> renderList;
	private long frameRate;
	private long lastTime =  System.currentTimeMillis();
	private JPanel panel;
	private int[] lastFrameRates = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	private int average;
	private int steadyRate;
	private int frameOn;
	private long lastTime2;
	private long delay;
//	private long lastNanoTime;
//	private long nanoDelay;
//	private long realNanoDelay;
	private boolean calculate = false;
	public Renderer(JPanel j,int refreshRate,int startDelay) throws IOException {
		graphics = j.getGraphics();
		graphics.setPaintMode();
		panel = j;
		renderList = new ArrayList<RenderObject>(0);
		frameOn = 0;
//		nanoDelay = 0;
		delay = startDelay;
		lastTime2 = System.currentTimeMillis();
	}
	public void render(RenderObject rO)
	{
		graphics.drawImage(rO.getBufferedImage(),rO.getPosX(),rO.getPosY(),null);
	}
	public void renderFrameRate()
	{
		graphics.drawString(Integer.toString(steadyRate), 0, 12);
	}
	public void addToRenderList(RenderObject rO)
	{
		renderList.add(rO);
		GameEngine.log("object added to renderlist");
	}
	public int calculateFrameRate(int fRate)
	{
//		GameEngine.log(fRate);
		for(int i = 0; i<lastFrameRates.length-1; i++)
		{
		lastFrameRates[i] = lastFrameRates[i+1];
		}
		lastFrameRates[lastFrameRates.length-1] = fRate;
		average = 0;
		for(int i = 0; i<lastFrameRates.length; i++)
		{
			average = average + lastFrameRates[i];
		}
		
		return average/lastFrameRates.length;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
//		if(calculate)
//		{
//			nanoDelay = 1000000000-(System.nanoTime() - lastNanoTime);
//			delay = 0;
//			while(nanoDelay>999999)
//			{
//				delay++;
//				nanoDelay = nanoDelay - 1000000;
//				GameEngine.log("delay " + nanoDelay);
//			}
//			delay = delay/RenderConstants.REFRESH_RATE;
//			nanoDelay = nanoDelay/RenderConstants.REFRESH_RATE;
//			GameEngine.log("uncompensated nano delay " + (System.nanoTime()-lastNanoTime));
//			GameEngine.log("nano delay "+nanoDelay);
//			GameEngine.log("compensatedDelay "+ delay);
//			GameEngine.log("nano reading " + System.nanoTime());
//			GameEngine.log("last time " + lastNanoTime);
//			GameEngine.log("frame rate "+frameRate);
//			calculate = false;
//		}

		while(frameOn<RenderConstants.REFRESH_RATE)
		{
		steadyRate = calculateFrameRate((int) frameRate);
		panel.update(graphics);
		for(int i = 0;i<renderList.size();i++)
		{
			render(renderList.get(i));
		}
		renderFrameRate();
		try
		{
		frameRate =  1000/((System.currentTimeMillis() - lastTime));
		}
		catch(ArithmeticException e)
		{
			e.printStackTrace();
		}
		lastTime = System.currentTimeMillis();
		frameOn++;
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
		}
		if(calculate)
		{
		if(System.currentTimeMillis()-lastTime2<=1000)
			{
				delay = calculateDelay((System.currentTimeMillis()-lastTime2));
				calculate = false;
				System.out.println("delay "+((System.currentTimeMillis()-lastTime2)));
				System.out.println("compensatedDelay "+ delay);
			}
			else
			{
				delay--;
				calculate = false;
				System.out.println("delay "+((int) (System.currentTimeMillis()-lastTime2)));
				System.out.println("compensatedDelay "+ delay);
			}
		}
		if((System.currentTimeMillis()-lastTime2)>=1000)
		{
			
			GameEngine.log("Frames " + frameOn);
			lastTime2 = System.currentTimeMillis();
			frameOn = 0;
			calculate = true;
			//nano
//			lastNanoTime = System.nanoTime();
		}
		}
	}
	public long calculateDelay(long extraTime)
	{
		return extraTime/RenderConstants.REFRESH_RATE;
	}

}
