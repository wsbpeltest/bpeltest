package pebl.benchmark.feature;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;
import pebl.HasID;
import pebl.HasName;

@XmlAccessorType(XmlAccessType.NONE)
public class Feature implements HasID, HasName {

    @XmlInverseReference(mappedBy="features")
    private final FeatureSet featureSet;

    @XmlElement(required = true)
    private final String name;

    @XmlElement(required = true)
    private final String description;

    @XmlID
    @XmlAttribute(required = true)
    private final String id;

    @XmlElement
    private final List<Metric> metrics = new LinkedList<>();

    public Feature addMetric(ScriptMetricType scriptMetricType) {
        metrics.add(new Metric(scriptMetricType, getID()));

        return this;
    }

    public Feature() {
        this(new FeatureSet(), "");
    }

    public Feature(FeatureSet featureSet, String name) {
        this(featureSet, name, "");
    }

    public Feature(FeatureSet featureSet, String name, String description) {
        this.featureSet = Objects.requireNonNull(featureSet);
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);

        this.id = String.join(HasID.SEPARATOR, featureSet.getID(), name);

        this.featureSet.addFeature(this);
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Feature feature = (Feature) o;
        return Objects.equals(getID(), feature.getID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getID());
    }

    public String getDescription() {
        return description;
    }

    public FeatureSet getFeatureSet() {
        return featureSet;
    }
}
