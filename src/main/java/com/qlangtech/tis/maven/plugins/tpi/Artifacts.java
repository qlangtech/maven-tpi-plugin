package com.qlangtech.tis.maven.plugins.tpi;

import com.google.common.base.Predicate;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;

import java.util.*;

/**
 * Collection filter operations on a set of {@link Artifact}s.
 * 
 * @author Kohsuke Kawaguchi
 */
public class Artifacts extends ArrayList<Artifact> {
    public Artifacts() {
    }

    public Artifacts(Collection<? extends Artifact> c) {
        super(c);
    }

    /**
     * Return the {@link Artifact}s representing dependencies of the given project.
     * 
     * A thin-wrapper of p.getArtifacts()
     */
    public static Artifacts of(MavenProject p) {
        return new Artifacts(p.getArtifacts());
    }

    public static Artifacts ofDirectDependencies(MavenProject p) {
        return new Artifacts(p.getDependencyArtifacts());
    }

    public Artifacts retainAll(Predicate<Artifact> filter) {
        for (Iterator<Artifact> itr = iterator(); itr.hasNext(); ) {
            if (!filter.apply(itr.next()))
                itr.remove();
        }
        return this;
    }

    public Artifacts removeAll(Predicate<Artifact> filter) {
        for (Iterator<Artifact> itr = iterator(); itr.hasNext(); ) {
            if (filter.apply(itr.next()))
                itr.remove();
        }
        return this;
    }
    
    public Artifacts scopeIs(String... scopes) {
        final List<String> s = Arrays.asList(scopes);
        return retainAll(new Predicate<Artifact>() {
            public boolean apply(Artifact a) {
                return s.contains(a.getScope());
            }
        });
    }

    public Artifacts scopeIsNot(String... scopes) {
        final List<String> s = Arrays.asList(scopes);
        return removeAll(new Predicate<Artifact>() {
            public boolean apply(Artifact a) {
                return s.contains(a.getScope());
            }
        });
    }

    public Artifacts typeIs(String... type) {
        final List<String> s = Arrays.asList(type);
        return retainAll(new Predicate<Artifact>() {
            public boolean apply(Artifact a) {
                return s.contains(a.getType());
            }
        });
    }

    public Artifacts typeIsNot(String... type) {
        final List<String> s = Arrays.asList(type);
        return removeAll(new Predicate<Artifact>() {
            public boolean apply(Artifact a) {
                return s.contains(a.getType());
            }
        });
    }

    public Artifacts groupIdIs(String... groupId) {
        final List<String> s = Arrays.asList(groupId);
        return retainAll(new Predicate<Artifact>() {
            public boolean apply(Artifact a) {
                return s.contains(a.getType());
            }
        });
    }

    public Artifacts groupIdIsNot(String... groupId) {
        final List<String> s = Arrays.asList(groupId);
        return removeAll(new Predicate<Artifact>() {
            public boolean apply(Artifact a) {
                return s.contains(a.getType());
            }
        });
    }

    public Artifacts artifactIdIs(String... artifactId) {
        final List<String> s = Arrays.asList(artifactId);
        return retainAll(new Predicate<Artifact>() {
            public boolean apply(Artifact a) {
                return s.contains(a.getType());
            }
        });
    }

    public Artifacts artifactIdIsNot(String... artifactId) {
        final List<String> s = Arrays.asList(artifactId);
        return removeAll(new Predicate<Artifact>() {
            public boolean apply(Artifact a) {
                return s.contains(a.getType());
            }
        });
    }
}
