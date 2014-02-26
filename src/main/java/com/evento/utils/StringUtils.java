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

		//elimina espa�os (inicio, fim e "  ")
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

			if ((cl == '�') || (cl == '�') || (cl == '�') || (cl == '�') || (cl == '�') || (cl == '�')) {
				sb.append(Character.isLowerCase(c) ? 'a' : 'A') ; continue ;
			}
			if ((cl == '�') || (cl == '�') || (cl == '�') || (cl == '�')) {
				sb.append(Character.isLowerCase(c) ? 'e' : 'E') ; continue ;
			}
			if ((cl == '�') || (cl == '�') || (cl == '�') || (cl == '�')) {
				sb.append(Character.isLowerCase(c) ? 'i' : 'I') ; continue ;
			}
			if ((cl == '�') || (cl == '�') || (cl == '�') || (cl == '�') || (cl == '�') || (cl == '�')) {
				sb.append(Character.isLowerCase(c) ? 'o' : 'O') ; continue ;
			}
			if ((cl == '�') || (cl == '�') || (cl == '�') || (cl == '�')) {
				sb.append(Character.isLowerCase(c) ? 'u' : 'U') ; continue ;
			}
			if (cl == '�') {
				sb.append(Character.isLowerCase(c) ? 'c' : 'C') ; continue ;
			}
			if (cl == '�') {
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
				res = res.replaceAll("[^A-z|0-9|�������������������������������������������Y����]", " ");

			} catch(Exception e) {
				e.printStackTrace();
			}	
			/*text = text.replace("\\�", "")
						.replace("�", "")
						.replace("+/�", "")
						.replace("�", "")
						.replace("�", "")
						.replace("�", "")
						.replace("�", "")
						.replace("�", "")
						.replace("�", "")
						.replace("�", "")
						.replace("�", "")
						.replace("�", "")
						.replace("�", "")
						.replace("�", "")
						.replace("�", ""); */

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
			if ((cl == '�') || (cl == '�') || (cl == '�') || (cl == '�') || (cl == '�') || (cl == '�')) {
				sb.append(Character.isLowerCase(c) ? 'a' : 'A') ; continue ;
			}
			if ((cl == '�') || (cl == '�') || (cl == '�') || (cl == '�')) {
				sb.append(Character.isLowerCase(c) ? 'e' : 'E') ; continue ;
			}
			if ((cl == '�') || (cl == '�') || (cl == '�') || (cl == '�')) {
				sb.append(Character.isLowerCase(c) ? 'i' : 'I') ; continue ;
			}
			if ((cl == '�') || (cl == '�') || (cl == '�') || (cl == '�') || (cl == '�') || (cl == '�')) {
				sb.append(Character.isLowerCase(c) ? 'o' : 'O') ; continue ;
			}
			if ((cl == '�') || (cl == '�') || (cl == '�') || (cl == '�')) {
				sb.append(Character.isLowerCase(c) ? 'u' : 'U') ; continue ;
			}
			if (cl == '�') {
				sb.append(Character.isLowerCase(c) ? 'c' : 'C') ; continue ;
			}
			if (cl == '�') {
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
		PATTERNS[0] = Pattern.compile("[�����]", Pattern.CASE_INSENSITIVE);  
		PATTERNS[1] = Pattern.compile("[����]", Pattern.CASE_INSENSITIVE);  
		PATTERNS[2] = Pattern.compile("[����]", Pattern.CASE_INSENSITIVE);  
		PATTERNS[3] = Pattern.compile("[�����]", Pattern.CASE_INSENSITIVE);  
		PATTERNS[4] = Pattern.compile("[����]", Pattern.CASE_INSENSITIVE);  
		PATTERNS[5] = Pattern.compile("[�]", Pattern.CASE_INSENSITIVE);
		PATTERNS[6] = Pattern.compile("[�]", Pattern.CASE_INSENSITIVE);
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
		characterToEntityName.put("�","&Agrave;");
		characterToEntityName.put("�","&Aacute;");
		characterToEntityName.put("�","&Acirc;");
		characterToEntityName.put("�","&Atilde;");
		characterToEntityName.put("�","&Auml;");
		characterToEntityName.put("�","&Aring;");
		characterToEntityName.put("�","&AElig;");
		characterToEntityName.put("�","&Ccedil;");
		characterToEntityName.put("�","&Egrave;");
		characterToEntityName.put("�","&Eacute;");
		characterToEntityName.put("�","&Ecirc;");
		characterToEntityName.put("�","&Euml;");
		characterToEntityName.put("�","&Igrave;");
		characterToEntityName.put("�","&Iacute;");
		characterToEntityName.put("�","&Icirc;");
		characterToEntityName.put("�","&Iuml;");
		characterToEntityName.put("�","&ETH;");
		characterToEntityName.put("�","&Ntilde;");
		characterToEntityName.put("�","&Ograve;");
		characterToEntityName.put("�","&Oacute;");
		characterToEntityName.put("�","&Ocirc;");
		characterToEntityName.put("�","&Otilde;");
		characterToEntityName.put("�","&Ouml;");
		characterToEntityName.put("�","&Oslash;");
		characterToEntityName.put("�","&Ugrave;");
		characterToEntityName.put("�","&Uacute;");
		characterToEntityName.put("�","&Ucirc;");
		characterToEntityName.put("�","&Uuml;");
		characterToEntityName.put("�","&Yacute;");
		characterToEntityName.put("�","&THORN;");
		characterToEntityName.put("�","&szlig;");
		characterToEntityName.put("�","&agrave;");
		characterToEntityName.put("�","&aacute;");
		characterToEntityName.put("�","&acirc;");
		characterToEntityName.put("�","&atilde;");
		characterToEntityName.put("�","&auml;");
		characterToEntityName.put("�","&aring;");
		characterToEntityName.put("�","&aelig;");
		characterToEntityName.put("�","&ccedil;");
		characterToEntityName.put("�","&egrave;");
		characterToEntityName.put("�","&eacute;");
		characterToEntityName.put("�","&ecirc;");
		characterToEntityName.put("�","&euml;");
		characterToEntityName.put("�","&igrave;");
		characterToEntityName.put("�","&iacute;");
		characterToEntityName.put("�","&icirc;");
		characterToEntityName.put("�","&iuml;");
		characterToEntityName.put("�","&eth;");
		characterToEntityName.put("�","&ntilde;");
		characterToEntityName.put("�","&ograve;");
		characterToEntityName.put("�","&oacute;");
		characterToEntityName.put("�","&ocirc;");
		characterToEntityName.put("�","&otilde;");
		characterToEntityName.put("�","&ouml;");
		characterToEntityName.put("�","&oslash;");
		characterToEntityName.put("�","&ugrave;");
		characterToEntityName.put("�","&uacute;");
		characterToEntityName.put("�","&ucirc;");
		characterToEntityName.put("�","&uuml;");
		characterToEntityName.put("�","&yacute;");
		characterToEntityName.put("�","&thorn;");
		characterToEntityName.put("�","&yuml;");




		characterToEntityNumber.put("�" ,"&#192;");
		characterToEntityNumber.put("�" ,"&#193;");
		characterToEntityNumber.put("�" ,"&#194;");
		characterToEntityNumber.put("�" ,"&#195;");
		characterToEntityNumber.put("�" ,"&#196;");
		characterToEntityNumber.put("�" ,"&#197;");
		characterToEntityNumber.put("�" ,"&#198;");
		characterToEntityNumber.put("�" ,"&#199;");
		characterToEntityNumber.put("�" ,"&#200;");
		characterToEntityNumber.put("�" ,"&#201;");
		characterToEntityNumber.put("�" ,"&#202;");
		characterToEntityNumber.put("�" ,"&#203;");
		characterToEntityNumber.put("�" ,"&#204;");
		characterToEntityNumber.put("�" ,"&#205;");
		characterToEntityNumber.put("�", "&#206;");
		characterToEntityNumber.put("�" ,"&#207;");
		characterToEntityNumber.put("�" ,"&#208;");
		characterToEntityNumber.put("�" ,"&#209;");
		characterToEntityNumber.put("�" ,"&#210;");
		characterToEntityNumber.put("�" ,"&#211;");
		characterToEntityNumber.put("�" ,"&#212;");
		characterToEntityNumber.put("�" ,"&#213;");
		characterToEntityNumber.put("�" ,"&#214;");
		characterToEntityNumber.put("�" ,"&#216;");
		characterToEntityNumber.put("�" ,"&#217;");
		characterToEntityNumber.put("�" ,"&#218;");
		characterToEntityNumber.put("�" ,"&#219;");
		characterToEntityNumber.put("�" ,"&#220;");
		characterToEntityNumber.put("�" ,"&#221;");
		characterToEntityNumber.put("�" ,"&#222;");
		characterToEntityNumber.put("�" ,"&#223;");
		characterToEntityNumber.put("�" ,"&#224;");
		characterToEntityNumber.put("�" ,"&#225;");
		characterToEntityNumber.put("�" ,"&#226;");
		characterToEntityNumber.put("�" ,"&#227;");
		characterToEntityNumber.put("�" ,"&#228;");
		characterToEntityNumber.put("�" ,"&#229;");
		characterToEntityNumber.put("�" ,"&#230;");
		characterToEntityNumber.put("�" ,"&#231;");
		characterToEntityNumber.put("�" ,"&#232;");
		characterToEntityNumber.put("�" ,"&#233;");
		characterToEntityNumber.put("�" ,"&#234;");
		characterToEntityNumber.put("�" ,"&#235;");
		characterToEntityNumber.put("�" ,"&#236;");
		characterToEntityNumber.put("�" ,"&#237;");
		characterToEntityNumber.put("�" ,"&#238;");
		characterToEntityNumber.put("�" ,"&#239;");
		characterToEntityNumber.put("�" ,"&#240;");
		characterToEntityNumber.put("�" ,"&#241;");
		characterToEntityNumber.put("�" ,"&#242;");
		characterToEntityNumber.put("�" ,"&#243;");
		characterToEntityNumber.put("�" ,"&#244;");
		characterToEntityNumber.put("�" ,"&#245;");
		characterToEntityNumber.put("�" ,"&#246;");
		characterToEntityNumber.put("�" ,"&#248;");
		characterToEntityNumber.put("�" ,"&#249;");
		characterToEntityNumber.put("�" ,"&#250;");
		characterToEntityNumber.put("�" ,"&#251;");;
		characterToEntityNumber.put("�" ,"&#252;");
		characterToEntityNumber.put("�" ,"&#253;");
		characterToEntityNumber.put("�" ,"&#254;");
		characterToEntityNumber.put("�" ,"&#255;");
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

		//String text = analyzer.transform("c�mera ���� renato almeida");
		//System.out.println(StringUtil.suggestString(text));

		//System.out.println( "uhauhahauauh".contains(" ") );

		System.out.println( capitalize("exemplo teste") );

		//		System.out.println(replace("Consult�rio -10*)-+&%#%�&*)_*%$Odontol�gico Dr J�ferson Fagundes"));
		System.out.println(replace("energia-solar-utilizacao-e-empregos-praticos-v-isbn-9788528903812"));
		System.out.println(replace("_But_I_m_Almost_13__"));
	}


}
