
package org.raml.jaxrs.test.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Generated(value = "org.aml.raml2java", date = "Mon Mar 20 21:21:10 NOVT 2017")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Issue
    extends Base
{

    protected Project parent;
    protected String body;
    protected List<String> labels = new ArrayList<String>();

    public Project getParent() {
        return parent;
    }

    public void setParent(Project value) {
        this.parent=value;
    }

    public Issue withParent(Project value) {
        this.parent=value;
        return this;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String value) {
        this.body=value;
    }

    public Issue withBody(String value) {
        this.body=value;
        return this;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> value) {
        this.labels=value;
    }

    public Issue withLabels(List<String> value) {
        this.labels=value;
        return this;
    }

}
