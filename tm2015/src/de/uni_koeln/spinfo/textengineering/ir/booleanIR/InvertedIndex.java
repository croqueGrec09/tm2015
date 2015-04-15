package de.uni_koeln.spinfo.textengineering.ir.booleanIR;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import de.uni_koeln.spinfo.textengineering.ir.basicIR.Corpus;
import de.uni_koeln.spinfo.textengineering.ir.basicIR.InformationRetrieval;

public class InvertedIndex implements InformationRetrieval {

	// der invertierte Index für die spätere Suche
	private Map<String, SortedSet<Integer>> invIndex;
	// eine Instanz des Preprocessors für Indexierung und Query-Verarbeitung
	private static final Preprocessor PREPROCESSOR = new Preprocessor();

	public InvertedIndex(Corpus corpus) {
		long start = System.currentTimeMillis();
		invIndex = index(corpus);
		System.out.println("Index erstellt, Dauer: "
				+ (System.currentTimeMillis() - start) + " ms.");
	}

	private Map<String, SortedSet<Integer>> index(Corpus corpus) {
		HashMap<String, SortedSet<Integer>> index = new HashMap<String, SortedSet<Integer>>();
		// wir indexieren Werk für Werk:
		List<String> works = corpus.getWorks();
		for (int i = 0; i < works.size(); i++) {
			List<String> terms = PREPROCESSOR.process(works.get(i));
			for (String t : terms) {
				// wir holen uns die postings-Liste des terms aus dem Index:
				SortedSet<Integer> postings = index.get(t);
				/*
				 * beim ersten Vorkommen des Terms ist diese noch leer (null),
				 * also legen wir uns einfach eine neue an:
				 */
				if (postings == null) {
					postings = new TreeSet<Integer>();
					index.put(t, postings);
				}
				/*
				 * Der Term wird indexiert, indem die Id des aktuellen Werks (=
				 * der aktuelle Zählerwert) der postings-list hinzugefügt wird:
				 */
				postings.add(i);
			}
			// printSortedIndexTerms(index);//optionale Ausgabe der Indexterme
		}
		return index;
	}

	@Override
	public Set<Integer> search(String query) {

		long start = System.currentTimeMillis();
		// gleicher Preprocessor wie bei Indexierung!
		List<String> queries = PREPROCESSOR.process(query);
		/*
		 * Wir holen uns zunächst die Postings-Listen der Teilqueries:
		 */
		List<SortedSet<Integer>> allPostings = new ArrayList<SortedSet<Integer>>();
		for (String q : queries) {
			SortedSet<Integer> postings = invIndex.get(q);
			allPostings.add(postings);
		}
		/*
		 * Damit wir die Effizienz des Algorithmus aus Manning et al erreichen,
		 * müssen die einzelnen Postings-Listen nach Länge sortiert sein:
		 */
		Collections.sort(allPostings, new Comparator<SortedSet<Integer>>() {
			@Override
			public int compare(SortedSet<Integer> o1, SortedSet<Integer> o2) {
				return Integer.valueOf(o1.size()).compareTo(o2.size());
			}
		});
		// Ergebnis ist die Schnittmenge (Intersection) der ersten Liste...
		SortedSet<Integer> result = allPostings.get(0);
		// ... mit allen weiteren:
		for (SortedSet<Integer> set : allPostings) {
			result = Intersection.of(result, set);
			// Hier behandeln wir die Suchwörter als UND-Verknüpft!
		}
		System.out.println("Suchdauer: " + (System.currentTimeMillis() - start)
				+ " ms.");
		return result;
	}

	/*
	 * Ausgabe der Indexterme:
	 */
	@SuppressWarnings("unused")
	private void printSortedIndexTerms(Map<String, SortedSet<Integer>> index) {
		TreeSet<String> terms = new TreeSet<String>(index.keySet());
		for (String string : terms) {
			System.out.println(string);
		}
		System.out.println("Anzahl Terme: " + terms.size());
	}
}
