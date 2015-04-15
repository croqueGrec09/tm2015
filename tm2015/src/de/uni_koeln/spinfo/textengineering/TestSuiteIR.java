package de.uni_koeln.spinfo.textengineering;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.uni_koeln.spinfo.textengineering.ir.basicIR.TestBasicIR;
import de.uni_koeln.spinfo.textengineering.ir.booleanIR.TestBooleanIR;
import de.uni_koeln.spinfo.textengineering.ir.evaluation.TestEval;
import de.uni_koeln.spinfo.textengineering.ir.lucene.TestLucene;
import de.uni_koeln.spinfo.textengineering.ir.rankedIR.TestRankedIR;

/*
 * Eine Suite ermöglicht das ausführen von mehreren Klassen mit Tests. Wird ausgefuehrt mit Run As
 * -> JUnit Test.
 */
/**
 * Main test suite for the Information Retrieval exercises.
 * 
 * @author Fabian Steeg, Claes Neuefeind (c.neuefeind@uni-koeln.de)
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ TestBasicIR.class, TestBooleanIR.class, TestRankedIR.class, TestEval.class, TestLucene.class})
public class TestSuiteIR {
}
