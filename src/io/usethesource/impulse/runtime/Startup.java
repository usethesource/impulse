package io.usethesource.impulse.runtime;

import org.eclipse.ui.IStartup;

import io.usethesource.impulse.language.LanguageRegistry;

public class Startup implements IStartup {

	@Override
	public void earlyStartup() {
		LanguageRegistry.startup();
	}
}
