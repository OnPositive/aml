package org.aml.example.simple;

public class Vehicle {

	@FieldConfig(caption="Name of vehicle",order=1)
	protected String name;
	
	@FieldConfig(caption="Maximum Speed of vehicle",order=2)
	protected double speed;
	@Hidden
	protected VehicleKind kind;
	
}
