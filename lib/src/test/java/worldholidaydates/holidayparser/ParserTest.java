package worldholidaydates.holidayparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

/**
 * A test class for the {@link HolidayParser}, that takes data from the date.txt
 * file and tests the parser against it. A large portion of this code was taken
 * from the testing suite of <a href="https://github.com/simonpoole/OpeningHoursParser">OpeningHoursParser</a>,
 * specifically the file: <a href="https://github.com/simonpoole/OpeningHoursParser/blob/master/src/test/java/ch/poole/openinghoursparser/DataTest.java">DataTest.java</a>
 * 
 * @author (large portion) Simon Poole
 */
public class ParserTest {

    boolean quiet = true; // stops large amounts of not very helpful output
    
    /**
     * Compare strict mode output
     */
    @Test
    public void dataTest() {
        parseData("test-data/date.txt", false, "test-data/date.txt-result-strict", "test-data/date.txt-stats");
        parseData("test-data/date.txt", true, "test-data/date.txt-debug-result-strict", "test-data/date.txt-stats");
    }

    /**
     * This completes successfully if parsing gives the same success result and for successful parses the same
     * regenerated OH string
     * 
     * @param inputFile input data file
     * @param strict if true use strict mode
     * @param debug if true produce debug output
     * @param resultsFile file to write results to
     */
    private void parseData(String inputFile, boolean debug, String resultsFile, String statsFile) {
        int differences = 0;
        int successful = 0;
        int errors = 0;
        int lexical = 0;
        int lineNumber = 1;
        BufferedReader inputRules = null;
        BufferedReader inputExpected = null;
        BufferedWriter outputExpected = null;
        BufferedWriter outputFail = null;
        BufferedWriter outputStats = null;
        String line = null;
        try {
            inputRules = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8));
            try {
                inputExpected = new BufferedReader(new InputStreamReader(new FileInputStream(resultsFile), StandardCharsets.UTF_8));
            } catch (FileNotFoundException fnfex) {
                System.out.println("File not found " + fnfex.toString());
            }
            outputExpected = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(inputFile + "-result" + (debug ? "-debug" : "")), StandardCharsets.UTF_8));
            outputFail = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(inputFile + "-fail" + (debug ? "-debug" : "")), StandardCharsets.UTF_8));
            outputStats = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(statsFile + (debug ? "-debug" : "")), StandardCharsets.UTF_8));

            String expectedResultCode = null;
            String expectedResult = null;
            while ((line = inputRules.readLine()) != null) {
                if ("".equals(line.trim())) {
                    continue;
                }
                if (inputExpected != null) {
                    String[] expected = inputExpected.readLine().split("\t");
                    expectedResultCode = expected[0];
                    if (expected.length == 2) {
                        expectedResult = expected[1];
                    } else {
                        expectedResult = null;
                    }
                }
                try {
                    HolidayParser parser = new HolidayParser(new ByteArrayInputStream(line.getBytes()));
                    Rule rule = parser.parse();
                    LocalDateTime actual = rule.calculate(2021);
                    String result = (actual != null) ? actual.toString() : null;
                    successful++;
                    outputExpected.write("0\t" + result + "\n");
                    if (expectedResultCode != null && (!"0".equals(expectedResultCode) || (expectedResult != null && !expectedResult.equals(result)))) {
                        System.out.println("Line " + lineNumber + " expected " + expectedResult + " got " + result);
                        differences++;
                    }
                } catch (ParseException pex) {
                    if (pex.toString().contains("Lexical")) {
                        lexical++;
                    } else if (!quiet) {
                        System.out.println("Parser exception for " + line + " " + pex.toString());
                    }
                    errors++;
                    outputExpected.write("1\n");
                    outputFail.write(lineNumber + "\t" + line + "\t" + pex.toString() + "\n");
                    if (expectedResultCode != null && !"1".equals(expectedResultCode)) {
                        System.out.println("Line " + lineNumber + " expected " + expectedResultCode + " got 1");
                        differences++;
                    }
                } catch (NumberFormatException nfx) {
                    if (!quiet) {
                        System.out.println("Parser exception for " + line + " " + nfx.toString());
                    }
                    lexical++;
                    errors++;
                    outputExpected.write("2\n");
                    if (expectedResultCode != null && !"2".equals(expectedResultCode)) {
                        System.out.println("Line " + lineNumber + " expected " + expectedResultCode + " got 2");
                        differences++;
                    }
                } catch (Error err) {
                    if (err.toString().contains("Lexical")) {
                        lexical++;
                    } else if (!quiet) {
                        System.out.println("Parser err for " + line + " " + err.toString());
                    }
                    errors++;
                    outputExpected.write("3\n");
                    outputFail.write(lineNumber + "\t" + line + "\t" + err.toString() + "\n");
                    if (expectedResultCode != null && !"3".equals(expectedResultCode)) {
                        System.out.println("Line " + lineNumber + " expected " + expectedResultCode + " got 3");
                        differences++;
                    }
                }
                lineNumber++;
            }
            // record statistics
            // fail("Is this shit working?");
            outputStats.write("Successful: " + successful + "\n");
            outputStats.write("Error: " + errors + "\n");
            outputStats.write("Total: " + (lineNumber-1) + "\n");
        } catch (FileNotFoundException fnfex) {
            System.out.println("File not found " + fnfex.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (AssertionError ae) {
            System.out.println("Assertion failed for " + line);
            throw ae;
        } finally {
            if (inputRules != null) {
                try {
                    inputRules.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputExpected != null) {
                try {
                    outputExpected.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputFail != null) {
                try {
                    outputFail.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStats != null) {
                try {
                    outputStats.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (differences > 0) {
            fail(Integer.toString(differences) + " differences found.");
        }
        System.out.println("Successful " + successful + " errors " + errors + " of which " + lexical + " are lexical errors");
    }
}
