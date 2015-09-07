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

package io.usethesource.impulse.editor;

import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.ISourceViewer;

import io.usethesource.impulse.language.Language;
import io.usethesource.impulse.language.ServiceFactory;
import io.usethesource.impulse.parser.IParseController;
import io.usethesource.impulse.parser.ISourcePositionLocator;
import io.usethesource.impulse.services.IDocumentationProvider;
import io.usethesource.impulse.services.IHoverHelper;
import io.usethesource.impulse.services.IReferenceResolver;
import io.usethesource.impulse.utils.AnnotationUtils;
import io.usethesource.impulse.utils.HTMLPrinter;

/**
 * Helper class for implementing "hover help" which encapsulates the process of locating
 * the AST node under the cursor, and asking the documentation provider for the relevant
 * information to show in the hover.<br>
 * No longer an extension point itself, this class is instantiated directly by the
 * HoverHelpController and initialized with a Language so that it can instantiate the
 * correct documentation provider.
 * @author rfuhrer
 * @author awtaylor, per attachment to bug #322427
 */
public class HoverHelper implements IHoverHelper {
    private final Language fLanguage;

    public HoverHelper(Language lang) {
        fLanguage= lang;
    }

    public String getHoverHelpAt(IParseController parseController, ISourceViewer srcViewer, int offset) {
		try {
			int lineOfOffset= srcViewer.getDocument().getLineOfOffset(offset);
            List<Annotation> annotations= AnnotationUtils.getAnnotationsForLine(srcViewer, lineOfOffset);

			if (annotations != null && annotations.size() > 0) {
				String annString = AnnotationUtils.formatAnnotationList(annotations);

				if (annString != null && annString.length() > 0) {
					return annString;
				}
			}
		} catch (BadLocationException e) {
			return "???";
		}

    	IReferenceResolver refResolver = ServiceFactory.getInstance().getReferenceResolver(fLanguage);
        Object root= parseController.getCurrentAst();
        ISourcePositionLocator nodeLocator = parseController.getSourcePositionLocator();

        if (root == null) return null;

        Object selNode = nodeLocator.findNode(root, offset);

        if (selNode == null) return null;

        // determine whether this is a reference to something else 
       	Object target = (refResolver != null) ? refResolver.getLinkTarget(selNode, parseController) : selNode;

       	// if target is null, we're hovering over a declaration whose javadoc is right before us, but
       	// showing it can still be useful for previewing the javadoc formatting
       	// OR if target is null we're hovering over something not a decl or ref (e.g. integer literal)
       	// ==>  show information if something other than the source is available:
       	// 1. if target != src, show something
       	// 2. if target == src, and docProvider gives something, then show it
       	// 3. if target == src, and docProvider doesn't give anything, don't show anything
       	//
       	
       	// if this is not a reference, provide info for it anyway 
       	if (target == null) target=selNode;

       	IDocumentationProvider docProvider= ServiceFactory.getInstance().getDocumentationProvider(fLanguage);
       	String doc= (docProvider != null) ? docProvider.getDocumentation(target, parseController) : null;			

       	if (doc != null)
       		return doc;

       	if (target==selNode)
       		return null;

       	StringBuffer buffer= new StringBuffer();

       	HTMLPrinter.addSmallHeader(buffer, target.toString());
       	HTMLPrinter.addParagraph(buffer, doc);
       	return buffer.toString();
    }
}
