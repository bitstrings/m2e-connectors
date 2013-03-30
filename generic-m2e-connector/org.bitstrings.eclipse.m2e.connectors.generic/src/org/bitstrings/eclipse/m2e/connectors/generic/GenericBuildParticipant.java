/*
 *
 *    Copyright (c) 2011 bitstrings.org - Pino Silvaggio
 *
 *    All rights reserved. This program and the accompanying materials
 *    are made available under the terms of the Eclipse Public License v1.0
 *    which accompanies this distribution, and is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 *
 */
package org.bitstrings.eclipse.m2e.connectors.generic;

import java.io.File;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecution;
import org.bitstrings.eclipse.m2e.common.BuildHelper;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.m2e.core.project.configurator.MojoExecutionBuildParticipant;
import org.sonatype.plexus.build.incremental.BuildContext;

public class GenericBuildParticipant
    extends MojoExecutionBuildParticipant
{
    protected final String outputDirectoryParameterName;

    protected final String sourceDirectoryParameterName;

    protected final String sourceIncludesParameterName;

    protected final String sourceExcludesParameterName;

    public GenericBuildParticipant(
                MojoExecution execution,
                String outputDirectoryParameterName,
                String sourceDirectoryParameterName,
                String sourceIncludesParameterName,
                String sourceExcludesParameterName)
    {
        super(execution, true);

        this.outputDirectoryParameterName = outputDirectoryParameterName;
        this.sourceDirectoryParameterName = sourceDirectoryParameterName;
        this.sourceIncludesParameterName = sourceIncludesParameterName;
        this.sourceExcludesParameterName = sourceExcludesParameterName;
    }

    @Override
    public Set<IProject> build(int kind, IProgressMonitor monitor)
        throws Exception
    {
        final IMaven maven = MavenPlugin.getMaven();
        final BuildContext buildContext = getBuildContext();
        final MavenSession mavenSession = getSession();
        final MojoExecution mojoExecution = getMojoExecution();

        boolean filesModified =
                    !ArrayUtils.isEmpty(
                            BuildHelper.getModifiedFiles(
                                            mavenSession, mojoExecution,
                                            maven, buildContext,
                                            sourceDirectoryParameterName,
                                            sourceIncludesParameterName,
                                            sourceExcludesParameterName));

        if (!filesModified)
        {
            return null;
        }

        final Set<IProject> result = super.build(kind, monitor);

        final File generated =
                        maven.getMojoParameterValue(
                                    mavenSession,
                                    mojoExecution,
                                    outputDirectoryParameterName,
                                    File.class);

        if (generated != null)
        {
            buildContext.refresh(generated);
        }

        return result;
    }
}
