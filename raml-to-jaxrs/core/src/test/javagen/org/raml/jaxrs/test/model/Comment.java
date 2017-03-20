
package org.raml.jaxrs.test.model;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Generated(value = "org.aml.raml2java", date = "Mon Mar 20 17:15:47 ICT 2017")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Comment
    extends Base
{

    protected Issue parent;
    protected String body;

    public Issue getParent() {
        return parent;
    }

    public void setParent(Issue value) {
        this.parent=value;
    }

    public Comment withParent(Issue value) {
        this.parent=value;
        return this;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String value) {
        this.body=value;
    }

    public Comment withBody(String value) {
        this.body=value;
        return this;
    }

}
