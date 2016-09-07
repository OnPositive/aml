package org.aml.typesystem.meta;

/**
 * <p>BasicMeta class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class BasicMeta {

	/** Constant <code>BUILTIN</code> */
	public static final TypeInformation BUILTIN = new TypeModifier(); // NOSONAR

	/** Constant <code>INTERNAL</code> */
	public static final TypeInformation INTERNAL = new TypeModifier(); // NOSONAR

	private BasicMeta() {
	}
}
