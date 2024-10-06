package xyz.telosaddon.yuno.utils.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SerializeUtils {

	public static int parseHexRGB(String input) throws Exception {
		if (input == null) {
			throw new Exception();
		}

		Pattern hexPattern = Pattern.compile("^#[0-9A-Fa-f]{6}$");
		Matcher matcher = hexPattern.matcher(input);

		if (!matcher.matches()) {
			throw new Exception();
		}

		input = input.substring(1);
		return Integer.parseInt(input, 16);
	}

	public static int parseHexARGB(String input) throws Exception {
		if (input == null) {
			throw new Exception();
		}

		Pattern hexPattern = Pattern.compile("^#[0-9A-Fa-f]{8}$");
		Matcher matcher = hexPattern.matcher(input);

		if (!matcher.matches()) {
			throw new Exception();
		}

		input = input.substring(1);
		return Integer.parseUnsignedInt(input, 16);
	}
}
