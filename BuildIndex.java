import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.*;

import acm.program.ConsoleProgram;
/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * BuildIndex
 * 
 * Reads a text file and counts unique words, after filtering by length, ending
 * and plural. Writes result to a csv file.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Khen Brian N. K. Quartey
 */
public class BuildIndex extends ConsoleProgram {

	private Map<String, Index> words = new TreeMap<String, Index>();

	private ArrayList<String> stopWords = new ArrayList<>();

	int lineNumber = 0;

	public void run() {
		setSize(400, 200);
		setFont("monospaced-18");

		readfileStopWords();

		String docName = readLine("Enter document to scan: (TomSawyer.txt) ");

		readTextAndBuildIndex(docName);
		try {
			writeToCsv();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// method for reading and building the index of the words
	private void readTextAndBuildIndex(String docName) {

		String line = "";

		try {
			FileReader fileName = new FileReader(docName);
			BufferedReader read = new BufferedReader(fileName);

			while (true) {
				line = read.readLine();
				if (line == null) {
					break;
				} else {
					lineNumber++;
				}
				StringTokenizer st = new StringTokenizer(line, "[]\"',;:.!?()-/ \t\n\r\f");
				while (st.hasMoreTokens()) {
					String word = st.nextToken().trim();
					String key = word.toLowerCase();
					ArrayList<Integer> storeLine = new ArrayList<>();
					Index index = new Index();
					key = filterStringsLessThan(key, 0);
					key = filterBadEndings(key);
					key = filterPlural(key);
					addToHashMap(key, storeLine, index);
				}
			}

			println(words.toString());

			fileName.close();
			read.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// method for add words to the hashmap
	private void addToHashMap(String key, ArrayList<Integer> storeLine, Index index) {
		if (key != null && !stopWords.contains(key)) {
			if (!words.containsKey(key)) {
				index.setCount(1);
				storeLine.add(lineNumber);
				index.setLines(storeLine);
				words.put(key, index);
			} else {
				index = words.get(key);
				int value = index.getCount();
				value++;
				index.setCount(value);
				storeLine = index.getLines();
				storeLine.add(lineNumber);
				index.setLines(storeLine);
				words.put(key, index);
			}
		}
	}

	// method for filtering Plural words
	private String filterPlural(String word) {
		if (word != null) {
			if (word.endsWith("s")) {
				word = word.substring(0, word.length() - 1);
			}
			return word;
		}
		return null;
	}

	// private String[] badEndings = { "ly", "ial", "ive", "ous", "ed" };
	private String filterBadEndings(String word) {
		if (word != null) {
			String w = word.toLowerCase();
			if (!w.endsWith("ly") && !w.endsWith("ial") && !w.endsWith("ive") && !w.endsWith("ous")
					&& !w.endsWith("ed")) {
				return word;
			}
		}
		return null;
	}

	// method for excluding a specific word length
	private String filterStringsLessThan(String word, int len) {
		if (word != null && word.length() >= len) {
			return word;
		}
		return null;
	}

	// method for reading and storing stop words
	private void readfileStopWords() {
		// open file
		try {
			FileReader fr = new FileReader("stopwords.txt");
			BufferedReader rd = new BufferedReader(fr);
			while (true) {
				String line = rd.readLine();
				if (line == null) {
					break;
				}
				stopWords.add(line);
			}
			rd.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// method for creating and writing to a csv file
	private void writeToCsv() throws IOException {
		FileWriter writer = new FileWriter("indexBuilder.csv");
		for (Map.Entry<String, Index> entry : words.entrySet()) {
			String key = entry.getKey();
			Index value = entry.getValue();
			writer.append(key + "," + value.getCount() + "," + value.getLines() + "\n");
		}
		writer.close();
	}

}
