package bibliomar.bibliomarserver.utils;

import com.manticoresearch.client.ApiClient;
import com.manticoresearch.client.ApiException;
import com.manticoresearch.client.Configuration;
import com.manticoresearch.client.model.*;
import com.manticoresearch.client.api.IndexApi;
import com.manticoresearch.client.api.UtilsApi;
import com.manticoresearch.client.api.SearchApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ManticoreUtils {

    @Value("${manticore.url}")
    private String manticoreUrl;

    private SearchApi searchApi;

    private void buildSearchClient() {
        ApiClient client = Configuration.getDefaultApiClient();
        client.setBasePath(this.manticoreUrl);
        SearchApi searchApi = new SearchApi(client);
        this.searchApi = searchApi;

    }

    public SearchApi getSearchApi() {
        if (this.searchApi == null) {
            this.buildSearchClient();
        }
        return this.searchApi;
    }


}
