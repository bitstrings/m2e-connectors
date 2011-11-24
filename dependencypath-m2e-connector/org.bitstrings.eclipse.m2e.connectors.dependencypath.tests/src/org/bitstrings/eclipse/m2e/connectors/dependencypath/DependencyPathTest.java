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
package org.bitstrings.eclipse.m2e.connectors.dependencypath;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.m2e.tests.common.AbstractMavenProjectTestCase;

@SuppressWarnings("restriction")
public class DependencyPathTest extends AbstractMavenProjectTestCase
{
    public void test01() throws Exception
    {
        final IProject project = importProject("projects/test/pom.xml");
        waitForJobsToComplete();

        assertNoErrors(project);

        project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
        waitForJobsToComplete();

        assertNoErrors(project);

        Properties prop = new Properties();

        BufferedInputStream in = null;

        try
        {
            in =
                new BufferedInputStream(
                        new FileInputStream(
                                project.getFile("target/classes/test.properties").getRawLocation().toFile()));

            prop.load(in);
        }
        finally
        {
            if (in != null)
            {
                in.close();
            }
        }

        assertFalse(
                "${org.bitstrings.maven.plugins:dependencypath-maven-plugin:jar}"
                        .equals(
                                prop.getProperty("org.bitstrings.maven.plugins:dependencypath-maven-plugin:jar")));
    }
}
