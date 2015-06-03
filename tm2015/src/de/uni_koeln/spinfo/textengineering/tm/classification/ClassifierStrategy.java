/**
 * Material for the course 'Text-Engineering', University of Cologne.
 * (http://www.spinfo.phil-fak.uni-koeln.de/spinfo-textengineering.html)
 * <p/>
 * Copyright (C) 2008-2009 Fabian Steeg
 * <p/>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.uni_koeln.spinfo.textengineering.tm.classification;

import de.uni_koeln.spinfo.textengineering.tm.document.Document;

/**
 * Strategy interface for classifier implementations.
 * @author Fabian Steeg
 */
public interface ClassifierStrategy {

    /**
     * @param document The document to train the classifier with
     * @param classLabel The correct class label for the document
     * @return The altered, trained classifier
     */
	ClassifierStrategy train(Document document);
	
    /**
     * @param document The document to classify
     * @return The class label for the document
     */
	String classify(Document document);
	
}
