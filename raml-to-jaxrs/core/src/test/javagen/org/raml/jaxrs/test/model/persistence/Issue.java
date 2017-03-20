
package org.raml.jaxrs.test.model.persistence;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Generated;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import org.aml.persistance.jdo.VisibleWhen;

@Generated(value = "org.aml.raml2java", date = "Mon Mar 20 17:15:46 ICT 2017")
@PersistenceCapable
public class Issue {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
    protected long id;
    protected String title;
    @VisibleWhen("+details")
    protected Project parent;
    @VisibleWhen("+details, +create, +update")
    protected String body;
    @VisibleWhen("+details, +create, +update")
    protected List<String> labels = new ArrayList<String>();
    @Element(dependent = "true")
    @VisibleWhen("+none")
    private Set<Comment> comments = new HashSet();

    public long getId() {
        return id;
    }

    public void setId(long value) {
        this.id=value;
    }

    public Issue withId(long value) {
        this.id=value;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title=value;
    }

    public Issue withTitle(String value) {
        this.title=value;
        return this;
    }

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
