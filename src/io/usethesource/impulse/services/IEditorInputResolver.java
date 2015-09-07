package io.usethesource.impulse.services;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorInput;

import io.usethesource.impulse.language.ILanguageService;

public interface IEditorInputResolver extends ILanguageService {
	public IPath getPath(IEditorInput editorInput);

	public IFile getFile(IEditorInput editorInput);

	public String getNameExtension(IEditorInput editorInput);
}
