package org.dysd.util.spring;

import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class SpelTest {

	@Test  
	public void testParserContext() {  
	    ExpressionParser parser = new SpelExpressionParser();  
	    ParserContext parserContext = new ParserContext() {  
	        @Override  
	         public boolean isTemplate() {  
	            return true;  
	        }  
	        @Override  
	        public String getExpressionPrefix() {  
	            return "#{";  
	        }  
	        @Override  
	        public String getExpressionSuffix() {  
	            return "}";  
	        }  
	    };  
	    String template = "aaa#{'Hello '}#{'World!'}";  
	    Expression expression = parser.parseExpression(template, parserContext);  
	    System.out.println(expression.getValue());
	} 
}
