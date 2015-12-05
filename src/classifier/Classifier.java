package classifier;

import java.util.Collection;

public interface Classifier {
    String classify(String... words);

    void add(Document document);

    void addAll(Document... documents);

    void addAll(Collection<Document> documents);
}
