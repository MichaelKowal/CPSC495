/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.library;

import java.util.ArrayList;
import java.util.Map;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author behnish
 */
public class ConfigTableModel extends AbstractTableModel {

    private String[] columnNames = {"Simulation Run ID", "Mutual Exclusion Algorithm", "Workload"};
    private Map<Integer, SimulationRun> data;

    public ConfigTableModel(Map<Integer, SimulationRun> data) {
        this.data = data;
    }

    public void setData(Map<Integer, SimulationRun> data) {
        this.data = data;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {

        SimulationRun entry = (SimulationRun) data.get(row);                
        if (col == 0) {
            return entry.getSimulationRunid();
        } else if(col==1){
            return entry.getAlgorithm();
        }else{
            return entry.getWorkload();
        }
    }
    
}
