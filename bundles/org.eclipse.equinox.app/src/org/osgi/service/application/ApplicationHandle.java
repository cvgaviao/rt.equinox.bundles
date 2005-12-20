/*
 * $Header: /cvsroot/eclipse/org.eclipse.equinox.app/src/org/osgi/service/application/ApplicationHandle.java,v 1.1 2005/12/14 22:17:04 twatson Exp $
 * 
 * Copyright (c) OSGi Alliance (2004, 2005). All Rights Reserved.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this 
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html.
 */

package org.osgi.service.application;

import org.osgi.framework.Constants;

/**
 * ApplicationHandle is an OSGi service interface which represents an instance
 * of an application. It provides the functionality to query and manipulate the
 * lifecycle state of the represented application instance. It defines constants
 * for the lifecycle states.
 */
public abstract class ApplicationHandle {

	/**
	 * The property key for the unique identifier (PID) of the application
	 * instance.
	 */
	public static final String APPLICATION_PID = Constants.SERVICE_PID;
	
	/**
	 * The property key for the pid of the corresponding application descriptor.
	 */
	public final static String APPLICATION_DESCRIPTOR	= "application.descriptor";
	
	/**
	 * The property key for the state of this appliction instance.
	 */
	public final static String APPLICATION_STATE		= "application.state";

	/**
	 * The application instance is running. This is the initial state of a newly
	 * created application instance.
	 */
	public final static String RUNNING = "RUNNING";
	
  /**
   * The application instance is being stopped. This is the state of the
   * application instance during the execution of the <code>destroy()</code>
   * method.
   */
	public final static String STOPPING = "STOPPING";

	private final String instanceId;
	
	/**
	 * Application instance identifier is specified by the container when the
	 * instance is created. The instance identifier must remain static for the 
	 * lifetime of the instance, it must remain the same even across framework
	 * restarts for the same application instance. This value must be the same
	 * as the <code>service.pid</code> service property of this application
	 * handle.
	 * <p>
	 * The instance identifier should follow the following scheme: 
	 * &lt;<i>application descriptor PID</i>&gt;.&lt;<i>index</i>&gt;
	 * where &lt;<i>application descriptor PID</i>&gt; is the PID of the 
	 * corresponding <code>ApplicationDescriptor</code> and &lt;<i>index</i>&gt;
	 * is a unique integer index assigned by the application container. 
	 * Even after destroying the application index the same index value should not
	 * be reused in a reasonably long timeframe.
	 * 
	 * @param instanceId the instance identifier of the represented application
	 * instance. It must not be null.
	 * 
	 * @param descriptor the <code>ApplicationDescriptor</code> of the represented
	 * application instance. It must not be null.
	 * 
	 * @throws NullPointerException if any of the arguments is null.
	 */
	protected ApplicationHandle(String instanceId, ApplicationDescriptor descriptor ) {
		if( (null == instanceId) || (null == descriptor) ) {
			throw new NullPointerException("Parameters must not be null!");
		}
		
		this.instanceId	= instanceId;
		this.descriptor = descriptor;
	}

	/**
	 * Retrieves the <code>ApplicationDescriptor</code> to which this 
	 * <code>ApplicationHandle</code> belongs. 
	 * 
	 * @return The corresponding <code>ApplicationDescriptor</code>
	 */
	public final ApplicationDescriptor getApplicationDescriptor() {
		return descriptor;
	}

	/**
	 * Get the state of the application instance.
	 * 
	 * @return the state of the application.
	 * 
	 * @throws IllegalStateException
	 *             if the application handle is unregistered
	 */
	public abstract String getState();

	/**
	 * Returns the unique identifier of this instance. This value is also
	 * available as a service property of this application handle's service.pid.
	 * 
	 * @return the unique identifier of the instance
	 */
	public final String getInstanceId() {
		return instanceId;
	}

	/**
	 * The application instance's lifecycle state can be influenced by this
	 * method. It lets the application instance perform operations to stop
	 * the application safely, e.g. saving its state to a permanent storage.
	 * <p>
	 * The method must check if the lifecycle transition is valid; a STOPPING
	 * application cannot be stopped. If it is invalid then the method must
	 * exit. Otherwise the lifecycle state of the application instance must be
	 * set to STOPPING. Then the destroySpecific() method must be called to
	 * perform any application model specific steps for safe stopping of the
	 * represented application instance.
	 * <p>
	 * At the end the <code>ApplicationHandle</code> must be unregistered. 
	 * This method should  free all the resources related to this 
	 * <code>ApplicationHandle</code>.
	 * <p>
	 * When this method is completed the application instance has already made
	 * its operations for safe stopping, the ApplicationHandle has been
	 * unregistered and its related resources has been freed. Further calls on
	 * this application should not be made because they may have unexpected
	 * results.
	 * 
	 * @throws SecurityException
	 *             if the caller doesn't have "lifecycle"
	 *             <code>ApplicationAdminPermission</code> for the corresponding application.
	 * 
	 * @throws IllegalStateException
	 *             if the application handle is unregistered
	 */
	public final void destroy() {
		if (STOPPING.equals(getState()))
			return;
		SecurityManager sm = System.getSecurityManager();
		if (sm != null)
			sm.checkPermission(new ApplicationAdminPermission(getApplicationDescriptor(), ApplicationAdminPermission.LIFECYCLE_ACTION));
		destroySpecific();
	}

	/**
	 * Called by the destroy() method to perform application model specific
	 * steps to stop and destroy an application instance safely.
	 * 
	 * @throws IllegalStateException
	 *             if the application handle is unregistered
	 */
	protected abstract void destroySpecific();
	
	ApplicationDescriptor		descriptor;

	/**
	 * @skip
	 *
	 */
	public interface Delegate {
		void setApplicationHandle(ApplicationHandle d, ApplicationDescriptor.Delegate descriptor );
		void destroy();
	}
}
