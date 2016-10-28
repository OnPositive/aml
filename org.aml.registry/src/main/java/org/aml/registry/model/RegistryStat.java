package org.aml.registry.model;

public class RegistryStat {

	int uniqueSpecCount;
	int apisCount;
	
	public int getUniqueSpecCount() {
		return uniqueSpecCount;
	}

	public void setUniqueSpecCount(int uniqueSpecCount) {
		this.uniqueSpecCount = uniqueSpecCount;
	}

	public int getApisCount() {
		return apisCount;
	}

	public void setApisCount(int apisCount) {
		this.apisCount = apisCount;
	}

	@Override
	public String toString() {
		return "Specifications count: "+this.uniqueSpecCount+", APIs count:"+this.apisCount;
	}
}
