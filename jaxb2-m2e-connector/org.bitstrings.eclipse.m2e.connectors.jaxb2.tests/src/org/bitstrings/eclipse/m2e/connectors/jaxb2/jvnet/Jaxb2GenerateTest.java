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
package org.bitstrings.eclipse.m2e.connectors.jaxb2.jvnet;

import org.bitstrings.eclipse.m2e.common.JavaProjectHelper;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.m2e.core.project.ResolverConfiguration;
import org.eclipse.m2e.tests.common.AbstractMavenProjectTestCase;

@SuppressWarnings( "restriction" )
public class Jaxb2GenerateTest extends AbstractMavenProjectTestCase
{
    public void testGenerateVersion074() throws Exception
    {
        commonGenerateTest("jaxb2-plugin, jaxb2-version-0.7.4");
    }

    public void testGenerateVersion075() throws Exception
    {
        commonGenerateTest("jaxb2-plugin, jaxb2-version-0.7.5");
    }

    public void testGenerateVersion080() throws Exception
    {
        commonGenerateTest("jaxb2-plugin, jaxb2-version-0.8.0");
    }

    public void testGenerate0Version075() throws Exception
    {
        commonGenerateTest("jaxb20-plugin, jaxb2-version-0.7.5");
    }

    public void testGenerate0Version080() throws Exception
    {
        commonGenerateTest("jaxb20-plugin, jaxb2-version-0.8.0");
    }

    public void testGenerate1Version075() throws Exception
    {
        commonGenerateTest("jaxb21-plugin, jaxb2-version-0.7.5");
    }

    public void testGenerate1Version080() throws Exception
    {
        commonGenerateTest("jaxb21-plugin, jaxb2-version-0.8.0");
    }

    public void testGenerate2Version075() throws Exception
    {
        commonGenerateTest("jaxb22-plugin, jaxb2-version-0.7.5");
    }

    public void testGenerate2Version080() throws Exception
    {
        commonGenerateTest("jaxb22-plugin, jaxb2-version-0.8.0");
    }

    protected void commonGenerateTest(String profiles) throws Exception
    {
        ResolverConfiguration configuration = new ResolverConfiguration();
        if (profiles != null)
        {
            configuration.setActiveProfiles(profiles);
        }
        IProject project = importProject("projects/jvnet-generate-test/pom.xml", configuration);
        waitForJobsToComplete();

        project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
        project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
        waitForJobsToComplete();

        assertNoErrors(project);

        IClasspathEntry[] cp = JavaCore.create(project).getRawClasspath();

        assertTrue(
                JavaProjectHelper.containsPath(
                                new Path("/jvnet-generate-test/target/generated-sources/xjc"), cp));

        assertTrue(
                project
                    .getFile("target/generated-sources/xjc/generated/Items.java")
                        .isSynchronized(IResource.DEPTH_ZERO));

        assertTrue(
                project
                    .getFile("target/generated-sources/xjc/generated/Items.java")
                        .isAccessible());
    }
}
