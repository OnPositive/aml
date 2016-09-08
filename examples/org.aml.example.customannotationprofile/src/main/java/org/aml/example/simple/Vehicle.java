package org.aml.example.simple;

public class Vehicle {

	@Optional
	@Default("Ford")
	protected String name;
	@Optional
	@Default("4.0")
	protected double speed;
	@Optional
	protected VehicleKind kind;
}
