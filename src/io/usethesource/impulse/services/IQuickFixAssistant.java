package io.usethesource.impulse.services;

import java.util.Collection;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.source.Annotation;

import io.usethesource.impulse.editor.hover.ProblemLocation;
import io.usethesource.impulse.language.ILanguageService;

public interface IQuickFixAssistant extends ILanguageService {
	public boolean canFix(Annotation annotation);

	public boolean canAssist(IQuickFixInvocationContext invocationContext);

	public String[] getSupportedMarkerTypes();

	public void addProposals(IQuickFixInvocationContext context,
			ProblemLocation problem, Collection<ICompletionProposal> proposals);
}
