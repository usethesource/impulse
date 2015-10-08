/*******************************************************************************
* Copyright (c) 2007 IBM Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation

*******************************************************************************/

package io.usethesource.impulse.model.internal;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IProgressMonitor;

import io.usethesource.impulse.model.ISourceEntity;
import io.usethesource.impulse.model.ISourceProject;
import io.usethesource.impulse.model.IWorkspaceModel;
import io.usethesource.impulse.model.ModelFactory;
import io.usethesource.impulse.model.ModelFactory.ModelException;
import io.usethesource.impulse.runtime.RuntimePlugin;

public class WorkspaceModel implements IWorkspaceModel {
    private final IWorkspaceRoot fWSRoot;

    public WorkspaceModel(IWorkspaceRoot wsRoot) {
        fWSRoot= wsRoot;
    }

    public String getName() {
        return "";
    }

    public ISourceEntity getParent() {
        return null;
    }

    public ISourceEntity getAncestor(Class<?> ofType) {
        if (ofType == IWorkspaceModel.class) {
            return this;
        }
        return null;
    }

    public ISourceEntity[] getChildren() {
        return getProjects();
    }

    public ISourceProject[] getProjects() {
        IProject[] projects= fWSRoot.getProjects();
        ISourceProject[] srcProjects= new ISourceProject[projects.length];

        for(int i= 0; i < projects.length; i++) {
            IProject project= projects[i];

            try {
                ISourceProject srcProject= ModelFactory.open(project);
                srcProjects[i]= srcProject;
            } catch (ModelException e) {
                RuntimePlugin.getInstance().logException(e.getMessage(), e);
            }
        }
        return srcProjects;
    }

    public IResource getResource() {
        return fWSRoot;
    }

    public void commit(IProgressMonitor monitor) {
        // TODO Auto-generated method stub
    }

    public String toString() {
        return "<workspace>";
    }
}
