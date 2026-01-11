package util;

import java.awt.Color;

public class HexAConverter {
	public static Color convert(String hex) {
		int r = Integer.valueOf(hex.substring(0, 2), 16);
		int g = Integer.valueOf(hex.substring(2, 4), 16);
		int b = Integer.valueOf(hex.substring(4, 6), 16);

		return new Color(r, g, b);
	}
}
