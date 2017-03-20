
package org.raml.jaxrs.test.model;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Generated(value = "org.aml.raml2java", date = "Mon Mar 20 17:15:45 ICT 2017")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Org {

    protected AlertableAdmin onCall;
    protected Manager Head;

    public AlertableAdmin getOnCall() {
        return onCall;
    }

    public void setOnCall(AlertableAdmin value) {
        this.onCall=value;
    }

    public Org withOnCall(AlertableAdmin value) {
        this.onCall=value;
        return this;
    }

    public Manager getHead() {
        return Head;
    }

    public void setHead(Manager value) {
        this.Head=value;
    }

    public Org withHead(Manager value) {
        this.Head=value;
        return this;
    }

}
