# image2ascii
simple image to ascii program in java. made this in a weekend, nothing fancy i know but it was really fun to make.
- Main.java like always this one executes everything. You can choose one parameter here which determines how big the range of pixellation is. In other words if you set rangeOfPixellation to x then imageReader will determine the average luminance of every square of x^2 pixels in the image and create one ascii value for that.
  also dont worry about making the value too big. the code inside imageReader makes sure that we essentially just "cut-off" the square once its range exceeds the image, so instead of a square we get a non-square rectangle. i know my explaination is wonky but the code documents it too.
- ImageReader does all the image analysis work. It does this quantization i talked about above, it takes care of reading the image, determines the grayscale values, determines the luminance (which is scaled with a random function btw you can just change it if you dont like the way ive weiged the luminance for difference grey scale values)
- asciiWriter just get the input of imageReader and then according to the string i put in gives certain luminance values certain ascii values and writes those to the output file.

if my explaination was yet again thoroughly terrible do feel free to contact me if you really gaf.
