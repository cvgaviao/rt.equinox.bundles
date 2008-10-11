/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   David Knibb               initial implementation      
 *   Matthew Webster           Eclipse 3.2 changes     
 *******************************************************************************/

package org.eclipse.equinox.service.weaving;

import java.io.IOException;

import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.State;
import org.osgi.framework.Bundle;

public interface IWeavingService {

    public void flushGeneratedClasses(ClassLoader loader);

    public boolean generatedClassesExistFor(ClassLoader loader, String className);

    public IWeavingService getInstance(ClassLoader loader, Bundle bundle,
            State resolverState, BundleDescription bundleDesciption,
            ISupplementerRegistry supplementerRegistry);

    public String getKey();

    public byte[] preProcess(String name, byte[] classbytes, ClassLoader loader)
            throws IOException;

}
