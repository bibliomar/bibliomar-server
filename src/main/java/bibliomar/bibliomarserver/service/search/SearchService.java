package bibliomar.bibliomarserver.service.search;

import bibliomar.bibliomarserver.model.search.SearchRequestModel;
import bibliomar.bibliomarserver.utils.ManticoreUtils;
import bibliomar.bibliomarserver.utils.contants.Topics;
import com.manticoresearch.client.ApiException;
import com.manticoresearch.client.api.SearchApi;
import com.manticoresearch.client.model.SearchRequest;
import com.manticoresearch.client.model.SearchResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class SearchService {

    @Autowired
    private ManticoreUtils manticoreUtils;

    private SearchApi searchApi;

    @PostConstruct
    protected void buildSearchApi() {
        this.searchApi = manticoreUtils.getSearchApi();
    }

    public SearchResponse search(SearchRequestModel searchRequestModel) throws ApiException {
        Topics topic = searchRequestModel.getTopic();
        String userQuery = searchRequestModel.getQuery();

        String requestIndex;
        if (topic == Topics.fiction) {
            requestIndex = "fiction";
        } else {
            requestIndex = "scitech";
        }

        HashMap<String, Object> queryObject = new HashMap<>();
        queryObject.put("query_string", userQuery);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setIndex(requestIndex);
        searchRequest.setQuery(queryObject);

        SearchResponse response = searchApi.search(searchRequest);
        System.out.println(response);

        return response;


    }


}
