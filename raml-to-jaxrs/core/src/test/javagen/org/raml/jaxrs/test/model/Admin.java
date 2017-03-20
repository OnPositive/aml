
package org.raml.jaxrs.test.model;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Generated(value = "org.aml.raml2java", date = "Mon Mar 20 17:15:45 ICT 2017")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Admin
    extends Person
{

    protected AdminclearanceLevel clearanceLevel;

    public AdminclearanceLevel getClearanceLevel() {
        return clearanceLevel;
    }

    public void setClearanceLevel(AdminclearanceLevel value) {
        this.clearanceLevel=value;
    }

    public Admin withClearanceLevel(AdminclearanceLevel value) {
        this.clearanceLevel=value;
        return this;
    }

}
