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
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import io.usethesource.impulse.core.ErrorHandler;
import io.usethesource.impulse.language.Language;
import io.usethesource.impulse.language.ServiceFactory;
import io.usethesource.impulse.parser.IModelListener;
import io.usethesource.impulse.parser.IParseController;
import io.usethesource.impulse.services.IContentProposer;

public class CompletionProcessor implements IContentAssistProcessor, IModelListener {
    private final IContextInformation[] NO_CONTEXTS= new IContextInformation[0];

    private ICompletionProposal[] NO_COMPLETIONS= new ICompletionProposal[0];

    private final Language fLanguage;

    private IParseController fParseController;

    private IContentProposer fContentProposer;

    // private HippieProposalProcessor hippieProcessor= new HippieProposalProcessor();

    public CompletionProcessor(Language language) {
        fLanguage= language;
        fContentProposer= ServiceFactory.getInstance().getContentProposer(fLanguage);
    }

    public AnalysisRequired getAnalysisRequired() {
        return AnalysisRequired.LEXICAL_ANALYSIS;
    }

    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
        try {
            if (fParseController != null && fContentProposer != null) {
                return fContentProposer.getContentProposals(fParseController, offset, viewer);
            }
            // TODO Once we move to 3.2, delegate to the HippieProposalProcessor
            // return hippieProcessor.computeCompletionProposals(viewer, offset);
        } catch (Throwable e) {
            ErrorHandler.reportError("Exception caught from language-specific content proposer implementation", e);
        }
        return NO_COMPLETIONS;
    }

    public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
        return NO_CONTEXTS;
    }

    public char[] getCompletionProposalAutoActivationCharacters() {
        return null;
    }

    public char[] getContextInformationAutoActivationCharacters() {
        return null;
    }

    public IContextInformationValidator getContextInformationValidator() {
        return null;
    }

    public String getErrorMessage() {
        return null;
    }

    public void update(IParseController parseController, IProgressMonitor monitor) {
        fParseController= parseController;
    }

    // RMF 7 Jan 2010 - It seems that JFace caches our CompletionProcessor, and doesn't
    // get rid of the reference when the IMP editor closes. So, to avoid memory leaks,
    // we define a dispose() method here, to be called when the IMP editor is closed.
    public void dispose() {
        fParseController= null;
        fContentProposer= null;
    }

    public String toString() {
        return "Completion processor for " + fLanguage.getName() + " on " + fParseController.getPath().toPortableString();
    }
}
