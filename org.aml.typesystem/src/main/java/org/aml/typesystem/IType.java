package org.aml.typesystem;

import java.util.Set;

import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;

public interface IType {

	IType ac(Object object);

	Set<AbstractType> allDependencies();

	boolean canDoAC();

	Set<TypeInformation> declaredMeta();

	Set<AbstractType> dependencies();

	boolean isConfluent();

	Set<TypeInformation> meta();

	Set<AbstractRestricton> restrictions();

	/**
	 *
	 * @return directly known sub types of a given type
	 */
	Set<AbstractType> subTypes();

	/**
	 *
	 * @return direct super types of a given type
	 */
	Set<AbstractType> superTypes();

	Set<AbstractType> typeFamily();

	Status validate(Object object);

	Status validateDirect(Object object);

	Status validateMeta(ITypeRegistry types);
}