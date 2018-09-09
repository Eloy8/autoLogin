package excel;

import code.Code;
import PrivateData.PrivateData;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.HashMap;
import java.util.Random;

public class excelManager {

    //TODO: Integrate excel.excelTest while loading the sourcefile!
    //The file shall be opened and loaded, where several checks shall be done

    //The excel sheet shall consist out of the codes (first column) and the login attempts(second column).

    //The 3 codes can be picked with a random method(via the length of the array?), which will send them to the codemanager

    private static ArrayList<Code> codeList = new ArrayList<Code>();


    //This class should also be able to delete used codes, and maintain the amount of tries to the excel sheet.
    public static void main(String[] args) throws Exception {

        loadCodeList();

        //New method


    }

    private static void loadCodeList() throws IOException {
        File src = new File(PrivateData.codeSheetPathName);

        FileInputStream fis = new FileInputStream(src);

        XSSFWorkbook wb = new XSSFWorkbook(fis);

        //Takes the first sheet from the excel file
        XSSFSheet sheet = wb.getSheetAt(0);

        FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();

//        HashMap<Integer, String> loginCodeMap = new HashMap<Integer, String>();
//        HashMap<Integer, String> codeUseMap = new HashMap<Integer, String>();

        for (int i = 1; i < getSheetSize(); i++) {
            Code code;
            String loginCode = sheet.getRow(i).getCell(0).getStringCellValue();
//            loginCodeMap.put(i, loginCode);

            DataFormatter formatter = new DataFormatter();
            String codeUseString = formatter.formatCellValue(sheet.getRow(i).getCell(1));
//            codeUseMap.put(i, codeUseString);
            int codeUse = Integer.parseInt(codeUseString);

//            System.out.println("#" + i + " " + loginCodeMap.get(i) + " (" + codeUseMap.get(i) + "/3 times used)");
            code = new Code(i, loginCode, codeUse);
            codeList.add(code);
            System.out.println(code);

        }

        System.out.println("\n");
        for(int i=0;i<codeList.size();i++){
            System.out.println(codeList.get(i));
        }

    }

    //PROBLEMS:
    //How to acces the sheet?
    public static int[] getRandomUniqueNumber() throws IOException {
        int randomNumberPosition = 0;


        Random random = new Random();

//        System.out.println(codes.get(random.nextInt(sheet.getLastRowNum() + 1)));
        int[] uniqueRandomNumberArray = new int[0];

        while (uniqueRandomNumberArray.length < 3) {
            int randomNumber = random.nextInt(getSheetSize());
            if (uniqueRandomNumberArray[randomNumberPosition]/* DOES THIS WORK ALLRIGHT?*/ == 0) {
                uniqueRandomNumberArray = new int[randomNumberPosition];
                randomNumberPosition++;
            }
        }
        return uniqueRandomNumberArray;
    }

    private static int getSheetSize() throws IOException {

        int sheetSize = 0;

        File src = new File(PrivateData.codeSheetPathName);

        FileInputStream fis = new FileInputStream(src);

        XSSFWorkbook wb = new XSSFWorkbook(fis);

        //Takes the first sheet from the excel file
        XSSFSheet sheet = wb.getSheetAt(0);
        sheetSize = sheet.getLastRowNum() + 1;

        return sheetSize;
    }
}

