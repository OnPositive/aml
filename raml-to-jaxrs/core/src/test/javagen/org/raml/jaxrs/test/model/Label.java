
package org.raml.jaxrs.test.model;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Generated(value = "org.aml.raml2java", date = "Mon Mar 20 17:15:47 ICT 2017")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Label
    extends Base
{

    protected Project parent;
    protected String color;

    public Project getParent() {
        return parent;
    }

    public void setParent(Project value) {
        this.parent=value;
    }

    public Label withParent(Project value) {
        this.parent=value;
        return this;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String value) {
        this.color=value;
    }

    public Label withColor(String value) {
        this.color=value;
        return this;
    }

}
