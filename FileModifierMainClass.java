package com.ucguy4u.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * This class will find the specified file in the workspace recursively and store the location. This file path is passed
 * to the line appender method. Line appender will write the specified data at the beginning of the file. Operation is
 * done on same file no duplicate file is created.
 * 
 * @author chauhanuday
 */
public class FileModifierMainClass {

	private static List<Path> result = new ArrayList<>();
	private static String pathToWorkspace = "";
	private static String filename = "";
	public static final String DIRLOCATION = System.getProperty("user.dir") + File.separator + "configurations.properties";
	static String data = "";
	public static void main(String[] args) throws IOException {

		Properties dbDetails = loadProperties(DIRLOCATION);
		pathToWorkspace = dbDetails.getProperty("workspaceLocation");
		filename = dbDetails.getProperty("filename");
		data = dbDetails.getProperty("data");
		//pathToWorkspace = args[0];
		//filename = args[1];

		FileModifierMainClass fileSearch = new FileModifierMainClass();
		fileSearch.listf(pathToWorkspace);
		int count = fileSearch.getResult().size();
		if (count == 0) {
			System.out.println("\nNo result found!");
		} else {
			System.out.println("\nFound " + count + " result!\n");
			for (Path matched : fileSearch.result) {
				System.out.println("Found : " + matched);
			}
		}
		writetofile(fileSearch);
	}

	/**
	 * These methiod is used to append the data at the begining of the file
	 * 
	 * @param
	 * @throws IOException
	 */
	private static void writetofile(FileModifierMainClass fileSearch) throws IOException {

		//String data = "set sqlblanklines on;\r\n" + "SET DEFINE OFF;\r\n" + "set sqlprefix `; \n";
		try {
			for (Path element : fileSearch.result) {
				InputStream in = new FileInputStream(new File(element.toString()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder out = new StringBuilder();
			out.append(data);
			String line;
			while ((line = reader.readLine()) != null) {
				out.append(line + "\n");
			}

			OutputStream os = null;
			try {
					os = new FileOutputStream(new File(element.toString()));
				os.write(out.toString().getBytes(), 0, out.length());
			} catch (IOException e) {
				e.printStackTrace();
				} finally {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			}
	} catch (IOException e) {

		e.printStackTrace();

		}
	}

	/**
	 * Pass the root directory name to method and it will return the filepath
	 * 
	 * @param directoryName
	 * @throws IOException
	 */
	public void listf(String directoryName) throws IOException {

		result = Files.find(Paths.get(directoryName), Integer.MAX_VALUE, (filePath, fileAttr) -> fileAttr.isRegularFile())
		              .filter(PathtoFile -> PathtoFile.getFileName().endsWith(filename))
		     .collect(Collectors.toList());
	}

	public List<Path> getResult() {

		return result;
	}

	public static Properties loadProperties(String filePath) {

		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(filePath));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return properties;
	}
	
}