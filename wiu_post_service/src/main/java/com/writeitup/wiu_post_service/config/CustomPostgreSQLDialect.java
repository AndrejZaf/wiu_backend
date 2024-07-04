package com.writeitup.wiu_post_service.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

public class CustomPostgreSQLDialect extends PostgreSQLDialect {

    @Override
    public void initializeFunctionRegistry(final FunctionContributions functionContributions) {
        super.initializeFunctionRegistry(functionContributions);
        final SqmFunctionRegistry functionRegistry = functionContributions.getFunctionRegistry();
        functionRegistry.registerPattern("tsvector_match", "(?1 @@ ?2)");
    }
}
