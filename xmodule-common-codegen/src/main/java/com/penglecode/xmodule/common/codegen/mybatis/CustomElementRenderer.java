package com.penglecode.xmodule.common.codegen.mybatis;

import java.util.Comparator;
import java.util.stream.Stream;

import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.api.dom.xml.render.AttributeRenderer;
import org.mybatis.generator.internal.util.CustomCollectors;

public class CustomElementRenderer implements ElementVisitor<Stream<String>> {
    
    private AttributeRenderer attributeRenderer = new AttributeRenderer();

    @Override
    public Stream<String> visit(TextElement element) {
        return Stream.of(element.getContent());
    }

    @Override
    public Stream<String> visit(XmlElement element) {
        if (element.hasChildren()) {
            return renderWithChildren(element);
        } else {
            return renderWithoutChildren(element);
        }
    }

    protected Stream<String> renderWithoutChildren(XmlElement element) {
        return Stream.of("<" //$NON-NLS-1$
                + element.getName()
                + renderAttributes(element)
                + " />"); //$NON-NLS-1$
    }

    public Stream<String> renderWithChildren(XmlElement element) {
        return Stream.of(renderOpen(element), renderChildren(element), renderClose(element))
                .flatMap(s -> s);
    }

    protected String renderAttributes(XmlElement element) {
        return element.getAttributes().stream()
                .sorted(Comparator.comparing(Attribute::getName))
                .map(attributeRenderer::render)
                .collect(CustomCollectors.joining(" ", " ", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    protected Stream<String> renderOpen(XmlElement element) {
        return Stream.of("<" //$NON-NLS-1$
                + element.getName()
                + renderAttributes(element)
                + ">"); //$NON-NLS-1$
    }

    protected Stream<String> renderChildren(XmlElement element) {
        return element.getElements().stream()
                .flatMap(this::renderChild)
                .map(this::indent);
    }

    protected Stream<String> renderChild(VisitableElement child) {
        return child.accept(this);
    }

    //修改为javaIndent
    protected String indent(String s) {
        return "    " + s; //$NON-NLS-1$
    }

    protected Stream<String> renderClose(XmlElement element) {
        return Stream.of("</" //$NON-NLS-1$
                + element.getName()
                + ">"); //$NON-NLS-1$
    }
}