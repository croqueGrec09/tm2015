package de.uni_koeln.spinfo.textengineering.ir.booleanIR;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/*
 * Erweiterte Variante für 'PositionalIntersect': zusätzliche Methode 'of' für
 * die Schnittmenge zweier Maps
 */

public class Intersection {

	/*
	 * Implementierung der Listen-Intersection, die die Sortierung der Listen ausnutzt, fast Zeile-für-Zeile umgesetzt
	 * wie in Manning et al. 2008, S. 11, beschrieben.
	 */
	public static SortedSet<Integer> of(SortedSet<Integer> pl1, SortedSet<Integer> pl2) {

		SortedSet<Integer> answer = new TreeSet<Integer>();
		Iterator<Integer> it1 = pl1.iterator();
		Iterator<Integer> it2 = pl2.iterator();
		// ein einfaches 'next()' reicht leider nicht - s. Kommentar unten
		Integer p1 = nextOrNull(it1);
		Integer p2 = nextOrNull(it2);
		while (p1 != null && p2 != null) {
			if (p1 == p2) {
				answer.add(p1);
				p1 = nextOrNull(it1);
				p2 = nextOrNull(it2);
			} else if (p1 < p2) {
				p1 = nextOrNull(it1);
			} else {
				p2 = nextOrNull(it2);
			}
		}
		return answer;
	}

	/*
	 * Um nah am Pseudocode zu bleiben, müssen wir u.a. NoSuchElementExceptions vermeiden (wenn der Iterator keine
	 * Elemente mehr hat).
	 */
	protected static Integer nextOrNull(Iterator<Integer> i1) {
		return i1.hasNext() ? i1.next() : null;
	}

	// Eine alternative Umsetzung mithilfe der Java-API:
	public static SortedSet<Integer> ofApi(SortedSet<Integer> pl1, SortedSet<Integer> pl2) {
		// Wir brauchen ein neues set, da sonst das Teilergebnis verändert wird:
		SortedSet<Integer> answer = new TreeSet<Integer>(pl1);
		answer.retainAll(pl2);
		return answer;
	}

	/*
	 * Implementierung der PositionalIntersect, fast Zeile-für-Zeile umgesetzt wie in Manning et al. 2008, S. 42,
	 * beschrieben. Diese Variante erlaubt sog. 'proximity'-suchen ("finde Term1 und Term2 innnerhalb eines max Abstands
	 * von k Wörtern"). PositionalIntersect ist weitgehend analog zu normaler Intersection - im Grunde wird einfach das
	 * Statement answer.add(p1)) ersetzt durch das Handling der Positions-Listen.
	 */

	public static SortedMap<Integer, List<Integer>> of(SortedMap<Integer, List<Integer>> pl1,
			SortedMap<Integer, List<Integer>> pl2, int k) {

		Iterator<Integer> it1 = pl1.keySet().iterator();
		Iterator<Integer> it2 = pl2.keySet().iterator();
		// wir nehmen hier wieder das Hilfskonstrukt für den null-check:
		Integer p1 = nextOrNull(it1);// erstes Werk ...
		Integer p2 = nextOrNull(it2);

		SortedMap<Integer, List<Integer>> answer = new TreeMap<Integer, List<Integer>>();
		while (p1 != null && p2 != null) {
			if (p1 == p2) {
				// answer.add(p1); // wird ersetzt durch:

				List<Integer> l = new ArrayList<Integer>();
				Iterator<Integer> posIt1 = pl1.get(p1).iterator();
				Iterator<Integer> posIt2 = pl2.get(p2).iterator();
				Integer pp1 = nextOrNull(posIt1);
				Integer pp2 = nextOrNull(posIt2);
				while (pp1 != null) {
					while (pp2 != null) {
						if (Math.abs(pp1 - pp2) <= k) {
							l.add(pp2);
						} else if (pp2 > pp1) {
							break;
						}
						pp2 = nextOrNull(posIt2);
					}
					while (l.size() != 0 && Math.abs(l.get(0) - pp1) > k) {
						l.remove(0);
					}
					for (Integer p : l) {
						List<Integer> posList = answer.get(p1);
						if (posList == null) {
							posList = new ArrayList<Integer>();
						}
						/*
						 * abweichend zum Seminar hier zusätzlich noch eine contains-Abfrage, damit Positionen nur
						 * einmal im Ergebnis landen (die Verwendung von Sets würde einen größeren Umbau erfordern).
						 */
						if (!posList.contains(p)) {
							posList.add(p);
							answer.put(p1, posList);
						}
					}
					pp1 = nextOrNull(posIt1);
				}
				// der Rest des Algorithmus bleibt wie bisher:
				p1 = nextOrNull(it1);
				p2 = nextOrNull(it2);
			} else if (p1 < p2) {
				p1 = nextOrNull(it1);
			} else {
				p2 = nextOrNull(it2);
			}
		}
		return answer;
	}

}
