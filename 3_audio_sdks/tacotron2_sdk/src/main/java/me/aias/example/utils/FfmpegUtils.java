package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.aspectj.util.FileUtil;
import org.bytedeco.javacpp.Loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 使用ffmpeg 将文件mp3转为wav文件 
 * 
 * 仿python的audioread
 * 
 */
public class FfmpegUtils {
	
	static String likeStr = "subtitle";
	public static int samplerate = 0;
	public static int channels = 0;
	public static float duration = 0.0f;
	
	public static void main(String[] args) throws Exception {
		NDArray  and = __audioread_load("src/test/resources/voice/biaobei-009502.mp3",
				0,DataType.FLOAT32);
		System.out.println(and.toDebugString(1000000000, 1000, 1000, 1000));
	}
	
	public static NDArray load_wav_to_torch(String path, int sr_force) throws Exception {
		NDArray audio_norm = read(path,sr_force);
		return audio_norm;
	}
	
	
	
	public static NDArray read(String path, int sr_force) throws Exception {
		NDArray  wav = __audioread_load(path,
				0,DataType.FLOAT32);  
		wav = wav.mul(0.9).div(NDArrays.maximum(wav.abs().max(),0.01)); 
		return wav;
	}
	
	
	
	public static Queue<byte[]> audio_open(String path, int offset, DataType dtype) throws InterruptedException, IOException {
		 
		String ffmpeg = Loader.load(org.bytedeco.ffmpeg.ffmpeg.class);
		ProcessBuilder pb1 = new ProcessBuilder(ffmpeg, "-i",
				 path); 
		 
		BufferedReader br = new BufferedReader(new InputStreamReader(pb1.start().getErrorStream(),"UTF-8"));
		List<String> out_parts = Lists.newArrayList();
		String ch = "";
		while((ch=br.readLine())!= null){ 
			 out_parts.add(ch.toLowerCase()); 
             _parse_info(Joiner.on("").join(out_parts)); 
		} 
		 
		 ProcessBuilder pb = new ProcessBuilder(ffmpeg, "-i", path, "-f","s16le","-");
		 InputStream input = pb.start().getInputStream();
		 
		 byte[] all = FileUtil.readAsByteArray(input);
		 int len = all.length;
		 int base = 4096;
		 int loop = len / base;
		 int copylen = base; 
		 Queue<byte[]> queue = new LinkedList<byte[]>();
		 int yu = len % base;
		 if(yu > 0){
			 for(int i=0;i<loop;i++ ){
				 if(copylen < len){
					 queue.add(Arrays.copyOfRange(all, i*base, copylen));
				 }
				 copylen += base;
			 }
		 }
		 queue.add(Arrays.copyOfRange(all, len-yu, len));
	     input.close(); 
	     return queue;
	}
	
	public static NDArray __audioread_load(String path, int offset, DataType dtype) throws Exception {
		NDList y = new NDList();
		Queue<byte[]> queue = audio_open(path,offset,dtype);
		
		int sr_native = samplerate;
		int n_channels = channels;
		int s_start = Math.round(sr_native * offset) * n_channels;
        int s_end = 0;
		if (duration == 0.0){
            s_end = Integer.MAX_VALUE;
        }else{
            s_end = s_start + (Math.round(sr_native * duration) * n_channels);
        }
		int  n = 0 ;
		
		NDManager manager = NDManager.newBaseManager(Device.cpu());
	     for(byte[] que: queue){ 
	    	 NDArray frame = buf_to_float(que,manager); 
	    	 int n_prev = n;
	    	 n = n + (int) frame.size();
	         if( n < s_start){
	                //# offset is after the current frame
	               // # keep reading
	                continue;
	         }
             if( s_end < n_prev){
                // we're off the end.  stop reading
                break;
             }
             if( s_end < n){
                 // the end is in this frame.  crop.
            	 frame = frame.get(":"+(s_end - n_prev)); 
             }
             if( n_prev <= s_start && s_start <= n){
                 // beginning is in this frame 
                 frame = frame.get((s_start - n_prev)+":"); 
             }
             //System.out.println(frame.toDebugString(1000000000, 1000, 1000, 1000));
             y.add(frame); 
	     }
	     NDArray yy = null;
	     if(y.size() > 0){
	    	 yy = NDArrays.concat(y); 
	     }
	     
	     yy.setName(sr_native+""); 
		return yy;
	}
	
	static float scale = (float)1.0 / Float.valueOf(1 << ((8 * 2) - 1));
	static int n_bytes=2;
	
	private static NDArray buf_to_float(byte[] frame, NDManager manager){
		
		//System.out.println(Arrays.toString(frame));
		//System.out.println(HexUtil.encodeHex(frame));
		int size = frame.length / n_bytes;
		int[]  framei = new int[size];
		for(int i=0;i<size;i++){
			framei[i] = IntegerConversion.convertTwoBytesToInt1(frame[2*i],frame[2*i+1]);
		} 
		NDArray ans = manager.create(framei).toType(DataType.FLOAT32,false).mul(scale); 
		 
		return ans;
	}
    public static void _parse_info(String s){
        /* Given relevant data from the ffmpeg output, set audio
        parameter fields on this object.*/
    	Pattern re = Pattern.compile("(\\d+) hz");
        // Sample rate.
    	Matcher match =re.matcher(s);
        if(match.find()){
            samplerate = Integer.valueOf(match.group(1));
        }else{
            samplerate = 0;
        }
        // Channel count.
        Pattern rec = Pattern.compile("hz, ([^,]+),");
        match =rec.matcher(s);
        if(match.find()){
            String mode = match.group(1);
            if (mode == "stereo"){
                channels = 2;
            }else{
            	Pattern rec1 = Pattern.compile("(\\d+)\\.?(\\d)?");
            	Matcher cmatch =rec1.matcher(mode);
                if(cmatch.find()){
                    channels = Stream.of(cmatch.group().split("\\.")).mapToInt(t -> Integer.valueOf(t)).sum();
                }else{
                    channels = 1;
                }
            }
        }else{
            channels = 0;
        }
        // Duration.
        Pattern rec2 = Pattern.compile("duration: (\\d+):(\\d+):(\\d+)\\.(\\d)");
        match =rec2.matcher(s);  
        if(match.find()){
        	String index = match.group();
        	index = index.substring(10,index.length());
        	String[] dur = index.split(":");
        	String[] dur1 = dur[2].split("\\.");
        	duration =  Float.valueOf(dur[0]) * 60 * 60 + Float.valueOf(dur[1]) * 60 +  Float.valueOf(dur1[0]) +  Float.valueOf(dur1[1]) / 10;
             
        } 
    }
}
