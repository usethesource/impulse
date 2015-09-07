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

/*
 * Created on Feb 8, 2006
 */
package io.usethesource.impulse.services;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.texteditor.ITextEditor;

import io.usethesource.impulse.language.ILanguageService;
import io.usethesource.impulse.parser.IParseController;

public interface ISourceHyperlinkDetector extends ILanguageService {
    IHyperlink[] detectHyperlinks(IRegion region, ITextEditor editor, ITextViewer textViewer, IParseController parseController);
}
