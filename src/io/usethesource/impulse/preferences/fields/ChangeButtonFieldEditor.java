package io.usethesource.impulse.preferences.fields;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import io.usethesource.impulse.preferences.IPreferencesService;
import io.usethesource.impulse.preferences.PreferencesTab;

public abstract class ChangeButtonFieldEditor extends FieldEditor {

    public ChangeButtonFieldEditor() { }

    public ChangeButtonFieldEditor(PreferencePage page, PreferencesTab tab, IPreferencesService service, String level) {
        super(page, tab, service, level);
    }

    public ChangeButtonFieldEditor(PreferencePage page, PreferencesTab tab, IPreferencesService service, String level,
            String name, String labelText, Composite parent) {
        super(page, tab, service, level, name, labelText, parent);
    }

    public abstract Button getChangeControl();
}
