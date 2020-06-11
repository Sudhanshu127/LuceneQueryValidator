package ValidateQuery.classic;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.validate.query.QueryExplanation;
import org.elasticsearch.action.admin.indices.validate.query.ValidateQueryRequest;
import org.elasticsearch.action.admin.indices.validate.query.ValidateQueryResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;


public class ElasticSearchQuery {
    //The config parameters for the connection
    private static final String HOST = "localhost";
    private static final int PORT_ONE = 9200;
    private static final int PORT_TWO = 9201;
    private static final String SCHEME = "http";
    private static int users = 0;
    static AtomicInteger counter = new AtomicInteger(0);

    private static RestHighLevelClient restHighLevelClient = null;
//    private static final ObjectMapper objectMapper = new ObjectMapper();

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

    static void makeQuery(String query){
        QueryStringQueryBuilder qb = QueryBuilders.queryStringQuery(query);
//        System.out.println(qb);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(new SearchSourceBuilder().query(qb));
        searchRequest.indices(INDEX);
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(searchResponse.getHits());
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
//        System.out.println(response.toString());
//        System.out.println(response.isValid());
//        System.out.println(response.getFailedShards());
//        if (!response.isValid()) {
//            for(DefaultShardOperationFailedException failure: response.getShardFailures()) {
////                String failedIndex = failure.index();
////                int shardId = failure.shardId();
//                String reason = failure.reason();
//                System.out.println(reason);
//            }
//        }
        if(!response.isValid())
        for(QueryExplanation explanation: response.getQueryExplanation()) {
//            String explanationIndex = explanation.getIndex();
//            int shardId = explanation.getShard();
//            System.out.println(explanation.getError());
//            System.out.println(query + "\n");
        }
        return response.isValid();
    }
}
