
package org.raml.jaxrs.test.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Generated(value = "org.aml.raml2java", date = "Mon Mar 20 17:15:47 ICT 2017")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Project
    extends Base
{

    protected List<Label> labels = new ArrayList<Label>();

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> value) {
        this.labels=value;
    }

    public Project withLabels(List<Label> value) {
        this.labels=value;
        return this;
    }

}
