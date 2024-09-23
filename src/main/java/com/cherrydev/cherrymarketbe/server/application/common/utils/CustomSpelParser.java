package com.cherrydev.cherrymarketbe.server.application.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CustomSpelParser {

  /**
   * SpEL을 이용하여 동적으로 값을 가져온다.
   *
   * @param parameterNames 파라미터 이름
   * @param args 파라미터 값
   * @param name SpEL 표현식
   * @return 동적으로 가져온 값
   */
  public static Object getDynamicValue(String[] parameterNames, Object[] args, String name) {
    ExpressionParser parser = new SpelExpressionParser();
    EvaluationContext context = new StandardEvaluationContext();

    for (int i = 0; i < parameterNames.length; i++) {
      context.setVariable(parameterNames[i], args[i]);
    }

    return parser.parseExpression(name).getValue(context);
  }
}
