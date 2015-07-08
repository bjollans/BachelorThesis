package JolleeB.BachelorT.UI;

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
import java.util.ArrayList;

import javax.imageio.ImageIO;

import JolleeB.BachelorT.Algorithmen.AlgorithmusController;
import JolleeB.BachelorT.Algorithmen.GMMAnalyzer;
import JolleeB.BachelorT.Algorithmen.MOGAnalyzer;
import JolleeB.BachelorT.DeEnCoding.DynamicFrameGetter;
import JolleeB.BachelorT.DeEnCoding.DynamicVideoCreator;
import JolleeB.BachelorT.Deprecated.FrameGetter;
import JolleeB.BachelorT.Deprecated.OpticalFlow;
import JolleeB.BachelorT.Deprecated.Stabilizer;
import JolleeB.BachelorT.Deprecated.VideoCreator;
import JolleeB.BachelorT.Utils.ImageFormatingUtils;
import JolleeB.BachelorT.Utils.ImageUtils;
import JolleeB.BachelorT.Utils.VideoUtils;


public class App 
{
    public static void main( String[] args )
    {
//    	long time = System.currentTimeMillis();
//    	function4();
//    	time = System.currentTimeMillis() - time;
//    	
//    	System.out.println("Time taken: "+ (time/1000)+"."+(time-(time/1000))+" seconds");
    	String inputFile = "../../Videos/Video_003";
    	String fileType = ".avi";
    	String outPutSuffix = "Test2";
    	String inputFileString = inputFile+fileType;
    	String outputFileString = inputFile+outPutSuffix+fileType;
    	try {
			AlgorithmusController controller = new AlgorithmusController(inputFileString, outputFileString);
			controller.start("spatialgmm", 9);
		} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
			System.out.println("FAILED");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			System.out.println("FAILED");
			e.printStackTrace();
		} catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
			// TODO Auto-generated catch block
			System.out.println("FAILED");
			e.printStackTrace();
		}
        return;
    }
    
    private static void function1(){
    	//TODO do motion estimation with block matching
    	System.out.println("Initializing..");
    	FrameGetter fg = new FrameGetter();
    	VideoCreator vc = new VideoCreator();
    	String fileStringInput = "../../Videos/SaigonSmall.mp4";
    	double fps = fg.getFPS(fileStringInput);
    	ArrayList<BufferedImage> bufImList = fg.getFramesAsBufferedImages(0,fileStringInput,0, 100);
    	OpticalFlow optf = new OpticalFlow();
//    	bufImList = optf.simpleMotionDetection(bufImList);
    	bufImList = optf.computeGlobalFlow(bufImList);
//    	bufImList = MovementDetetor.detectWColorsIntOptimized(bufImList);
    	vc.createMP4( bufImList.get(0).getWidth(),bufImList.get(0).getHeight(), fps, bufImList, "../../Videos/AutosTest.mp4");
//    	vc.encode(bufImList,"../../Videos/testXuggler.mp4");
    	System.out.println("done");
    }
    
    private static void function2(){
    	String inputFileString = "../../Videos/Saigon";
    	String fileType = ".mp4";
    	String outPutSuffix = "Test";
    	File inputFile = new File(inputFileString+fileType);
    	File outputFile = new File(inputFileString+outPutSuffix+fileType);
		Stabilizer stabilizer = new Stabilizer();
    	try{
    		int frameAmount = 50;
	    	DynamicFrameGetter fg = new DynamicFrameGetter(inputFile, frameAmount);
	    	DynamicVideoCreator vc = new DynamicVideoCreator(outputFile.getAbsolutePath(), fg.getWidth(), fg.getHeight(), fg.getFramerate());
			System.out.println("inited");
	    	vc.start();
	    	System.out.println("started");
	    	while(!vc.isStarted()){System.out.println("waiting...");}
	    	int totalFrames = fg.getFrameAmount();
	    	int frameCounter =0;
	    	int width = fg.getWidth();
	    	int height = fg.getHeight();
	    	byte[] frameLast = new byte[width*3*height];
	    	while(fg.hasFrames()){
	    		frameCounter++;
	    		int percentage = (100*frameCounter/totalFrames);
	    		System.out.println(""+percentage+"%");
	    		
	    		byte[][] framesIn = fg.getNextFrames();
	    		if(percentage >10) {
	    			byte[] frame2 = framesIn[framesIn.length-2];
	    			ColorModel cm2 = new ComponentColorModel(ColorModel.getRGBdefault().getColorSpace(), false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
	    			DataBuffer dataBuf2 = new DataBufferByte(frame2, frame2.length);
	    			WritableRaster raster2 = Raster.createInterleavedRaster(dataBuf2, width, height,width*3, 3, new int[]{2, 1, 0}, new Point());
	    			BufferedImage bi2 =new BufferedImage(cm2, raster2, false, null);
	    			ImageIO.write(bi2, "png", new File("1.png"));
	    			
	    			byte[] frame = framesIn[framesIn.length-1];
	    			ColorModel cm3 = new ComponentColorModel(ColorModel.getRGBdefault().getColorSpace(), false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
	    			DataBuffer dataBuf3 = new DataBufferByte(frame, frame.length);
	    			WritableRaster raster3 = Raster.createInterleavedRaster(dataBuf3, width, height,width*3, 3, new int[]{2, 1, 0}, new Point());
	    			BufferedImage bi3 =new BufferedImage(cm3, raster3, false, null);
	    			ImageIO.write(bi3, "png", new File("2.png"));
	    			
	    			byte[] frame3 = ImageUtils.subtractGrayScaleImgByteToByte(frame, frame2, 0);
	    			frame3 = ImageFormatingUtils.makeGrayScaleImage(frame3);
	    			frame3 = ImageFormatingUtils.makeColorScale(frame3);
	    			frame3 = ImageUtils.binaryThresholding(frame3, 100);
	    			ColorModel cm = new ComponentColorModel(ColorModel.getRGBdefault().getColorSpace(), false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
	    			DataBuffer dataBuf = new DataBufferByte(frame3, frame3.length);
	    			WritableRaster raster = Raster.createInterleavedRaster(dataBuf, width, height,width*3, 3, new int[]{2, 1, 0}, new Point());
	    			BufferedImage bi =new BufferedImage(cm, raster, false, null);
	    			ImageIO.write(bi, "png", new File("3.png"));
	    			break;
	    		}
//	    		byte[][] frames = VideoUtils.makeGrayScale(framesIn);
//	    		byte[] frame = VideoUtils.preAndPostProcessedDerivative(frames, 10, VideoUtils.PREPROC_AVG, 0, width, height);
////	    		frame = ImageUtils.dilateBinary(frame, width, height, 20);
////	    		frame = ImageUtils.eroseBinary(frame, width, height, 10);
//	    		frame = ImageFormatingUtils.makeColorScale(frame);
//	    		frameLast = ImageUtils.simpleCombineReverseMask(frameLast, framesIn[framesIn.length-1], frame, width*3, height);
//	    		vc.encodeBytes(frameLast);
	    	}
	    	vc.stop();
    	}
    	catch(Exception e){
    		System.out.println(e.getClass().toString());
    		e.printStackTrace();
    	}
    }
    
    private static void function3(){
    	String inputFileString = "../../Videos/Saigon";
    	String fileType = ".mp4";
    	String outPutSuffix = "Test";
    	File inputFile = new File(inputFileString+fileType);
    	File outputFile = new File(inputFileString+outPutSuffix+fileType);
    	try{
    		int frameAmount = 2;
	    	DynamicFrameGetter fg = new DynamicFrameGetter(inputFile, frameAmount);
	    	DynamicVideoCreator vc = new DynamicVideoCreator(outputFile.getAbsolutePath(), fg.getWidth(), fg.getHeight(), fg.getFramerate());
			System.out.println("inited");
	    	vc.start();
	    	System.out.println("started");
	    	while(!vc.isStarted()){System.out.println("waiting...");}
	    	int totalFrames = fg.getFrameAmount();
	    	int frameCounter =0;
	    	int width = fg.getWidth();
	    	int height = fg.getHeight();
	    	GMMAnalyzer gaussianAnalyzer = new GMMAnalyzer(3*width* height);
	    	while(fg.hasFrames()){
	    		byte[][] framesIn = fg.getNextFrames();
	    		byte[] frame = framesIn[framesIn.length-1];
	    		if(frameCounter ==0){
	    			gaussianAnalyzer = new GMMAnalyzer(3*width* height);
	    		}
	    		frame = gaussianAnalyzer.convertImage(frame);
	    		frameCounter++;
	    		int percentage = (100*frameCounter/totalFrames);
	    		System.out.println(""+percentage+"%");
	    		
	    		
	    		vc.encodeBytes(frame);
//	    		if(percentage >10) break;
	    	}
	    	vc.stop();
    	}
    	catch(Exception e){
    		System.out.println(e.getClass().toString());
    		e.printStackTrace();
    	}
    }
    private static void function4(){
    	String inputFileString = "../../Videos/FischeSmall";
    	String fileType = ".mp4";
    	String outPutSuffix = "Test";
    	File inputFile = new File(inputFileString+fileType);
    	File outputFile = new File(inputFileString+outPutSuffix+fileType);
    	try{
    		int frameAmount = 2;
    		DynamicFrameGetter fg = new DynamicFrameGetter(inputFile, frameAmount);
    		DynamicVideoCreator vc = new DynamicVideoCreator(outputFile.getAbsolutePath(), fg.getWidth(), fg.getHeight(), fg.getFramerate());
    		System.out.println("inited");
    		vc.start();
    		System.out.println("started");
    		while(!vc.isStarted()){System.out.println("waiting...");}
    		int totalFrames = fg.getFrameAmount();
    		int frameCounter =0;
    		int width = fg.getWidth();
    		int height = fg.getHeight();
    		MOGAnalyzer gaussianAnalyzer = new MOGAnalyzer(3*width*height,4);
    		while(fg.hasFrames()){
    			byte[][] framesIn = fg.getNextFrames();
    			byte[] frame = framesIn[framesIn.length-1];
    			frame = gaussianAnalyzer.convertImage(frame);
    			frameCounter++;
    			int percentage = (100*frameCounter/totalFrames);
    			System.out.println(""+percentage+"%");
    			
    			
    			vc.encodeBytes(frame);
//	    		if(percentage >3) break;
    		}
    		vc.stop();
    	}
    	catch(Exception e){
    		System.out.println(e.getClass().toString());
    		e.printStackTrace();
    	}
    }
    
    public static void function5(){
    	String inputFileString = "../../Videos/Video_003";
    	String fileType = ".avi";
    	String outPutSuffix = "Test";
    	File inputFile = new File(inputFileString+fileType);
    	File outputFile = new File(inputFileString+outPutSuffix+fileType);
		Stabilizer stabilizer = new Stabilizer();
    	try{
    		int frameAmount = 50;
	    	DynamicFrameGetter fg = new DynamicFrameGetter(inputFile, frameAmount);
	    	DynamicVideoCreator vc = new DynamicVideoCreator(outputFile.getAbsolutePath(), fg.getWidth(), fg.getHeight(), fg.getFramerate());
			System.out.println("inited");
	    	vc.start();
	    	System.out.println("started");
	    	while(!vc.isStarted()){System.out.println("waiting...");}
	    	int totalFrames = fg.getFrameAmount();

    		java.util.Random r = new java.util.Random();
    		int Low = 0;
    		int High = fg.getHeight()*fg.getWidth()-1;
    		int R = r.nextInt(High-Low) + Low;
	    	while(fg.hasFrames()){
	    		
	    		byte[][] framesIn = fg.getNextFrames();
	    		byte[] frame = VideoUtils.localMedian(framesIn);
	    		System.out.println(""+frame[R]);
	    		vc.encodeBytes(frame);
	    	}
	    	vc.stop();
    	}
    	catch(Exception e){
    		System.out.println(e.getClass().toString());
    		e.printStackTrace();
    	}
    }
}