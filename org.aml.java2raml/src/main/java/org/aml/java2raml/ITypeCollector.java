package org.aml.java2raml;

import java.util.Collection;

import org.aml.typesystem.ITypeModel;

public interface ITypeCollector {

	public Collection<ITypeModel> gather(Config cfg);
}
