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
package org.bitstrings.eclipse.m2e.connectors.xmlbeans;

import org.bitstrings.eclipse.m2e.common.JavaProjectUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.m2e.tests.common.AbstractMavenProjectTestCase;

@SuppressWarnings( "restriction" )
public class XmlBeansGenerationTest extends AbstractMavenProjectTestCase
{
    public void test01() throws Exception
    {
        final IProject project = importProject("projects/test/pom.xml");
        waitForJobsToComplete();

        project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
        project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
        waitForJobsToComplete();

        assertNoErrors(project);

        IClasspathEntry[] cp = JavaCore.create(project).getRawClasspath();

        assertTrue(
                JavaProjectUtils.containsPath(
                        new Path("/test/target/generated-sources/xmlbeans"), cp));

        assertTrue(
                project
                    .getFile("target/generated-sources/xmlbeans/noNamespace/Items.java")
                        .isSynchronized(IResource.DEPTH_ZERO));

        assertTrue(
                project
                    .getFile("target/generated-sources/xmlbeans/noNamespace/Items.java")
                        .isAccessible());
    }
}
