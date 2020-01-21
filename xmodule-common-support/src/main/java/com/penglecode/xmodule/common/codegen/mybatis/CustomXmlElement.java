package com.penglecode.xmodule.common.codegen.mybatis;

import java.util.Collections;
import java.util.List;

import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class CustomXmlElement extends XmlElement {

	public CustomXmlElement(String name) {
		super(name);
	}

	public CustomXmlElement(XmlElement original) {
		super(original);
	}

	/* (non-Javadoc)
     * @see org.mybatis.generator.api.dom.xml.Element#getFormattedContent(int)
     */
    @Override
    public String getFormattedContent(int indentLevel) {
    	List<Attribute> attributes = getAttributes();
    	List<Element> elements = getElements();
    	String name = getName();
    	
        StringBuilder sb = new StringBuilder();

        OutputUtilities.javaIndent(sb, indentLevel);
        sb.append('<');
        sb.append(name);
        
        Collections.sort(attributes);
        for (Attribute att : attributes) {
            sb.append(' ');
            sb.append(att.getFormattedContent());
        }

        if (elements.size() > 0) {
            sb.append(">"); //$NON-NLS-1$
            for (Element element : elements) {
                OutputUtilities.newLine(sb);
                sb.append(element.getFormattedContent(indentLevel + 1));
            }
            OutputUtilities.newLine(sb);
            OutputUtilities.javaIndent(sb, indentLevel);
            sb.append("</"); //$NON-NLS-1$
            sb.append(name);
            sb.append('>');

        } else {
            sb.append(" />"); //$NON-NLS-1$
        }

        return sb.toString();
    }

}
