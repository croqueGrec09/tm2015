package de.uni_koeln.spinfo.textengineering.ir.ranked;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class Ranker {

	/*
	 * Query und Index werden als Klassenvariablen direkt bei der Instantiierung
	 * gesetzt (siehe Konstruktor), da sie bei Aufruf der Methode rank() für
	 * jede Vergleichsoperation in sort benötigt werden, um die
	 * Cosinus-Ähnlichkeit jedes Documents des result-Sets zu ermitteln.
	 */
	private Document query;
	private InformationRetrieval index;

	public Ranker(String query, InformationRetrieval index) {
		// hier wird aus der query ein kleines Document erzeugt (text = query,
		// title = "Query")
		this.query = new Document(query, "Query");
		this.index = index;
	}

	public List<Document> rank(Set<Document> result) {

		// result wird zunächst in eine Liste umgewandelt:
		List<Document> ranked = new ArrayList<Document>(result);
		/*
		 * Wir setzen das Ranking des Ergebnis-Sets als einfache Sortierung um;
		 * Java stellt für Collections (Listen, Maps, etc) die Methode sort()
		 * bereit, der man einen Sortierschlüssel (einen Comparator) übergeben
		 * kann. Wir wollen Dokumente anhand ihrer Ähnlichkeit zur query
		 * sortieren, deshalb müssen wir uns zunächst einen geeigneten
		 * Comparator schreiben:
		 */
		Collections.sort(ranked, new Comparator<Document>() {
			@Override
			public int compare(Document d1, Document d2) {
				/*
				 * Wir sortieren alle Vektoren nach ihrer (Cosinus-) Ähnlichkeit
				 * zur Anfrage (query), dazu benötigen wir zunächst die
				 * Ähnlichkeiten von d1 zum Query und d2 zum Query:
				 */
				Double s1 = d1.similarity(query, index);
				Double s2 = d2.similarity(query, index);
				/*
				 * Anschließend sortieren wir nach diesen beiden Ähnlichkeiten.
				 * Wir wollen absteigende Ähnlichkeit, d.h. s2.compareTo(s1)
				 * statt s1.compareTo(s2) d.h. die höchsten Werte und damit
				 * besten Treffer zuerst:
				 */
				return s2.compareTo(s1);
			}
		});
		return ranked;// Rückgabe = sortierte Liste (nach Ähnlichkeit zur query)
	}

}
