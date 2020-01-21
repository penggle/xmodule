package com.penglecode.xmodule.common.util;

import java.util.Map;

import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * 基于Spring SpEL表达式语言的表达式模板工具类
 * 
 * @author 	pengpeng
 * @date	2020年1月10日 上午9:46:28
 */
public class SpelExpressionUtils {

	private static final ExpressionParser DEFAULT_SPEL_EXPRESSION_PARSER = new SpelExpressionParser(new SpelParserConfiguration(true,true));
	
	/**
	 * 默认的模板解析上下文: ${...}
	 */
	private static final TemplateParserContext DEFAULT_TEMPLATE_PARSE_CONTEXT = new TemplateParserContext("${", "}");
	
	/**
	 * 解析模板表达式
	 * @param template
	 * @param variables
	 * @return
	 */
	public static String parseTemplate(String template, Map<String,Object> variables) {
		return parseTemplate(DEFAULT_SPEL_EXPRESSION_PARSER, template, variables);
	}
	
	/**
	 * 解析模板表达式
	 * @param parser
	 * @param template
	 * @param variables
	 * @return
	 */
	public static String parseTemplate(ExpressionParser parser, String template, Map<String,Object> variables) {
		return parseTemplate(parser, DEFAULT_TEMPLATE_PARSE_CONTEXT, template, variables);
	}
	
	/**
	 * 解析模板表达式
	 * @param parserContext
	 * @param template
	 * @param variables
	 * @return
	 */
	public static String parseTemplate(ParserContext parserContext, String template, Map<String,Object> variables) {
		return parseTemplate(DEFAULT_SPEL_EXPRESSION_PARSER, parserContext, template, variables);
	}
	
	/**
	 * 解析模板表达式
	 * @param parser
	 * @param parserContext
	 * @param template
	 * @param variables
	 * @return
	 */
	public static String parseTemplate(ExpressionParser parser, ParserContext parserContext, String template, Map<String,Object> variables) {
		StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
		evaluationContext.setRootObject(variables);
		evaluationContext.addPropertyAccessor(new MapAccessor());
		return (String)parser.parseExpression(template, parserContext).getValue(evaluationContext);
	}
	
	/**
	 * 对表达式进行求值
	 * @param parser
	 * @param expression
	 * @param variables
	 * @param valueType
	 */
	public static <T> T evalExpression(String expression, Map<String,Object> variables, Class<T> valueType) {
		return evalExpression(DEFAULT_SPEL_EXPRESSION_PARSER, expression, variables, valueType);
	}
	
	/**
	 * 对表达式进行求值
	 * @param parser
	 * @param expression
	 * @param variables
	 * @param valueType
	 */
	public static <T> T evalExpression(ExpressionParser parser, String expression, Map<String,Object> variables, Class<T> valueType) {
		StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
		evaluationContext.setVariables(variables);
		return parser.parseExpression(expression).getValue(evaluationContext, valueType);
	}
	
}
