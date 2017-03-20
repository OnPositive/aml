
package org.raml.jaxrs.test.model;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Generated(value = "org.aml.raml2java", date = "Mon Mar 20 17:15:46 ICT 2017")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Person {

    protected String name;
    protected String lastName;
    protected double age;

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name=value;
    }

    public Person withName(String value) {
        this.name=value;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String value) {
        this.lastName=value;
    }

    public Person withLastName(String value) {
        this.lastName=value;
        return this;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double value) {
        this.age=value;
    }

    public Person withAge(double value) {
        this.age=value;
        return this;
    }

}
