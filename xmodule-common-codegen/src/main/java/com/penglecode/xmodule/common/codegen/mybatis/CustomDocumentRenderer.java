package com.penglecode.xmodule.common.codegen.mybatis;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mybatis.generator.api.dom.xml.DocType;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.render.DocTypeRenderer;

public class CustomDocumentRenderer {

    public String render(Document document) {
        return Stream.of(renderXmlHeader(),
                renderDocType(document),
                renderRootElement(document))
                .flatMap(Function.identity())
                .collect(Collectors.joining(System.getProperty("line.separator"))); //$NON-NLS-1$
    }

    protected Stream<String> renderXmlHeader() {
        return Stream.of("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"); //$NON-NLS-1$
    }
    
    protected Stream<String> renderDocType(Document document) {
        return Stream.of("<!DOCTYPE " //$NON-NLS-1$
                + document.getRootElement().getName()
                + document.getDocType().map(this::renderDocType).orElse("") //$NON-NLS-1$
                + ">"); //$NON-NLS-1$
    }
    
    protected String renderDocType(DocType docType) {
        return " " + docType.accept(new DocTypeRenderer()); //$NON-NLS-1$
    }
    
    protected Stream<String> renderRootElement(Document document) {
        return document.getRootElement().accept(new CustomElementRenderer());
    }
}
