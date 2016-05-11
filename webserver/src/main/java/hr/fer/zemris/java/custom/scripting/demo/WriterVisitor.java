package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

/**
 * Concrete {@link INodeVisitor} which prints all the nodes contained by
 * document node.
 * 
 * @author Marin Maršić
 *
 */
public class WriterVisitor implements INodeVisitor {

	@Override
	public void visitTextNode(TextNode node) {
		System.out.printf("%s", node);
	}

	@Override
	public void visitForLoopNode(ForLoopNode node) {
		System.out.printf("%s", node);
		for (int i = 0; i < node.numberOfChildren(); i++) {
			node.getChild(i).accept(this);
		}
		System.out.printf("{$ END $}", node);
	}

	@Override
	public void visitEchoNode(EchoNode node) {
		System.out.printf("%s", node);
	}

	@Override
	public void visitDocumentNode(DocumentNode node) {
		for (int i = 0; i < node.numberOfChildren(); i++) {
			node.getChild(i).accept(this);
		}
	}

}
