package io.usethesource.impulse.services;

import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;

import io.usethesource.impulse.model.ICompilationUnit;

public interface IQuickFixInvocationContext extends IQuickAssistInvocationContext{
	public ICompilationUnit getModel();
}
