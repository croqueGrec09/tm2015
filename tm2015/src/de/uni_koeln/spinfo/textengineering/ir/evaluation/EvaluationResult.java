package de.uni_koeln.spinfo.textengineering.ir.evaluation;

/*
 * Ergebnisdarstellung einer Evaluation, kapselt im Wesentlichen die Werte für Precision, Recall, F-Maß.
 * toString() erzeugt eine Ausgabe der Form: "EvaluationResult mit p=0,31, r=0,92 and f=0,46".
 */

public class EvaluationResult {

	private double p;
	private double r;
	public double f;

	public EvaluationResult(double p, double r, double f) {
		this.p = p;
		this.r = r;
		this.f = f;
	}

	@Override
	public String toString() {
		/*
		 * In der toString-Darstellung können wir die Ausgabe vorformatieren,
		 * u.a. die Zahlen auf zwei Nachkommastellen formatieren (%.2f):
		 */
		return String.format("Ergebnis mit p=%.2f ,  r=%.2f, f=%.2f", p, r, f);
	}
}
