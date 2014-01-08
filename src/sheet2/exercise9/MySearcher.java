package sheet2.exercise9;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.lucene.index.*;
import org.apache.lucene.misc.*;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.FSDirectory;

public class MySearcher {

    // Path to index directory
    public static String indexDir = "./data/index";

    public static List<String> computeTopTerms(String freq, int k) throws IOException, Exception {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(
                indexDir)));

        TermStats[] highFreqTerms;
        if (freq.equals("cf")) {
            highFreqTerms = HighFreqTerms.getHighFreqTerms(reader, k, null, new HighFreqTerms.TotalTermFreqComparator());
        } else if (freq.equals("df")) {
            highFreqTerms = HighFreqTerms.getHighFreqTerms(reader, k, null, new HighFreqTerms.DocFreqComparator());
        } else {
            throw new RuntimeException("für freq sind nur cf und df gültige Parameter");
        }


        List<String> terms = new ArrayList<>();
        for (int i = 0; i < highFreqTerms.length; i++) {
            TermStats termStats = highFreqTerms[i];
            terms.add(termStats.termtext.utf8ToString());
        }
        return terms;
    }

    // Main method for testing
    public static void main(String[] args) throws IllegalArgumentException,
            IOException, ParseException, Exception {

        List<String> topterms = MySearcher.computeTopTerms("df", 100);
        for (String term : topterms) {
            System.out.println(term);
        }
    }
}
