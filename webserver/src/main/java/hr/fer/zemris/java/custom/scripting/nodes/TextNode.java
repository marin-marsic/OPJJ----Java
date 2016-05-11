package hr.fer.zemris.java.custom.scripting.nodes;

public class TextNode extends Node{

	/**
	 * Text contained by TextNode.
	 */
	private String text;
	
	/**
	 * Constructor.
	 * @param text
	 */
	public TextNode(String text){
		this.text = text;
	}
	
	/**
	 * @return Returns text contained by this node.
	 */
	public String getText() {
		return text;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.scripting.nodes.Node#toString()
	 */
	@Override
	public String toString(){
		String str = text.replace("\\", "\\\\");
		str = str.replace("{", "\\{");
		return str + " ";
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitTextNode(this);
	}
}
