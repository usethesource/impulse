/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package io.usethesource.impulse.editor.internal.hover;

import java.util.ArrayList;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.TextInvocationContext;
import org.eclipse.ui.texteditor.MarkerAnnotation;

import io.usethesource.impulse.editor.hover.AbstractAnnotationHover;
import io.usethesource.impulse.editor.hover.ProblemLocation;
import io.usethesource.impulse.editor.internal.QuickFixController;
import io.usethesource.impulse.editor.quickfix.IAnnotation;

/**
 * This annotation hover shows the description of the selected java annotation.
 * 
 * XXX: Currently this problem hover only works for Java and spelling problems.
 * see: https://bugs.eclipse.org/bugs/show_bug.cgi?id=62081
 * 
 * @since 3.0
 */
public class ProblemHover extends AbstractAnnotationHover {

	protected static class ProblemInfo extends AnnotationInfo {
		private static final ICompletionProposal[] NO_PROPOSALS = new ICompletionProposal[0];

		public ProblemInfo(Annotation annotation, Position position,
				ITextViewer textViewer) {
			super(annotation, position, textViewer);
		}

		/*
		 * @see
		 * org.eclipse.jdt.internal.ui.text.java.hover.AbstractAnnotationHover
		 * .AnnotationInfo#getCompletionProposals()
		 */
		public ICompletionProposal[] getCompletionProposals() {
			if (annotation instanceof IAnnotation) {
				ICompletionProposal[] result = getAnnotationFixes((IAnnotation) annotation);
				if (result.length > 0)
					return result;
			}

			if (annotation instanceof MarkerAnnotation) {
				return getMarkerAnnotationFixes((MarkerAnnotation) annotation);
			}
			return NO_PROPOSALS;
		}

		private ICompletionProposal[] getAnnotationFixes(IAnnotation annotation) {
			QuickFixController qac = new QuickFixController(annotation.getEditor());
			
			final ProblemLocation location = new ProblemLocation(position
					.getOffset(), position.getLength(), annotation);
			
			IQuickAssistInvocationContext quickAssistContext = new IQuickAssistInvocationContext() {
				public ISourceViewer getSourceViewer() {
					if (viewer instanceof ISourceViewer)
						return (ISourceViewer) viewer;
					return null;
				}

				public int getOffset() {
					return location.getOffset();
				}

				public int getLength() {
					return location.getLength();
				}
			};

			ArrayList<ICompletionProposal> proposals = new ArrayList<ICompletionProposal>();
			qac.collectCorrections(qac.getContext(quickAssistContext),
					new ProblemLocation[] { location }, proposals);
			// Collections.sort(proposals, new CompletionProposalComparator());

			return (ICompletionProposal[]) proposals
					.toArray(new ICompletionProposal[proposals.size()]);
		}

		private ICompletionProposal[] getMarkerAnnotationFixes(MarkerAnnotation markerAnnotation) {
			if (markerAnnotation.isQuickFixableStateSet() && !markerAnnotation.isQuickFixable())
				return NO_PROPOSALS;

			TextInvocationContext context = new TextInvocationContext(
					((ISourceViewer) this.viewer), position.getOffset(),
					position.getLength());
			QuickFixController c = new QuickFixController(markerAnnotation.getMarker());
			return c.computeQuickAssistProposals(context);
		}

		/*
		 * @see
		 * org.eclipse.jdt.internal.ui.text.java.hover.AbstractAnnotationHover
		 * .AnnotationInfo#fillToolBar(org.eclipse.jface.action.ToolBarManager)
		 */
		public void fillToolBar(ToolBarManager manager, IInformationControl infoControl) {
			super.fillToolBar(manager, infoControl);
			// if (!(annotation instanceof IJavaAnnotation))
			// return;

			// IJavaAnnotation javaAnnotation= (IJavaAnnotation) annotation;
			//
			// String optionId=
			// JavaCore.getOptionForConfigurableSeverity(javaAnnotation.getId());
			// if (optionId != null) {
			// IJavaProject javaProject=
			// javaAnnotation.getCompilationUnit().getJavaProject();
			// boolean isJavadocProblem= (javaAnnotation.getId() &
			// IProblem.Javadoc) != 0;
			// ConfigureProblemSeverityAction problemSeverityAction= new
			// ConfigureProblemSeverityAction(javaProject, optionId,
			// isJavadocProblem, infoControl);
			// manager.add(problemSeverityAction);
			// }
		}

	}

	public ProblemHover() {
		super(false);
	}

	protected AnnotationInfo createAnnotationInfo(Annotation annotation,
			Position position, ITextViewer textViewer) {
		return new ProblemInfo(annotation, position, textViewer);
	}
}
