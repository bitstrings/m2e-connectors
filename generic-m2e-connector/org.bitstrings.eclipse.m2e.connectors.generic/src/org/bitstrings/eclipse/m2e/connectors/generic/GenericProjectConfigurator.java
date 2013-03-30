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

import org.apache.maven.plugin.MojoExecution;
import org.eclipse.m2e.core.lifecyclemapping.model.IPluginExecutionMetadata;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.configurator.AbstractBuildParticipant;
import org.eclipse.m2e.jdt.AbstractJavaProjectConfigurator;

public class GenericProjectConfigurator
    extends AbstractJavaProjectConfigurator
{
    protected String outputDirectoryParameterName;

    protected String sourceDirectoryParameterName;

    protected String sourceIncludesParameterName;

    protected String sourceExcludesParameterName;

    @Override
    protected String getOutputFolderParameterName()
    {
        return outputDirectoryParameterName;
    }

    @Override
    public AbstractBuildParticipant getBuildParticipant(
                    IMavenProjectFacade projectFacade,
                    MojoExecution execution,
                    IPluginExecutionMetadata executionMetadata)
    {
        return
            new GenericBuildParticipant(
                    execution,
                    outputDirectoryParameterName,
                    sourceDirectoryParameterName,
                    sourceIncludesParameterName,
                    sourceExcludesParameterName);
    }
}
