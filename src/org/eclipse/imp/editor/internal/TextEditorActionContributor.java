/**
 * 
 */
package org.eclipse.uide.editor;

import java.util.ResourceBundle;
import org.eclipse.jdt.ui.actions.JdtActionConstants;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.texteditor.BasicTextEditorActionContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.RetargetTextEditorAction;

public class TextEditorActionContributor extends BasicTextEditorActionContributor {
    private GotoAnnotationAction fNextAnnotation;

    private GotoAnnotationAction fPreviousAnnotation;

    private RetargetTextEditorAction fShowOutline;

    private RetargetTextEditorAction fToggleComment;

    public TextEditorActionContributor() {
	super();
	fPreviousAnnotation= new GotoAnnotationAction("PreviousAnnotation.", false); //$NON-NLS-1$
	fNextAnnotation= new GotoAnnotationAction("NextAnnotation.", true); //$NON-NLS-1$
	fShowOutline= new RetargetTextEditorAction(ResourceBundle.getBundle(UniversalEditor.MESSAGE_BUNDLE), "ShowOutline."); //$NON-NLS-1$
	fShowOutline.setActionDefinitionId(UniversalEditor.SHOW_OUTLINE_COMMAND);
	fToggleComment= new RetargetTextEditorAction(ResourceBundle.getBundle(UniversalEditor.MESSAGE_BUNDLE), "ToggleComment."); //$NON-NLS-1$
	fToggleComment.setActionDefinitionId(UniversalEditor.TOGGLE_COMMENT_COMMAND);
    }

    public void init(IActionBars bars, IWorkbenchPage page) {
	super.init(bars, page);
	bars.setGlobalActionHandler(ITextEditorActionDefinitionIds.GOTO_NEXT_ANNOTATION, fNextAnnotation);
	bars.setGlobalActionHandler(ITextEditorActionDefinitionIds.GOTO_PREVIOUS_ANNOTATION, fPreviousAnnotation);
	bars.setGlobalActionHandler(ActionFactory.NEXT.getId(), fNextAnnotation);
	bars.setGlobalActionHandler(ActionFactory.PREVIOUS.getId(), fPreviousAnnotation);
	bars.setGlobalActionHandler(UniversalEditor.SHOW_OUTLINE_COMMAND, fShowOutline);
	bars.setGlobalActionHandler(UniversalEditor.TOGGLE_COMMENT_COMMAND, fToggleComment);
    }

    /*
     * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToMenu(org.eclipse.jface.action.IMenuManager)
     */
    public void contributeToMenu(IMenuManager menu) {
	super.contributeToMenu(menu);

	IMenuManager navigateMenu= menu.findMenuUsingPath(IWorkbenchActionConstants.M_NAVIGATE);

	if (navigateMenu != null) {
	    navigateMenu.appendToGroup(IWorkbenchActionConstants.SHOW_EXT, fShowOutline);
	}

	IMenuManager editMenu= menu.findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);

	if (editMenu != null) {
	    editMenu.appendToGroup(IWorkbenchActionConstants.EDIT_END, fToggleComment);
	}
    }

    public void setActiveEditor(IEditorPart part) {
	super.setActiveEditor(part);

	ITextEditor textEditor= null;

	if (part instanceof ITextEditor)
	    textEditor= (ITextEditor) part;

	fPreviousAnnotation.setEditor(textEditor);
	fNextAnnotation.setEditor(textEditor);
	fShowOutline.setAction(getAction(textEditor, UniversalEditor.SHOW_OUTLINE_COMMAND));
	fToggleComment.setAction(getAction(textEditor, UniversalEditor.TOGGLE_COMMENT_COMMAND));

	IActionBars bars= getActionBars();

	bars.setGlobalActionHandler(JdtActionConstants.FORMAT, getAction(textEditor, "Format")); //$NON-NLS-1$
    }
}
