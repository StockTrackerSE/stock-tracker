package view;

//package components;

/*
* SimpleTableDemo.java requires no other files.
*/

import javax.print.DocFlavor.STRING;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controller.AlertSettingsController;
import controller.HomeController;
import controller.IController;
import controller.TopStockController;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
// Added Top Stock View
public class TopStockView implements IView {
  private boolean DEBUG = false;
  TopStockController tsCont = null;

  public TopStockView(TopStockController tsc){
	  
	  this.tsCont = tsc;
	  frame.getContentPane().removeAll();
	  frame.setLayout(new GridLayout(1,0));
      
      /*Stock stock = YahooFinance.get("AAPL");
		String stockname = stock.getName();
		BigDecimal stockprice = stock.getQuote().getPrice();
*/
      
      String stockSymbols[] = tsCont.getTopStocks();


      Object[][] data = new Object[30][30];
      
      try {
			Map<String, Stock> stocks = YahooFinance.get(stockSymbols);
			Iterator iterator = stocks.entrySet().iterator();
			int row = 0;
			int col = 0;
			utility.YahooClient yahooClient = new utility.YahooClient();
			while (iterator.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry pair = (Map.Entry)iterator.next();
				Stock stock = (Stock) pair.getValue();
				Object[] temp = {stock.getName(), stock.getSymbol(), stock.getQuote().getPrice(), yahooClient.getNetGainFromYesterday(stock), yahooClient.getURL(stock.getSymbol()), stock.getSymbol()};
				for (int i = 0; i < 5; i ++) {
					data[row][i] = temp[i]; 
				}
				row ++;
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
      String[] columnNames = {"Company",
    		  				  "Ticker",
                              "Price ",
                              "Net Gain",
                              "Chart/Link",
                              "Buy",
                              "Alert"};
      
      // How to sort a column 
      final JTable table = new JTable(data, columnNames);
      table.setPreferredScrollableViewportSize(new Dimension(800, 481));
      table.setFillsViewportHeight(true);
      Action buyButtons = new AbstractAction()
      {
          public void actionPerformed(ActionEvent e)
          {
              JTable table = (JTable)e.getSource();
              int modelRow = Integer.valueOf( e.getActionCommand() );
              
              BuyStockView buyStock = new BuyStockView();
              BigDecimal temp = (BigDecimal) table.getModel().getValueAt(modelRow, 2);
              
              buyStock.buyStock(tsCont.user, table.getModel().getValueAt(modelRow, 1).toString(), temp.doubleValue());
          }
      };
       
      ButtonColumn buttonColumn = new ButtonColumn(table, buyButtons, 5);
      
      
      Action alertButtons = new AbstractAction()
      {
          public void actionPerformed(ActionEvent e)
          {
              JTable table = (JTable)e.getSource();
              int modelRow = Integer.valueOf( e.getActionCommand() );
              
              AlertSettingsController asCont = new AlertSettingsController();
              asCont.switchContext(false, (String) table.getValueAt(modelRow, 1));
//              BigDecimal temp = (BigDecimal) table.getModel().getValueAt(modelRow, 2);
              
//              buyStock.buyStock(tsCont.user, table.getModel().getValueAt(modelRow, 1).toString(), temp.doubleValue());
          }
      };
       
      ButtonColumn alertColumn = new ButtonColumn(table, alertButtons, 6);


      if (DEBUG) {
          table.addMouseListener(new MouseAdapter() {
              public void mouseClicked(MouseEvent e) {
                  printDebugData(table);
              }
          });
      }

      //Create the scroll pane and add the table to it.
      JScrollPane scrollPane = new JScrollPane(table);
      
      JPanel panel = new JPanel();
      panel.setLayout(new GridBagLayout());
      GridBagConstraints c = new GridBagConstraints();
      
      c.fill = GridBagConstraints.VERTICAL;
      
      c.gridx = 0;
      c.gridy = 0;
      c.gridheight = 1;
      panel.add(this.backButton(), c);
      
      
      c.gridx = 1;
      c.gridy = 0;
      c.gridheight = 5;
      panel.add(scrollPane, c);

      //Add the scroll pane to this panel.
      frame.add(panel);
      frame.pack();
      frame.setVisible(true);
  }



private void printDebugData(JTable table) {
      int numRows = table.getRowCount();
      int numCols = table.getColumnCount();
      javax.swing.table.TableModel model = table.getModel();

      System.out.println("Value of data: ");
      for (int i=0; i < numRows; i++) {
          System.out.print("    row " + i + ":");
          for (int j=0; j < numCols; j++) {
              System.out.print("  " + model.getValueAt(i, j));
          }
          System.out.println();
      }
      System.out.println("--------------------------");
  }

  /**
   * Create the GUI and show it.  For thread safety,
   * this method should be invoked from the
   * event-dispatching thread.
   */
//  public void createAndShowGUI() {
//      //Create and set up the window.
//      JFrame frame = new JFrame("Top 30 Stock");
//      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//      //Create and set up the content pane.
//      TopStockView newContentPane = new TopStockView();
//      newContentPane.setOpaque(true); //content panes must be opaque
//      frame.setContentPane(newContentPane);
//
//      //Display the window.
//      frame.pack();
//      frame.setVisible(true);
//  }

  @Override
  public IController getController() {
	  // TODO Auto-generated method stub
	  return null;
  }

  @Override
  public void setController(IController some) {
	  // TODO Auto-generated method stub
	
  }

  public JButton backButton()
  {
	  JButton button = new JButton();
	  button.setText("go back");
	  button.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			tsCont.goBackToHome();
			
		}
	});
	  return button;
  }
}

