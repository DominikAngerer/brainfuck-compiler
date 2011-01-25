package brainfuckcompiler.expressions.nodetypes;

import brainfuckcompiler.expressions.Node;
import brainfuckcompiler.expressions.operators.Operators;

/**
 *
 * @author vrighter
 */
public abstract class BinaryOperator extends Node
{

    /**
     *
     */
    /**
     *
     */
    public Node left = null, right = null;

    /**
     *
     * @param tokens
     * @param index
     * @return
     */
    public int populate(String[] tokens, int index)
    {
        right = Operators.createNode(tokens[index--]);
        index = right.populate(tokens, index);
        left = Operators.createNode(tokens[index--]);
        index = left.populate(tokens, index);
        return index;
    }
}
