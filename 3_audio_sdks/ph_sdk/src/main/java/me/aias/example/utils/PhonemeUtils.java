package me.aias.example.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.text.MessageFormat;
import java.util.Map;

public class PhonemeUtils {
	// 拼音转音素映射表：420
	// Phonetic-to-phoneme mapping table: 420
	static ImmutableMap<String,String> shengyun2ph_dict = ImmutableMap.<String, String>builder()
			.put("a", "aa a")
		    .put("ai", "aa ai")
		    .put("an", "aa an")
		    .put("ang", "aa ang")
		    .put("ao", "aa ao")
		    .put("ba", "b a")
		    .put("bai", "b ai")
		    .put("ban", "b an")
		    .put("bang", "b ang")
		    .put("bao", "b ao")
		    .put("bei", "b ei")
		    .put("ben", "b en")
		    .put("beng", "b eng")
		    .put("bi", "b i")
		    .put("bian", "b ian")
		    .put("biao", "b iao")
		    .put("bie", "b ie")
		    .put("bin", "b in")
		    .put("bing", "b ing")
		    .put("bo", "b o")
		    .put("bu", "b u")
		    .put("ca", "c a")
		    .put("cai", "c ai")
		    .put("can", "c an")
		    .put("cang", "c ang")
		    .put("cao", "c ao")
		    .put("ce", "c e")
		    .put("cen", "c en")
		    .put("ceng", "c eng")
		    .put("ci", "c iy")
		    .put("cong", "c ong")
		    .put("cou", "c ou")
		    .put("cu", "c u")
		    .put("cuan", "c uan")
		    .put("cui", "c ui")
		    .put("cun", "c un")
		    .put("cuo", "c uo")
		    .put("cha", "ch a")
		    .put("chai", "ch ai")
		    .put("chan", "ch an")
		    .put("chang", "ch ang")
		    .put("chao", "ch ao")
		    .put("che", "ch e")
		    .put("chen", "ch en")
		    .put("cheng", "ch eng")
		    .put("chi", "ch ix")
		    .put("chong", "ch ong")
		    .put("chou", "ch ou")
		    .put("chu", "ch u")
		    .put("chuai", "ch uai")
		    .put("chuan", "ch uan")
		    .put("chuang", "ch uang")
		    .put("chui", "ch ui")
		    .put("chun", "ch un")
		    .put("chuo", "ch uo")
		    .put("da", "d a")
		    .put("dai", "d ai")
		    .put("dan", "d an")
		    .put("dang", "d ang")
		    .put("dao", "d ao")
		    .put("de", "d e")
		    .put("dei", "d ei")
		    .put("deng", "d eng")
		    .put("di", "d i")
		    .put("dia", "d ia")
		    .put("dian", "d ian")
		    .put("diao", "d iao")
		    .put("die", "d ie")
		    .put("ding", "d ing")
		    .put("diu", "d iu")
		    .put("dong", "d ong")
		    .put("dou", "d ou")
		    .put("du", "d u")
		    .put("duan", "d uan")
		    .put("dui", "d ui")
		    .put("dun", "d un")
		    .put("duo", "d uo")
		    .put("e", "ee e")
		    .put("ei", "ee ei")
		    .put("en", "ee en")
		    .put("er", "ee er")
		    .put("fa", "f a")
		    .put("fan", "f an")
		    .put("fang", "f ang")
		    .put("fei", "f ei")
		    .put("fen", "f en")
		    .put("feng", "f eng")
		    .put("fo", "f o")
		    .put("fou", "f ou")
		    .put("fu", "f u")
		    .put("ga", "g a")
		    .put("gai", "g ai")
		    .put("gan", "g an")
		    .put("gang", "g ang")
		    .put("gao", "g ao")
		    .put("ge", "g e")
		    .put("gei", "g ei")
		    .put("gen", "g en")
		    .put("geng", "g eng")
		    .put("gong", "g ong")
		    .put("gou", "g ou")
		    .put("gu", "g u")
		    .put("gua", "g ua")
		    .put("guai", "g uai")
		    .put("guan", "g uan")
		    .put("guang", "g uang")
		    .put("gui", "g ui")
		    .put("gun", "g un")
		    .put("guo", "g uo")
		    .put("ha", "h a")
		    .put("hai", "h ai")
		    .put("han", "h an")
		    .put("hang", "h ang")
		    .put("hao", "h ao")
		    .put("he", "h e")
		    .put("hei", "h ei")
		    .put("hen", "h en")
		    .put("heng", "h eng")
		    .put("hong", "h ong")
		    .put("hou", "h ou")
		    .put("hu", "h u")
		    .put("hua", "h ua")
		    .put("huai", "h uai")
		    .put("huan", "h uan")
		    .put("huang", "h uang")
		    .put("hui", "h ui")
		    .put("hun", "h un")
		    .put("huo", "h uo")
		    .put("yi", "ii i")
		    .put("ya", "ii ia")
		    .put("yan", "ii ian")
		    .put("yang", "ii iang")
		    .put("yao", "ii iao")
		    .put("ye", "ii ie")
		    .put("yin", "ii in")
		    .put("ying", "ii ing")
		    .put("yong", "ii iong")
		    .put("you", "ii iu")
		    .put("ji", "j i")
		    .put("jia", "j ia")
		    .put("jian", "j ian")
		    .put("jiang", "j iang")
		    .put("jiao", "j iao")
		    .put("jie", "j ie")
		    .put("jin", "j in")
		    .put("jing", "j ing")
		    .put("jiong", "j iong")
		    .put("jiu", "j iu")
		    .put("ju", "j v")
		    .put("juan", "j van")
		    .put("jue", "j ve")
		    .put("jun", "j vn")
		    .put("ka", "k a")
		    .put("kai", "k ai")
		    .put("kan", "k an")
		    .put("kang", "k ang")
		    .put("kao", "k ao")
		    .put("ke", "k e")
		    .put("ken", "k en")
		    .put("keng", "k eng")
		    .put("kong", "k ong")
		    .put("kou", "k ou")
		    .put("ku", "k u")
		    .put("kua", "k ua")
		    .put("kuai", "k uai")
		    .put("kuan", "k uan")
		    .put("kuang", "k uang")
		    .put("kui", "k ui")
		    .put("kun", "k un")
		    .put("kuo", "k uo")
		    .put("la", "l a")
		    .put("lai", "l ai")
		    .put("lan", "l an")
		    .put("lang", "l ang")
		    .put("lao", "l ao")
		    .put("le", "l e")
		    .put("lei", "l ei")
		    .put("leng", "l eng")
		    .put("li", "l i")
		    .put("lia", "l ia")
		    .put("lian", "l ian")
		    .put("liang", "l iang")
		    .put("liao", "l iao")
		    .put("lie", "l ie")
		    .put("lin", "l in")
		    .put("ling", "l ing")
		    .put("liu", "l iu")
		    .put("lo", "l o")
		    .put("long", "l ong")
		    .put("lou", "l ou")
		    .put("lu", "l u")
		    .put("luan", "l uan")
		    .put("lun", "l un")
		    .put("luo", "l uo")
		    .put("lv", "l v")
		    .put("lve", "l ve")
		    .put("ma", "m a")
		    .put("mai", "m ai")
		    .put("man", "m an")
		    .put("mang", "m ang")
		    .put("mao", "m ao")
		    .put("me", "m e")
		    .put("mei", "m ei")
		    .put("men", "m en")
		    .put("meng", "m eng")
		    .put("mi", "m i")
		    .put("mian", "m ian")
		    .put("miao", "m iao")
		    .put("mie", "m ie")
		    .put("min", "m in")
		    .put("ming", "m ing")
		    .put("miu", "m iu")
		    .put("mo", "m o")
		    .put("mou", "m ou")
		    .put("mu", "m u")
		    .put("na", "n a")
		    .put("nai", "n ai")
		    .put("nan", "n an")
		    .put("nang", "n ang")
		    .put("nao", "n ao")
		    .put("ne", "n e")
		    .put("nei", "n ei")
		    .put("nen", "n en")
		    .put("neng", "n eng")
		    .put("ni", "n i")
		    .put("nian", "n ian")
		    .put("niang", "n iang")
		    .put("niao", "n iao")
		    .put("nie", "n ie")
		    .put("nin", "n in")
		    .put("ning", "n ing")
		    .put("niu", "n iu")
		    .put("nong", "n ong")
		    .put("nu", "n u")
		    .put("nuan", "n uan")
		    .put("nuo", "n uo")
		    .put("nv", "n v")
		    .put("nve", "n ve")
		    .put("o", "oo o")
		    .put("ou", "oo ou")
		    .put("pa", "p a")
		    .put("pai", "p ai")
		    .put("pan", "p an")
		    .put("pang", "p ang")
		    .put("pao", "p ao")
		    .put("pei", "p ei")
		    .put("pen", "p en")
		    .put("peng", "p eng")
		    .put("pi", "p i")
		    .put("pian", "p ian")
		    .put("piao", "p iao")
		    .put("pie", "p ie")
		    .put("pin", "p in")
		    .put("ping", "p ing")
		    .put("po", "p o")
		    .put("pou", "p ou")
		    .put("pu", "p u")
		    .put("qi", "q i")
		    .put("qia", "q ia")
		    .put("qian", "q ian")
		    .put("qiang", "q iang")
		    .put("qiao", "q iao")
		    .put("qie", "q ie")
		    .put("qin", "q in")
		    .put("qing", "q ing")
		    .put("qiong", "q iong")
		    .put("qiu", "q iu")
		    .put("qu", "q v")
		    .put("quan", "q van")
		    .put("que", "q ve")
		    .put("qun", "q vn")
		    .put("ran", "r an")
		    .put("rang", "r ang")
		    .put("rao", "r ao")
		    .put("re", "r e")
		    .put("ren", "r en")
		    .put("reng", "r eng")
		    .put("ri", "r iz")
		    .put("rong", "r ong")
		    .put("rou", "r ou")
		    .put("ru", "r u")
		    .put("ruan", "r uan")
		    .put("rui", "r ui")
		    .put("run", "r un")
		    .put("ruo", "r uo")
		    .put("sa", "s a")
		    .put("sai", "s ai")
		    .put("san", "s an")
		    .put("sang", "s ang")
		    .put("sao", "s ao")
		    .put("se", "s e")
		    .put("sen", "s en")
		    .put("seng", "s eng")
		    .put("si", "s iy")
		    .put("song", "s ong")
		    .put("sou", "s ou")
		    .put("su", "s u")
		    .put("suan", "s uan")
		    .put("sui", "s ui")
		    .put("sun", "s un")
		    .put("suo", "s uo")
		    .put("sha", "sh a")
		    .put("shai", "sh ai")
		    .put("shan", "sh an")
		    .put("shang", "sh ang")
		    .put("shao", "sh ao")
		    .put("she", "sh e")
		    .put("shei", "sh ei")
		    .put("shen", "sh en")
		    .put("sheng", "sh eng")
		    .put("shi", "sh ix")
		    .put("shou", "sh ou")
		    .put("shu", "sh u")
		    .put("shua", "sh ua")
		    .put("shuai", "sh uai")
		    .put("shuan", "sh uan")
		    .put("shuang", "sh uang")
		    .put("shui", "sh ui")
		    .put("shun", "sh un")
		    .put("shuo", "sh uo")
		    .put("ta", "t a")
		    .put("tai", "t ai")
		    .put("tan", "t an")
		    .put("tang", "t ang")
		    .put("tao", "t ao")
		    .put("te", "t e")
		    .put("teng", "t eng")
		    .put("ti", "t i")
		    .put("tian", "t ian")
		    .put("tiao", "t iao")
		    .put("tie", "t ie")
		    .put("ting", "t ing")
		    .put("tong", "t ong")
		    .put("tou", "t ou")
		    .put("tu", "t u")
		    .put("tuan", "t uan")
		    .put("tui", "t ui")
		    .put("tun", "t un")
		    .put("tuo", "t uo")
		    .put("wu", "uu u")
		    .put("wa", "uu ua")
		    .put("wai", "uu uai")
		    .put("wan", "uu uan")
		    .put("wang", "uu uang")
		    .put("weng", "uu ueng")
		    .put("wei", "uu ui")
		    .put("wen", "uu un")
		    .put("wo", "uu uo")
		    .put("yu", "vv v")
		    .put("yuan", "vv van")
		    .put("yue", "vv ve")
		    .put("yun", "vv vn")
		    .put("xi", "x i")
		    .put("xia", "x ia")
		    .put("xian", "x ian")
		    .put("xiang", "x iang")
		    .put("xiao", "x iao")
		    .put("xie", "x ie")
		    .put("xin", "x in")
		    .put("xing", "x ing")
		    .put("xiong", "x iong")
		    .put("xiu", "x iu")
		    .put("xu", "x v")
		    .put("xuan", "x van")
		    .put("xue", "x ve")
		    .put("xun", "x vn")
		    .put("za", "z a")
		    .put("zai", "z ai")
		    .put("zan", "z an")
		    .put("zang", "z ang")
		    .put("zao", "z ao")
		    .put("ze", "z e")
		    .put("zei", "z ei")
		    .put("zen", "z en")
		    .put("zeng", "z eng")
		    .put("zi", "z iy")
		    .put("zong", "z ong")
		    .put("zou", "z ou")
		    .put("zu", "z u")
		    .put("zuan", "z uan")
		    .put("zui", "z ui")
		    .put("zun", "z un")
		    .put("zuo", "z uo")
		    .put("zha", "zh a")
		    .put("zhai", "zh ai")
		    .put("zhan", "zh an")
		    .put("zhang", "zh ang")
		    .put("zhao", "zh ao")
		    .put("zhe", "zh e")
		    .put("zhei", "zh ei")
		    .put("zhen", "zh en")
		    .put("zheng", "zh eng")
		    .put("zhi", "zh ix")
		    .put("zhong", "zh ong")
		    .put("zhou", "zh ou")
		    .put("zhu", "zh u")
		    .put("zhua", "zh ua")
		    .put("zhuai", "zh uai")
		    .put("zhuan", "zh uan")
		    .put("zhuang", "zh uang")
		    .put("zhui", "zh ui")
		    .put("zhun", "zh un")
		    .put("zhuo", "zh uo")
		    .put("cei", "c ei")
		    .put("chua", "ch ua")
		    .put("den", "d en")
		    .put("din", "d in")
		    .put("eng", "ee eng")
		    .put("ng", "ee ng")
		    .put("fiao", "f iao")
		    .put("yo", "ii o")
		    .put("kei", "k ei")
		    .put("len", "l en")
		    .put("nia", "n ia")
		    .put("nou", "n ou")
		    .put("nun", "n un")
		    .put("rua", "r ua")
		    .put("tei", "t ei")
		    .put("wong", "uu uong")
		    .put("n", "n ng")
		    .build();
	static ImmutableMap<String,String>  diao2ph_dict = ImmutableMap.<String, String>builder()
			.put("1", "1")
			.put("2", "2")
			.put("3", "3")
			.put("4", "4")
			.put("5", "5") 
			.build();	
	
