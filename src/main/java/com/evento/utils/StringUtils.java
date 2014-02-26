package com.evento.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.mozilla.universalchardet.UniversalDetector;


public class StringUtils {

	public static String capitalize(String text) {

		if(text==null || text.length()==0) return "";

		//elimina espaços (inicio, fim e "  ")
		String res = text.trim().replaceAll(".\\s{2,}.", " ");

		if(res.contains(" ")) {

			String[] words = res.split("\\s");

			StringBuilder sb = new StringBuilder();

			for(int i=0, j=words.length; i<j; i++) {

				if (words[i].length() > 1) {
					sb.append(words[i].substring(0,1).toUpperCase())
					.append(words[i].substring(1).toLowerCase());

					if(i<j-1) {
						sb.append(" ");
					}
				}
			}
			res = sb.toString();

		} else {
			res = res.substring(0,1).toUpperCase() + res.substring(1).toLowerCase();
		}

		return res;
	}

	public static String removeBlankSpacesInsideString(String s){
		char[] sArray = s.toCharArray();
		StringBuilder sb = new StringBuilder();
		for(Character c : sArray){
			if(c.toString().trim()!=""){
				sb.append(c);
			}
		}

		return sb.toString();
	}

	public static String removeAccents(String s) {
		StringBuilder sb = new StringBuilder(s.length());

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			char cl = Character.toLowerCase(c);

			if ((cl == 'ã') || (cl == 'á') || (cl == 'â') || (cl == 'à') || (cl == 'ä') || (cl == 'ª')) {
				sb.append(Character.isLowerCase(c) ? 'a' : 'A') ; continue ;
			}
			if ((cl == 'é') || (cl == 'è') || (cl == 'ê') || (cl == 'ë')) {
				sb.append(Character.isLowerCase(c) ? 'e' : 'E') ; continue ;
			}
			if ((cl == 'í') || (cl == 'ì') || (cl == 'î') || (cl == 'ï')) {
				sb.append(Character.isLowerCase(c) ? 'i' : 'I') ; continue ;
			}
			if ((cl == 'ó') || (cl == 'ò') || (cl == 'ô') || (cl == 'ö') || (cl == 'õ') || (cl == 'º')) {
				sb.append(Character.isLowerCase(c) ? 'o' : 'O') ; continue ;
			}
			if ((cl == 'ú') || (cl == 'ù') || (cl == 'ü') || (cl == 'û')) {
				sb.append(Character.isLowerCase(c) ? 'u' : 'U') ; continue ;
			}
			if (cl == 'ç') {
				sb.append(Character.isLowerCase(c) ? 'c' : 'C') ; continue ;
			}
			if (cl == 'ñ') {
				sb.append(Character.isLowerCase(c) ? 'n' : 'N') ; continue ;
			}

			sb.append(c);
		}

