package imageToAscii;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageReader {
	private BufferedImage image;
	
	ImageReader(String inputImage) throws IOException{
		File inputImageFile = new File(inputImage);
		image = ImageIO.read(inputImageFile);
	}
	
	/** Returns the image's width */
	public int getWidth() {
		return image.getWidth();
	}
	
	/** Returns the image's height */
	public int getHeight() {
		return image.getHeight();
	}
	
	/** Calculates the luminance of a pixel, given its x and y coordinates.
	 * Returns a value, such that 0 <= value >= 1. Where 0 is black and 1 is white*/
	public float getLuminance(int x, int y) {
		int rgbMap = image.getRGB(x, y);
		
		int blue = (rgbMap & 0xff);
		int green = (rgbMap & 0xff00) >> 8;
		int red = (rgbMap & 0xff0000) >> 16;
		
		/* returns value s.t. value is in [0,1], bias added so we get more contrast
		 * (used for the ascii thing obiously) */
		return (float)Math.pow(((0.299f*red + 0.587f*green + 0.114f*blue)/255), 2);
	}
	
	/** Sets the image to grayscale */
	public void setGrayScale() {
		int width = image.getWidth();
		int height = image.getHeight();
		int widthIndex = 0;
		int heightIndex = 0;
		int luminanceAsRGBValue = 0;
		float luminance = 0;
		
		while((widthIndex < width) && (heightIndex < height)) {
			luminance = this.getLuminance(widthIndex, heightIndex);
			luminanceAsRGBValue = (int)(luminance * 255) 
								| ((int)(luminance * 255) << 8) 
								| ((int)(luminance * 255) << 16) 
								| (255 << 24);
			image.setRGB(widthIndex, heightIndex, luminanceAsRGBValue);
			
			
			if(widthIndex == (width-1)) {widthIndex = 0; heightIndex++;}
			else {widthIndex++;}
		}			
	}
	
	/** Computes and writes the average color of an image given a certain non-zero quantization range*/
	public void averageColorInRange(int range) {
		int width = 0;
		int height = 0;
		int widthIndex = 0;
		int heightIndex = 0;

		width = getRelativeWidth(range);
		height = getRelativeHeight(range);
		
		/* iterates through the image and determines the average color within a given range */
		while((widthIndex/range < width) && (heightIndex/range < height)) {
			int relativeXEnd = 0;
			int relativeYEnd = 0;
			int totalRedValue = 0;
			int totalBlueValue = 0;
			int totalGreenValue = 0;
			int numberOfPixels = 0; //number of pixels in a given range
			
			/* if last width bit isn't 'range' long, calculate relativeXEnd */
			if((widthIndex/range == width-1) && (width*range > image.getWidth())) 
			{relativeXEnd = image.getWidth() % range;}
			else 
			{relativeXEnd = range;}
			
			if((heightIndex/range == height-1) && (height*range > image.getHeight()))
			{relativeYEnd = image.getHeight() % range;}
			else
			{relativeYEnd = range;}
			
			numberOfPixels = relativeXEnd*relativeYEnd;
			
			/* read all the pixel's color values and compute the totalColorValues */
			for(int relativeY = 0; relativeY < relativeYEnd; relativeY++) {
				for(int relativeX = 0; relativeX < relativeXEnd; relativeX++) {
					int rgbMap = image.getRGB(relativeX + widthIndex, relativeY + heightIndex);
					int blue = (rgbMap & 0xff);
					int green = (rgbMap & 0xff00) >> 8;
					int red = (rgbMap & 0xff0000) >> 16;
								
					totalRedValue += red;
					totalBlueValue += blue;
					totalGreenValue += green;
				}
			}
			
			/* average over the totalColorValues */
				totalRedValue = totalRedValue/(numberOfPixels);
				totalBlueValue = totalBlueValue/(numberOfPixels);
				totalGreenValue = totalGreenValue/(numberOfPixels);

			int averageColor = (int)(totalBlueValue) 
							 | ((int)(totalGreenValue) << 8) 
							 | ((int)(totalRedValue) << 16) 
							 | (0xff << 24);
			
			/* write new color to range*range pixels */
			for(int relativeY = 0; relativeY < relativeYEnd; relativeY++) {
				for(int relativeX = 0; relativeX < relativeXEnd; relativeX++) {
					image.setRGB(relativeX + widthIndex, relativeY + heightIndex, averageColor);
				}
			}
			
			if((widthIndex + range)/range == width) {widthIndex = 0; heightIndex += range;}
			else {widthIndex += range;}
		}
	}
	
	/** Renders the BufferedImage into a specified format */
	public void renderImage(String outputFilePath, String fileExtension) throws IOException {
		File outputImage = new File(outputFilePath);
		ImageIO.write(image, fileExtension, outputImage);
	}
	
	/** returns the width of the image with respect to the quantization range */
	public int getRelativeWidth(int range) {
		if(image.getWidth() % range != 0) 
		{return (int)(image.getWidth()/range) + 1;}
		else 
		{return (int)(image.getWidth()/range);}
	}
	
	/** returns the height of the image with respect to the quantization range */
	public int getRelativeHeight(int range) {
		if(image.getHeight() % range != 0) 
		{return (int)(image.getHeight()/range) + 1;}
		else 
		{return (int)(image.getHeight()/range);}
	}
}
