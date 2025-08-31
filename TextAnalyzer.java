package epicor.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TextAnalyzer {

	// 3A,3C - A set of pronouns,conjunctions,prepositions and articles to be
	// excluded
	// from word frequency analysis
	private static final Set<String> exclusions = Set.of("a", "an", "the", "i", "you", "he", "she", "it", "we", "they",
			"me", "him", "her", "us", "them", "my", "your", "his", "its", "our", "their", "yours", "hers", "ours",
			"theirs", "this", "that", "these", "those", "who", "whom", "whose", "which", "what", "and", "or", "but",
			"so", "for", "nor", "yet", "although", "because", "about", "above", "after", "along", "around", "at", "by",
			"from", "in", "into", "like", "near", "of", "off", "on", "out", "over", "past", "with", "is", "are", "was",
			"were", "be", "been", "being", "to", "as", "there", "all", "have", "has", "had", "do", "does", "did", "can",
			"could", "shall", "should", "will", "would", "may", "might", "must");

	public static void main(String[] args) throws IOException {
		// To get the start time
		Instant start = Instant.now();

		String filePath = "moby.txt";

		// Get entire content into a String
		String content = new String(Files.readAllBytes(Paths.get(filePath)));

		// Validate if content is empty
		if (content == null || content.isBlank()) {
			System.out.println("moby.txt is empty");
			return;
		}

		// method to process the content
		analyseTextFile(content);

		// To get end time
		Instant end = Instant.now();
		double durationInSeconds = Duration.between(start, end).toMillis() / 1000.0;
		System.out.println("Total Processing Time: " + durationInSeconds + " seconds");
	}

	public static void analyseTextFile(String content) {
		// 4A,3B - Convert all characters to lowercase, remove 's, remove
		// non-lettercharacters
		content = content.toLowerCase().replaceAll("'s\\b", "") // remove 's
				.replaceAll("[^a-z\\s]", " ") // keep only letters and whitespace
				.trim();

		// Split text into words using whitespace as delimiter
		String[] words = content.split("\\s+");

		int wordCount = 0;

		// Storing frequency of each word
		Map<String, Integer> frequencyMap = new HashMap<>();

		// Iterate through words and count frequencies excluding stop words
		for (String word : words) {
			if (exclusions.contains(word))
				continue;
			frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
			wordCount++;
		}

		// 2A - Print total number of valid words
		System.out.println("2A - Total number of words in the text:" + wordCount);

		// 2B - Finding top 5 Most frequent words
		List<Map.Entry<String, Integer>> top5Words = frequencyMap.entrySet().stream()
				.sorted((a, b) -> b.getValue().compareTo(a.getValue())) // sort in descending order of frequency
				.limit(5) // taking top 5
				.collect(Collectors.toList());

		System.out.println("2B - Top 5 Words: "
				+ top5Words.stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(", ")));

		// 2C - Find all unique words in alphabetical order
		List<String> uniqueWords = frequencyMap.entrySet().stream().filter(entry -> entry.getValue() == 1) // filter
																											// words
																											// with
																											// frequency
																											// 1

				.map(Map.Entry::getKey) // get the word itself from map
				.sorted() // sort alphabetically
				.limit(50) // taking top 50
				.collect(Collectors.toList());

		System.out.println("2C - Unique Words:" + uniqueWords);
	}
}