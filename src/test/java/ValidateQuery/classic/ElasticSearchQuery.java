package ValidateQuery.classic;

//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.validate.query.QueryExplanation;
import org.elasticsearch.action.admin.indices.validate.query.ValidateQueryRequest;
import org.elasticsearch.action.admin.indices.validate.query.ValidateQueryResponse;
//import org.elasticsearch.action.delete.DeleteRequest;
//import org.elasticsearch.action.delete.DeleteResponse;
//import org.elasticsearch.action.get.GetRequest;
//import org.elasticsearch.action.get.GetResponse;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.action.support.DefaultShardOperationFailedException;
//import org.elasticsearch.action.update.UpdateRequest;
//import org.elasticsearch.action.update.UpdateResponse;
//import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.TransportAddress;
//import org.elasticsearch.common.xcontent.XContentType;
//import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import java.util.HashMap;
//import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

public class ElasticSearchQuery {
    private static final Logger logger = LogManager.getLogger(ElasticSearchQuery.class);
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
        logger.info("Making Connection");
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
            logger.info("Closing Connection");
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
            System.out.println(explanation.getError());
            System.out.println(query + "\n");
        }
        return response.isValid();
    }

//    static Tweet insertTweet(Tweet tweet){
//        logger.info("Inserting tweet");
//        logger.trace("Creating dataMap for tweet");
//        tweet.setTweetId(Integer.toString(counter.getAndIncrement()));
//        Map<String, Object> dataMap = new HashMap<>();
//        dataMap.put("tweetId", tweet.getTweetId());
//        dataMap.put("text", tweet.getText());
//        dataMap.put("highlight", tweet.getHighlight());
//        dataMap.put("url", tweet.getUrl());
//        dataMap.put("score", tweet.getScore());
//        dataMap.put("discoverDate", tweet.getDiscoverDate());
//        logger.trace("Creating request");
//        IndexRequest indexRequest = new IndexRequest(INDEX).id(tweet.getTweetId())
//                .source(dataMap);
//        try {
//            logger.trace("Sending request");
//            IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
//        } catch(ElasticsearchException e) {
//            e.getDetailedMessage();
//        } catch (java.io.IOException ex){
//            ex.getLocalizedMessage();
//        }
//        return tweet;
//    }
//
//    static Tweet getTweetById(String id){
//        GetRequest getTweetRequest = new GetRequest(INDEX, id);
//        GetResponse getResponse = null;
//        try {
//            getResponse = restHighLevelClient.get(getTweetRequest, RequestOptions.DEFAULT);
//        } catch (java.io.IOException e){
//            e.getLocalizedMessage();
//        }
//        return getResponse != null ?
//                objectMapper.convertValue(getResponse.getSourceAsMap(), Tweet.class) : null;
//    }
//
//    static Tweet updateTweetById(String id, Tweet tweet){
//        UpdateRequest updateRequest = new UpdateRequest(INDEX, id)
//                .fetchSource(true);    // Fetch Object after its update
//        try {
//            String tweetJson = objectMapper.writeValueAsString(tweet);
//            updateRequest.doc(tweetJson, XContentType.JSON);
//            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
//            return objectMapper.convertValue(updateResponse.getGetResult().sourceAsMap(), Tweet.class);
//        }catch (JsonProcessingException e){
//            e.getMessage();
//        } catch (java.io.IOException e){
//            e.getLocalizedMessage();
//        }
//        System.out.println("Unable to update person");
//        return null;
//    }

//    static void deleteTweetById(String id) {
//        DeleteRequest deleteRequest = new DeleteRequest(INDEX, id);
//        try {
//            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
//        } catch (java.io.IOException e){
//            e.getLocalizedMessage();
//        }
//    }
}
