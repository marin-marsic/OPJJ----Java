package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Stack;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantDouble;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantInteger;
import hr.fer.zemris.java.custom.scripting.tokens.TokenFunction;
import hr.fer.zemris.java.custom.scripting.tokens.TokenOperator;
import hr.fer.zemris.java.custom.scripting.tokens.TokenString;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Method whose job is to actually execute the document whose parsed tree it
 * obtains.
 * 
 * @author Marin Maršić
 *
 */
public class SmartScriptEngine {

	/**
	 * Documents root node.
	 */
	private DocumentNode documentNode;
	/**
	 * {@link RequestContext} object of a client being served.
	 */
	private RequestContext requestContext;
	/**
	 * {@link ObjectMultistack} needed for storing variables of for-loops.
	 */
	private ObjectMultistack multistack = new ObjectMultistack();

	/**
	 * Implementation of a visitor going through all the nodes of a document and
	 * doing some work on them.
	 */
	private INodeVisitor visitor = new INodeVisitor() {

		@Override
		public void visitTextNode(TextNode node) {
			if (node.getText().trim().isEmpty()) {
				return;
			}
			try {
				requestContext.write(node.getText());
			} catch (IOException e) {
			}
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			int start = ((TokenConstantInteger) node.getStartExpression())
					.getValue();
			int end = ((TokenConstantInteger) node.getEndExpression())
					.getValue();
			int step = ((TokenConstantInteger) node.getStepExpression())
					.getValue();
			String variable = node.getVariable().getName();

			multistack.push(variable, new ValueWrapper(Integer.valueOf(start)));

			while ((Integer) multistack.peek(variable).getValue() <= end) {
				for (int i = 0; i < node.numberOfChildren(); i++) {
					node.getChild(i).accept(this);
				}
				start = (Integer) multistack.pop(variable).getValue();
				start += step;
				multistack.push(variable,
						new ValueWrapper(Integer.valueOf(start)));
			}

			multistack.pop(variable);
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Stack<Object> stack = new Stack<>();

			for (int i = 0; i < node.numberOfTokens(); i++) {
				Token token = node.tokenAt(i);
				if (token instanceof TokenConstantDouble) {
					stack.push(((TokenConstantDouble) token).getValue());
				} else if (token instanceof TokenConstantInteger) {
					stack.push(((TokenConstantInteger) token).getValue());
				} else if (token instanceof TokenString) {
					stack.push(((TokenString) token).getValue());
				} else if (token instanceof TokenVariable) {
					String variable = ((TokenVariable) token).getName();
					ValueWrapper value = null;
					try {
						value = multistack.peek(variable);
					} catch (Exception e) {
						multistack.push(variable, new ValueWrapper(0));
						value = new ValueWrapper(0);
					}

					if (value == null) {
						throw new RuntimeException("Variable '" + variable
								+ "' not initialized.");
					}

					stack.push(value.getValue());
				} else if (token instanceof TokenOperator) {
					String operator = ((TokenOperator) token).getOperator();
					executeCommand(operator, stack);
				} else if (token instanceof TokenFunction) {
					String function = ((TokenFunction) token).getName();
					executeFunction(function, stack);
				}
			}

			ArrayList<Object> reversedStack = new ArrayList<>();
			while (!stack.isEmpty()) {
				reversedStack.add(stack.pop());
			}

			for (int i = reversedStack.size() - 1; i >= 0; i--) {
				try {
					requestContext.write(reversedStack.get(i).toString());
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
			}
		}

		/**
		 * Executes simple function. Pops all needed values from temporary
		 * stack, calculates the result, and pushes the result back to the
		 * stack. <br>
		 * <br>
		 * 
		 * Supported functions: <br>
		 * sin<br>
		 * decfmt<br>
		 * dup<br>
		 * swap<br>
		 * setMimeType<br>
		 * paramGet<br>
		 * pparamGet<br>
		 * pparamSet<br>
		 * pparamDel<br>
		 * tparamGe<br>
		 * tparamSet<br>
		 * tparamDel<br>
		 * 
		 * @param function
		 *            Determines function.
		 * @param stack
		 *            Temporary stack.
		 */
		private void executeFunction(String function, Stack<Object> stack) {
			if (function.equalsIgnoreCase("@sin")) {
				ValueWrapper a = new ValueWrapper(stack.pop());
				a.sin(1.1);
				stack.push(a.getValue());
			}

			else if (function.equalsIgnoreCase("@decfmt")) {
				Object format = stack.pop();
				Object a = stack.pop();

				if (format instanceof String) {
					DecimalFormat decimalFormat = new DecimalFormat(
							(String) format);
					a = new String(decimalFormat.format(a));
				}
				stack.push(a);
			}

			else if (function.equalsIgnoreCase("@dup")) {
				Object a = stack.pop();
				stack.push(a);
				stack.push(a);
			}

			else if (function.equalsIgnoreCase("@swap")) {
				Object a = stack.pop();
				Object b = stack.pop();
				stack.push(a);
				stack.push(b);
			}

			else if (function.equalsIgnoreCase("@setMimeType")) {
				Object a = stack.pop();
				if (a instanceof String) {
					requestContext.setMimeType((String) a);
				}
			}

			else if (function.equalsIgnoreCase("@paramGet")) {
				Object defValue = stack.pop();
				Object name = stack.pop();

				if (name instanceof String) {
					String value = requestContext.getParameter((String) name);
					stack.push(value == null ? defValue : value);
				}
			}

			else if (function.equalsIgnoreCase("@pparamGet")) {
				try {
					Object defValue = stack.pop();
					Object name = stack.pop();

					if (name instanceof String) {
						String value = requestContext
								.getPersistentParameter((String) name);
						stack.push(value == null ? defValue : value);
					}
				} catch (Exception e) {
				}
			}

			else if (function.equalsIgnoreCase("@tparamGet")) {
				Object defValue = stack.pop();
				Object name = stack.pop();

				if (name instanceof String) {
					String value = requestContext
							.getTemporaryParameter((String) name);
					stack.push(value == null ? defValue : value);
				}
			}

			else if (function.equalsIgnoreCase("@pparamSet")) {
				Object name = stack.pop();
				Object value = stack.pop();

				if (name instanceof String) {
					requestContext.setPersistentParameter((String) name,
							value.toString());
				}
			}

			else if (function.equalsIgnoreCase("@tparamSet")) {
				Object name = stack.pop();
				Object value = stack.pop();

				if (name instanceof String) {
					requestContext.setTemporaryParameter((String) name,
							value.toString());
				}
			}

			else if (function.equalsIgnoreCase("@pparamDel")) {
				Object name = stack.pop();

				if (name instanceof String) {
					requestContext.removePersistentParameter((String) name);
				}
			}

			else if (function.equalsIgnoreCase("@tparamDel")) {
				Object name = stack.pop();

				if (name instanceof String) {
					requestContext.removeTemporaryParameter((String) name);
				}
			}
		}

		/**
		 * Executes simple operator operation. Pops two needed values from
		 * temporary stack, calculates the result, and pushes the result back to
		 * the stack.
		 * 
		 * @param operator
		 *            Determines operation (+,-,*,/).
		 * @param stack
		 *            Temporary stack.
		 */
		private void executeCommand(String operator, Stack<Object> stack) {
			ValueWrapper b = new ValueWrapper(stack.pop());
			ValueWrapper a = new ValueWrapper(stack.pop());

			if (operator.equals("+")) {
				a.increment(b.getValue());
			} else if (operator.equals("-")) {
				a.decrement(b.getValue());
			} else if (operator.equals("*")) {
				a.multiply(b.getValue());
			} else if (operator.equals("/")) {
				a.divide(b.getValue());
			}

			stack.push(a.getValue());
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0; i < node.numberOfChildren(); i++) {
				node.getChild(i).accept(this);
			}
		}
	};

	/**
	 * {@link SmartScriptEngine} constructor.
	 * 
	 * @param documentNode
	 *            Root node of a document.
	 * @param requestContext
	 *            {@link RequestContext} object of a client being served.
	 */
	public SmartScriptEngine(DocumentNode documentNode,
			RequestContext requestContext) {
		this.requestContext = requestContext;
		this.documentNode = documentNode;
	}

	/**
	 * Starts executing the script.
	 */
	public void execute() {
		documentNode.accept(visitor);
	}
}
