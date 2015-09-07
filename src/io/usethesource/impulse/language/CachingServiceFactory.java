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

package io.usethesource.impulse.language;

import java.util.Set;

import io.usethesource.impulse.editor.OutlineContentProviderBase;
import io.usethesource.impulse.editor.OutlineLabelProvider.IElementImageProvider;
import io.usethesource.impulse.indexing.IndexContributorBase;
import io.usethesource.impulse.parser.IModelListener;
import io.usethesource.impulse.parser.IParseController;
import io.usethesource.impulse.services.IASTAdapter;
import io.usethesource.impulse.services.IAnnotationHover;
import io.usethesource.impulse.services.IAutoEditStrategy;
import io.usethesource.impulse.services.IContentProposer;
import io.usethesource.impulse.services.IDocumentationProvider;
import io.usethesource.impulse.services.IEntityNameLocator;
import io.usethesource.impulse.services.IFoldingUpdater;
import io.usethesource.impulse.services.IHoverHelper;
import io.usethesource.impulse.services.ILabelProvider;
import io.usethesource.impulse.services.ILanguageActionsContributor;
import io.usethesource.impulse.services.ILanguageSyntaxProperties;
import io.usethesource.impulse.services.IOccurrenceMarker;
import io.usethesource.impulse.services.IOutliner;
import io.usethesource.impulse.services.IRefactoringContributor;
import io.usethesource.impulse.services.IReferenceResolver;
import io.usethesource.impulse.services.ISourceFormatter;
import io.usethesource.impulse.services.ISourceHyperlinkDetector;
import io.usethesource.impulse.services.ITokenColorer;
import io.usethesource.impulse.services.base.TreeModelBuilderBase;

public class CachingServiceFactory extends ServiceFactory {
    private IContentProposer contentProposer;
    private IOutliner outliner;
    private IElementImageProvider elementImageProvider;
    private ILanguageSyntaxProperties syntaxProperties;
    private IOccurrenceMarker occurrenceMarker;
    private IDocumentationProvider documentationProvider;
    private Set<ILanguageActionsContributor> languageActionsContributors;
    private IReferenceResolver referenceResolver;
    private Set<IRefactoringContributor> refactoringContributors;
    private OutlineContentProviderBase outlineContentProvider;
    private ILabelProvider labelProvider;
    private ISourceHyperlinkDetector sourceHyperlinkDetector;
    private ISourceFormatter sourceFormatter;
    private IAnnotationHover annotationHover;
    private IFoldingUpdater foldingUpdater;
    private Set<IAutoEditStrategy> autoEditStrategies;
    private IModelListener modelListener;
    private TreeModelBuilderBase treeModelBuilder;
    private IParseController parseController;
    private IndexContributorBase indexContributor;
    private ITokenColorer tokenColorer;
    private IHoverHelper hoverHelper;
    private IASTAdapter astAdapter;
    private IEntityNameLocator entityNameLocator;

    public CachingServiceFactory() { }

    @Override
    public IContentProposer getContentProposer(Language lang) {
        if (contentProposer == null) {
            contentProposer = super.getContentProposer(lang);
        }
        return contentProposer;
    }

    @Override
    public IHoverHelper getHoverHelper(Language lang) {
        if (hoverHelper == null) {
            hoverHelper = super.getHoverHelper(lang);
        }
        return hoverHelper;
    }

    @Override
    public ITokenColorer getTokenColorer(Language lang) {
        if (tokenColorer == null) {
            tokenColorer = super.getTokenColorer(lang);
        }
        return tokenColorer;
    }

    @Override
    public IndexContributorBase getIndexContributor(Language lang) {
        if (indexContributor == null) {
            indexContributor = super.getIndexContributor(lang);
        }
        return indexContributor;
    }

    @Override
    public IParseController getParseController(Language lang) {
        if (parseController == null) {
            parseController = super.getParseController(lang);
        }
        return parseController;
    }

    @Override
    public TreeModelBuilderBase getTreeModelBuilder(Language lang) {
        if (treeModelBuilder == null) {
            treeModelBuilder = super.getTreeModelBuilder(lang);
        }
        return treeModelBuilder;
    }

