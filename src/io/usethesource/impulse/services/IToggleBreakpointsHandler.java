package io.usethesource.impulse.services;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import io.usethesource.impulse.language.ILanguageService;

public interface IToggleBreakpointsHandler extends ILanguageService {
	void clearLineBreakpoint(IFile file, int lineNumber) throws CoreException;
	void setLineBreakpoint(IFile file, int lineNumber) throws CoreException;
	void disableLineBreakpoint(IFile file, int lineNumber) throws CoreException;
	void enableLineBreakpoint(IFile file, int lineNumber) throws CoreException;

	// TODO
	//void toggleEntryBreakpoint(IFile file, int lineNumber);
}
