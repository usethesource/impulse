/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Mateusz Matela <mateusz.matela@gmail.com> - [code manipulation] [dcr] toString() builder wizard - https://bugs.eclipse.org/bugs/show_bug.cgi?id=26070
 *******************************************************************************/
package io.usethesource.impulse.editor;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.texteditor.IUpdate;

/**
 * Action group that adds the source and generate actions to a part's context
 * menu and installs handlers for the corresponding global menu actions.
 *
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 *
 * @noextend This class is not intended to be subclassed by clients.
 */
public class GenerateActionGroup extends ActionGroup {
	/**
	 * Pop-up menu: id of the source sub menu (value <code>io.usethesource.impulse.source.menu</code>).
	 *
	 * @since 3.0
	 */
	public static final String MENU_ID= "io.usethesource.impulse.source.menu"; //$NON-NLS-1$

	/**
	 * Pop-up menu: id of the import group of the source sub menu (value
	 * <code>importGroup</code>).
	 *
	 * @since 3.0
	 */
	public static final String GROUP_IMPORT= "importGroup";  //$NON-NLS-1$

	/**
	 * Pop-up menu: id of the generate group of the source sub menu (value
	 * <code>generateGroup</code>).
	 *
	 * @since 3.0
	 */
	public static final String GROUP_GENERATE= "generateGroup";  //$NON-NLS-1$

	/**
	 * Pop-up menu: id of the code group of the source sub menu (value
	 * <code>codeGroup</code>).
	 *
	 * @since 3.0
	 */
	public static final String GROUP_CODE= "codeGroup";  //$NON-NLS-1$

	/**
	 * Pop-up menu: id of the externalize group of the source sub menu (value
	 * <code>externalizeGroup</code>).
	 *
	 * TODO: Make API
	 */
	private static final String GROUP_EXTERNALIZE= "externalizeGroup"; //$NON-NLS-1$

	/**
	 * Pop-up menu: id of the comment group of the source sub menu (value
	 * <code>commentGroup</code>).
	 *
	 * TODO: Make API
	 */
	private static final String GROUP_COMMENT= "commentGroup"; //$NON-NLS-1$

	/**
	 * Pop-up menu: id of the edit group of the source sub menu (value
	 * <code>editGroup</code>).
	 *
	 * TODO: Make API
	 */
	private static final String GROUP_EDIT= "editGroup"; //$NON-NLS-1$

	private UniversalEditor fEditor;
	private String fGroupName= IWorkbenchActionConstants.GROUP_REORGANIZE;

	private static final String QUICK_MENU_ID= "io.usethesource.impulse.source.quickMenu"; //$NON-NLS-1$

	private IHandlerActivation fQuickAccessHandlerActivation;
	private IHandlerService fHandlerService;

	/**
	 * Note: This constructor is for internal use only. Clients should not call this constructor.
	 * @param editor the compilation unit editor
	 * @param groupName the group name to add the action to
	 *
	 * @noreference This constructor is not intended to be referenced by clients.
	 */
	public GenerateActionGroup(UniversalEditor editor, String groupName) {
		fEditor= editor;
		fGroupName= groupName;
	}

	/*
	 * The state of the editor owning this action group has changed.
	 * This method does nothing if the group's owner isn't an
	 * editor.
	 */
	/**
	 * Note: This method is for internal use only. Clients should not call this method.
	 * 
	 * @noreference This method is not intended to be referenced by clients.
	 * @deprecated As of 3.5, this method is no longer called
	 */
	public void editorStateChanged() {
		Assert.isTrue(isEditorOwner());
	}

	public void fillActionBars(IActionBars actionBar) {
		// TODO: review this and see if we can add something
		super.fillActionBars(actionBar);
	}

	/* (non-Javadoc)
	 * Method declared in ActionGroup
	 */
	public void fillContextMenu(IMenuManager menu) {
		super.fillContextMenu(menu);
		MenuManager subMenu= new MenuManager("Source", MENU_ID);
		subMenu.setActionDefinitionId(QUICK_MENU_ID);
		int added= 0;
		if (isEditorOwner()) {
			added= fillEditorSubMenu(subMenu);
		} else {
			added= fillViewSubMenu(subMenu);
		}
		if (added > 0)
			menu.appendToGroup(fGroupName, subMenu);
	}

//	private void fillQuickMenu(IMenuManager menu) {
//		if (isEditorOwner()) {
//			if (!fEditor.isBreadcrumbActive())
//				fillEditorSubMenu(menu);
//		} else {
//			fillViewSubMenu(menu);
//		}
//	}

