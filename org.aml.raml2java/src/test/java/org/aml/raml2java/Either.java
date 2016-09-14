package org.aml.raml2java;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(EitherAdapter.class)

public class Either {

	double val;
	String s;
}
