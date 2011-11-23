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

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.bitstrings.eclipse.m2e.common.JavaProjectUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.m2e.core.project.ResolverConfiguration;
import org.eclipse.m2e.tests.common.AbstractMavenProjectTestCase;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

@SuppressWarnings( "restriction" )
public class Jaxb2SchemagenTest extends AbstractMavenProjectTestCase
{
    public void testSchemagen() throws Exception
    {
        schemagenCommon(null);
    }

    public void testSchemagenIncludes1() throws Exception
    {
        IProject project = schemagenCommon("includes-1");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(false);
        Document doc =
                dbf.newDocumentBuilder()
                        .parse(
                            project.getFile("target/generated-resources/schemagen/schema1.xsd").getLocation().toFile());

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        NodeList nodeList =
                    ((NodeList) xpath.evaluate("/schema//complexType[@name='bean2']", doc, XPathConstants.NODESET));

        assertEquals(nodeList.getLength(), 1);

        nodeList =
            ((NodeList) xpath.evaluate("/schema//complexType[@name='bean']", doc, XPathConstants.NODESET));

        assertEquals(nodeList.getLength(), 0);
    }

    public void testSchemagenExcludes1() throws Exception
    {
        IProject project = schemagenCommon("excludes-1");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(false);
        Document doc =
                dbf.newDocumentBuilder()
                        .parse(
                            project.getFile("target/generated-resources/schemagen/schema1.xsd").getLocation().toFile());

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        NodeList nodeList =
                    ((NodeList) xpath.evaluate("/schema//complexType[@name='bean']", doc, XPathConstants.NODESET));

        assertEquals(nodeList.getLength(), 1);

        nodeList =
            ((NodeList) xpath.evaluate("/schema//complexType[@name='bean2']", doc, XPathConstants.NODESET));

        assertEquals(nodeList.getLength(), 0);
    }

    protected IProject schemagenCommon(String profiles) throws Exception
    {
        ResolverConfiguration configuration = new ResolverConfiguration();
        if (profiles != null)
        {
            configuration.setActiveProfiles(profiles);
        }
        IProject project = importProject("projects/codehaus-schemagen-test/pom.xml", configuration);
        waitForJobsToComplete();

        project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
        project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
        waitForJobsToComplete();

        assertNoErrors(project);

        IClasspathEntry[] cp = JavaCore.create(project).getRawClasspath();

        assertTrue(
                JavaProjectUtils.containsPath(
                                new Path("/codehaus-schemagen-test/target/generated-resources/schemagen"), cp));

        assertTrue(
                project
                    .getFile("target/generated-resources/schemagen/schema1.xsd")
                        .isSynchronized(IResource.DEPTH_ZERO));

        assertTrue(
                project
                    .getFile("target/generated-resources/schemagen/schema1.xsd")
                        .isAccessible());

        return project;
    }
}
