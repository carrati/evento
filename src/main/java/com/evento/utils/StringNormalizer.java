package com.evento.utils;

import java.text.Normalizer;

public class StringNormalizer {
	
	public static String normalize(String input) {
//		String input = "çaláda";  
		String cleaned = Normalizer.normalize(input, Normalizer.Form.NFD);  
		cleaned = cleaned.replaceAll("[^\\p{ASCII}]", "");  
		return cleaned;  
	}
	
	public static String parameterize(String input) {
		String cleaned = normalize(input);
		cleaned = clean(cleaned);
		cleaned = cleaned.replaceAll(" ", "-");

		return cleaned;
	}
	
	public static String clean(String cleaned) {
		cleaned = cleaned.replace("/", "");
		cleaned = cleaned.replace("||", "");
		cleaned = cleaned.replace("'", "");
		cleaned = cleaned.replace("(", "");
		cleaned = cleaned.replace(")", "");
		cleaned = cleaned.replace(",", "");
		cleaned = cleaned.replace(":", "");
		cleaned = cleaned.toLowerCase();
		
		return cleaned;
	}
	
	public static String capitalize(String input) {
		
		String lowerCasedString = input.toLowerCase();
		
		String[] splitted = lowerCasedString.split(" ");
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < splitted.length; i++) {
			String word = splitted[i];
			
			sb.append(word.substring(0, 1).toUpperCase()).append(word.substring(1));
			
			if (i < splitted.length - 1) {
				sb.append(" ");
			}
		}
		
		return sb.toString();
	}
	
	public static String toCamelCase(String name) {
		String[] words = name.split("_");
		
		String finalName = "";
		int count = 0;
		for (String word : words) {
			finalName += (count == 0 ? word : word.substring(0, 1).toUpperCase() + word.substring(1));
			count++;
		}
		
		return finalName;
	}
	
	public static String plurarize(String name) {
		String lastLetter = new String(new char[] {name.charAt(name.length()-1)});
		String pluralName = (lastLetter.equals("y") ? name.substring(0, name.length() - 1) + "ies" : name + "s");
		
		return pluralName;
	}
	
	public static String toUnderscore(String name) {
		return name.replaceAll("::", "/").
		replaceAll("([A-Z]+)([A-Z][a-z])","$1_$2").
		replaceAll("([a-z\\d])([A-Z])","$1_$2").
	    replace("-", "_").
	    toLowerCase();
	}
	
	public static void main(String[] args) {
		System.out.println(capitalize("BUENOS AIRES"));
		
		
		System.out.println("1,000.00".matches("(.*),[0-9]{3}\\.[0-9]{2}"));
	}

}
