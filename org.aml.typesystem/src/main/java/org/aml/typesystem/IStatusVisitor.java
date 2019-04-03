package org.aml.typesystem;


public interface IStatusVisitor {

	void startVisiting(Status st);
	void endVisiting(Status st);
}
