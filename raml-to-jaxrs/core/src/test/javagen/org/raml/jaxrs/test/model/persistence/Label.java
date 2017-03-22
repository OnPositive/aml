
package org.raml.jaxrs.test.model.persistence;

import javax.annotation.Generated;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import org.aml.persistance.jdo.VisibleWhen;

@Generated(value = "org.aml.raml2java", date = "Mon Mar 20 21:21:10 NOVT 2017")
@PersistenceCapable
public class Label {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
    protected long id;
    protected String title;
    @VisibleWhen("+details")
    protected Project parent;
    protected String color;

    public long getId() {
        return id;
    }

    public void setId(long value) {
        this.id=value;
    }

    public Label withId(long value) {
        this.id=value;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title=value;
    }

    public Label withTitle(String value) {
        this.title=value;
        return this;
    }

    public Project getParent() {
        return parent;
    }

    public void setParent(Project value) {
        this.parent=value;
    }

    public Label withParent(Project value) {
        this.parent=value;
        return this;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String value) {
        this.color=value;
    }

    public Label withColor(String value) {
        this.color=value;
        return this;
    }

}
