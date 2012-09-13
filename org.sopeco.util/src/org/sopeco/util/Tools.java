/**
 * 
 */
package org.sopeco.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.tools.ant.DirectoryScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A collection of auxiliary helper methods.
 * 
 * Some of the methods are based on utility classes of the CoreASM project
 * (www.coreasm.org).
 * 
 * @author Roozbeh Farahbod
 * 
 */
public class Tools {

	private static Logger logger = LoggerFactory.getLogger(Tools.class);

	public enum SupportedTypes {
		Double, Integer, String, Boolean;

		/**
		 * If the given class is supported, returns the supported type for that
		 * class. Otherwise, returns null.
		 * 
		 * @see #valueOf(String)
		 */
		public static SupportedTypes get(Class<?> c) {
			return get(c.getSimpleName());
		}

		/**
		 * If the given class name is supported, returns the supported type for
		 * that class. Otherwise, returns null.
		 */
		public static SupportedTypes get(String name) {
			if (Tools.strEqualName(name, Double.class.getSimpleName()))
				return Double;
			if (Tools.strEqualName(name, Integer.class.getSimpleName()))
				return Integer;
			if (Tools.strEqualName(name, String.class.getSimpleName()))
				return String;
			if (Tools.strEqualName(name, Boolean.class.getSimpleName()))
				return Boolean;

			return null;
		}

		public static List<String> asList() {
			ArrayList<String> supportedTypes = new ArrayList<String>();
			for (SupportedTypes t : EnumSet.allOf(SupportedTypes.class)) {
				supportedTypes.add(t.toString());
			}
			return supportedTypes;
		}

	};

	private static String eol;

	/**
	 * Returns the next word in the src string after the beginWith portion
	 */
	public static String nextWordAfter(String src, String beginWith) {
		final int index = src.indexOf(beginWith);
		if (index < 0)
			return "";

		final String rest = src.substring(index).replace(beginWith, "");
		StringTokenizer tk = new StringTokenizer(rest, " ,");
		if (tk.hasMoreTokens())
			return tk.nextToken();
		else
			return "";
	}

	/**
	 * Reads the lines of the given file.
	 * 
	 */
	public static List<String> readLines(String fileName) throws IOException {
		List<String> result = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

		String line = "";

		do {
			line = reader.readLine();
			if (line != null)
				result.add(line);
		} while (line != null);

		return result;
	}

	/**
	 * Reads the lines of the given URL.
	 */
	public static List<String> readLines(URL url) throws IOException {
		List<String> result = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

		String line = "";

		do {
			line = reader.readLine();
			if (line != null)
				result.add(line);
		} while (line != null);

		return result;
	}

