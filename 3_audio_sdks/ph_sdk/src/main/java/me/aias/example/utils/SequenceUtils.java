package me.aias.example.utils;

import com.github.houbb.pinyin.constant.enums.PinyinStyleEnum;
import com.github.houbb.pinyin.util.PinyinHelper;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rnkrsoft.bopomofo4j.Bopomofo4j;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SequenceUtils {
	// 分隔英文字母
	static Pattern _en_re = Pattern.compile("([a-zA-Z]+)");
	 
	
	static Map<String,Integer> ph2id_dict = Maps.newHashMap(); 
	static Map<Integer,String> id2ph_dict =  Maps.newHashMap();
	static{
		int size = SymbolUtils.symbol_chinese.length;
		for(int i=0;i<size;i++){
			ph2id_dict.put(SymbolUtils.symbol_chinese[i], i);
			id2ph_dict.put(i, SymbolUtils.symbol_chinese[i]);
		}
	}
	 
	public static List<Integer> text2sequence(String text){
		   
	   /* 	文本转为ID序列。
	    :param text:
	    :return:*/
		List<String> phs = text2phoneme(text);
		List<Integer> seq = phoneme2sequence(phs);
		return seq; 
	}

	public static List<String> text2phoneme(String text){
	    
	  /*  文本转为音素，用中文音素方案。
	    中文转为拼音，按照清华大学方案转为音素，分为辅音、元音、音调。
	    英文全部大写，转为字母读音。
	    英文非全部大写，转为英文读音。
	    标点映射为音素。
	    :param text: str,正则化后的文本。
	    :return: list,音素列表*/
	    
	    text = normalizeChinese(text);
	    text = normalizeEnglish(text);
	    //System.out.println(text);
	    String pys = text2pinyin(text);
	    List<String> phs = pinyin2phoneme(pys);
	    phs = changeDiao(phs);
	    return phs ; 
	}
	
	public static List<Integer> phoneme2sequence(List<String> src){
		List<Integer> out = Lists.newArrayList();
	    for(String w : src){
	        if( ph2id_dict.containsKey(w)){
	        	out.add(ph2id_dict.get(w));
	        } 
	    }
	    return out;
	}

	
	public static List<String> changeDiao(List<String> src){
	   /*
	    	拼音变声调，连续上声声调的把前一个上声变为阳平。
	    :param src: list,音素列表
	    :return: list,变调后的音素列表*/
	     
	    int flag = -5;
	    List<String> out = Lists.newArrayList();
	    Collections.reverse(src);
	    int size = src.size();
	    for(int i=0;i<size;i++){
	    	String w = src.get(i);
	    	if(w.equals("3")){
	    		if(i - flag ==4){
	    			 out.add("2");
	    		}else{
	    			flag = i;
	    	        out.add(w);
	    		}
	    	}else{
	    		 out.add(w);
	    	}
	    } 
	    Collections.reverse(out);
	    return out;
	}




	public static String text2pinyin(String text){
		Bopomofo4j.local();//启用本地模式（也就是禁用沙盒）
		return  PinyinHelper.toPinyin(text, PinyinStyleEnum.NUM_LAST, " ");
		//return Bopomofo4j.pinyin(text,1, false, false," ").replaceAll("0", "5");
	}
	
	
	
	static String normalizeChinese(String text){
	    text = ConvertUtils.quan2ban(text);
	    text = ConvertUtils.fan2jian(text);
	    text = NumberUtils.convertNumber(text);
	    return text;
	}

	static String normalizeEnglish(String text){
		Matcher matcher =_en_re.matcher(text); 
	    LinkedList<Integer> postion = new LinkedList();
	    while(matcher.find()){   
	    	postion.add(matcher.start());
	    	postion.add(matcher.end()); 
	    } 
	    if(postion.size() == 0){
	    	return text;
	    }
	    List<String> parts = Lists.newArrayList();
	    parts.add(text.substring(0, postion.getFirst())); 
	    int size = postion.size()-1; 
	    for(int i=0;i<size;i++){ 
	    	parts.add(text.substring(postion.get(i),postion.get(i+1)));
	    }
	    parts.add(text.substring(postion.getLast()));  
	    LinkedList<String> out = new LinkedList();
	    for(String part : parts){
	    	out.add(part.toLowerCase());
	    } 
	    return Joiner.on("").join(out);
	}
	public static List<String>  pinyin2phoneme(String src){  
	    String[] srcs = src.split(" "); 
	    List<String> out = Lists.newArrayList();
	    for(String py : srcs){
	        List<String> phs = Lists.newArrayList();
	       
	        if(PhonemeUtils.pinyin2ph_dict.containsKey(py)){
	            String[] ph = PhonemeUtils.pinyin2ph_dict.get(py).split(" ");
	            List<String> list = new ArrayList<>(ph.length);
	            Collections.addAll(list, ph);
	            phs.addAll(list);
	        }else{
	        	String[] pys = py.split("");
	            for(String w : pys){
	            	List<String> ph = py_errors(w);
	                phs.addAll(ph);
	            }
	        }
	        phs.add(SymbolUtils._chain);  // 一个字符对应一个chain符号
	        out.addAll(phs);
	    }
	    out.add(SymbolUtils._eos);
	    out.add(SymbolUtils._pad);
	    return out;
	}
	
	static List<String> py_errors(String text){
		List<String> out = Lists.newArrayList();
		String[] texts = text.split(""); 
        for(String p : texts){
            if(PhonemeUtils.char2ph_dict.containsKey(p)){
                out.add(PhonemeUtils.char2ph_dict.get(p));
            }
        }
        return out;
	}
	
	public static void main(String[] args) {
		System.out.println(normalizeEnglish("我hello,I love you 我是你"));
		System.out.println(text2pinyin("这是实力很牛逼,"));
		System.out.println(text2phoneme("这是实力很牛逼,"));
		System.out.println(text2sequence("这是实力很牛逼,"));
		//System.out.println(MessageFormat.format("{0}","1"));
	}
	
}
