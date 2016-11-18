package org.aml.apimodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.aml.apimodel.impl.NamedParamImpl;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.TypeOps;

public interface Resource extends Annotable, Comparable<Resource>, IHasResources {

	/**
	 * Relative URL of this resource from the parent resource
	 **/
	String relativeUri();

	/**
	 * The displayName attribute specifies the resource display name. It is a
	 * friendly name used only for display or documentation purposes. If
	 * displayName is not specified, it defaults to the element's key (the name
	 * of the property itself).
	 **/
	String displayName();

	/**
	 * A nested resource is identified as any property whose name begins with a
	 * slash ("&#47;") and is therefore treated as a relative URI.
	 **/
	List<Resource> resources();

	Resource parentResource();

	/**
	 * Methods that are part of this resource type definition
	 **/
	List<Action> methods();

	String description();

	/**
	 * Detailed information about any URI parameters of this resource
	 **/
	List<? extends INamedParam> uriParameters();

	String getUri();

	Api getApi();

	default int compareTo(Resource o) {
		return relativeUri().compareTo(o.relativeUri());
	}

	default Optional<? extends INamedParam> uriParameterOpt(String name) {
		return this.uriParameters().stream().filter(x -> x.getKey().equals(name)).findFirst();
	}

	default INamedParam uriParameter(String name) {
		return uriParameterOpt(name).orElse(null);
	}

	default List<INamedParam> allUriParameters() {
		String relativeUri = this.relativeUri();
		HashSet<String> pNames = new HashSet<>();
		ArrayList<INamedParam> result = new ArrayList<>();
		Resource r = this;
		while (r != null) {
			while (true) {
				int indexOf = relativeUri.indexOf('{');
				if (indexOf == -1) {
					break;
				}
				int end = relativeUri.indexOf('}', indexOf);
				if (end == -1) {
					break;
				}
				String pName = relativeUri.substring(indexOf + 1, end);
				relativeUri = relativeUri.substring(end);
				pNames.add(pName);
			}
			for (String s : pNames) {
				boolean found = false;
				for (INamedParam p : result) {
					if (p.getKey().equals(s)) {
						found = true;
						break;
					}
				}
				if (!found) {
					NamedParamImpl namedParam = new NamedParamImpl(TypeOps.derive(s, BuiltIns.STRING), false, false);
					result.add(namedParam);
				}
			}
			r=r.parentResource();
		}
		Collections.reverse(result);
		return result;
	}

	default Action method(String name) {
		return this.methods().stream().filter(x -> x.method().equals(name)).findFirst().orElse(null);
	}
}