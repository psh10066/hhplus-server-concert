package kr.hhplus.be.server.support.util

import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext

class CustomSpringELParser {

    companion object {
        @JvmStatic
        fun getByMethodArgument(parameterNames: Array<String>, args: Array<Any>, key: String): Any? {
            val context = StandardEvaluationContext()
            for (i in parameterNames.indices) {
                context.setVariable(parameterNames[i], args[i])
            }

            return SpelExpressionParser().parseExpression(key).getValue(context)
        }
    }
}