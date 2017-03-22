
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

@Generated(value = "org.aml.raml2java", date = "Mon Mar 20 21:21:10 NOVT 2017")
@PersistenceCapable
public class Project {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
    protected long id;
    protected String title;
    protected List<Label> labels = new ArrayList<Label>();
    @Element(dependent = "true")
    @VisibleWhen("+none")
    private Set<Issue> issues = new HashSet();

    public long getId() {
        return id;
    }

    public void setId(long value) {
        this.id=value;
    }

    public Project withId(long value) {
        this.id=value;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title=value;
    }

    public Project withTitle(String value) {
        this.title=value;
        return this;
    }

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
