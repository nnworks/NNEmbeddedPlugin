package nl.nnworks.nnembedded.project.nature;

public class NaturePropertyTester extends org.eclipse.core.expressions.PropertyTester {

	public NaturePropertyTester() {
        System.out.println("hola created");			
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
	  if ("hasNNEmbeddedNature".equals(property)) {
        System.out.println("hola");			
	  }
	  return true;
	}

}
