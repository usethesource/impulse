/*******************************************************************************
* Copyright (c) 2008 IBM Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Stan Sutton (suttons@us.ibm.com) - initial API and implementation

*******************************************************************************/

package io.usethesource.impulse.services;

import io.usethesource.impulse.editor.UniversalEditor;
import io.usethesource.impulse.parser.IModelListener;

public interface IEditorService
	extends IModelListener
{
	
	public String getName();
	
	public void setEditor(UniversalEditor editor);

}
