package excel;

import constants.PrivateData;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExcelTest {

    //This class is used to test the containment and the index of the excelsheet.
    //The excel.ExcelManager calls this class, which returns a boolean to know the credibility!

    //TODO: Prioritize test - Existence and extension file test

    public void readExcel(String filePath, String fileName, String sheetName) throws IOException {

        //Create an object of File class to open xlsx file

        File file = new File(PrivateData.codeSheetPathName);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found");
        }

        //Create an object of FileInputStream class to read excel file

        FileInputStream inputStream = new FileInputStream(file);

        Workbook prepaidCodes = null;

        //Find the file extension by splitting file name in substring  and getting only extension name

        String fileExtensionName = fileName.substring(fileName.indexOf("."));

        //Check condition if the file is an Excel file (.xlsx/.xls)

        if (fileExtensionName.equals(".xlsx")) {
            prepaidCodes = new XSSFWorkbook(inputStream);

        } else if (fileExtensionName.equals(".xls")) {

            prepaidCodes = new HSSFWorkbook(inputStream);

        } else {
            throw new Error("Unknown file type!");
        }

        Sheet sheet = null;
        //Read sheet inside the workbook by its name
        if (prepaidCodes.getSheet(sheetName) != null) {
            sheet = prepaidCodes.getSheet(sheetName);
        }

//        int rowCount = 0;

        //Find number of rows in excel file
//        if(sheet.getLastRowNum()==0) {
        int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
//        }
        //Create a loop over all the rows of excel file to read it

        for (int i = 0; i < rowCount + 1; i++) {

            Row row = sheet.getRow(i);

            //Create a loop to print cell values in a row

            for (int j = 0; j < row.getLastCellNum(); j++) {

                //Print Excel data in console

                System.out.print(row.getCell(j).getStringCellValue() + "|| ");

            }

            System.out.println();

        }


    }


    //Main function is calling readExcel function to read data from excel file

    public static void main(String... strings) throws IOException, FileNotFoundException {


        //Create an object of ReadGuru99ExcelFile class

        ExcelTest objExcelFile = new ExcelTest();

        //Prepare the path of excel file

        String filePath = System.getProperty("user.dir") + "autoLogin\\src\\main\\java";

        //Call read file method of the class to read data

        objExcelFile.readExcel(filePath, "PrepaidCodes.xlsx", "Blad1");

    }

}