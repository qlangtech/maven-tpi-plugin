package com.qlangtech.tis.maven.plugins.tpi;

//import hudson.util.VersionNumber;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.MavenProjectHelper;

import java.util.List;

/**
 * Mojos that need to figure out the Jenkins version it's working with.
 *
 * @author Kohsuke Kawaguchi
 */
public abstract class AbstractJenkinsMojo extends AbstractMojo {

    /**
     * The maven project.
     */
    @Component
    protected MavenProject project;

//    /**
//     * Optional string that represents "groupId:artifactId" of Jenkins core jar.
//     * If left unspecified, the default groupId/artifactId pair for Jenkins is looked for.
//     *
//     * @since 1.65
//     */
//    @Parameter
//    protected String jenkinsCoreId;

    /**
     * Optional string that represents the version of Jenkins core to report plugins as requiring.
     * This parameter is only used when unbundling functionality from Jenkins core and the version specified
     * will be ignored if older than the autodetected version.
     */
    @Parameter
    private String tisCoreVersionOverride;


    /**
     * List of Remote Repositories used by the resolver
     */
    @Parameter(defaultValue = "${project.remoteArtifactRepositories}", readonly = true, required = true)
    protected List<ArtifactRepository> remoteRepos;

    @Component
    @Parameter(defaultValue = "${localRepository}", readonly = true, required = true)
    protected ArtifactRepository localRepository;

    @Component
    protected ArtifactMetadataSource artifactMetadataSource;

    @Component
    protected ArtifactFactory artifactFactory;

    @Component
    protected ArtifactResolver artifactResolver;

    @Component
    protected MavenProjectBuilder projectBuilder;

    @Component
    protected MavenProjectHelper projectHelper;


    protected String findTISVersion() throws MojoExecutionException {
        for (Dependency a : (List<Dependency>) project.getDependencies()) {
            boolean match;
//            if (jenkinsCoreId != null)
//                match = (a.getGroupId() + ':' + a.getArtifactId()).equals(jenkinsCoreId);
//            else
            match = (a.getGroupId().equals("com.qlangtech.tis"))
                    && (a.getArtifactId().equals("tis-plugin"));

            if (match) {
                if (StringUtils.isNotBlank(tisCoreVersionOverride)) {
                    VersionNumber v1 = new VersionNumber(a.getVersion());
                    VersionNumber v2 = new VersionNumber(tisCoreVersionOverride);
                    if (v1.compareTo(v2) == -1) {
                        return tisCoreVersionOverride;
                    }
                    getLog().warn("Ignoring 'tisCoreVersionOverride' of " + tisCoreVersionOverride + " as the "
                            + "autodetected version, " + a.getVersion() + ", is newer. Please remove the redundant "
                            + "version override.");
                }
                return a.getVersion();
            }
        }
        if (StringUtils.isNotBlank(tisCoreVersionOverride)) {
            return tisCoreVersionOverride;
        }
        throw new MojoExecutionException("Failed to determine Jenkins version this plugin depends on.");
    }

    protected MavenArtifact wrap(Artifact a) {
        return new MavenArtifact(a, artifactResolver, artifactFactory, projectBuilder, remoteRepos, localRepository);
    }
}
