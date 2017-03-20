
package org.raml.jaxrs.test.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Generated(value = "org.aml.raml2java", date = "Mon Mar 20 17:15:45 ICT 2017")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Manager
    extends Person
{

    protected List<Person> reports = new ArrayList<Person>();
    protected String phone;

    public List<Person> getReports() {
        return reports;
    }

    public void setReports(List<Person> value) {
        this.reports=value;
    }

    public Manager withReports(List<Person> value) {
        this.reports=value;
        return this;
    }

    @Pattern(regexp = "[0-9|-]+")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String value) {
        this.phone=value;
    }

    public Manager withPhone(String value) {
        this.phone=value;
        return this;
    }

}