    @Override
    public IModelListener getModelListener(Language lang) {
        if (modelListener == null) {
            modelListener = super.getModelListener(lang);
        }
        return modelListener;
    }

    @Override
    public Set<IAutoEditStrategy> getAutoEditStrategies(Language lang) {
        if (autoEditStrategies == null) {
            autoEditStrategies = super.getAutoEditStrategies(lang);
        }
        return autoEditStrategies;
    }

    @Override
    public IFoldingUpdater getFoldingUpdater(Language lang) {
        if (foldingUpdater == null) {
            foldingUpdater = super.getFoldingUpdater(lang);
        }
        return foldingUpdater;
    }

    @Override
    public IAnnotationHover getAnnotationHover(Language lang) {
        if (annotationHover == null) {
            annotationHover = super.getAnnotationHover(lang);
        }
        return annotationHover;
    }

    @Override
    public ISourceFormatter getSourceFormatter(Language lang) {
        if (sourceFormatter == null) {
            sourceFormatter = super.getSourceFormatter(lang);
        }
        return sourceFormatter;
    }

    @Override
    public ISourceHyperlinkDetector getSourceHyperlinkDetector(Language lang) {
        if (sourceHyperlinkDetector == null) {
            sourceHyperlinkDetector = super.getSourceHyperlinkDetector(lang);
        }
        return sourceHyperlinkDetector;
    }

    @Override
    public ILabelProvider getLabelProvider(Language lang) {
        if (labelProvider == null) {
            labelProvider = super.getLabelProvider(lang);
        }
        return labelProvider;
    }

    @Override
    public OutlineContentProviderBase getOutlineContentProvider(Language lang) {
        if (outlineContentProvider == null) {
            outlineContentProvider = super.getOutlineContentProvider(lang);
        }
        return outlineContentProvider;
    }

    @Override
    public Set<IRefactoringContributor> getRefactoringContributors(Language lang) {
        if (refactoringContributors == null) {
            refactoringContributors = super.getRefactoringContributors(lang);
        }
        return refactoringContributors;
    }

    @Override
    public IReferenceResolver getReferenceResolver(Language lang) {
        if (referenceResolver == null) {
            referenceResolver = super.getReferenceResolver(lang);
        }
        return referenceResolver;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<ILanguageActionsContributor> getLanguageActionsContributors(Language lang) {
        if (languageActionsContributors == null) {
            languageActionsContributors = super
                    .getLanguageActionsContributors(lang);
        }
        return languageActionsContributors;
    }

    @Override
    public IDocumentationProvider getDocumentationProvider(Language lang) {
        if (documentationProvider == null) {
            documentationProvider = super.getDocumentationProvider(lang);
        }
        return documentationProvider;
    }

    @Override
    public IOccurrenceMarker getOccurrenceMarker(Language lang) {
        if (occurrenceMarker == null) {
            occurrenceMarker = super.getOccurrenceMarker(lang);
        }
        return occurrenceMarker;
    }

    @Override
    public ILanguageSyntaxProperties getSyntaxProperties(Language lang) {
        if (syntaxProperties == null) {
            syntaxProperties = super.getSyntaxProperties(lang);
        }
        return syntaxProperties;
    }

    @Override
    public IElementImageProvider getElementImageProvider(Language lang) {
        if (elementImageProvider == null) {
            elementImageProvider = super.getElementImageProvider(lang);
        }
        return elementImageProvider;
    }

    @Override
    public IOutliner getOutliner(Language lang) {
        if (outliner == null) {
            outliner = super.getOutliner(lang);
        }
        return outliner;
    }

    @Override
    public IASTAdapter getASTAdapter(Language lang) {
        if (astAdapter == null) {
            astAdapter = super.getASTAdapter(lang);
        }
        return astAdapter;
    }

    @Override
    public IEntityNameLocator getEntityNameLocator(Language lang) {
        if (entityNameLocator == null) {
            entityNameLocator = super.getEntityNameLocator(lang);
        }
        return entityNameLocator;
    }
}
