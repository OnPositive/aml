package org.aml.typesystem.ramlreader;

import org.aml.apimodel.Annotable;
import org.aml.apimodel.DocumentationItem;
import org.aml.apimodel.TopLevelModel;
import org.raml.yagi.framework.nodes.Node;

public class DocumentationItemImpl extends AbstractWrappedNodeImpl<Annotable, Node> implements DocumentationItem{

	public DocumentationItemImpl(TopLevelModel mdl, Annotable parent, Node node) {
		super(mdl, parent, node);
	}

	@Override
	public String getTitle() {
		return getChildWithKeyAs("title", String.class, null);
	}

	@Override
	public String getContent() {
		return getChildWithKeyAs("content", String.class, null);
	}

}
