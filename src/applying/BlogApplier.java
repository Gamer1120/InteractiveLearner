package applying;

import classifier.Classifier;
import utils.Utils;

public class BlogApplier {

    public static Applier apply(Classifier classifier) {
        Applier applier = new Applier(classifier);
        applier.trainAll(Utils.toDocuments(Utils.readFiles("db/blogs/F/train"), "female"));
        applier.trainAll(Utils.toDocuments(Utils.readFiles("db/blogs/M/train"), "male"));
        applier.addAll(Utils.readFiles("db/blogs/F/test"));
        applier.addAll(Utils.readFiles("db/blogs/M/test"));
        return applier;
    }
}
