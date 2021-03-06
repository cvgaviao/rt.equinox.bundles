/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.equinox.metatype.tests;

import org.osgi.framework.Bundle;
import org.osgi.service.metatype.*;

public class BugTests extends AbstractTest {
	private Bundle bundle;

	/*
	 * A cardinality of zero should not require a default value to be specified.
	 */
	public void test334642() {
		MetaTypeInformation mti = metatype.getMetaTypeInformation(bundle);
		assertNotNull("Metatype information not found", mti); //$NON-NLS-1$
		ObjectClassDefinition ocd = mti.getObjectClassDefinition("org.eclipse.equinox.metatype.tests.tb3", null); //$NON-NLS-1$
		assertNotNull("Object class definition not found", ocd); //$NON-NLS-1$
		AttributeDefinition[] ads = ocd.getAttributeDefinitions(ObjectClassDefinition.ALL);
		assertNotNull("Attribute definitions not found", ads); //$NON-NLS-1$
		assertEquals("Wrong number of attribute definitions", 3, ads.length); //$NON-NLS-1$

		AttributeDefinition ad = findAttributeDefinitionById("password1", ads); //$NON-NLS-1$
		assertNotNull("Attribute definition not found", ad); //$NON-NLS-1$
		assertEquals("Wrong cardinality", 0, ad.getCardinality()); //$NON-NLS-1$
		assertNull("Default value should be null", ad.getDefaultValue()); //$NON-NLS-1$
		assertNotNull("Validation should be present", ad.validate(getFirstDefaultValue(ad.getDefaultValue()))); //$NON-NLS-1$
		assertTrue("Validation should fail", ad.validate(getFirstDefaultValue(ad.getDefaultValue())).length() > 0); //$NON-NLS-1$

		ad = findAttributeDefinitionById("password2", ads); //$NON-NLS-1$
		assertNotNull("Attribute definition not found", ad); //$NON-NLS-1$
		assertEquals("Wrong cardinality", 0, ad.getCardinality()); //$NON-NLS-1$
		assertNull("Default value should be null", ad.getDefaultValue()); //$NON-NLS-1$
		assertNotNull("Validation should be present", ad.validate(getFirstDefaultValue(ad.getDefaultValue()))); //$NON-NLS-1$
		assertTrue("Validation should fail", ad.validate(getFirstDefaultValue(ad.getDefaultValue())).length() > 0); //$NON-NLS-1$

		ad = findAttributeDefinitionById("string1", ads); //$NON-NLS-1$
		assertNotNull("Attribute definition not found", ad); //$NON-NLS-1$
		assertEquals("Wrong cardinality", 0, ad.getCardinality()); //$NON-NLS-1$
		assertEquals("Only one default value should exist", 1, ad.getDefaultValue().length); //$NON-NLS-1$
		assertEquals("Wrong default value", "Hello, world!", getFirstDefaultValue(ad.getDefaultValue())); //$NON-NLS-1$ //$NON-NLS-2$
		assertValidationPass(escape(getFirstDefaultValue(ad.getDefaultValue())), ad);
	}

	/*
	 * StringIndexOutOfBoundsException when description or name attributes are an empty string
	 */
	public void test341963() {
		MetaTypeInformation mti = metatype.getMetaTypeInformation(bundle);
		assertNotNull("Metatype information not found", mti); //$NON-NLS-1$
		ObjectClassDefinition ocd = mti.getObjectClassDefinition("ocd2", null); //$NON-NLS-1$
		assertNotNull("Object class definition not found", ocd); //$NON-NLS-1$
		assertEquals("Wrong name", "", ocd.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Wrong description", "", ocd.getDescription()); //$NON-NLS-1$ //$NON-NLS-2$
		AttributeDefinition[] ads = ocd.getAttributeDefinitions(ObjectClassDefinition.ALL);
		assertNotNull("Attribute definitions not found", ads); //$NON-NLS-1$
		assertEquals("Wrong number of attribute definitions", 1, ads.length); //$NON-NLS-1$

		AttributeDefinition ad = findAttributeDefinitionById("ad1", ads); //$NON-NLS-1$
		assertNotNull("Attribute definition not found", ad); //$NON-NLS-1$
		assertEquals("Wrong name", "", ad.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Wrong description", "", ad.getDescription()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	protected void setUp() throws Exception {
		super.setUp();
		bundle = bundleInstaller.installBundle("tb3"); //$NON-NLS-1$
		bundle.start();
	}

	protected void tearDown() throws Exception {
		bundle.stop();
		super.tearDown();
	}
}