	private int fillEditorSubMenu(IMenuManager source) {
		int added= 0;
		source.add(new Separator(GROUP_COMMENT));
		added+= addEditorAction(source, IEditorActionDefinitionIds.TOGGLE_COMMENT); //$NON-NLS-1$
		added+= addEditorAction(source, "AddBlockComment"); //$NON-NLS-1$
		added+= addEditorAction(source, "RemoveBlockComment"); //$NON-NLS-1$
//		added+= addAction(source, fAddJavaDocStub);
		source.add(new Separator(GROUP_EDIT));
		added+= addEditorAction(source, IEditorActionDefinitionIds.CORRECT_INDENTATION); //$NON-NLS-1$
		added+= addEditorAction(source, IEditorActionDefinitionIds.FORMAT /*"Format"*/); //$NON-NLS-1$
		source.add(new Separator(GROUP_IMPORT));
//		added+= addAction(source, fAddImport);
//		added+= addAction(source, fOrganizeImports);
//		added+= addAction(source, fSortMembers);
//		added+= addAction(source, fCleanUp);
		source.add(new Separator(GROUP_GENERATE));
//		added+= addAction(source, fOverrideMethods);
//		added+= addAction(source, fAddGetterSetter);
//		added+= addAction(source, fAddDelegateMethods);
//		added+= addAction(source, fHashCodeEquals);
//		added+= addAction(source, fToString);
//		added+= addAction(source, fGenerateConstructorUsingFields);
//		added+= addAction(source, fAddUnimplementedConstructors);
		source.add(new Separator(GROUP_CODE));
		source.add(new Separator(GROUP_EXTERNALIZE));
//		added+= addAction(source, fExternalizeStrings);
		return added;
	}

	private int fillViewSubMenu(IMenuManager source) {
		int added= 0;
		source.add(new Separator(GROUP_COMMENT));
//		added+= addAction(source, fAddJavaDocStub);
		source.add(new Separator(GROUP_EDIT));
//		added+= addAction(source, fFormatAll);
		source.add(new Separator(GROUP_IMPORT));
//		added+= addAction(source, fAddImport);
//		added+= addAction(source, fOrganizeImports);
//		added+= addAction(source, fSortMembers);
//		added+= addAction(source, fCleanUp);
		source.add(new Separator(GROUP_GENERATE));
//		added+= addAction(source, fOverrideMethods);
//		added+= addAction(source, fAddGetterSetter);
//		added+= addAction(source, fAddDelegateMethods);
//		added+= addAction(source, fHashCodeEquals);
//		added+= addAction(source, fToString);
//		added+= addAction(source, fGenerateConstructorUsingFields);
//		added+= addAction(source, fAddUnimplementedConstructors);
		source.add(new Separator(GROUP_CODE));
		source.add(new Separator(GROUP_EXTERNALIZE));
//		added+= addAction(source, fExternalizeStrings);
//		added+= addAction(source, fFindNLSProblems);
		return added;
	}

	/* (non-Javadoc)
	 * Method declared in ActionGroup
	 */
	public void dispose() {
		if (fQuickAccessHandlerActivation != null && fHandlerService != null) {
			fHandlerService.deactivateHandler(fQuickAccessHandlerActivation);
		}
		fEditor= null;
		super.dispose();
	}

	private int addEditorAction(IMenuManager menu, String actionID) {
		if (fEditor == null)
			return 0;
		IAction action= fEditor.getAction(actionID);
		if (action == null)
			return 0;
		if (action instanceof IUpdate)
			((IUpdate)action).update();
		if (action.isEnabled()) {
			menu.add(action);
			return 1;
		}
		return 0;
	}

	private boolean isEditorOwner() {
		return fEditor != null;
	}
}

