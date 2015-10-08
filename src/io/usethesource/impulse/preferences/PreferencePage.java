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

package io.usethesource.impulse.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import io.usethesource.impulse.runtime.RuntimePlugin;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
    public PreferencePage() {
        super(GRID);
        setPreferenceStore(RuntimePlugin.getInstance().getPreferenceStore());
        setDescription("Preferences for the Impulse framework");
    }

    public void createFieldEditors() {
        final BooleanFieldEditor emitMessagesField= new BooleanFieldEditor(PreferenceConstants.P_EMIT_MESSAGES, "E&mit diagnostic messages from IMP UI",
                getFieldEditorParent());
        addField(emitMessagesField);

        final BooleanFieldEditor dumpTokensField= new BooleanFieldEditor(PreferenceConstants.P_DUMP_TOKENS, "&Dump tokens after scanning",
                getFieldEditorParent());
        addField(dumpTokensField);

        final BooleanFieldEditor emitBuilderDiagnosticsField= new BooleanFieldEditor(PreferenceConstants.P_EMIT_BUILDER_DIAGNOSTICS, "Emit diagnostic messages while building",
                getFieldEditorParent());
        addField(emitBuilderDiagnosticsField);

        // Don't need a preference store listener here; the UniversalEditor already listens
        // to the preference store, and takes the necessary actions. Moreover, some preference
        // changes require some resource management that only it can do properly (e.g. it must
        // only dispose of the old Font *after* telling the ITextViewer to use the new one).
    }

    public void init(IWorkbench workbench) {
    }
}
