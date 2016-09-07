package org.aml.typesystem.meta.restrictions;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.aml.typesystem.meta.facets.internal.ANDRestricton;

/**
 * <p>RestrictionsOptimizer class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class RestrictionsOptimizer {

	/**
	 * <p>optimize.</p>
	 *
	 * @param restrictions a {@link java.util.Set} object.
	 * @return a {@link java.util.Set} object.
	 */
	public static Set<AbstractRestricton> optimize(Set<AbstractRestricton> restrictions) {
		Set<AbstractRestricton> restrs = restrictions;
		LinkedHashSet<AbstractRestricton> result = new LinkedHashSet<>();

		for (final AbstractRestricton r : restrs) {
			result.add(preoptimize(r));
		}
		if (result.size() == 1) {
			final AbstractRestricton r = result.iterator().next();
			if (r instanceof ANDRestricton) {
				result = new LinkedHashSet<>(Arrays.asList(((ANDRestricton) r).options()));
			}
		}
		restrs = result;
		boolean transformed = true;
		while (transformed) {
			transformed = false;
			for (final AbstractRestricton r : restrs) {
				for (final AbstractRestricton r1 : restrs) {
					if (r != r1) {
						final AbstractRestricton composeWith = r.tryCompose(r1);
						if (composeWith != null) {
							result.remove(r);
							result.remove(r1);
							result.add(composeWith);
							restrs = result;
							transformed = true;
							break;
						}
					}
				}
				if (transformed) {
					break;
				}
			}
		}
		return result;
	}

	private static AbstractRestricton preoptimize(AbstractRestricton r) {
		if (r instanceof ANDRestricton) {
			final ANDRestricton a = (ANDRestricton) r;
			final Set<AbstractRestricton> optimize = optimize(new LinkedHashSet<>(Arrays.asList(a.options())));
			if (optimize.size() == 1) {
				return optimize.iterator().next();
			}
			return new ANDRestricton(optimize.toArray(new AbstractRestricton[optimize.size()]));
		}
		
		return r.preoptimize();
	}

	private RestrictionsOptimizer() {
	}
}
