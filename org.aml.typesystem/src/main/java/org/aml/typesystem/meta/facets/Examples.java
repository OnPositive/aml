package org.aml.typesystem.meta.facets;

import java.util.ArrayList;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.beans.ISimpleFacet;
import org.aml.typesystem.meta.TypeInformation;

/**
 * <p>Example class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class Examples extends TypeInformation implements ISimpleFacet {

	
	public Examples() {
		super(true);
	}

	protected ArrayList<Example>examples=new ArrayList<>();

	public ArrayList<Example> getExamples() {
		return examples;
	}

	public void setExamples(ArrayList<Example> examples) {
		this.examples = examples;
	}

	@Override
	public String facetName() {
		return "examples";
	}

	@Override
	public Object value() {
		return examples;
	}

	@Override
	public void setValue(Object vl) {
		
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
