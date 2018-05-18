/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excelExport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import ldhproject.Main;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author macan
 */
public class excelExportHandler {
    
    private DefaultTableModel table_model;
    
    public excelExportHandler(DefaultTableModel table_model){
        this.table_model = table_model;
    }
    
    private String getCellValue(int x, int y) {
        return table_model.getValueAt(y, x) != null ? table_model.getValueAt(y, x).toString() : "";
    }

    //write files to excel
    public void writeToExcel() {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet ws = wb.createSheet();

        //load data to treemap
        int count = 1;
        TreeMap<String, Object[]> data = new TreeMap<>();
        data.put("0", new Object[]{table_model.getColumnName(0), table_model.getColumnName(1), table_model.getColumnName(2), table_model.getColumnName(3), table_model.getColumnName(4), "", ""});
        Object[] newo = new Object[7];
        int rowID = 0;

        for (int i = 0; i < (table_model.getRowCount()); i++) {

            //  data.put("0"+i, new Object[]{table_model.getColumnName(i)});
            for (int j = 0; j < (table_model.getColumnCount()); j++) {
                newo[j] = getCellValue(j, i);
            }
            data.put(count + "", newo);
            count++;
            //write to sheet
            Set<String> ids = data.keySet();
            XSSFRow row;

            for (String key : ids) {
                row = ws.createRow(rowID++);
                Object[] values = data.get(key);
                int cellID = 0;
                for (Object o : values) {
                    //System.out.println(o);
                    Cell cell = row.createCell(cellID++);
                    cell.setCellValue(o + "");
                }
            }
            data = new TreeMap<>();
        }
        //write excell to file systeem
        try {
            String workingDir = System.getProperty("user.dir") + "/excel/";
            //FileOutputStream outputstream = new FileOutputStream(new File("D:/newExcelfile.xlsx"));
            FileOutputStream outputstream = new FileOutputStream(new File(workingDir + "/newExcelfile.xlsx"));
            wb.write(outputstream);
            outputstream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
