
package com.isa.othello.faces.board;

import java.util.Arrays;
import java.util.Collection;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 * 8X8 Game board setup.
 * @author Manish
 */
@ManagedBean
@ApplicationScoped
public class GameBoardDimensions {
    private final Collection<String> rows = Arrays.asList(new String[]{"a", "b", "c", "d", "e", "f", "g", "h"});
    private final Collection<String> columns = Arrays.asList(new String[]{"1", "2", "3", "4", "5", "6", "7", "8"});

    public Collection<String> getColumns() {
        return columns;
    }

    public Collection<String> getRows() {
        return rows;
    }
    
    
}
