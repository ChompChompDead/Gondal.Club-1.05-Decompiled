package org.spongepowered.asm.lib.tree;

import org.spongepowered.asm.lib.*;
import java.util.*;

public class TypeInsnNode extends AbstractInsnNode
{
    public String desc;
    
    public TypeInsnNode(final int opcode, final String desc) {
        super(opcode);
        this.desc = desc;
    }
    
    public void setOpcode(final int opcode) {
        this.opcode = opcode;
    }
    
    public int getType() {
        return 3;
    }
    
    public void accept(final MethodVisitor mv) {
        mv.visitTypeInsn(this.opcode, this.desc);
        this.acceptAnnotations(mv);
    }
    
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> labels) {
        return new TypeInsnNode(this.opcode, this.desc).cloneAnnotations(this);
    }
}
