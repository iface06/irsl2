package sheet2.exercise9;

// TODO Implement this class!
import java.io.Reader;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.core.LowerCaseTokenizer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.util.Version;

public class MyStemAnalyzer extends Analyzer {

    // http://lucene.apache.org/core/4_0_0/analyzers-common/org/apache/lucene/analysis/en/PorterStemFilter.html
    @Override
    protected TokenStreamComponents createComponents(String string, Reader reader) {
        Tokenizer source = new LowerCaseTokenizer(Version.LUCENE_45, reader);
        return new TokenStreamComponents(source, new PorterStemFilter(source));
    }
}
