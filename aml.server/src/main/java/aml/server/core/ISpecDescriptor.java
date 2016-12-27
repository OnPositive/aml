package aml.server.core;

public interface ISpecDescriptor {
	
	ISpecDescriptor[] referencingSpecifications();
	
	IRegistryDescriptor[] referencingRegistries();
}
