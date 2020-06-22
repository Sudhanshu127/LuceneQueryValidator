package ValidateQuery.classic;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.Query;

import java.util.ArrayList;
import java.util.List;


// TODO: Clean up the code
// TODO: Check line change :- No line change, defaulted to one, hence useless
// TODO: Error response update :- Done
public class Validate {
    private static final QueryParser queryParser = new QueryParser("*", new StandardAnalyzer());


    public static ValidateResult validateQuery(String query){
        Query q;
        try{
            q = queryParser.parse(query);
            System.out.println(q);
        }
        catch (ParseException e){
            ValidateResult.Builder builder = new ValidateResult.Builder(false);

            if(e.getMessage().equals("Syntax Error") || e.getMessage().equals("Lexical Error")){
                builder.type(e.getMessage())
                        .query(query)
                        .errorToken(e.currentToken.next == null ? null : e.currentToken.next.image)
                        .validToken(e.currentToken.image)
                        .errorIndex(e.currentToken.endColumn);

            }

            if(e.getMessage().equals("Syntax Error")){
                for (int i = 0; i < e.expectedTokenSequences.length; i++) {
                    for (int j = 0; j < e.expectedTokenSequences[i].length; j++) {
                        builder.addExpectedToken(e.tokenImage[e.expectedTokenSequences[i][j]]);
                    }
                }
            }

            return builder.build();
        }
        return new ValidateResult.Builder(true).build();
    }
}

class ValidateResult {
    private final boolean valid;
    private final String type;
    private final String query;
    private final int errorIndex;
    private final String validToken;
    private final String errorToken;
    private final List<String> expectedTokens;

    public boolean isValid() {
        return valid;
    }

    @Override
    public String toString(){
        StringBuilder response = new StringBuilder();
        response.append("{\n");
        response.append("valid : ").append(valid).append(",\n");
        if(!valid){
            response.append("type : ").append(type).append(",\n");
            response.append("query : ").append(query).append(",\n");
            response.append("errorIndex : ").append(errorIndex).append(",\n");
            response.append("validToken : ").append(validToken).append(",\n");
            response.append("errorToken : ").append(errorToken).append(",\n");
            response.append("expectedTokens : [");
            for(int i = 0; i < expectedTokens.size() ; i++){
                response.append(expectedTokens.get(i));
                if(i==expectedTokens.size()-1){
                    break;
                }
                response.append(",");
            }
            response.append("]").append(",\n");
        }
        response.append("}");
        return response.toString();
    }

    public static class Builder{
        private final boolean valid;
        private String type = null;
        private String query = null;
        private int errorIndex = -1;
        private String validToken = null;
        private String errorToken = null;
        private final List<String> expectedToken = new ArrayList<>();

        public Builder(boolean valid){
            this.valid = valid;
        }

        public Builder type(String type){
            this.type = type;
            return this;
        }

        public Builder query(String query){
            this.query = query;
            return this;
        }

        public Builder errorIndex(int errorIndex){
            this.errorIndex = errorIndex;
            return this;
        }

        public Builder validToken(String validToken){
            this.validToken = validToken;
            return this;
        }

        public Builder errorToken(String errorToken){
            this.errorToken = errorToken;
            return this;
        }

        public Builder addExpectedToken(String token){
            this.expectedToken.add(token);
            return this;
        }

        public Builder removeExpectedToken(String token){
            this.expectedToken.remove(token);
            return this;
        }

        public ValidateResult build(){
            return new ValidateResult(this);
        }
    }

    private ValidateResult(Builder builder){
        this.valid = builder.valid;
        this.type = builder.type;
        this.errorIndex = builder.errorIndex;
        this.errorToken = builder.errorToken;
        this.expectedTokens = builder.expectedToken;
        this.query = builder.query;
        this.validToken = builder.validToken;
    }
}