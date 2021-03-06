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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import io.usethesource.impulse.editor.OutlineLabelProvider.IElementImageProvider;
import io.usethesource.impulse.parser.IModelListener;
import io.usethesource.impulse.parser.IParseController;
import io.usethesource.impulse.parser.ISourcePositionLocator;
import io.usethesource.impulse.runtime.RuntimePlugin;
import io.usethesource.impulse.services.IEntityNameLocator;
import io.usethesource.impulse.services.base.TreeModelBuilderBase;

public class IMPOutlinePage extends ContentOutlinePage implements IModelListener {
    private final OutlineContentProviderBase fContentProvider;
    private final TreeModelBuilderBase fModelBuilder;
    private final ILabelProvider fLabelProvider;
    private final IParseController fParseController;
    private final IRegionSelectionService regionSelector;
    private final IEntityNameLocator fNameLocator;
	private static final ExecutorService treeBuilder = Executors.newCachedThreadPool();

    /**
     * Constructor flavor introduced for backward-compatibility with clients that extend this
     * class but do not furnish an IEntityNameLocator.
     */
    public IMPOutlinePage(IParseController parseController,
            TreeModelBuilderBase modelBuilder,
            ILabelProvider labelProvider, IElementImageProvider imageProvider,
            IRegionSelectionService regionSelector) {
        this(parseController, modelBuilder, labelProvider, imageProvider, null, regionSelector);
    }

    public IMPOutlinePage(IParseController parseController,
            TreeModelBuilderBase modelBuilder,
            ILabelProvider labelProvider, IElementImageProvider imageProvider,
            IEntityNameLocator nameLocator,
            IRegionSelectionService regionSelector) {
        fParseController= parseController;
        fModelBuilder= modelBuilder;
        fLabelProvider= labelProvider;
        fNameLocator= nameLocator;
        
        // SMS 21 Aug 2008
        if (regionSelector != null)
        	this.regionSelector = regionSelector;
        else {
        	this.regionSelector = new IRegionSelectionService() {
        	    public void selectAndReveal(int startOffset, int length) {
        	        IEditorPart activeEditor= PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        	        ITextEditor textEditor= (ITextEditor) activeEditor;

        	        textEditor.selectAndReveal(startOffset, length);
        	    }
        	};
        }

        fContentProvider= new OutlineContentProviderBase(null) {
            public Object[] getChildren(Object element) {
                ModelTreeNode node= (ModelTreeNode) element;
                return node.getChildren();
            }
            public Object getParent(Object element) {
                ModelTreeNode node= (ModelTreeNode) element;
                return node.getParent();
            }
        };
        
    }

    public void update(final IParseController parseController, IProgressMonitor monitor) {
    	TreeViewer currentTreeViewer = getTreeViewer();
        if (currentTreeViewer != null && !currentTreeViewer.getTree().isDisposed()) {
        	Object currentAst = fParseController.getCurrentAst();

        	CompletableFuture.supplyAsync(() -> {
        		synchronized (fModelBuilder) {
        			return fModelBuilder.buildTree(currentAst);
				}
        	}, treeBuilder)
        	.thenAccept(t -> {
        		if (currentAst == fParseController.getCurrentAst()) {
        			// still relevant, so we schedule an update on the gui thread
                    currentTreeViewer.getTree().getDisplay().asyncExec(new Runnable() {
                        public void run() {
                            if (currentTreeViewer != null && !currentTreeViewer.getTree().isDisposed())
                                currentTreeViewer.setInput(t);
                        }
                    });
        		}
        	});
        }
    }

    @Override
    public void selectionChanged(SelectionChangedEvent event) {
        super.selectionChanged(event);
        ITreeSelection sel= (ITreeSelection) event.getSelection();

        if (sel.isEmpty())
            return;

        ModelTreeNode selNode= (ModelTreeNode) sel.getFirstElement();
        ISourcePositionLocator locator= fParseController.getSourcePositionLocator();
        Object node= selNode.getASTNode();
        if (fNameLocator != null) {
            Object name= fNameLocator.getName(node);
            if (name != null) {
                node= name;
            }
        }
        
        int startOffset= locator.getStartOffset(node);
        int endOffset= locator.getEndOffset(node);
        int length= endOffset - startOffset + 1;
        
        IPath path = locator.getPath(node);
        if (path != null) {
            if (!fParseController.getProject().resolvePath(fParseController.getPath()).equals(path)) {
                // we have a different file to move to!
                new TargetLink("external outline", startOffset, length, path, startOffset, length, regionSelector).open();
                return;
            }
        }
        
        regionSelector.selectAndReveal(startOffset, length);
    }

