## LuceneQueryValidator
Creating a syntatical and lexical validator for a query in elasticsearch 7.7 based on lucene 8.4.1  

### About
The code has been used from org.apache.lucene:lucene-queryparser/classic  

### File Changes
* <strong>ParseException</strong>
    * Returns "Syntax Error" 

* <strong>TokenMgrError</strong>
    * Returns "Lexical Error" during parsing 
    * Contains a token specifying the error line, column, image
    
* <strong>QueryParserBase</strong>  
    * Handles ParseException by retrieving expected token images
    * Handles TokenMgrError by parsing into ParseException
