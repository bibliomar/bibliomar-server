package bibliomar.bibliomarserver.model.search;

import bibliomar.bibliomarserver.utils.contants.Topics;
import lombok.Data;

@Data
public class SearchRequestModel {

    private String query;
    private Topics topic;

}
