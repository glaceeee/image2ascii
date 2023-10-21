package imageToAscii;

import java.io.IOException;

public class Main {
	
	public static void image2Ascii(String inputImagePath, String outputFilePath, int rangeOfPixellation) throws IOException {
		ImageReader reader = new ImageReader(inputImagePath);
		AsciiWriter writer = new AsciiWriter(reader, rangeOfPixellation, outputFilePath);
		int width = writer.getRelativeWidth();
		int height = writer.getRelativeHeight();
		int widthIndex = 0;
		int heightIndex = 0;
		
		while((widthIndex/rangeOfPixellation < width) && (heightIndex/rangeOfPixellation < height)) {
			writer.determineAndWriteAsciiCharacter(widthIndex, heightIndex);
			if((widthIndex + rangeOfPixellation)/rangeOfPixellation == width) {
				widthIndex = 0; 
				heightIndex += rangeOfPixellation;
				writer.writeln();
			}
			else {widthIndex += rangeOfPixellation;}
		}
		
		writer.close();
	}

	public static void main(String[] args) throws IOException {
		image2Ascii("E:\\monalisa.jpg", "E:\\testImageOutput.txt", 1);
	}
}
