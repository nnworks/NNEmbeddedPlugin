package nl.nnworks.nnembedded.project.nature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class NNEmbeddedProjectNature implements IProjectNature {

    public static final String NATURE_ID = "nl.nnworks.nnembedded.project.nature";
    public static final String CNATURE_ID = "org.eclipse.cdt.core.cnature";
    public static final String CCNATURE_ID = "org.eclipse.cdt.core.ccnature";
	
    private IProject project;    
    
	@Override
	public void configure() throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deconfigure() throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public IProject getProject() {
		return project;
	}

	@Override
	public void setProject(IProject project) {
		this.project = project;
	}

}
