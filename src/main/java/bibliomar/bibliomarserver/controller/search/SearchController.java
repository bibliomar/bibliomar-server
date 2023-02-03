package bibliomar.bibliomarserver.controller.search;

import bibliomar.bibliomarserver.model.search.SearchRequestModel;
import bibliomar.bibliomarserver.service.search.SearchService;
import com.manticoresearch.client.ApiException;
import com.manticoresearch.client.model.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/{topic}")
    public ResponseEntity<SearchResponse> search(SearchRequestModel searchRequestModel) throws ApiException {
        return ResponseEntity.ok(searchService.search(searchRequestModel));
    }

}
