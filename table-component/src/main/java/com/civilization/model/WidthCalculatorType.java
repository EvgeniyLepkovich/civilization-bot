package com.civilization.model;

import com.civilization.custom.CWC_LongestLineWithHeaderAndFooter;
import de.vandermeer.asciitable.*;

public enum WidthCalculatorType {
    CWC_ABSOLUTE_EVEN {
        @Override
        public AT_ColumnWidthCalculator getCalculator() {
            return new CWC_AbsoluteEven();
        }
    },
    CWC_LONGEST_WORLD {
        @Override
        public AT_ColumnWidthCalculator getCalculator() {
            return new CWC_LongestWord();
        }
    },
    CWC_FIXED_WIDTH {
        @Override
        public AT_ColumnWidthCalculator getCalculator() {
            return new CWC_FixedWidth();
        }
    },
    CWC_LONGEST_LINE {
        @Override
        public AT_ColumnWidthCalculator getCalculator() {
            return new CWC_LongestLine();
        }
    },
    CWC_LONGEST_LINE_WITH_HEADER_AND_FOOTER {
        @Override
        public AT_ColumnWidthCalculator getCalculator() {
            return new CWC_LongestLineWithHeaderAndFooter();
        }
    };

    public abstract AT_ColumnWidthCalculator getCalculator();
}
