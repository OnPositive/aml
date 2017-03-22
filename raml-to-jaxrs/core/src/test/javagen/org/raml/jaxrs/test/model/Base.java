
package org.raml.jaxrs.test.model;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Generated(value = "org.aml.raml2java", date = "Mon Mar 20 21:21:10 NOVT 2017")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Base {

    protected long id;
    protected String title;

    public long getId() {
        return id;
    }

    public void setId(long value) {
        this.id=value;
    }

    public Base withId(long value) {
        this.id=value;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title=value;
    }

    public Base withTitle(String value) {
        this.title=value;
        return this;
    }

}
