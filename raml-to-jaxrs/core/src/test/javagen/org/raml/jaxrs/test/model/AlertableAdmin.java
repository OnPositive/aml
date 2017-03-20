
package org.raml.jaxrs.test.model;

import javax.annotation.Generated;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Generated(value = "org.aml.raml2java", date = "Mon Mar 20 17:15:45 ICT 2017")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class AlertableAdmin
    extends Admin
{

    protected String phone;

    @Pattern(regexp = "[0-9|-]+")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String value) {
        this.phone=value;
    }

    public AlertableAdmin withPhone(String value) {
        this.phone=value;
        return this;
    }

}
