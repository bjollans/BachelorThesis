package JolleeB.BachelorT.Algorithmen;

import JolleeB.BachelorT.Utils.Conversion;

public class MRFOptimizer {

	public float[] optimizeRandomField(byte[] img, float[] field, int width, int height,int runs){
		if(field.length != width*height) throw new IllegalArgumentException();
		float[] fieldOut = field.clone();
		for(int i =0; i < runs; i++){
			
			//Neues Hilfsfeld erstellen und drüberlaufen
			float[] newField = new float[field.length];
			for(int y =0; y < height; y++){
				for(int x =0; x < width; x++){
					int pos = y*width+x;
					
					//Filterkernel initialisieren
					float distanceSum =0;
					float probabilitiesFactorDistance =0;
					int smallestDiff = 50*50;
					for(int lFactor =-1; lFactor<=1; lFactor+=1){
						for(int kFactor =-1; kFactor<=1;kFactor+=1){
							
							//Position im Kernel berrechnen
							int l = lFactor*3;
							int k = kFactor*3;
							int newY = y+l;
							int newX = x+k;
							if(newY <0 || newY >=height)newY-=l;
							if(newX <0 || newX >=width )newX-=k;
							int newPos = newY*width+newX;
							
							//Alle Nachbarn in die Rechnung mit einbeziehen
							if(newPos != pos){
								int diff = (Conversion.byteToInt(img[pos])-Conversion.byteToInt(img[newPos]));
								diff *= diff;
								if(diff <=smallestDiff) diff =smallestDiff+1;
								distanceSum += 1f/diff;
								probabilitiesFactorDistance += fieldOut[newPos]*(1f/diff);
							}
						}
					}
					//Rechnung mit gewichtetem Durchschnitt abschließen und in Hilfsfeld packen
					newField[pos] = probabilitiesFactorDistance/distanceSum;
				}
			}
			//Hilfsfeld in Outputfeld kopieren für nächste Iteration oder Ausgabe
			fieldOut = newField;
		}
		return fieldOut;
	}
	
	public float[] optimizeRandomFieldHardAssign(byte[] img, float[] field, int width, int height,int runs){
		if(field.length != width*height) throw new IllegalArgumentException();
		float[] fieldOut = field.clone();
		for(int i =0; i < runs; i++){
			
			//Neues Hilfsfeld erstellen und drüberlaufen
			float[] newField = new float[field.length];
			for(int y =0; y < height; y++){
				for(int x =0; x < width; x++){
					int pos = y*width+x;
					
					//Filterkernel initialisieren
					float distanceSum =0;
					float probabilitiesFactorDistance =0;
					int smallestDiff = 50*50;
					for(int lFactor =-1; lFactor<=1; lFactor+=1){
						for(int kFactor =-1; kFactor<=1;kFactor+=1){
							
							//Position im Kernel berrechnen
							int l = lFactor*3;
							int k = kFactor*3;
							int newY = y+l;
							int newX = x+k;
							if(newY <0 || newY >=height)newY-=l;
							if(newX <0 || newX >=width )newX-=k;
							int newPos = newY*width+newX;
							
							//Alle Nachbarn in die Rechnung mit einbeziehen
							if(newPos != pos){
								int diff = (Conversion.byteToInt(img[pos])-Conversion.byteToInt(img[newPos]));
								diff *= diff;
								if(diff <=smallestDiff) diff =smallestDiff+1;
								distanceSum += (1f/diff);
								probabilitiesFactorDistance += fieldOut[newPos]*((1/diff));
								
							}
						}
					}
					//Rechnung mit gewichtetem Durchschnitt abschließen und in Hilfsfeld packen
					newField[pos] = probabilitiesFactorDistance/distanceSum;
					if(newField[pos]>0.5){
						System.out.println("kajshd");
					}
					newField[pos] = (float)Math.floor(newField[pos]+0.5);
//					if(newField[pos] >1) newField[pos] =1;
				}
			}
			//Hilfsfeld in Outputfeld kopieren für nächste Iteration oder Ausgabe
			fieldOut = newField.clone();
		}
		return fieldOut;
	}
}
