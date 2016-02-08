package main;

import render.RenderConstants;
import render.RenderObject;
import render.Renderer;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RenderEngine {
	private Graphics graphics;
	private Renderer renderer;
	private static BufferedWriter bFR;
	public RenderEngine(JPanel j, int refreshRate, File logfile) throws IOException
	{
		graphics = j.getGraphics();
		renderer = new Renderer(j,refreshRate,RenderConstants.guessDelay);
		Thread t = new Thread(renderer);
		bFR = new BufferedWriter(new FileWriter(logfile));
		t.start();
		RenderEngine.log("Engine started");
	}
	public Graphics getGraphics()
	{
		return graphics;
	}
	public void addObject(RenderObject r)
	{		
		renderer.addToRenderList(r);
	}
	public static void log(String s)
	{
		try {
			bFR.write(s);
			bFR.newLine();;
			bFR.flush();
		} catch (IOException e) {
//			TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
