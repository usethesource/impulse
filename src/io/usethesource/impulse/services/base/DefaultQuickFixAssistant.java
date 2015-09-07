package io.usethesource.impulse.services.base;

import java.util.Collection;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.source.Annotation;

import io.usethesource.impulse.editor.hover.ProblemLocation;
import io.usethesource.impulse.services.IQuickFixAssistant;
import io.usethesource.impulse.services.IQuickFixInvocationContext;

public class DefaultQuickFixAssistant implements IQuickFixAssistant {

	public boolean canAssist(IQuickFixInvocationContext invocationContext) {
		return false;
	}

	public boolean canFix(Annotation annotation) {
		return false;
	}

	public String[] getSupportedMarkerTypes() {
		return new String[0];
	}

	public void addProposals(IQuickFixInvocationContext context,
			ProblemLocation problem, Collection<ICompletionProposal> proposals) {
	}
}
