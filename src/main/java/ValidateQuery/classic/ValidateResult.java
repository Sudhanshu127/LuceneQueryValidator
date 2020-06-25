package ValidateQuery.classic;

import java.util.ArrayList;
import java.util.List;

public class ValidateResult {
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
    public String getType(){
        return type;
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
