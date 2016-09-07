package org.aml.typesystem.values;

/**
 * <p>IArray interface.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public interface IArray {

	/**
	 * <p>item.</p>
	 *
	 * @param position a int.
	 * @return a {@link java.lang.Object} object.
	 */
	Object item(int position);

	/**
	 * <p>length.</p>
	 *
	 * @return a int.
	 */
	int length();
}
