package bibliomar.bibliomarserver.model.metadata;

import bibliomar.bibliomarserver.utils.constants.Topics;
import jakarta.validation.constraints.NotNull;

public class MetadataDownloadMirrors {
    public String libgenMirror;
    public String librocksMirror;

    public MetadataDownloadMirrors(@NotNull String MD5, @NotNull Topics topic) {
        this.buildLibgenMirror(MD5, topic);
        this.buildLibrocksMirror(MD5);
    }

    public void buildLibgenMirror(String MD5, Topics topic) {
        final String libgenBase = "https://library.lol";
        String topicURL;
        if (topic == Topics.fiction) {
            topicURL = "fiction";
        } else {
            topicURL = "main";
        }
        String libgenMirror = String.format("%s/%s/%s", libgenBase, topicURL, MD5);
        this.libgenMirror = libgenMirror;
    }

    public void buildLibrocksMirror(String MD5) {
        final String librocksBase = "https://libgen.rocks/ads.php?md5=%s";
        String librocksMirror = String.format(librocksBase, MD5);
        this.librocksMirror = librocksMirror;
    }
}
