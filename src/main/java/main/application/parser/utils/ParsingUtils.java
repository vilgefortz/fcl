package main.application.parser.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingUtils {
	public static String getFirstWord(String text, String regex) {
		regex = "(?is)^" + regex;
		text = text.trim();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		String result = "";
		if (matcher.find())
			result = matcher.group();
		return result;
	}

	public static String getFirstWord(String text) {
		return getFirstWord(text, "[a-z][a-z0-9_]*");
	}

	public static String implode(String connector, List<String> l) {
		StringBuilder sb=new StringBuilder();
		for (String s : l) {
			sb.append(l).append(connector);
		}
		String res = sb.toString();
		return res.substring(0, res.length()-connector.length());
	}
}
