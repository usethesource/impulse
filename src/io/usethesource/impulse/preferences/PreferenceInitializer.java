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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.progress.UIJob;

import io.usethesource.impulse.runtime.RuntimePlugin;

/**
 * Initializes IMP framework-wide preferences to reasonable default values.
 * @author rfuhrer@watson.ibm.com
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {
    public void initializeDefaultPreferences() {
        if (Display.getCurrent() == null) {
        	// This isn't the UI thread, so schedule a job to do the initialization later.
        	UIJob job= new UIJob("Impulse Preference Initializer") {
				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					if (Display.getCurrent() != null) { // this should never be false, but let's be safe
						new PreferenceInitializer().initializeDefaultPreferences();
					}
					return Status.OK_STATUS;
				}
			};
			job.schedule(0);
			return;
        }
        IPreferenceStore store= RuntimePlugin.getInstance().getPreferenceStore();
        EditorsUI.useAnnotationsPreferencePage(store);
		EditorsUI.useQuickDiffPreferencePage(store);

        store.setDefault(PreferenceConstants.P_EMIT_MESSAGES, false);
        store.setDefault(PreferenceConstants.P_EMIT_BUILDER_DIAGNOSTICS, false);
        store.setDefault(PreferenceConstants.EDITOR_MATCHING_BRACKETS, true);
        store.setDefault(PreferenceConstants.EDITOR_CORRECTION_INDICATION, true);
        
        String colorKey= RuntimePlugin.IMP_RUNTIME + "." + PreferenceConstants.EDITOR_MATCHING_BRACKETS_COLOR;
        ColorRegistry registry= null;
        
        if (PlatformUI.isWorkbenchRunning()) {
            registry= PlatformUI.getWorkbench().getThemeManager().getCurrentTheme().getColorRegistry();
        }
        PreferenceConverter.setDefault(store, PreferenceConstants.EDITOR_MATCHING_BRACKETS_COLOR,
                    findRGB(registry, colorKey, new RGB(192, 192, 192)));
    }

    /**
     * Returns the RGB for the given key in the given color registry.
     * 
     * @param registry the color registry
     * @param key the key for the constant in the registry
     * @param defaultRGB the default RGB if no entry is found
     * @return RGB the RGB
     */
    private static RGB findRGB(ColorRegistry registry, String key, RGB defaultRGB) {
        if (registry == null)
            return defaultRGB;

        RGB rgb= registry.getRGB(key);
        if (rgb != null)
            return rgb;

        return defaultRGB;
    }
}
