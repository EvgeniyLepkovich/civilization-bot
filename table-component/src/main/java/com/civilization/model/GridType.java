package com.civilization.model;

import de.vandermeer.asciithemes.TA_Grid;
import de.vandermeer.asciithemes.a7.A7_Grids;
import de.vandermeer.asciithemes.a8.A8_Grids;
import de.vandermeer.asciithemes.u8.U8_Grids;

public enum GridType {

    MINUS_BAR_PLUS_EQUALS {
        @Override
        public TA_Grid getGridType() {
            return A7_Grids.minusBarPlusEquals();
        }
    },
    LINE_DOUBLE_BLOCKS {
        @Override
        public TA_Grid getGridType() {
            return A8_Grids.lineDoubleBlocks();
        }
    },
    BORDER_DOUBLE_LIGHT {
        @Override
        public TA_Grid getGridType() {
            return U8_Grids.borderDoubleLight();
        }
    },
    BORDER_DOUBLE {
        @Override
        public TA_Grid getGridType() {
            return U8_Grids.borderDouble();
        }
    };

    public abstract TA_Grid getGridType();
}
