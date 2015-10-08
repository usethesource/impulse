/*******************************************************************************
* Copyright (c) 2007, IBM Corporation, 2015 CWI
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation

*******************************************************************************/

package io.usethesource.impulse.parser;

import org.eclipse.core.runtime.IProgressMonitor;

import io.usethesource.impulse.language.ILanguageService;

/**
 * A language service that needs to be notified in order to update in response to source
 * code changes.
 * @author Claffra
 * @author rfuhrer@watson.ibm.com
 */
public interface IModelListener extends ILanguageService {

    /**
     * Notify the listener that the document has been updated and a new AST has been computed
     * @param parseController the parse controller that, among other things, provides the most recent AST
     * @param monitor the progress monitor; listener should cancel when monitor.isCanceled() is true
     */
    public void update(IParseController parseController, IProgressMonitor monitor);
}
