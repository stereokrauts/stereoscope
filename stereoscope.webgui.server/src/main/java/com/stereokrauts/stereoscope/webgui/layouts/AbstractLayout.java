package com.stereokrauts.stereoscope.webgui.layouts;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public abstract class AbstractLayout {
	private String layout;
	public final String br = System.getProperty("line.separator");
	
	abstract void setFrontendProperties(String mediaQuery);
	
	abstract void buildLayout() throws JsonGenerationException, JsonMappingException, ScriptException, IOException;
	
	abstract String getHeader();
	
	public String replacePattern(String macro, String param, int value) {
		String replaced = macro;
		Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(macro);
		while (matcher.find()) {
			String rawExpr = macro.substring(matcher.start(), matcher.end());
			String expr = rawExpr;
			if (expr.contains(param)) {
				expr = stripPatternShell(expr);
				expr = replaceParameter(expr, param, value);
				int result = calcFromString(expr);
								
				replaced = replaced.replace(rawExpr, Integer.toString(result));
			}
		}
		return replaced;
	}
	
	public String replacePattern(String macro, String param, String value) {
		String replaced = macro;
		Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(macro);
		while (matcher.find()) {
			String rawExpr = macro.substring(matcher.start(), matcher.end());
			String expr = rawExpr;
			if (expr.contains(param)) {
				expr = stripPatternShell(expr);
				expr = replaceParameter(expr, param, value);
								
				replaced = replaced.replace(rawExpr, expr);
			}
		}
		return replaced;
	}
	
	private String stripPatternShell(String pattern) {
		return pattern.replace("${", "").replace("}", "");
	}
	
	private String replaceParameter(String pattern, String param, int value) {
		return pattern.replace(param, Integer.toString(value));
	}
	
	private String replaceParameter(String pattern, String param, String value) {
		return pattern.replace(param, value);
	}
	
	public String stripComments(String strWithComments) {
		// this strips comments like: /* bla */
		return strWithComments.replaceAll("/\\*(?:.|[\\n\\r])*?\\*/", "");
	}
	
	public int calcFromString(String pattern) {
		pattern = pattern.replaceAll("\\s","");
		int result = -1;
		if (pattern.contains("+")) {
			int i = pattern.indexOf("+", 1);
			result = Integer.parseInt(pattern.substring(0, i))
					+ Integer.parseInt(pattern.substring(i + 1, pattern.length()));
		} else if (pattern.substring(1).contains("-")) {
			int i = pattern.indexOf("-", 1);
			result = Integer.parseInt(pattern.substring(0, i))
					- Integer.parseInt(pattern.substring(i + 1, pattern.length()));
			
		} else if (pattern.contains("*")) {
			int i = pattern.indexOf("*");
			result = Integer.parseInt(pattern.substring(0, i))
					* Integer.parseInt(pattern.substring(i + 1, pattern.length()));
		} else if (pattern.contains("/")) {
			int i = pattern.indexOf("/");
			result = Integer.parseInt(pattern.substring(0, i))
					/ Integer.parseInt(pattern.substring(i + 1, pattern.length()));
		} else {
			result = Integer.parseInt(pattern);
		}
		return result;
	}
	
	public String evaluateLayout(String layout) throws ScriptException, JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Object pojo = mapper.readTree(layout);
		return mapper.writeValueAsString(pojo);
		
	}
	
	public String capitalize(String line) {
		return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}
	
	public String addLineNumbers(String input) {
		String output = "";
		String[] lines = input.split(br);
		for (int i = 0; i < lines.length; i++) {
			output += (i + 1) + "\t" + lines[i] + br;
		}
		return output;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}
}
