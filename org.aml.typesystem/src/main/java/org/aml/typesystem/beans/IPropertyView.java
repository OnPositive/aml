package org.aml.typesystem.beans;

import java.util.List;

import org.aml.typesystem.INamedEntity;

public interface IPropertyView extends INamedEntity {

	List<IProperty> properties();

	List<IProperty> allProperties();

	List<IProperty> facets();

	List<IProperty> allFacets();

}