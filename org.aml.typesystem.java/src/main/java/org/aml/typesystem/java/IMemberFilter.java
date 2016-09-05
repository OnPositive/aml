package org.aml.typesystem.java;

import org.aml.typesystem.IMember;

public interface IMemberFilter {

	boolean accept(IMember member);
}
