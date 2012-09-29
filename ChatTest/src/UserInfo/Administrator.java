package UserInfo;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import Client.ClientLoginFrame;

public class Administrator extends AbstractTableModel {
	Font font = new Font("Verdana", Font.PLAIN, 12);
	ImageIcon iconChat = new ImageIcon("./image/p.png");
	
	    private static final long serialVersionUID = 1L;
	    private ArrayList<String> columnNames = new ArrayList<String>();
		private ArrayList<Class> columnTypes = new ArrayList<Class>();
	    private ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
	    
	    public int getRowCount() {
	        synchronized (data) {
	            return data.size();
	        }
	    }

	    public int getColumnCount() {
	        return columnNames.size();
	    }

	    public Object getValueAt(int row, int col) {
	        synchronized (data) {
	            return data.get(row).get(col);
	        }
	    }

	    public String getColumnName(int col) {
	        return columnNames.get(col);
	    }

	    public Class getColumnClass(int col) {
	        return columnTypes.get(col);
	    }

	    public boolean isCellEditable(int row, int col) {
	        return true;
	    }

	    public void setValueAt(Object obj, int row, int col) {
	        synchronized (data) {
	            data.get(row).set(col, obj);
	        }
	    }

	    /**
	     * Core of the model. Initializes column names, types, data from ResultSet.
	     *
	     * @param rs ResultSet from which all information for model is token.
	     * @throws SQLException
	     * @throws ClassNotFoundException
	     */
	    public void setDataSource(ResultSet rs) throws SQLException, ClassNotFoundException {
	        ResultSetMetaData rsmd = rs.getMetaData();
	        columnNames.clear();
	        columnTypes.clear();
	        data.clear();

	        int columnCount = rsmd.getColumnCount();
	        columnCount += 1;
	        
	        for (int i = 0; i < columnCount - 1; i++) {
	            columnNames.add(rsmd.getColumnName(i + 1));
	            Class type = Class.forName(rsmd.getColumnClassName(i + 1));
	            columnTypes.add(type);
	        }
	        fireTableStructureChanged();
	        while (rs.next()) {
	            ArrayList rowData = new ArrayList();
	            for (int i = 0; i < columnCount - 1; i++) {
	                if (columnTypes.get(i) == String.class)
	                    rowData.add(rs.getString(i + 1));
	                else
	                    rowData.add(rs.getObject(i + 1));
	            }
	            synchronized (data) {
	                data.add(rowData);
	                this.fireTableRowsInserted(data.size() - 1, data.size() - 1);
	            }
	        }
	    }
	    
	    public void adminFrame (Administrator model, ResultSet rs) throws ClassNotFoundException, SQLException{
	    	int height = 150;
	       	int width = 500;
	       	
	       	model.setDataSource(rs);
            JTable table = new JTable(model);
           
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(new JScrollPane(table), BorderLayout.CENTER);

            JFrame frame = new JFrame("Interface for administrator");
	    	frame.setResizable(true);
	    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    	frame.setBounds(((screenSize.width - width)/2), ((screenSize.height - height)/2), width, height);
	    	frame.setIconImage(iconChat.getImage());
            frame.setContentPane(panel);
            frame.setVisible(true);
	    }
	    public static void main(String[] args) {
	    	ClientLoginFrame clf = new ClientLoginFrame("admin");
	    	clf.setVisible(true);
	    }
	}
