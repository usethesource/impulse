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

package io.usethesource.impulse.editor.internal;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;

import io.usethesource.impulse.parser.IModelListener;
import io.usethesource.impulse.parser.IParseController;
import io.usethesource.impulse.runtime.RuntimePlugin;
import io.usethesource.impulse.services.IFoldingUpdater;

public class FoldingController implements IModelListener {
    private final ProjectionAnnotationModel fAnnotationModel;
    private final IFoldingUpdater fFoldingUpdater;

    public FoldingController(ProjectionAnnotationModel annotationModel, IFoldingUpdater foldingUpdater) {
        super();
        this.fAnnotationModel= annotationModel;
        this.fFoldingUpdater= foldingUpdater;
    }

    public void update(IParseController parseController, IProgressMonitor monitor) {
        if (fAnnotationModel != null) { // can be null if file is outside workspace
            try {
                fFoldingUpdater.updateFoldingStructure(parseController, fAnnotationModel);
            } catch (Exception e) {
                RuntimePlugin.getInstance().logException("Error while updating folding annotations for " + parseController.getPath(), e);
            }
        }
    }
}