    public void createControl(Composite parent) {
        super.createControl(parent);
        TreeViewer viewer= getTreeViewer();
        viewer.setContentProvider(fContentProvider);
        if (fLabelProvider != null) {
            viewer.setLabelProvider(fLabelProvider);
        }
        viewer.addSelectionChangedListener(this);
        ModelTreeNode rootNode= fModelBuilder.buildTree(fParseController.getCurrentAst());
        viewer.setInput(rootNode);
        viewer.setAutoExpandLevel(2);

        IPageSite site= getSite();
        IActionBars actionBars= site.getActionBars();

        registerToolbarActions(actionBars);
     }

    class LexicalSortingAction extends Action {
        // TODO Need to introduce some API to provide language-specific "categories" that get used for sorting and filtering; perhaps on ModelTreeNode?
        private ViewerComparator fElementComparator= new ViewerComparator() {
            @Override
            public int compare(Viewer viewer, Object e1, Object e2) {
                ModelTreeNode t1= (ModelTreeNode) e1;
                ModelTreeNode t2= (ModelTreeNode) e2;
                int cat1= t1.getCategory();
                int cat2= t2.getCategory();

                if (cat1 == cat2) {
                    return fLabelProvider.getText(t1).compareTo(fLabelProvider.getText(t2));
                }
                return cat1 - cat2;
            }
        };
        private ISourcePositionLocator fLocator= fParseController.getSourcePositionLocator();

        private ViewerComparator fPositionComparator= new ViewerComparator() {
            @Override
            public int compare(Viewer viewer, Object e1, Object e2) {
                int pos1= fLocator.getStartOffset(e1);
                int pos2= fLocator.getStartOffset(e2);

                return pos1 - pos2;
            }
        };

        public LexicalSortingAction() {
            super();
//          PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IJavaHelpContextIds.LEXICAL_SORTING_OUTLINE_ACTION);
            setText("Sort");
            setToolTipText("Sort by name");
            setDescription("Sort entries lexically by name");

            ImageDescriptor desc= RuntimePlugin.getImageDescriptor("icons/alphab_sort_co.gif"); //$NON-NLS-1$
            this.setHoverImageDescriptor(desc);
            this.setImageDescriptor(desc); 

            boolean checked= RuntimePlugin.getInstance().getPreferenceStore().getBoolean("LexicalSortingAction.isChecked"); //$NON-NLS-1$
            valueChanged(checked, false);
        }

        public void run() {
            valueChanged(isChecked(), true);
        }

        private void valueChanged(final boolean on, boolean store) {
            final TreeViewer outlineViewer= getTreeViewer();
            setChecked(on);
            BusyIndicator.showWhile(outlineViewer.getControl().getDisplay(), new Runnable() {
                public void run() {
                    if (on)
                        outlineViewer.setComparator(fElementComparator);
                    else
                        outlineViewer.setComparator(fPositionComparator);
                }
            });

            if (store) {
                // RMF Need to store separate settings per language
                RuntimePlugin.getInstance().getPreferenceStore().setValue("LexicalSortingAction.isChecked", on); //$NON-NLS-1$
            }
        }
    }

    private void registerToolbarActions(IActionBars actionBars) {
        IToolBarManager toolBarManager= actionBars.getToolBarManager();
        toolBarManager.add(new LexicalSortingAction());

//        fMemberFilterActionGroup= new MemberFilterActionGroup(fOutlineViewer, "org.eclipse.jdt.ui.JavaOutlinePage"); //$NON-NLS-1$
//        fMemberFilterActionGroup.contributeToToolBar(toolBarManager);
//
//        fCustomFiltersActionGroup.fillActionBars(actionBars);
//
//        IMenuManager viewMenuManager= actionBars.getMenuManager();
//        viewMenuManager.add(new Separator("EndFilterGroup")); //$NON-NLS-1$
//
//        fToggleLinkingAction= new ToggleLinkingAction(this);
//        viewMenuManager.add(new ClassOnlyAction());
//        viewMenuManager.add(fToggleLinkingAction);
//
//        fCategoryFilterActionGroup= new CategoryFilterActionGroup(fOutlineViewer, "org.eclipse.jdt.ui.JavaOutlinePage", new IJavaElement[] {fInput}); //$NON-NLS-1$
//        fCategoryFilterActionGroup.contributeToViewMenu(viewMenuManager);
    }
}
