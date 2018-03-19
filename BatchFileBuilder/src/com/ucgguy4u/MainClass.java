
package com.ucgguy4u;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will generate the OS specific script which we can use for
 * Update/Revert&Update/Update&PostponedConflicts/SVNStatus the Subversion SVN workspace user can append many more
 * options. Generated files can schedule according to needs.
 * 
 * @author ucguy4u
 */
public class MainClass {

	private static List<String> result = new ArrayList<String>();
	private static String pathToWorkspace = "";
	static String os = System.getProperty("os.name").toLowerCase();
	static String baseOS = "";
	static String fileExtension = "";

	public static void main(String[] args) throws IOException {

		if (isWindows()) {
			baseOS = "Windows";
			System.out.println("This is Windows");
			fileExtension = ".bat";
		} else if (isUnix()) {
			baseOS = "Linux";
			System.out.println("This is Unix or Linux");
			fileExtension = ".sh";
		}
		pathToWorkspace = args[0];
		pathToWorkspace = pathToWorkspace + File.separator;
		MainClass fileSearch = new MainClass();
		fileSearch.listf(pathToWorkspace);
		int count = fileSearch.getResult().size();
		if (count == 0) {
			System.out.println("\nNo result found!");
		} else {
			System.out.println("\nFound " + count + " result!\n");
			for (String matched : fileSearch.getResult()) {
				System.out.println("Found : " + matched);
			}
		}
		writetofile(fileSearch);
	}

	/**
	 * These methiod is used to write the scripts You can add many more options to generate scripts
	 * 
	 * @param fileSearch
	 * @throws IOException
	 */
	private static void writetofile(MainClass fileSearch) throws IOException {

		String svn_update = "svn update --depth infinity " + "\n\n";
		String svn_revert_and_update = "svn revert --depth=infinity .  " + "\n\n";
		String svn_update_postponed_conflict = "svn update --accept p --depth infinity  " + "\n\n";
		String svn_status = "svn status > status.txt" + "\n\n";
		BufferedWriter writer;
		writer = new BufferedWriter(new FileWriter("svn_update" + fileExtension));
		for (String matched : fileSearch.getResult()) {
			matched = "cd " + matched.replace(".svn", "") + "\n";
			writer.write(matched);
			writer.write(svn_update);
		}
		writer.close();
		writer = new BufferedWriter(new FileWriter("svn_revert_and_update" + fileExtension));
		for (String matched : fileSearch.getResult()) {
			matched = "cd " + matched.replace(".svn", "") + "\n";
			writer.write(matched);
			writer.write(svn_revert_and_update);
			writer.write(svn_update);
		}
		writer.close();
		writer = new BufferedWriter(new FileWriter("svn_update_postponed_conflict" + fileExtension));
		for (String matched : fileSearch.getResult()) {
			matched = "cd " + matched.replace(".svn", "") + "\n";
			writer.write(matched);
			writer.write(svn_update_postponed_conflict);
		}
		writer.close();
		writer = new BufferedWriter(new FileWriter("svn_status" + fileExtension));
		for (String matched : fileSearch.getResult()) {
			matched = "cd " + matched.replace(".svn", "") + "\n";
			writer.write(matched);
			writer.write(svn_status);
		}
		writer.close();
	}

	/**
	 * Pass the root directory name to method and it will return the .svn folder list
	 * 
	 * @param directoryName
	 */
	public void listf(String directoryName) {

		File directory = new File(directoryName);
		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isDirectory()) {
				listf(file.getAbsolutePath());
				if (file.getAbsolutePath().endsWith(".svn")) {
					result.add(file.getAbsoluteFile().toString());
					System.out.println(file.getAbsolutePath().toString());
				}
			}
		}
	}

	public List<String> getResult() {

		return result;
	}

	public static boolean isWindows() {

		return (os.indexOf("win") >= 0);
	}

	public static boolean isUnix() {

		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0);
	}
}