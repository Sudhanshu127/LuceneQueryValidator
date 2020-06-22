package ValidateQuery.classic;

import org.apache.lucene.analysis.standard.StandardAnalyzer;


// TODO: Clean up the code
// TODO: Check line change :- No line change, defaulted to one, hence useless
// TODO: Error response update :- Done
public class Validate {
    private static final QueryParser queryParser = new QueryParser("*", new StandardAnalyzer());


    public static ValidateResult validateQuery(String query){
        try{
            queryParser.parse(query);
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

