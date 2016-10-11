package org.aml.typesystem.meta.restrictions;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.ITransientMeta;
import org.aml.typesystem.meta.TypeInformation;

public class ExternalSchemaMeta extends TypeInformation implements ITransientMeta{

	private String content;

	public ExternalSchemaMeta(String content) {
		super(true);
		this.content=content;
	}
	
	public String getContent(){
		return this.content;
	}

	@Override
	public AbstractType requiredType() {
		return BuiltIns.ANY;
	}

	@Override
	public Status validate(ITypeRegistry registry) {
		return Status.OK_STATUS;
	}

}
