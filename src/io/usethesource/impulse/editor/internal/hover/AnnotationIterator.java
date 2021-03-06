/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package io.usethesource.impulse.editor.internal.hover;

import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.text.source.Annotation;

import org.eclipse.ui.texteditor.MarkerAnnotation;

import io.usethesource.impulse.editor.quickfix.IAnnotation;


/**
 * Filters problems based on their types.
 */
public class AnnotationIterator implements Iterator<Annotation> {

	private Iterator<Annotation> fIterator;
	private Annotation fNext;
	private boolean fReturnAllAnnotations;


	/**
	 * Returns a new JavaAnnotationIterator.
	 * @param parent the parent iterator to iterate over annotations
	 * @param returnAllAnnotations whether to return all annotations or just problem annotations
	 */
	public AnnotationIterator(Iterator<Annotation> parent, boolean returnAllAnnotations) {
		fReturnAllAnnotations= returnAllAnnotations;
		fIterator= parent;
		skip();
	}

	private void skip() {
		while (fIterator.hasNext()) {
			Annotation next= (Annotation) fIterator.next();

			if (next.isMarkedDeleted())
				continue;

			if (fReturnAllAnnotations || next instanceof IAnnotation || isProblemMarkerAnnotation(next)) {
				fNext= next;
				return;
			}
		}
		fNext= null;
	}

	private static boolean isProblemMarkerAnnotation(Annotation annotation) {
		if (!(annotation instanceof MarkerAnnotation))
			return false;
		try {
			return(((MarkerAnnotation)annotation).getMarker().isSubtypeOf(IMarker.PROBLEM));
		} catch (CoreException e) {
			return false;
		}
	}

	/*
	 * @see Iterator#hasNext()
	 */
	public boolean hasNext() {
		return fNext != null;
	}

	/*
	 * @see Iterator#next()
	 */
	public Annotation next() {
		try {
			return fNext;
		} finally {
			skip();
		}
	}

	/*
	 * @see Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
