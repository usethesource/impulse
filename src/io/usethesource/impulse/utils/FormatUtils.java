package io.usethesource.impulse.utils;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.formatter.ContentFormatter;

import io.usethesource.impulse.editor.LanguageServiceManager;
import io.usethesource.impulse.editor.internal.FormattingController;
import io.usethesource.impulse.language.Language;
import io.usethesource.impulse.services.ISourceFormatter;

public class FormatUtils {
    private FormatUtils() { }

    public static void format(Language language, IDocument document, IRegion region) {
		LanguageServiceManager man = new LanguageServiceManager(language);
		ContentFormatter cf = new ContentFormatter();

		ISourceFormatter sf = man.getFormattingStrategy();
		if (sf == null) {
			return;
		}

		FormattingController fc = new FormattingController(sf);
		cf.setFormattingStrategy(fc, null);
		cf.format(document, region);
	}
}
