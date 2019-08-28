package com.github.rasmussoot.wiremock;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.servlet.WireMockHttpServletRequestAdapter;

import java.lang.reflect.Field;

public class WireMockProxyTransformer extends ResponseDefinitionTransformer {
    private static final String PREFIX_FIELD_NAME = "urlPrefixToRemove";

    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource files, Parameters parameters) {
        String urlPrefixToRemove = parameters.getString(PREFIX_FIELD_NAME);

        try {
            Field field = WireMockHttpServletRequestAdapter.class.getDeclaredField(PREFIX_FIELD_NAME);
            field.setAccessible(true);
            field.set(request, urlPrefixToRemove);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }

        return responseDefinition;
    }

    @Override
    public String getName() {
        return "proxy";
    }
}