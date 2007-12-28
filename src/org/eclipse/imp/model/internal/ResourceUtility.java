package org.eclipse.imp.model.internal;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.imp.runtime.RuntimePlugin;

public class ResourceUtility {
    private ResourceUtility() { } // Not instantiatable

    /*package*/ static Set<IResource> getImmediateChildren(final IResource r) {
        final Set<IResource> result= new HashSet<IResource>();
        try {
            r.accept(new IResourceVisitor() {
                public boolean visit(IResource resource) throws CoreException {
                    if (resource == r || resource.getFullPath().equals(r.getFullPath()))
                        return true;
                    result.add(resource);
                    return false;
                }
            });
        } catch (CoreException e) {
            RuntimePlugin.getInstance().logException("Error enumerating resource children", e);
        }
        return result;
    }
}