	/**
	 * Writes the given lines to a file.
	 */
	public static void writeLines(String fileName, List<String> lines) throws IOException {
		PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));

		for (String line : lines)
			pw.println(line);

		pw.close();
	}

	/**
	 * Writes the given content to the file.
	 */
	public static void printToFile(String content, String fileName) throws IOException {
		final PrintWriter pw = new PrintWriter(new FileWriter(fileName));
		pw.println(content);
		pw.close();
	}

	/**
	 * Writes the given text to a file.
	 */
	public static void writeText(String fileName, String text) throws IOException {
		PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));

		pw.print(text);

		pw.close();
	}

	// /**
	// * Returns a list of files that their names match the given pattern.
	// *
	// * @param pattern filename pattern
	// * @return array of file names
	// */
	// public static String[] getFileNames(String baseDir, String pattern) {
	// DirectoryScanner scanner = new DirectoryScanner();
	// scanner.setIncludes(new String[]{pattern});
	// scanner.setBasedir(baseDir);
	// scanner.setCaseSensitive(false);
	// scanner.scan();
	// final String[] result = scanner.getIncludedFiles();
	// if (result == null)
	// return new String[] {};
	// else
	// return result;
	// }

	/**
	 * Reads the content of the given stream as a string
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String readFromInputStream(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		StringBuffer content = new StringBuffer();
		String line;

		while ((line = reader.readLine()) != null)
			content.append(line + " ");

		return content.toString();
	}

	/**
	 * Reads the content of the given file.
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 * 
	 * @see #readFromInputStream(InputStream)
	 */
	public static String readFromFile(String fileName) throws FileNotFoundException, IOException {
		return readFromInputStream(new FileInputStream(fileName));
	}

	/**
	 * Formats a <code>double</code> value into a <code>String</code> with
	 * <i>d</i> digits after decimal point.
	 */
	public static String dFormat(double v, int d) {
		double p = Math.pow(10, d);
		return String.valueOf(Math.round(v * p) / p);
	}

	/**
	 * @return a system independent EOL string.
	 */
	public static String getEOL() {
		if (eol == null) {
			eol = System.getProperty("line.separator");
			if (eol == null)
				eol = "\n";
		}
		return eol;
	}

	/**
	 * Checks the equivalency of two strings ignoring lower/upper cases.
	 * 
	 * @return <code>true</code> if the two strings are equal.
	 */
	public static boolean strEqualCaseInsensitive(String a, String b) {
		return a.toLowerCase().equals(b.toLowerCase());
	}

	/**
	 * Checks the equivalency of two names ignoring lower/upper cases. It trims
	 * the strings before calling
	 * {@link #strEqualCaseInsensitive(String, String)}.
	 * 
	 * @return <code>true</code> if the two names are equal.
	 */
	public static boolean strEqualName(String a, String b) {
		return strEqualCaseInsensitive(a.trim(), b.trim());
	}

	/**
	 * Checks if the given name is the simple name of the given class. The test
	 * is case insensitive.
	 */
	public static boolean isClassName(String name, Class<?> c) {
		return strEqualName(name, c.getSimpleName());
	}

	/**
	 * Returns a list of files that their names match the given pattern.
	 * 
	 * @param pattern
	 *            filename pattern
	 * @return array of file names
	 */
	public static String[] getFileNames(String baseDir, String pattern) {
		DirectoryScanner scanner = new DirectoryScanner();
		scanner.setIncludes(new String[] { pattern });
		scanner.setBasedir(baseDir);
		scanner.setCaseSensitive(false);
		scanner.scan();
		final String[] result = scanner.getIncludedFiles();
		if (result == null)
			return new String[] {};
		else
			return result;
	}

	/**
	 * Returns a list of directories that their names match the given pattern.
	 * 
	 * @param pattern
	 *            filename pattern
	 * @return array of file names
	 */
	public static String[] getDirNames(String baseDir, String pattern) {
		DirectoryScanner scanner = new DirectoryScanner();
		scanner.setIncludes(new String[] { pattern });
		scanner.setBasedir(baseDir);
		scanner.setCaseSensitive(false);
		scanner.scan();
		final String[] result = scanner.getIncludedDirectories();
		if (result == null)
			return new String[] {};
		else
			return result;
	}

	/**
	 * Returns the index of the given object in the given array if it exsists.
	 * It uses the {@link Object#equals(Object)} method.
	 * 
	 * @param obj
	 *            object to look for
	 * @param array
	 *            array of objects
	 * @return the index of the object or -1 if the object doesn't exist
	 */
	public static int exists(Object obj, Object[] array) {
		for (int i = 0; i < array.length; i++)
			if (array[i].equals(obj))
				return i;
		return -1;
	}

	/**
	 * Detects and returns the root folder of the running application.
	 */
	public static String getRootFolder() {
		return getRootFolder(null);
	}

	/**
	 * Detects and returns the root folder of the running application.
	 */
	public static String getRootFolder(Class<?> mainClass) {
		if (mainClass == null)
			mainClass = Tools.class;

		final String baseErrorMsg = "Cannot locate root folder.";

		final String classFile = mainClass.getName().replaceAll("\\.", "/") + ".class";
		final URL classURL = ClassLoader.getSystemResource(classFile);

		String fullPath = "";
		String sampleClassFile = "/org/sopeco/util/Tools.class";
		if (classURL == null) {
			Tools tempObject = new Tools();
			fullPath = tempObject.getClass().getResource(sampleClassFile).toString();
			// logger.warn("{} The application may be running in an OSGi container.",
			// baseErrorMsg);
			// return ".";
		} else {
			fullPath = classURL.toString();
		}

		try {
			fullPath = URLDecoder.decode(fullPath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.warn("{} UTF-8 encoding is not supported.", baseErrorMsg);
			return ".";
		}

		if (fullPath.indexOf("file:") > -1) {
			fullPath = fullPath.replaceFirst("file:", "").replaceFirst(classFile, "");
			fullPath = fullPath.substring(0, fullPath.lastIndexOf('/'));
		}
		if (fullPath.indexOf("jar:") > -1) {
			fullPath = fullPath.replaceFirst("jar:", "").replaceFirst("!" + classFile, "");
			fullPath = fullPath.substring(0, fullPath.lastIndexOf('/'));
		}
		if (fullPath.indexOf("bundleresource:") > -1) {
			fullPath = fullPath.substring(0, fullPath.indexOf(sampleClassFile));
		}

		// replace the java separator with the
		fullPath = fullPath.replace('/', File.separatorChar);

		// remove leading backslash
		if (fullPath.startsWith("\\")) {
			fullPath = fullPath.substring(1);
		}

		// remove the final 'bin'
		final int binIndex = fullPath.indexOf(File.separator + "bin");
		if (binIndex == fullPath.length() - 4)
			fullPath = fullPath.substring(0, binIndex);

		logger.debug("Root folder is detected at {}.", fullPath);

		return fullPath;
	}

	/**
	 * Returns the average (mean) of the given values.
	 * 
	 * @param values
	 *            a collection of values
	 * @return the average as a double value
	 */
	public static double average(Collection<? extends Number> values) {
		double result = 0;

		if (values.size() == 0)
			return 0;
		else {
			for (Number n : values) {
				result += n.doubleValue();
			}
		}

		return result / values.size();
	}

	/**
	 * Returns the average (mean) of the given values.
	 * 
	 * @param values
	 *            an array of values
	 * @return the average as a double value
	 */
	public static double average(double[] values) {
		double result = 0;

		if (values.length == 0)
			return 0;
		else {
			for (Number n : values) {
				result += n.doubleValue();
			}
		}

		return result / values.length;

	}

	/**
	 * Returns the population standard deviation of the given values.
	 * 
	 * @param values
	 *            input values
	 * @return the population standard deviation
	 */
	public static double stdDev(double[] values) {
		double result = 0;

		if (values.length < 1)
			throw new IllegalArgumentException("Cannot calculated standard deviation on an empty set.");

		final double mean = average(values);

		for (double v : values) {
			result += (v - mean) * (v - mean);
		}

		result = Math.sqrt(result / values.length);

		return result;
	}

	/**
	 * Returns the population standard deviation of the given values.
	 * 
	 * @param values
	 *            input values
	 * @return the population standard deviation
	 */
	public static double stdDev(Double[] array) {
		double[] temp = new double[array.length];
		for (int i = 0; i < array.length; i++)
			temp[i] = array[i];
		return stdDev(temp);
	}

	/**
	 * Returns the population standard deviation of the given values.
	 * 
	 * @param values
	 *            input values
	 * @return the population standard deviation
	 */
	public static double stdDev(Collection<Double> values) {
		return stdDev(values.toArray(new Double[] {}));
	}

	/**
	 * Returns a time stamp of the format "yy.MM.dd - HH:mm" for the given date.
	 * 
	 * @param date
	 */
	public static String getTimeStamp(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yy.MM.dd - HH:mm");
		return formatter.format(date);
	}

	/**
	 * Returns a time stamp for the current time and date.
	 * 
	 * @see #getTimeStamp(Date)
	 */
	public static String getTimeStamp() {
		return getTimeStamp(new Date());
	}

	/**
	 * Adds a sequence of the given character to the beginning of the given
	 * string until it reaches the given length.
	 * 
	 * @param src
	 *            source string
	 * @param filler
	 *            filler character
	 * @param fixedLen
	 *            desired length of the resulting string
	 * @return
	 */
	public static String extendStr(String src, char filler, int fixedLen) {
		String result = src;
		while (result.length() < fixedLen)
			result = filler + result;
		return result;
	}

	/**
	 * Performs a simple tokenization (!) on the given source string.
	 * 
	 * @param src
	 * @param separator
	 * @return
	 */
	public static String[] tokenize(String src, String separator) {
		StringTokenizer tokenizer = new StringTokenizer(src, separator);

		String[] result = new String[tokenizer.countTokens()];
		int i = 0;
		while (tokenizer.hasMoreTokens()) {
			result[i] = tokenizer.nextToken();
			i++;
		}

		return result;
	}

	/**
	 * Given a base directory and a path to a file, it concatinates the two
	 * parts and takes care of missing file separators.
	 * 
	 * @param baseDir
	 *            base directory
	 * @param fileName
	 *            file name
	 * @return the absolute path to the file
	 */
	public static String concatFileName(String baseDir, String fileName) {
		// cleanup
		baseDir = baseDir.trim();
		fileName = fileName.trim();

		if (baseDir.lastIndexOf(File.separator) != baseDir.length() - 1)
			baseDir = baseDir + File.separator;

		String concat = baseDir + fileName;

		return concat;
	}

	/**
	 * Given a base directory and a path to a file, it creates a full path to
	 * the file. If the base directory is not absolute, it adds the application
	 * root directory It also takes care of missing file separators.
	 * 
	 * @param baseDir
	 *            base directory
	 * @param fileName
	 *            file name
	 * @param rootFolder
	 *            root folder of the application
	 * @return the absolute path to the file
	 */
	public static String toFullPath(String baseDir, String fileName, String rootFolder) {
		String result = concatFileName(baseDir, fileName);

		File file = new File(result);
		if (!file.isAbsolute()) {
			result = concatFileName(rootFolder, result);
		}

		return result;
	}

	/**
	 * Returns true if the given filename has is an absolute path.
	 * 
	 * @param fileName
	 *            a file name
	 */
	public static boolean isAbsolutePath(String fileName) {
		final File file = new File(fileName);
		return file.isAbsolute();
	}

	/**
	 * Returns <code>true</code> if the given file exists.
	 * 
	 * @param fileName
	 *            a file name
	 */
	public static boolean fileExists(String fileName) {
		final File file = new File(fileName);
		return file.exists();
	}
}
