package org.aml.graphmodel;

public final class SemanticContext {

	public final String name;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SemanticContext other = (SemanticContext) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public SemanticContext(String name) {
		super();
		this.name = name;
	}

	public static final SemanticContext DETAILS = new SemanticContext("details");
	
	public static final SemanticContext LIST = new SemanticContext("list");	
	
	public static final SemanticContext CREATE = new SemanticContext("create");
	
	public static final SemanticContext UPDATE = new SemanticContext("update");
	
	public static final SemanticContext DELETE = new SemanticContext("delete");
}