	static Map<String,String> pinyin2ph_dict = Maps.newHashMap();
	static{
		
		/*for(String ksy : shengyun2ph_dict.keySet()){
			String vsy = shengyun2ph_dict.get(ksy);
			for(String kd : diao2ph_dict.keySet()){
				String vd = diao2ph_dict.get(kd); 
				pinyin2ph_dict.put(MessageFormat.format("{0}{1}", ksy,kd),MessageFormat.format("{0} {1}", vsy,vd));
			}
		} */
		/*for(String kye : pinyin2ph_dict.keySet()){
			if(kye.equals("bi1")){
				System.out.println(kye+"="+pinyin2ph_dict.get(kye));
			}
		}*/
		
		shengyun2ph_dict.forEach((ksy, vsy) -> {
			diao2ph_dict.forEach((kd, vd) -> { 
				pinyin2ph_dict.put(MessageFormat.format("{0}{1}", ksy,kd),MessageFormat.format("{0} {1}", vsy,vd));
			});
		}); 
	} 
	
	// 字母音素：26
	static String[] _alphabet = "Aa Bb Cc Dd Ee Ff Gg Hh Ii Jj Kk Ll Mm Nn Oo Pp Qq Rr Ss Tt Uu Vv Ww Xx Yy Zz".split(" ");
	// 字母：26
	static String[] _upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");
	static String[] _lower = "abcdefghijklmnopqrstuvwxyz".split("");
	static ImmutableMap<String,String> upper2ph_dict = ImmutableMap.<String, String>builder()
			.put("A", "Aa")
			.put("B", "Bb")
			.put("C", "Cc")
			.put("D", "Dd")
			.put("E", "Ee")
			.put("F", "Ff")
			.put("G", "Gg")
			.put("H", "Hh")
			.put("I", "Ii")
			.put("J", "Jj")
			.put("K", "Kk")
			.put("L", "Ll")
			.put("M", "Mm")
			.put("N", "Nn")
			.put("O", "Oo")
			.put("P", "Pp")
			.put("Q", "Qq")
			.put("R", "Rr")
			.put("S", "Ss")
			.put("T", "Tt")
			.put("U", "Uu")
			.put("V", "Vv")
			.put("W", "Ww")
			.put("X", "Xx")
			.put("Y", "Yy")
			.put("Z", "Zz") 
			.build();	
	static ImmutableMap<String,String> lower2ph_dict = ImmutableMap.<String, String>builder()
			.put("a", "Aa")
			.put("b", "Bb")
			.put("c", "Cc")
			.put("d", "Dd")
			.put("e", "Ee")
			.put("f", "Ff")
			.put("g", "Gg")
			.put("h", "Hh")
			.put("i", "Ii")
			.put("j", "Jj")
			.put("k", "Kk")
			.put("l", "Ll")
			.put("m", "Mm")
			.put("n", "Nn")
			.put("o", "Oo")
			.put("p", "Pp")
			.put("q", "Qq")
			.put("r", "Rr")
			.put("s", "Ss")
			.put("t", "Tt")
			.put("u", "Uu")
			.put("v", "Vv")
			.put("w", "Ww")
			.put("x", "Xx")
			.put("y", "Yy")
			.put("z", "Zz") 
			.build();

