package org.aml.typesystem.meta.restrictions;

import java.util.HashMap;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.IntersectionType;
import org.aml.typesystem.TypeOps;

public abstract class IntersectRequires extends TypedRestriction {

	static class CountingPointer {
		int count;
		AbstractType value;
	}

	static HashMap<AbstractType, CountingPointer> map = new HashMap<>();

	AbstractType intersect(AbstractType t, AbstractType t1) {
		final AbstractType intersectionType = new IntersectionType("X", t, t1);
		CountingPointer countingPointer = map.get(intersectionType);
		if (countingPointer == null) {
			countingPointer = new CountingPointer();
			countingPointer.count = 1;
			countingPointer.value = intersectionType;
			map.put(intersectionType, countingPointer);
			AbstractType intersect = TypeOps.intersect("X", t,t1);
			countingPointer.value=intersect;
			return intersect;
		} else {
			countingPointer.count++;
			return countingPointer.value;
		}
	}

	void release(AbstractType t) {
		final CountingPointer countingPointer = map.get(t);
		if (countingPointer != null) {
			countingPointer.count--;
			if (countingPointer.count == 0) {
				map.remove(t);
			}
		}
	}
}
