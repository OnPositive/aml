package org.aml.java2raml;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ValidationBean {

	@Min(1)
	@Max(3)
	long mm;
	
	@DecimalMax("3.0")
	@DecimalMin("2.0")
	double vv;
	
	@Pattern(regexp="..")
	String mmm;
	
	@Size(min=8,max=9)
	@NotNull
	String anotherString;
}
