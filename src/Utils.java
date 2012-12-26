import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;


public class Utils {
	public static void writeToFile(String fileName, int index, byte[] content){
		assert content.length%4 == 0;
		assert index >= 0;
		File file = new File(fileName); // Creates the file if not exists
		System.out.println("-----------CREATED FILE:"+fileName+"----------");
		try {
			RandomAccessFile RAF = new RandomAccessFile(file, "rw");
			RAF.seek(index);
			RAF.write(content);
			RAF.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writePeaksToFile(String fileName, int peaksOffset, byte[] peaks){
		assert peaks.length%4 == 0;
		assert peaksOffset >= 0;
		int numOfPeaks = peaks.length/4;
		
		String peakType;
		if(fileName.contains("mx")){
			peakType = "max";
		}else{
			peakType = "min";
		}
		System.out.println("-----------NUM OF PEAKS:"+numOfPeaks+"---------");
		File file = new File(fileName);
		try {
			FileWriter fw = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fw);
			for(int i = 0; i < numOfPeaks; i++){
				byte[] b = Arrays.copyOfRange(peaks, 4*i, 4*i+4); 
				int globalIndex = bytaToInt(b) + peaksOffset;
				System.out.println("-----------WRITING "+peakType+" PEAK:"+globalIndex+"----------");
				out.append(Integer.toString(globalIndex));
				out.append(',');
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static float[] readEDAByteFile(File file){	
		byte[] bArr;
		try {
			bArr = FileUtils.readFileToByteArray(file);
			float[] fArr = toFloatArr(bArr);
			return fArr;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * Converts double to float and places each float as 4-byte sequences in a byte array
	 * */
	public static byte[] toBytaArr(double[] arr){
		byte[] bArr = new byte[arr.length*4];
		for(int j =0; j<arr.length; j++){
			int bits = Float.floatToIntBits((float) arr[j]);
			byte[] b = intToByta(bits);
			bArr[4*j] = b[0];
			bArr[4*j+1] = b[1];
			bArr[4*j+2] = b[2];
			bArr[4*j+3] = b[3];
		}		
		return bArr;
	}
	
	public static byte[] toBytaArr(float[] arr){
		byte[] bArr = new byte[arr.length*4];
		for(int j =0; j<arr.length; j++){
			int bits = Float.floatToIntBits(arr[j]);
			byte[] b = intToByta(bits);
			bArr[4*j] = b[0];
			bArr[4*j+1] = b[1];
			bArr[4*j+2] = b[2];
			bArr[4*j+3] = b[3];
		}		
		return bArr;
	}
	
	public static byte[] toBytaArr(Integer[] arr){
		byte[] bArr = new byte[arr.length*4];
		for(int j =0; j<arr.length; j++){
			byte[] b = intToByta(arr[j]);
			bArr[4*j] = b[0];
			bArr[4*j+1] = b[1];
			bArr[4*j+2] = b[2];
			bArr[4*j+3] = b[3];
		}		
		return bArr;
	}
	
	/*
	 * Creates a byte array of size 4 representing this integer. The most-significant
	 * bits are in the first byte of the array
	 * */
	public static byte[] intToByta(int i){
		byte[] b = new byte[4];
		b[0] = (byte)((i >> 24) & 0xff);
		b[1] = (byte)((i >> 16) & 0xff);
		b[2] = (byte)((i >> 8) & 0xff);
		b[3] = (byte)(i & 0xff);
		return b;
	}
	
	public static float[] toFloatArr(byte[] byta){
		assert byta.length%4 == 0;
		int numOfVals = byta.length/4;
		float[] fArr = new float[numOfVals];
		for(int i = 0; i < numOfVals; i++){
			byte[] b = Arrays.copyOfRange(byta, 4*i, 4*i+4);
			fArr[i] = bytaToFloat(b); 
		}
		return fArr;
	}
	
	public static double[] toDoubla(float[] fArr){
		double[] dArr = new double[fArr.length];
		for(int i = 0; i < fArr.length; i++){
			dArr[i] = (double) fArr[i];
		}
		return dArr;
	}
	public static float bytaToFloat(byte[] byta){
		assert byta.length == 4;
		int asInt = (byta[3] & 0xFF) | ((byta[2] & 0xFF) << 8) | 
				((byta[1] & 0xFF) << 16) | ((byta[0] & 0xFF) << 24);
		return Float.intBitsToFloat(asInt);
	}
	
	public static int bytaToInt(byte[] byta){
		int asInt = (byta[3] & 0xFF) | ((byta[2] & 0xFF) << 8) | 
				((byta[1] & 0xFF) << 16) | ((byta[0] & 0xFF) << 24);
		return asInt;
	}
}
