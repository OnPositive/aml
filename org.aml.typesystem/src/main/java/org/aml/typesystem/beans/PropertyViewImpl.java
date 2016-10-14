package org.aml.typesystem.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.FacetDeclaration;
import org.aml.typesystem.meta.restrictions.AdditionalProperties;
import org.aml.typesystem.meta.restrictions.HasPropertyRestriction;
import org.aml.typesystem.meta.restrictions.IMatchesProperty;
import org.aml.typesystem.meta.restrictions.MapPropertyIs;

/**
 * <p>PropertyViewImpl class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class PropertyViewImpl implements IPropertyView {

	protected AbstractType original;

	protected LinkedHashMap<String, PropertyBean> props = new LinkedHashMap<>();
	protected HashMap<String, PropertyBean> allprops = new LinkedHashMap<>();
	protected HashMap<String, PropertyBean> superProperties = new LinkedHashMap<>();

	protected LinkedHashMap<String, PropertyBean> facets = new LinkedHashMap<>();
	protected LinkedHashMap<String, PropertyBean> allFacets = new LinkedHashMap<>();

	/** {@inheritDoc} */
	@Override
	public List<IProperty> properties() {
		return new ArrayList<IProperty>(props.values());
	}

	/** {@inheritDoc} */
	@Override
	public List<IProperty> allProperties() {
		return new ArrayList<IProperty>(allprops.values());
	}
	
	
	/**
	 * <p>superProperties.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<IProperty> superProperties() {
		return new ArrayList<IProperty>(superProperties.values());
	}

	/** {@inheritDoc} */
	@Override
	public List<IProperty> facets() {
		return new ArrayList<IProperty>(facets.values());
	}

	/** {@inheritDoc} */
	@Override
	public List<IProperty> allFacets() {
		return new ArrayList<IProperty>(allFacets.values());
	}

	/**
	 * <p>Constructor for PropertyViewImpl.</p>
	 *
	 * @param t a {@link org.aml.typesystem.AbstractType} object.
	 */
	public PropertyViewImpl(AbstractType t) {
		this.original=t;
		buildProperties(t.meta(), allprops);
		for (AbstractType q:t.superTypes()){
			buildProperties(q.meta(), superProperties);
		}
		buildProperties(t.declaredMeta(), props);
		buildFacetDeclarations(t.declaredMeta(), facets);
		buildFacetDeclarations(t.meta(), allFacets);
	}
	
	/**
	 * <p>getDeclaredFacetsMap.</p>
	 *
	 * @return a {@link java.util.Map} object.
	 */
	public Map<String,PropertyBean>getDeclaredFacetsMap(){
		return facets;
	}
	/**
	 * <p>getDeclaredPropertiesMap.</p>
	 *
	 * @return a {@link java.util.Map} object.
	 */
	public Map<String,PropertyBean>getDeclaredPropertiesMap(){
		return props;
	}

	private void buildProperties(Set<TypeInformation> meta, HashMap<String, PropertyBean> props) {
		for (final TypeInformation t : meta) {
			if (t instanceof IMatchesProperty) {
				registerPropertyBean(props, t);
			} else if (t instanceof HasPropertyRestriction) {
				updatePropertyBeanToMarkRequired(props, t);
			}
		}
	}
	

	private void buildFacetDeclarations(final Set<TypeInformation> declaredMeta,
			final HashMap<String, PropertyBean> facets) {
		for (final TypeInformation z : declaredMeta) {
			if (z instanceof FacetDeclaration) {
				final FacetDeclaration d = (FacetDeclaration) z;
				final PropertyBean pbean = new PropertyBean();
				pbean.required = true;
				pbean.id = d.getName();
				pbean.type = d.getType();
				facets.put(d.getName(), pbean);
			}
		}
	}

	private void registerPropertyBean(HashMap<String, PropertyBean> props, TypeInformation t) {
		final IMatchesProperty ps = (IMatchesProperty) t;
		PropertyBean value2 = props.get(ps.id());
		if (value2 == null) {
			value2 = new PropertyBean();
			value2.setDeclaredAt(t.ownerType());
		}
		if (ps instanceof AdditionalProperties) {
			value2.additional = true;
			value2.required = true;
		}
		if (ps instanceof MapPropertyIs) {
			value2.isMap = true;
			value2.required = true;
		}
		value2.type = ps.range();
		value2.id = ps.id();
		props.put(ps.id(), value2);
	}

	private void updatePropertyBeanToMarkRequired(HashMap<String, PropertyBean> props, TypeInformation t) {
		final HasPropertyRestriction pr = (HasPropertyRestriction) t;
		PropertyBean value2 = props.get(pr.id());

		if (value2 == null) {
			value2 = new PropertyBean();
			value2.setDeclaredAt(t.ownerType());
		}
		value2.id = pr.id();
		value2.required = true;
		props.put(pr.id(), value2);
	}

	/**
	 * <p>getXMLHints.</p>
	 *
	 * @return a {@link org.aml.typesystem.beans.XMLHints} object.
	 */
	public XMLHints getXMLHints() {
		return new XMLHints(null, this.original);
	}

	/**
	 * <p>getProperty.</p>
	 *
	 * @param s a {@link java.lang.String} object.
	 * @return a {@link org.aml.typesystem.beans.IProperty} object.
	 */
	public IProperty getProperty(String s) {
		return allprops.get(s);
	}

	@Override
	public IProperty property(String name) {
		return allprops.get(name);
	}
}