	static String[] _biaodian = "! ? . , ; : \" # ( )".split(" ");
	
	static ImmutableMap<String,String> biao2ph_dict = ImmutableMap.<String, String>builder()
			.put("!","!")
			.put("！", "!")
			.put("?", "?")
			.put("？", "?")
			.put(".", ".")      
			.put("。", ".")      
			.put(",", ",")       
			.put("，", ",")      
			.put("、", ",")      
			.put(";", ";")       
			.put("；", ";")      
			.put(":", ":")       
			.put("：", ":")      
			.put("\"", "\"")       
			.put("“", "\"")      
			.put("”", "\"")      
			.put("'", "\"")       
			.put("‘", "\"")      
			.put("’", "\"")      
			.put(" ", "#")       
			.put("\u3000", "#")  
			.put("\t", "#")      
			.put("(", "(")       
			.put("（", "(")      
			.put("[", "(")       
			.put("［", "(")      
			.put("{", "(")       
			.put("｛", "(")      
			.put("【", "(")      
			.put("<", "(")       
			.put("《", "(")      
			.put(")", ")")       
			.put("）", ")")      
			.put("]", ")")       
			.put("］", ")")      
			.put("}", ")")       
			.put("｝", ")")      
			.put("】", ")")      
			.put(">", ")")       
			.put("》", ")")   
			.build();
	static ImmutableMap<String,String> char2ph_dict = ImmutableMap.copyOf(upper2ph_dict).copyOf(lower2ph_dict).copyOf(biao2ph_dict);
		 
	public static void main(String[] args) {
		
		for(String a : _biaodian){
			System.out.println(a);
		}
		System.out.println(MessageFormat.format("我爱你我的祖国{0}，你是那么美丽","哈哈哈"));
	}
}
