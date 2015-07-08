package JolleeB.BachelorT.Deprecated;

import java.awt.Point;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;

import JolleeB.BachelorT.Utils.Conversion;

public class Test {
	public static int counter = 0;
	public static void main(String args[]) throws Exception{
		Random r = new Random();
		double variance =0;
		int iterations = 1000000;
		for(int i =0; i < iterations; i++){
			int[] array = new int[100];
			for(int j =0; j < array.length; j++){
				array[j] = r.nextInt(100);
			}
			int avg =0;
			int minVal =0;
			double minEnergy =9000000;
			for(int j =0; j< array.length;j++){
				double energy =0;
				for(int k =0; k < array.length; k++){
					energy+= (double)(array[j]-array[k])*(array[j]-array[k])/array.length;
				}
				if(energy < minEnergy){
					minEnergy =energy;
					minVal = j;
				}
				avg+=array[j];
			}
			avg/=array.length;
			variance += Math.abs(avg-array[minVal]);
		}
		variance /=iterations;
		System.out.println(""+variance);
	
	}	
	
	public static void test(File inputFile, int runs) throws IOException{
		BufferedImage bi = ImageIO.read(inputFile);
		int height = bi.getHeight();
		int width = bi.getWidth();
		int[] rgbInt = new int[width*height];
		byte[] rgbByte = new byte[0];
		long time = System.nanoTime();
		for(int i =0; i < runs; i++){
			for(int y = 0; y < height; y++){
				for(int x =0; x <width; x++){
					rgbInt [y*width+x]=bi.getRGB(x, y);
				}
			}
		}
		for(int i =0; i < runs; i++){
			BufferedImage bi2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			WritableRaster raster = bi2.getRaster();
	        raster.setDataElements(0, 0, width, height, rgbInt);
		}
		System.out.println("Picture size: "+width+"x"+height);
		long timeTaken = System.nanoTime() - time;
		System.out.println("Getting integer values took "+ ((double)timeTaken/(runs*width*height))+" ms");
		time = System.nanoTime();
		for(int i =0; i < runs; i++){
			rgbByte = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
		}
		for(int i =0; i < runs; i++){
			ColorModel cm = new ComponentColorModel(ColorModel.getRGBdefault().getColorSpace(), false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
	        DataBuffer dataBuf = new DataBufferByte(rgbByte, rgbByte.length);
	        WritableRaster raster = Raster.createInterleavedRaster(dataBuf, width, height,width*3, 3, new int[]{2, 1, 0}, new Point());
	        BufferedImage bi3 =new BufferedImage(cm, raster, false, null);
		}
		timeTaken = (System.nanoTime()-time)/timeTaken;
		System.out.println("Getting byte values took "+ ((double)timeTaken)+" ms");
	}
}