package com.cherrydev.cherrymarketbe.server.application.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CustomSpELParser {

    public static Object getDynamicValue(String[] parameterNames, Object[] args, String name) {
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        return parser.parseExpression(name).getValue(context);
    }

}
