/**
 * Material for the course 'Text-Mining', University of Cologne.
 * (http://www.spinfo.phil-fak.uni-koeln.de/spinfo-textmining.html)
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
package de.uni_koeln.spinfo.textengineering.tm.corpus;

import java.util.List;
import java.util.Set;

import de.uni_koeln.spinfo.textengineering.tm.document.Document;


/**
 * Interface to a corpus.
 * @author Fabian Steeg
 */
public interface Corpus {

    /** @return All documents in the corpus */
    List<Document> getDocuments();

    /**
     * @param query The query
     * @return Documents from a source that contains the given query
     */
    List<Document> getDocumentsForSource(String query);

    /**
     * @param query The query
     * @return Documents whose topic contains the given query
     */
    List<Document> getDocumentsForTopic(String query);

    /** @param document The document to add */
    void add(Document document);

    /** @param documents The documents to add */
    void addAll(List<? extends Document> documents);

    /** Close the connection to the source of the corpus, if any. */
    void close();

    /**
     * @return All terms in the corpus
     */
    Set<String> getTerms();

    /**
     * @param dictionaryTerm The term to find the document frequency for
     * @return The document frequency of the term, i.e. the number of documents
     *         in this corpus that contain the given term
     */
    Integer getDocumentFrequencyOf(String dictionaryTerm);

    /**
     * @return The number of documents in the corpus
     */
    Integer getNumberOfDocuments();
}
