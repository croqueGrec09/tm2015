package de.uni_koeln.spinfo.textengineering.ir.ranked;

public class TermWeighting {

	public static double tfIdf(String t, Document document, InformationRetrieval index) {

		/*
		 * Umsetzung der tfIdf-Formeln aus dem Seminar (siehe Folien)
		 */
		double tf = document.getTf(t);
		
		double n = index.getWorks().size();
		
		double df = index.getDocFreq(t);
		
		double idf = Math.log(n/df);

		double tfIdf = tf * idf;
		
		return tfIdf;
	}

	
	
	
}
