package com.evento.utils;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StopWords {
	public final static String[] BRAZILIAN_STOP_WORDS = { "a", "ainda", "alem",
			"ambas", "ambos", "antes", "ao", "aonde", "aos", "apos", "aquele",
			"aqueles", "as", "assim", "com", "como", "contra", "contudo",
			"cuja", "cujas", "cujo", "cujos", "da", "das", "de", "dela",
			"dele", "deles", "demais", "depois", "desde", "desta", "deste",
			"dispoe", "dispoem", "diversa", "diversas", "diversos", "do",
			"dos", "durante", "e", "ela", "elas", "ele", "eles", "em", "entao",
			"entre", "essa", "essas", "esse", "esses", "esta", "estas", "este",
			"estes", "ha", "isso", "isto", "logo", "mais", "mas", "mediante",
			"menos", "mesma", "mesmas", "mesmo", "mesmos", "na", "nas", "nao",
			"nas", "nem", "nesse", "neste", "nos", "o", "os", "ou", "outra",
			"outras", "outro", "outros", "pelas", "pelas", "pelo", "pelos",
			"perante", "para", "pois", "por", "porque", "portanto", "proprio",
			"propios", "quais", "qual", "qualquer", "quando", "quanto", "que",
			"quem", "quer", "se", "seja", "sem", "sendo", "seu", "seus", "sob",
			"sobre", "sua", "suas", "tal", "tambem", "teu", "teus", "toda",
			"todas", "todo", "todos", "tua", "tuas", "tudo", "um", "uma",
			"umas", "uns" };
	
	private static final Set<String> SET = new HashSet<String>(Arrays.asList(BRAZILIAN_STOP_WORDS)); 
	
	public static boolean isStopWord(String w) {
		w = w != null ? w.toLowerCase().trim() : null;

		w = Normalizer.normalize(w, Normalizer.Form.NFD);
		w = w.replaceAll("[^\\p{ASCII}]", "");

		return SET.contains(w);
	}
	
	public static void main(String[] args) {
		String w = "A";
		System.out.println(w + " - " + isStopWord(w));
		
		w = "de";
		System.out.println(w + " - " + isStopWord(w));
		
		w = "DE";
		System.out.println(w + " - " + isStopWord(w));
		
		w = "Á";
		System.out.println(w + " - " + isStopWord(w));
		
		w = "à";
		System.out.println(w + " - " + isStopWord(w));
	}
}