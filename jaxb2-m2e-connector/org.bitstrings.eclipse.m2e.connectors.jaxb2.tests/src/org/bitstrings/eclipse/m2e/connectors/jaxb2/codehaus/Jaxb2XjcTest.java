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
package org.bitstrings.eclipse.m2e.connectors.jaxb2.codehaus;

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
public class Jaxb2XjcTest extends AbstractMavenProjectTestCase
{
    public void testXjc() throws Exception
    {
        IProject project = xjcCommon(null);

        assertTrue(
                project
                    .getFile("target/generated-sources/jaxb/generated/Items.java")
                        .isSynchronized(IResource.DEPTH_ZERO));

        assertTrue(
                project
                    .getFile("target/generated-sources/jaxb/generated/Items.java")
                        .isAccessible());

        assertTrue(
                project
                    .getFile("target/generated-sources/jaxb/generated/CatalogType.java")
                        .isSynchronized(IResource.DEPTH_ZERO));

        assertTrue(
                project
                    .getFile("target/generated-sources/jaxb/generated/CatalogType.java")
                        .isAccessible());
    }

    public void testXjcSchemaSpecifyAll() throws Exception
    {
        IProject project = xjcCommon("schema-specify-all");

        assertTrue(
                project
                    .getFile("target/generated-sources/jaxb/generated/Items.java")
                        .isSynchronized(IResource.DEPTH_ZERO));

        assertTrue(
                project
                    .getFile("target/generated-sources/jaxb/generated/Items.java")
                        .isAccessible());

        assertTrue(
                project
                    .getFile("target/generated-sources/jaxb/generated/CatalogType.java")
                        .isSynchronized(IResource.DEPTH_ZERO));

        assertTrue(
                project
                    .getFile("target/generated-sources/jaxb/generated/CatalogType.java")
                        .isAccessible());
    }

    public void testXjcSchemaSpecify1() throws Exception
    {
        IProject project = xjcCommon("schema-specify-1");

        assertTrue(
                project
                    .getFile("target/generated-sources/jaxb/generated/Items.java")
                        .isSynchronized(IResource.DEPTH_ZERO));

        assertTrue(
                project
                    .getFile("target/generated-sources/jaxb/generated/Items.java")
                        .isAccessible());

        assertFalse(
                project
                    .getFile("target/generated-sources/jaxb/generated/CatalogType.java")
                        .isAccessible());
    }

    protected IProject xjcCommon(String profile) throws Exception
    {
        ResolverConfiguration configuration = new ResolverConfiguration();
        if (profile != null)
        {
            configuration.setActiveProfiles(profile);
        }
        IProject project = importProject("projects/codehaus-xjc-test/pom.xml", configuration);
        waitForJobsToComplete();

        project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
        project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
        waitForJobsToComplete();

        assertNoErrors(project);

        IClasspathEntry[] cp = JavaCore.create(project).getRawClasspath();

        assertTrue(
                JavaProjectHelper.containsPath(
                        new Path("/codehaus-xjc-test/target/generated-sources/jaxb"), cp));

        return project;
    }
}
