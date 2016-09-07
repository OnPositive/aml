package org.aml.typesystem;

import java.util.Set;

import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;

/**
 * <p>IType interface.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public interface IType {

	/**
	 * <p>ac.</p>
	 *
	 * @param object a {@link java.lang.Object} object.
	 * @return a {@link org.aml.typesystem.IType} object.
	 */
	IType ac(Object object);

	/**
	 * <p>allDependencies.</p>
	 *
	 * @return a {@link java.util.Set} object.
	 */
	Set<AbstractType> allDependencies();

	/**
	 * <p>canDoAC.</p>
	 *
	 * @return a boolean.
	 */
	boolean canDoAC();

	/**
	 * <p>declaredMeta.</p>
	 *
	 * @return a {@link java.util.Set} object.
	 */
	Set<TypeInformation> declaredMeta();

	/**
	 * <p>dependencies.</p>
	 *
	 * @return a {@link java.util.Set} object.
	 */
	Set<AbstractType> dependencies();

	/**
	 * <p>isConfluent.</p>
	 *
	 * @return a boolean.
	 */
	boolean isConfluent();

	/**
	 * <p>meta.</p>
	 *
	 * @return a {@link java.util.Set} object.
	 */
	Set<TypeInformation> meta();

	/**
	 * <p>restrictions.</p>
	 *
	 * @return a {@link java.util.Set} object.
	 */
	Set<AbstractRestricton> restrictions();

	/**
	 * <p>subTypes.</p>
	 *
	 * @return directly known sub types of a given type
	 */
	Set<AbstractType> subTypes();

	/**
	 * <p>superTypes.</p>
	 *
	 * @return direct super types of a given type
	 */
	Set<AbstractType> superTypes();

	/**
	 * <p>typeFamily.</p>
	 *
	 * @return a {@link java.util.Set} object.
	 */
	Set<AbstractType> typeFamily();

	/**
	 * <p>validate.</p>
	 *
	 * @param object a {@link java.lang.Object} object.
	 * @return a {@link org.aml.typesystem.Status} object.
	 */
	Status validate(Object object);

	/**
	 * <p>validateDirect.</p>
	 *
	 * @param object a {@link java.lang.Object} object.
	 * @return a {@link org.aml.typesystem.Status} object.
	 */
	Status validateDirect(Object object);

	/**
	 * <p>validateMeta.</p>
	 *
	 * @param types a {@link org.aml.typesystem.ITypeRegistry} object.
	 * @return a {@link org.aml.typesystem.Status} object.
	 */
	Status validateMeta(ITypeRegistry types);
}
