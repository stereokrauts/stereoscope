package com.stereokrauts.lib.midi.macos.jna.util;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public class CFRange extends Structure {

    public static class ByReference extends CFRange implements com.sun.jna.Structure.ByReference {
        // / Allocate a new Pair.ByRef struct on the heap
        public ByReference() {
        }

        // / Create an instance that shares its memory with another Pair
        // instance
        public ByReference(CFRange struct) {
            super(struct.getPointer(), 0);
        }
    }

    public static class ByValue extends CFRange implements Structure.ByValue {
        public ByValue() {
        }

        // / Create an instance that shares its memory with another Pair
        // instance
        public ByValue(CFRange struct) {
            super(struct.getPointer(), 0);
        }
    }

    public CFRange() {
    }

    // Cast data at given memory location (pointer + offset) as an existing
    // Pair struct
    public CFRange(com.sun.jna.Pointer pointer, int offset) {
        super();
        useMemory(pointer, offset);
        read();
    }

    @Override
    protected List<?> getFieldOrder() {
        return Arrays.asList(new String[] { "loc", "len" });
    }

    public long loc;
    public long len;
}