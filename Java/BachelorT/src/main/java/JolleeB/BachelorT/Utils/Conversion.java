package JolleeB.BachelorT.Utils;

public class Conversion {


	public static int byteToInt(byte in){
		int out = in<0?in+255:in;
		return out;
	}
	
	public static float byteToFloat(byte in){
		float out = in<0?in+255:in;
		return out;
	}
	
	public static int[] rgbToHsv(int r, int g, int b){
		int[] hsv = new int[3];
		double rD = r; 
		double gD = g; 
		double bD = b; 
		rD/=255;
		gD/=255;
		bD/=255;
		double min = rD<gD?rD:gD;
		min = min<bD?min:bD;
		double max = rD>gD?rD:gD;
		max = max>bD?max:bD;
		double delta = (max-min);
		
		
		double h =0;
		if(delta ==0) h =0;
		if(max == rD) h = 60*(((gD-bD)/delta)%6);
		if(max == gD) h = 60*(((bD-rD)/delta)+2);
		if(max == bD) h = 60*(((rD-gD)/delta)+4);
		
		double s =0;
		if(max ==0)s=0;
		else s = delta/max;
		s*=100;
		
		double v = max;
		v*=100;
		
		hsv = new int[]{(int)h,(int)s,(int)v};	
		return hsv;
	}
	
	public static int[] hsvToRgb(int h, int s, int v){
		int[] rgb = new int[3];
		double sD = (double)s/100;
		double vD = (double)v/100;
		double c = vD*sD;
		double sdh = (h/60)-Math.floor(h/60)/2;
		double x = c* (1-Math.abs(sdh-1));
		double m = vD-c;
		double rD=0;
		double gD =0;
		double bD=0;
		if(h>=0 && h<60){
			rD =c;
			gD =x;
			bD =0;
		}
		if(h>=60 && h<120){
			rD =x;
			gD =c;
			bD =0;
		}
		if(h>=120 && h<180){
			rD =0;
			gD =c;
			bD =x;
		}
		if(h>=180 && h<240){
			rD =0;
			gD =x;
			bD =c;
		}
		if(h>=240 && h<300){
			rD =x;
			gD =0;
			bD =c;
		}
		if(h>=300 && h<360){
			rD =c;
			gD =0;
			bD =x;
		}
		rgb[0] = (int)((rD+m)*255);
		rgb[1] = (int)((gD+m)*255);
		rgb[2] = (int)((bD+m)*255);
		return rgb;
	}
}
