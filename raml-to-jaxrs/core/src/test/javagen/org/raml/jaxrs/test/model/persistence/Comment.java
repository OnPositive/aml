
package org.raml.jaxrs.test.model.persistence;

import javax.annotation.Generated;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import org.aml.persistance.jdo.VisibleWhen;

@Generated(value = "org.aml.raml2java", date = "Mon Mar 20 17:15:46 ICT 2017")
@PersistenceCapable
public class Comment {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
    protected long id;
    protected String title;
    @VisibleWhen("+details")
    protected Issue parent;
    @VisibleWhen("+details, +create, +update")
    protected String body;

    public long getId() {
        return id;
    }

    public void setId(long value) {
        this.id=value;
    }

    public Comment withId(long value) {
        this.id=value;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title=value;
    }

    public Comment withTitle(String value) {
        this.title=value;
        return this;
    }

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
