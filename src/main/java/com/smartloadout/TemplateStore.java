package com.smartloadout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class TemplateStore
{
	private TemplateStore()
	{
	}

	public static String exportTemplates(List<ActivityTemplate> templates)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("[\n");
		List<ActivityTemplate> safeTemplates = templates == null ? Collections.emptyList() : templates;
		for (int index = 0; index < safeTemplates.size(); index++)
		{
			if (index > 0)
			{
				builder.append(",\n");
			}
			appendTemplate(builder, safeTemplates.get(index), "\t");
		}
		builder.append("\n]");
		return builder.toString();
	}

	public static List<ActivityTemplate> importTemplates(String json)
	{
		if (json == null || json.trim().isEmpty())
		{
			return Collections.emptyList();
		}

		Object parsed;
		try
		{
			parsed = new JsonReader(json).parse();
		}
		catch (IllegalArgumentException ex)
		{
			return Collections.emptyList();
		}

		if (!(parsed instanceof List))
		{
			return Collections.emptyList();
		}

		List<ActivityTemplate> templates = new ArrayList<>();
		for (Object entry : (List<?>) parsed)
		{
			ActivityTemplate template = toTemplate(entry);
			if (template != null)
			{
				templates.add(template);
			}
		}
		return templates;
	}

	private static void appendTemplate(StringBuilder builder, ActivityTemplate template, String indent)
	{
		ActivityTemplate safeTemplate = template == null
			? new ActivityTemplate("", "", Collections.emptyList(), Collections.emptyMap())
			: template;
		builder.append(indent).append("{\n");
		appendStringField(builder, "name", safeTemplate.getName(), indent + "\t", true);
		appendStringField(builder, "category", safeTemplate.getCategory(), indent + "\t", true);
		appendStringField(builder, "notes", safeTemplate.getNotes(), indent + "\t", true);
		appendRules(builder, safeTemplate.getRules(), indent + "\t", true);
		appendItemGroups(builder, safeTemplate.getItemGroups(), indent + "\t", false);
		builder.append(indent).append("}");
	}

	private static void appendRules(StringBuilder builder, List<LoadoutRule> rules, String indent, boolean comma)
	{
		builder.append(indent).append("\"rules\": [");
		if (!rules.isEmpty())
		{
			builder.append('\n');
			for (int index = 0; index < rules.size(); index++)
			{
				if (index > 0)
				{
					builder.append(",\n");
				}
				appendRule(builder, rules.get(index), indent + "\t");
			}
			builder.append('\n').append(indent);
		}
		builder.append("]");
		if (comma)
		{
			builder.append(',');
		}
		builder.append('\n');
	}

	private static void appendRule(StringBuilder builder, LoadoutRule rule, String indent)
	{
		LoadoutRule safeRule = rule == null ? new LoadoutRule() : rule;
		builder.append(indent).append("{\n");
		appendStringField(builder, "type", safeRule.getType() == null ? "" : safeRule.getType().name(), indent + "\t", true);
		appendStringField(builder, "title", safeRule.getTitle(), indent + "\t", true);
		appendStringField(builder, "itemGroup", safeRule.getItemGroup(), indent + "\t", true);
		appendIntegerListField(builder, "itemIds", safeRule.getItemIds(), indent + "\t", true);
		appendNumberField(builder, "minimumQuantity", safeRule.getMinimumQuantity(), indent + "\t", true);
		appendStringField(builder, "expectedValue", safeRule.getExpectedValue(), indent + "\t", true);
		appendNumberField(builder, "wildernessLimit", safeRule.getWildernessLimit(), indent + "\t", false);
		builder.append(indent).append("}");
	}

	private static void appendItemGroups(StringBuilder builder, Map<String, List<Integer>> itemGroups, String indent, boolean comma)
	{
		builder.append(indent).append("\"itemGroups\": {");
		if (!itemGroups.isEmpty())
		{
			builder.append('\n');
			int index = 0;
			for (Map.Entry<String, List<Integer>> entry : itemGroups.entrySet())
			{
				if (index > 0)
				{
					builder.append(",\n");
				}
				builder.append(indent).append('\t').append(quote(entry.getKey())).append(": ");
				appendIntegerList(builder, entry.getValue());
				index++;
			}
			builder.append('\n').append(indent);
		}
		builder.append("}");
		if (comma)
		{
			builder.append(',');
		}
		builder.append('\n');
	}

	private static void appendStringField(StringBuilder builder, String name, String value, String indent, boolean comma)
	{
		builder.append(indent).append(quote(name)).append(": ").append(quote(value));
		if (comma)
		{
			builder.append(',');
		}
		builder.append('\n');
	}

	private static void appendNumberField(StringBuilder builder, String name, int value, String indent, boolean comma)
	{
		builder.append(indent).append(quote(name)).append(": ").append(value);
		if (comma)
		{
			builder.append(',');
		}
		builder.append('\n');
	}

	private static void appendIntegerListField(StringBuilder builder, String name, List<Integer> values, String indent, boolean comma)
	{
		builder.append(indent).append(quote(name)).append(": ");
		appendIntegerList(builder, values);
		if (comma)
		{
			builder.append(',');
		}
		builder.append('\n');
	}

	private static void appendIntegerList(StringBuilder builder, List<Integer> values)
	{
		builder.append('[');
		for (int index = 0; index < values.size(); index++)
		{
			if (index > 0)
			{
				builder.append(", ");
			}
			builder.append(values.get(index));
		}
		builder.append(']');
	}

	private static ActivityTemplate toTemplate(Object value)
	{
		if (!(value instanceof Map))
		{
			return null;
		}

		Map<?, ?> map = (Map<?, ?>) value;
		ActivityTemplate template = new ActivityTemplate(
			asString(map.get("name")),
			asString(map.get("category")),
			toRules(map.get("rules")),
			toItemGroups(map.get("itemGroups")));
		template.setNotes(asString(map.get("notes")));
		return template;
	}

	private static List<LoadoutRule> toRules(Object value)
	{
		if (!(value instanceof List))
		{
			return Collections.emptyList();
		}

		List<LoadoutRule> rules = new ArrayList<>();
		for (Object entry : (List<?>) value)
		{
			if (entry instanceof Map)
			{
				rules.add(toRule((Map<?, ?>) entry));
			}
		}
		return rules;
	}

	private static LoadoutRule toRule(Map<?, ?> map)
	{
		LoadoutRule rule = new LoadoutRule(toRuleType(map.get("type")), asString(map.get("title")));
		rule.setItemGroup(asString(map.get("itemGroup")));
		rule.getItemIds().addAll(toIntegerList(map.get("itemIds")));
		rule.setMinimumQuantity(asInt(map.get("minimumQuantity")));
		rule.setExpectedValue(asString(map.get("expectedValue")));
		rule.setWildernessLimit(asInt(map.get("wildernessLimit")));
		return rule;
	}

	private static LoadoutRuleType toRuleType(Object value)
	{
		String text = asString(value);
		if (text.isEmpty())
		{
			return null;
		}

		try
		{
			return LoadoutRuleType.valueOf(text);
		}
		catch (IllegalArgumentException ex)
		{
			return null;
		}
	}

	private static Map<String, List<Integer>> toItemGroups(Object value)
	{
		if (!(value instanceof Map))
		{
			return Collections.emptyMap();
		}

		Map<String, List<Integer>> itemGroups = new LinkedHashMap<>();
		for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet())
		{
			itemGroups.put(asString(entry.getKey()), toIntegerList(entry.getValue()));
		}
		return itemGroups;
	}

	private static List<Integer> toIntegerList(Object value)
	{
		if (!(value instanceof List))
		{
			return Collections.emptyList();
		}

		List<Integer> values = new ArrayList<>();
		for (Object entry : (List<?>) value)
		{
			if (entry instanceof Number)
			{
				values.add(((Number) entry).intValue());
			}
		}
		return values;
	}

	private static String asString(Object value)
	{
		return value instanceof String ? (String) value : "";
	}

	private static int asInt(Object value)
	{
		return value instanceof Number ? ((Number) value).intValue() : 0;
	}

	private static String quote(String value)
	{
		StringBuilder builder = new StringBuilder();
		builder.append('"');
		String safeValue = value == null ? "" : value;
		for (int index = 0; index < safeValue.length(); index++)
		{
			char character = safeValue.charAt(index);
			switch (character)
			{
				case '"':
					builder.append("\\\"");
					break;
				case '\\':
					builder.append("\\\\");
					break;
				case '\n':
					builder.append("\\n");
					break;
				case '\r':
					builder.append("\\r");
					break;
				case '\t':
					builder.append("\\t");
					break;
				default:
					builder.append(character);
					break;
			}
		}
		builder.append('"');
		return builder.toString();
	}

	private static final class JsonReader
	{
		private final String text;
		private int index;

		private JsonReader(String text)
		{
			this.text = text;
		}

		private Object parse()
		{
			Object value = parseValue();
			skipWhitespace();
			if (index != text.length())
			{
				throw new IllegalArgumentException("Unexpected trailing text.");
			}
			return value;
		}

		private Object parseValue()
		{
			skipWhitespace();
			if (index >= text.length())
			{
				throw new IllegalArgumentException("Expected value.");
			}

			char character = text.charAt(index);
			if (character == '"')
			{
				return parseString();
			}
			if (character == '{')
			{
				return parseObject();
			}
			if (character == '[')
			{
				return parseArray();
			}
			if (character == '-' || Character.isDigit(character))
			{
				return parseNumber();
			}
			if (text.startsWith("null", index))
			{
				index += 4;
				return null;
			}

			throw new IllegalArgumentException("Unsupported value.");
		}

		private Map<String, Object> parseObject()
		{
			Map<String, Object> object = new LinkedHashMap<>();
			expect('{');
			skipWhitespace();
			if (consume('}'))
			{
				return object;
			}

			while (true)
			{
				skipWhitespace();
				String key = parseString();
				skipWhitespace();
				expect(':');
				object.put(key, parseValue());
				skipWhitespace();
				if (consume('}'))
				{
					return object;
				}
				expect(',');
			}
		}

		private List<Object> parseArray()
		{
			List<Object> array = new ArrayList<>();
			expect('[');
			skipWhitespace();
			if (consume(']'))
			{
				return array;
			}

			while (true)
			{
				array.add(parseValue());
				skipWhitespace();
				if (consume(']'))
				{
					return array;
				}
				expect(',');
			}
		}

		private String parseString()
		{
			expect('"');
			StringBuilder builder = new StringBuilder();
			while (index < text.length())
			{
				char character = text.charAt(index++);
				if (character == '"')
				{
					return builder.toString();
				}
				if (character == '\\')
				{
					builder.append(parseEscape());
				}
				else
				{
					builder.append(character);
				}
			}
			throw new IllegalArgumentException("Unclosed string.");
		}

		private char parseEscape()
		{
			if (index >= text.length())
			{
				throw new IllegalArgumentException("Unclosed escape.");
			}

			char character = text.charAt(index++);
			switch (character)
			{
				case '"':
				case '\\':
				case '/':
					return character;
				case 'n':
					return '\n';
				case 'r':
					return '\r';
				case 't':
					return '\t';
				case 'u':
					return parseUnicodeEscape();
				default:
					throw new IllegalArgumentException("Unsupported escape.");
			}
		}

		private char parseUnicodeEscape()
		{
			if (index + 4 > text.length())
			{
				throw new IllegalArgumentException("Invalid unicode escape.");
			}
			String hex = text.substring(index, index + 4);
			index += 4;
			try
			{
				return (char) Integer.parseInt(hex, 16);
			}
			catch (NumberFormatException ex)
			{
				throw new IllegalArgumentException("Invalid unicode escape.");
			}
		}

		private Number parseNumber()
		{
			int start = index;
			if (consume('-'))
			{
				if (index >= text.length() || !Character.isDigit(text.charAt(index)))
				{
					throw new IllegalArgumentException("Invalid number.");
				}
			}
			while (index < text.length() && Character.isDigit(text.charAt(index)))
			{
				index++;
			}
			try
			{
				return Integer.parseInt(text.substring(start, index));
			}
			catch (NumberFormatException ex)
			{
				throw new IllegalArgumentException("Invalid number.");
			}
		}

		private void skipWhitespace()
		{
			while (index < text.length() && Character.isWhitespace(text.charAt(index)))
			{
				index++;
			}
		}

		private boolean consume(char expected)
		{
			if (index < text.length() && text.charAt(index) == expected)
			{
				index++;
				return true;
			}
			return false;
		}

		private void expect(char expected)
		{
			if (!consume(expected))
			{
				throw new IllegalArgumentException("Expected " + expected + ".");
			}
		}
	}
}