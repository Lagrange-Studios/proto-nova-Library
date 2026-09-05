package gUI;

import java.util.ArrayList;

import javax.swing.JWindow;

public class GUI {
	//X and Y ints
	public static int[] getPixlesSizeFromScaleScreenSize(double xScale, double yScale, int screenWidth, int screenHeight) {
		double x;
		double y;
		
		x = screenWidth * xScale;
		y = screenHeight * yScale;
		
		return new int[] {(int)x,(int)y};
	}
	
	public static int[] getPixelsPositionFromOrigin(
	        int xPosition,
	        int yPosition,
	        double objectWidth,
	        double objectHeight,
	        double[] origin) {

	    double xOffset = objectWidth * origin[0];
	    double yOffset = objectHeight * origin[1];

	    int x = (int) Math.round(xPosition - xOffset);
	    int y = (int) Math.round(yPosition - yOffset);

	    return new int[] {x, y};
	}
	
	public static int[] getPixlesPositionFromScaleScreenSize(double xScale, double yScale, int screenWidth, int screenHeight) {
		double x;
		double y;
		
		x = screenWidth * xScale;
		y = screenHeight * yScale;
		
		return new int[] {(int)x,(int)y};
	}
}
