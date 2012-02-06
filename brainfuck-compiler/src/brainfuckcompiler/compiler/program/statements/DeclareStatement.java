package brainfuckcompiler.compiler.program.statements;

import brainfuckcompiler.compiler.expressions.ExpressionGenerator;
import brainfuckcompiler.compiler.expressions.Node;
import brainfuckcompiler.compiler.expressions.nodes.AssignmentOperator;
import brainfuckcompiler.compiler.program.Variable;
import brainfuckcompiler.compiler.program.structure.Block;
import brainfuckcompiler.compiler.program.structure.Item;
import brainfuckcompiler.compiler.program.structure.Line;
import brainfuckcompiler.statics;
import java.util.ArrayList;

public class DeclareStatement extends Statement
{

    Node expression = null;
    String variableName;

    public DeclareStatement(Block parentBlock, int lineNumber)
    {
        super(parentBlock, lineNumber);
    }

    public int parseStatement(ArrayList<Item> items, int currentPosition)
    {
        Line l = (Line) items.get(currentPosition);
        currentPosition++;
        String[] parts = l.getLine().substring(8).split(",");
        if (parts.length == 0)
        {
            System.out.println("Incomplete declare statement at line " + l.getLineNumber());
            System.exit(0);
        }
        if (parts.length > 2)
        {
            System.out.println("Invalid number of arguments in declare statement at line " + l.getLineNumber());
        }
        if (!parts[0].trim().matches("([_a-zA-Z][_0-9a-zA-Z]*)"))
        {
            System.out.println("Invalid variable name at line " + l.getLineNumber());
            System.exit(0);
        }
        variableName = parts[0].trim();
        if (parts.length == 1)
        {
            return currentPosition;
        }
        expression = ExpressionGenerator.generateExpression(parts[1], l.getLineNumber(), parentBlock);
        if (expression instanceof AssignmentOperator)
        {
            System.out.println("Initializer on line " + l.getLineNumber() + " cannot use an assignment operator");
            System.exit(0);
        }
        return currentPosition;
    }

    @Override
    public void generate()
    {
        if (getVariable() == null)
        {
            Variable v = new Variable(variableName);
            parentBlock.getVariableScope().add(v);
            if (expression != null)
            {
                int address = expression.generateBF();
                statics.t.move(v.getMemoryPosition(), address);
                statics.t.free(address);
            }
        } else
        {
            System.out.println("Double declaration of variable " + variableName + " at line " + lineNumber);
            System.exit(0);
        }
    }

    private Variable getVariable()
    {
        ArrayList<Variable> variables = parentBlock.getVariableScope();
        for (Variable v : variables)
        {
            if (v.getName().equals(variableName))
            {
                return v;
            }
        }
        return null;
    }
}
