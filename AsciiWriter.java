package imageToAscii;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class AsciiWriter {
	private ImageReader reader;
	private FileWriter writer;
	private int rangeOfPixellation;
	private static HashMap<Float, Character> luminanceAsciiMap;
	private static Float[] luminanceThresholds = 
		{0f, 1/69f, 2/69f, 3/69f, 4/69f, 5/69f, 6/69f, 7/69f, 8/69f, 9/69f, 10/69f, 11/69f, 12/69f,
		 13/69f, 14/69f, 15/69f, 16/69f, 17/69f, 18/69f, 19/69f, 20/69f, 21/69f, 22/69f, 23/69f, 24/69f, 
		 25/69f, 26/69f, 27/69f, 28/69f, 29/69f, 30/69f, 31/69f, 32/69f, 33/69f, 34/69f, 35/69f, 36/69f, 
		 37/69f, 38/69f, 39/69f, 40/69f, 41/69f, 42/69f, 43/69f, 44/69f, 45/69f, 46/69f, 47/69f, 48/69f, 
		 49/69f, 50/69f, 51/69f, 52/69f, 53/69f, 54/69f, 55/69f, 56/69f, 57/69f, 58/69f, 59/69f, 60/69f,
		 61/69f, 62/69f, 63/69f, 64/69f, 65/69f, 66/69f, 67/69f, 68/69f, 1f
		};
	
	/* from dark to bright: $@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\|()1{}[]?-_+~<>i!lIc;:,"^`. (stored the other way around) */
	private static String asciiMap = " .`^\",:;cIl!i><~+_-?][}{1)(|\\/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$";
	
	static{
		luminanceAsciiMap = new HashMap<Float, Character>();
		int i = 0;
		for(Float threshold : luminanceThresholds) {
			luminanceAsciiMap.put(threshold, (Character)asciiMap.charAt(i));
			i++;
		}
	}
	
	/** Upon construction the inputImage will be grayscaled and pixellated to the given degree. */
	AsciiWriter(ImageReader reader, int rangeOfPixellation, String outputFilePath) throws IOException{
		this.reader = reader;
		this.rangeOfPixellation = rangeOfPixellation;
		writer = new FileWriter(outputFilePath);
		reader.setGrayScale();
		reader.averageColorInRange(rangeOfPixellation);
	}
	
	/** returns the width of the image with respect to the specified rangeOfPixellation */
	public int getRelativeWidth() {
		return reader.getRelativeWidth(rangeOfPixellation);
	}
	
	/** returns the height of the image with respect to the specified rangeOfPixellation */
	public int getRelativeHeight() {
		return reader.getRelativeHeight(rangeOfPixellation);
	}
	
	/** Determines and writes to the output file the best ascii character for the given brightness */
	public void determineAndWriteAsciiCharacter(int x, int y) throws IOException {
		float luminance = reader.getLuminance(x, y);
		
		for(int index = 69; index > -1; index--) {
			if(luminance >= luminanceThresholds[index]) {		
				for(int i = 0; i < 2; i++) {
					writer.write(luminanceAsciiMap.get(luminanceThresholds[index]));
				}
				return;
			}
		}
	}
	
	/** Writes a newLine to the outputfile*/
	public void writeln() throws IOException {
		writer.write('\n');
	}
	
	/** Closes the fileWriter */
	public void close() throws IOException {
		writer.close();
	}
}
