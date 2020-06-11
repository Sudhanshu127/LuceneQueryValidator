package ValidateQuery.classic;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.validate.query.QueryExplanation;
import org.elasticsearch.action.admin.indices.validate.query.ValidateQueryRequest;
import org.elasticsearch.action.admin.indices.validate.query.ValidateQueryResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;

import java.io.IOException;


public class ElasticSearchQuery {
    //The config parameters for the connection
    private static final String HOST = "localhost";
    private static final int PORT_ONE = 9200;
    private static final int PORT_TWO = 9201;
    private static final String SCHEME = "http";
    private static int users = 0;

    private static RestHighLevelClient restHighLevelClient = null;

    private static final String INDEX = "tweetdata2";

    /**
     * Implemented Singleton pattern here
     * so that there is just one connection at a time.
     */

    static synchronized void makeConnection() {
        if(restHighLevelClient == null) {
            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(HOST, PORT_ONE, SCHEME),
                            new HttpHost(HOST, PORT_TWO, SCHEME)));
        }
        users = users + 1;

    }

    static synchronized void closeConnection() throws IOException {
        if(users == 1 && restHighLevelClient != null) {
            restHighLevelClient.close();
            restHighLevelClient = null;
        }
        users = users - 1;
    }

    static boolean validQuery(String query){
        QueryStringQueryBuilder qb = QueryBuilders.queryStringQuery(query);
        ValidateQueryRequest request = new ValidateQueryRequest(INDEX);
        request.query(qb);
        request.explain(true);
        ValidateQueryResponse response = null;
        try {
            response = restHighLevelClient.indices().validateQuery(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert response != null;

//        for(QueryExplanation explanation: response.getQueryExplanation()) {
//            System.out.println(explanation.getError());
//        }
        return response.isValid();
    }
}