		return sb.toString();
	}

	public static String fixEncoding(String text){
		try{
			if(text != null && "UTF-8".equals(StringUtils.getEncode(text))){
				return URLDecoder.decode( URLEncoder.encode(text, "ISO-8859-1"), "UTF-8");
			}
		} catch(UnsupportedEncodingException e){
		}
		return text;
	}

	public static String escapeJava(String str){
		return StringEscapeUtils.escapeJava(str);
	}

	public static String getSearchString(String str){
		return '%' + str.trim().replace(" " , "%").replace("\'", "\\\'") + '%';
	}

	public static String getEncode(String string){
		if (string != null)
			return getEncode(string.getBytes());
		return null;
	}


	public static String removeSpecialChar(String text) {

		String res = text;

		if(res==null) {
			return "";

		} else {

			try {
				res = res.replaceAll("[^A-z|0-9|éúíóáÉÚÍÓÁèùìòàÈÙÌÒÀõãñÕÃÑêûîôâÊÛÎÔÂëÿüïöäËYÜÏÖÄ]", " ");

			} catch(Exception e) {
				e.printStackTrace();
			}	
			/*text = text.replace("\\™", "")
						.replace("”", "")
						.replace("+/–", "")
						.replace("±", "")
						.replace("–", "")
						.replace("©", "")
						.replace("®", "")
						.replace("°", "")
						.replace("«", "")
						.replace("¼", "")
						.replace("½", "")
						.replace("¾", "")
						.replace("å", "")
						.replace("æ", "")
						.replace("ø", ""); */

		}

		return res;
	}


	public static String getEncode(byte[] data) {
		String encoding = null;
		try {
			byte[] buf = new byte[4096];
			//			String fileName = text;
			//			java.io.FileInputStream fis = new java.io.FileInputStream(fileName);
			java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(data);

			// (1)
			UniversalDetector detector = new UniversalDetector(null);

			// (2)
			int nread;

			while ((nread = bis.read(buf)) > 0 && !detector.isDone()) {
				detector.handleData(buf, 0, nread);
			}

			// (3)
			detector.dataEnd();

			// (4)
			encoding = detector.getDetectedCharset();

			// (5)
			detector.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return encoding;
	}

	public static String collectionToCSV(List<?> list){
		StringBuilder sb = new StringBuilder();

		for(Object o : list){
			sb.append(String.valueOf(o));
			sb.append(",");
		}

		if(sb.charAt(sb.length()-1) == ','){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}

	public static boolean isEmpty(String str) {
		return str == null || str.isEmpty();
	}

	public static String capitalizeFirstLetter(String str) {
		return capitalizeFirstLetter(str, false);
	}

	public static String capitalizeFirstLetter(String str, boolean onlyWhenFullyUpper) {
		if (str == null)
			return null;

		if (onlyWhenFullyUpper && !str.toUpperCase().equals(str))
			return str;

		String s = str.toLowerCase();
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	public static String replaceRomanNumbers(String s){

		return s.replaceAll(" i ", " 1 ")
				.replaceAll(" ii ", " 2 ")
				.replaceAll(" iii ", " 3 ")
				.replaceAll(" iv ", " 4 ")
				.replaceAll(" vi ", " 5 ")
				.replaceAll(" vii ", " 6 ")
				.replaceAll(" viii ", " 7 ")
				.replaceAll(" v ", " 8 ")
				.replaceAll(" ix ", " 9 ").trim();
	}

	public static boolean isDigitOrWord(String s) {
		for (int i = 0; i < s.length(); i++) {
			String charac = String.valueOf(s.charAt(i));
			if (charac.matches("\\d|\\w"))
				return true;
		}
		return false;
	}

	public static boolean isDigit(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (Exception e) {}
		return false;
	}

	public static boolean isWord(String s) {
		for (int i = 0; i < s.length(); i++) {
			String charac = String.valueOf(s.charAt(i));
			if (charac.matches("\\w"))
				return true;
		}
		return false;
	}

	public static String completeTrim(String s) {
		s = s.trim();
		while (s.contains("  ")) {
			s = s.replace("  ", " ");
		}
		s = s.replace(" ; ", " ");
		s = s.replace(" : ", " ");
		s = s.replace(" + ", " ");
		s = s.replace(" - ", " ");
		s = s.replace(" _ ", " ");
		return s;
	}

	private static Map<Integer, String> numberInWords = new HashMap<Integer, String>();
	static {
		numberInWords.put(0, "zero");
		numberInWords.put(1, "um");
		numberInWords.put(2, "dois");
		numberInWords.put(3, "tres");
		numberInWords.put(4, "quatro");
		numberInWords.put(5, "cinco");
		numberInWords.put(6, "seis");
		numberInWords.put(7, "sete");
		numberInWords.put(8, "oito");
		numberInWords.put(9, "nove");
	}

	public static String numberInWords(String number) {
		try {
			int n = Integer.parseInt(number);
			return numberInWords.get(n);
		} catch (Exception e) {}
		return "";
	}

	public static final String getStringId(String s) {
		return replace(s);
	}

	private static final String SEPARATOR = "_";

	//old
	public static final String parse(String s, String str) {
		if (s == null || s.length() == 0)
			return s;

		StringBuilder sb = new StringBuilder(s.length());
		for(int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			char cl = Character.toLowerCase(c);
			if (!Character.isLetter(cl) && !Character.isDigit(cl)) {
				sb.append(SEPARATOR); continue;
			}
			if (Character.isDigit(cl)) {
				sb.append(cl); continue;
			}
			if ((cl == 'ã') || (cl == 'á') || (cl == 'â') || (cl == 'à') || (cl == 'ä') || (cl == 'ª')) {
				sb.append(Character.isLowerCase(c) ? 'a' : 'A') ; continue ;
			}
			if ((cl == 'é') || (cl == 'è') || (cl == 'ê') || (cl == 'ë')) {
				sb.append(Character.isLowerCase(c) ? 'e' : 'E') ; continue ;
			}
			if ((cl == 'í') || (cl == 'ì') || (cl == 'î') || (cl == 'ï')) {
				sb.append(Character.isLowerCase(c) ? 'i' : 'I') ; continue ;
			}
			if ((cl == 'ó') || (cl == 'ò') || (cl == 'ô') || (cl == 'ö') || (cl == 'õ') || (cl == 'º')) {
				sb.append(Character.isLowerCase(c) ? 'o' : 'O') ; continue ;
			}
			if ((cl == 'ú') || (cl == 'ù') || (cl == 'ü') || (cl == 'û')) {
				sb.append(Character.isLowerCase(c) ? 'u' : 'U') ; continue ;
			}
			if (cl == 'ç') {
				sb.append(Character.isLowerCase(c) ? 'c' : 'C') ; continue ;
			}
			if (cl == 'ñ') {
				sb.append(Character.isLowerCase(c) ? 'n' : 'N') ; continue ;
			}
			sb.append(c);
		}

		return sb.toString();
	}

	public static String[] REPLACES = { "a", "e", "i", "o", "u", "c", "n", "-", "-" };  

	public static Pattern[] PATTERNS = null;  

	public static void compilePatterns() {  
		PATTERNS = new Pattern[REPLACES.length];  
		PATTERNS[0] = Pattern.compile("[âãáàä]", Pattern.CASE_INSENSITIVE);  
		PATTERNS[1] = Pattern.compile("[éèêë]", Pattern.CASE_INSENSITIVE);  
		PATTERNS[2] = Pattern.compile("[íìîï]", Pattern.CASE_INSENSITIVE);  
		PATTERNS[3] = Pattern.compile("[óòôõö]", Pattern.CASE_INSENSITIVE);  
		PATTERNS[4] = Pattern.compile("[úùûü]", Pattern.CASE_INSENSITIVE);  
		PATTERNS[5] = Pattern.compile("[ç]", Pattern.CASE_INSENSITIVE);
		PATTERNS[6] = Pattern.compile("[ñ]", Pattern.CASE_INSENSITIVE);
		PATTERNS[7] = Pattern.compile("[^0-9a-z]", Pattern.CASE_INSENSITIVE);
		PATTERNS[8] = Pattern.compile("-+", Pattern.CASE_INSENSITIVE);
	}  

	public static String replace(String text) {  
		if(PATTERNS == null){  
			compilePatterns();  
		}  

		String result = text.toLowerCase();  
		for (int i = 0; i < PATTERNS.length; i++) {  
			Matcher matcher = PATTERNS[i].matcher(result);  
			result = matcher.replaceAll(REPLACES[i]);  
		}

		result = result.toLowerCase().replaceAll("-e-", "-");
		result = result.toLowerCase().replaceAll("-e-", "-");

		if (result.startsWith("-")) {
			result = result.substring(1, result.length());
		}
		if (result.endsWith("-")) {
			result = result.substring(0, result.length()-1);
		}

		return result.toLowerCase();  
	}


	private static Map<String, String> characterToEntityName = new HashMap<String, String>();
	private static Map<String, String> characterToEntityNumber = new HashMap<String, String>();

	static {
		characterToEntityName.put("À","&Agrave;");
		characterToEntityName.put("Á","&Aacute;");
		characterToEntityName.put("Â","&Acirc;");
		characterToEntityName.put("Ã","&Atilde;");
		characterToEntityName.put("Ä","&Auml;");
		characterToEntityName.put("Å","&Aring;");
		characterToEntityName.put("Æ","&AElig;");
		characterToEntityName.put("Ç","&Ccedil;");
		characterToEntityName.put("È","&Egrave;");
		characterToEntityName.put("É","&Eacute;");
		characterToEntityName.put("Ê","&Ecirc;");
		characterToEntityName.put("Ë","&Euml;");
		characterToEntityName.put("Ì","&Igrave;");
		characterToEntityName.put("Í","&Iacute;");
		characterToEntityName.put("Î","&Icirc;");
		characterToEntityName.put("Ï","&Iuml;");
		characterToEntityName.put("Ð","&ETH;");
		characterToEntityName.put("Ñ","&Ntilde;");
		characterToEntityName.put("Ò","&Ograve;");
		characterToEntityName.put("Ó","&Oacute;");
		characterToEntityName.put("Ô","&Ocirc;");
		characterToEntityName.put("Õ","&Otilde;");
		characterToEntityName.put("Ö","&Ouml;");
		characterToEntityName.put("Ø","&Oslash;");
		characterToEntityName.put("Ù","&Ugrave;");
		characterToEntityName.put("Ú","&Uacute;");
		characterToEntityName.put("Û","&Ucirc;");
		characterToEntityName.put("Ü","&Uuml;");
		characterToEntityName.put("Ý","&Yacute;");
		characterToEntityName.put("Þ","&THORN;");
		characterToEntityName.put("ß","&szlig;");
		characterToEntityName.put("à","&agrave;");
		characterToEntityName.put("á","&aacute;");
		characterToEntityName.put("â","&acirc;");
		characterToEntityName.put("ã","&atilde;");
		characterToEntityName.put("ä","&auml;");
		characterToEntityName.put("å","&aring;");
		characterToEntityName.put("æ","&aelig;");
		characterToEntityName.put("ç","&ccedil;");
		characterToEntityName.put("è","&egrave;");
		characterToEntityName.put("é","&eacute;");
		characterToEntityName.put("ê","&ecirc;");
		characterToEntityName.put("ë","&euml;");
		characterToEntityName.put("ì","&igrave;");
		characterToEntityName.put("í","&iacute;");
		characterToEntityName.put("î","&icirc;");
		characterToEntityName.put("ï","&iuml;");
		characterToEntityName.put("ð","&eth;");
		characterToEntityName.put("ñ","&ntilde;");
		characterToEntityName.put("ò","&ograve;");
		characterToEntityName.put("ó","&oacute;");
		characterToEntityName.put("ô","&ocirc;");
		characterToEntityName.put("õ","&otilde;");
		characterToEntityName.put("ö","&ouml;");
		characterToEntityName.put("ø","&oslash;");
		characterToEntityName.put("ù","&ugrave;");
		characterToEntityName.put("ú","&uacute;");
		characterToEntityName.put("û","&ucirc;");
		characterToEntityName.put("ü","&uuml;");
		characterToEntityName.put("ý","&yacute;");
		characterToEntityName.put("þ","&thorn;");
		characterToEntityName.put("ÿ","&yuml;");




		characterToEntityNumber.put("À" ,"&#192;");
		characterToEntityNumber.put("Á" ,"&#193;");
		characterToEntityNumber.put("Â" ,"&#194;");
		characterToEntityNumber.put("Ã" ,"&#195;");
		characterToEntityNumber.put("Ä" ,"&#196;");
		characterToEntityNumber.put("Å" ,"&#197;");
		characterToEntityNumber.put("Æ" ,"&#198;");
		characterToEntityNumber.put("Ç" ,"&#199;");
		characterToEntityNumber.put("È" ,"&#200;");
		characterToEntityNumber.put("É" ,"&#201;");
		characterToEntityNumber.put("Ê" ,"&#202;");
		characterToEntityNumber.put("Ë" ,"&#203;");
		characterToEntityNumber.put("Ì" ,"&#204;");
		characterToEntityNumber.put("Í" ,"&#205;");
		characterToEntityNumber.put("Î", "&#206;");
		characterToEntityNumber.put("Ï" ,"&#207;");
		characterToEntityNumber.put("Ð" ,"&#208;");
		characterToEntityNumber.put("Ñ" ,"&#209;");
		characterToEntityNumber.put("Ò" ,"&#210;");
		characterToEntityNumber.put("Ó" ,"&#211;");
		characterToEntityNumber.put("Ô" ,"&#212;");
		characterToEntityNumber.put("Õ" ,"&#213;");
		characterToEntityNumber.put("Ö" ,"&#214;");
		characterToEntityNumber.put("Ø" ,"&#216;");
		characterToEntityNumber.put("Ù" ,"&#217;");
		characterToEntityNumber.put("Ú" ,"&#218;");
		characterToEntityNumber.put("Û" ,"&#219;");
		characterToEntityNumber.put("Ü" ,"&#220;");
		characterToEntityNumber.put("Ý" ,"&#221;");
		characterToEntityNumber.put("Þ" ,"&#222;");
		characterToEntityNumber.put("ß" ,"&#223;");
		characterToEntityNumber.put("à" ,"&#224;");
		characterToEntityNumber.put("á" ,"&#225;");
		characterToEntityNumber.put("â" ,"&#226;");
		characterToEntityNumber.put("ã" ,"&#227;");
		characterToEntityNumber.put("ä" ,"&#228;");
		characterToEntityNumber.put("å" ,"&#229;");
		characterToEntityNumber.put("æ" ,"&#230;");
		characterToEntityNumber.put("ç" ,"&#231;");
		characterToEntityNumber.put("è" ,"&#232;");
		characterToEntityNumber.put("é" ,"&#233;");
		characterToEntityNumber.put("ê" ,"&#234;");
		characterToEntityNumber.put("ë" ,"&#235;");
		characterToEntityNumber.put("ì" ,"&#236;");
		characterToEntityNumber.put("í" ,"&#237;");
		characterToEntityNumber.put("î" ,"&#238;");
		characterToEntityNumber.put("ï" ,"&#239;");
		characterToEntityNumber.put("ð" ,"&#240;");
		characterToEntityNumber.put("ñ" ,"&#241;");
		characterToEntityNumber.put("ò" ,"&#242;");
		characterToEntityNumber.put("ó" ,"&#243;");
		characterToEntityNumber.put("ô" ,"&#244;");
		characterToEntityNumber.put("õ" ,"&#245;");
		characterToEntityNumber.put("ö" ,"&#246;");
		characterToEntityNumber.put("ø" ,"&#248;");
		characterToEntityNumber.put("ù" ,"&#249;");
		characterToEntityNumber.put("ú" ,"&#250;");
		characterToEntityNumber.put("û" ,"&#251;");;
		characterToEntityNumber.put("ü" ,"&#252;");
		characterToEntityNumber.put("ý" ,"&#253;");
		characterToEntityNumber.put("þ" ,"&#254;");
		characterToEntityNumber.put("ÿ" ,"&#255;");
	}

	public static String characterToEntityNumber(String s) {
		return characterToEntity(s, characterToEntityNumber);
	}

	public static String characterToEntityName(String s) {
		return characterToEntity(s, characterToEntityName);
	}

	public static String characterToEntity(String s, Map<String, String> map) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < s.length(); i++) {
			char charac = s.charAt(i);
			String string = Character.toString(charac);
			if (map.containsKey(string)) {
				string = map.get(string);
			}
			sb.append(string);
		}

		return sb.toString();
	}

	public static String getString(String str, String beginIndexStr, String endIndexStr) {
		try {
			int beginIndex = str.indexOf(beginIndexStr) > -1 ? str.indexOf(beginIndexStr) + beginIndexStr.length() : -1;
			int endIndex = str.indexOf(endIndexStr, beginIndex);
			if (endIndex == -1)
				endIndex = str.length();
			return str.substring(beginIndex, endIndex);
		} catch (Exception e) {}
		return null;
	}


	public static void main(String[] args) {
		//		TextAnalyzer analyzer = new TextAnalyzer();

		//String text = analyzer.transform("câmera çãâá renato almeida");
		//System.out.println(StringUtil.suggestString(text));

		//System.out.println( "uhauhahauauh".contains(" ") );

		System.out.println( capitalize("exemplo teste") );

		//		System.out.println(replace("Consultório -10*)-+&%#%¨&*)_*%$Odontológico Dr Jéferson Fagundes"));
		System.out.println(replace("energia-solar-utilizacao-e-empregos-praticos-v-isbn-9788528903812"));
		System.out.println(replace("_But_I_m_Almost_13__"));
	}


}
