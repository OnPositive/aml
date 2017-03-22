
package org.raml.jaxrs.test.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Generated(value = "org.aml.raml2java", date = "Mon Mar 20 21:21:10 NOVT 2017")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Anonimous3 {

    protected List<Comment> items = new ArrayList<Comment>();
    protected int total;

    public List<Comment> getItems() {
        return items;
    }

    public void setItems(List<Comment> value) {
        this.items=value;
    }

    public Anonimous3 withItems(List<Comment> value) {
        this.items=value;
        return this;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int value) {
        this.total=value;
    }

    public Anonimous3 withTotal(int value) {
        this.total=value;
        return this;
    }

}
