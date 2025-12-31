package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReader {
	public static String readJSONFile(String filePath) {
		String text = "";
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				text = text + line;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return text;
	}
}
