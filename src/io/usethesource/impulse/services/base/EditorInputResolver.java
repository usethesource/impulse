package io.usethesource.impulse.services.base;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorInput;

import io.usethesource.impulse.editor.EditorInputUtils;
import io.usethesource.impulse.services.IEditorInputResolver;

/**
 * Default implementation of the IEditorInputResolver service interface
 * @author awtaylor
 * Added per patch attached to bug #322035
 */
public class EditorInputResolver implements IEditorInputResolver {
	public IFile getFile(IEditorInput editorInput) {
		return EditorInputUtils.getFile(editorInput);
	}

	public String getNameExtension(IEditorInput editorInput) {
		return EditorInputUtils.getNameExtension(editorInput);
	}

	public IPath getPath(IEditorInput editorInput) {
		return EditorInputUtils.getPath(editorInput);
	}
}
