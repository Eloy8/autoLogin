package excel;

import data.Code;
import data.PrivateData;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

//TODO: Integrate excel.ExcelTest while loading the sourcefile!
//The file shall be opened and loaded, where several checks shall be done
//This class should also be able to delete used codes, and maintain the amount of tries to the excel sheet.

/**
 * Reads Excel file and gives an X amount of codes back.
 *
 * @return
 * @throws IOException
 */

public class ExcelManager {

    private static int amountOfCodes = 3;
    private static int randomNumberPosition = 0;
    private static int[] uniqueRandomNumberArray = new int[amountOfCodes];
    private static ArrayList<Code> codeList = new ArrayList<>();

    public static Code[] getUniqueCodeList(int amount) throws IOException {
        loadCodeList();
        uniqueRandomNumberArray = getRandomUniqueNumberArray(amount);
        Code[] uniqueCodeList = new Code[amountOfCodes];
        for (int i = 0; i < amountOfCodes; i++) {
            uniqueCodeList[i] = codeList.get(uniqueRandomNumberArray[i]);
            System.out.println(uniqueCodeList[i]);
        }
        return uniqueCodeList;
    }

    private static void loadCodeList() throws IOException {
        File src = new File(PrivateData.getCodeSheetPathName());
        FileInputStream fis = new FileInputStream(src);
        XSSFWorkbook wb = new XSSFWorkbook(fis);

        //Takes the first sheet from the excel file
        XSSFSheet sheet = wb.getSheetAt(0);

        for (int i = 1; i < getSheetSize(); i++) {
            Code code;
            String loginCode = sheet.getRow(i).getCell(0).getStringCellValue();

            DataFormatter formatter = new DataFormatter();
            String codeUseString = formatter.formatCellValue(sheet.getRow(i).getCell(1));
            int codeUse = Integer.parseInt(codeUseString);

            code = new Code(i, loginCode, codeUse);
            codeList.add(code);
        }
    }

    private static int[] getRandomUniqueNumberArray(int amount) throws IOException {
        uniqueRandomNumberArray = getRandomUniqueNumber(amount);
        return uniqueRandomNumberArray;
    }

    private static int[] getRandomUniqueNumber(int amount) throws IOException {
        int position = 0;
        while (position < amount) { //randomNumberPosition
            Random random = new Random();
            int randomNumber = random.nextInt(getSheetSize() - 1);
            if (position == 0) {
                uniqueRandomNumberArray[position] = randomNumber;
                position++;
            } else {
                uniquenessCheck(randomNumber, position);
                position++;
            }
        }
        return uniqueRandomNumberArray;
    }

    private static void uniquenessCheck(int randomNumber, int position) {
        boolean noUniqueNumber = true;
        for (int anUniqueRandomNumberArray : uniqueRandomNumberArray) {
            if (randomNumber == anUniqueRandomNumberArray) {
                noUniqueNumber = false;
                position++;
                break;
            }
        }
        if (noUniqueNumber) {
            uniqueRandomNumberArray[position] = randomNumber;

        }
    }


    private static int getSheetSize() throws IOException {
        File src = new File(PrivateData.getCodeSheetPathName());
        FileInputStream fis = new FileInputStream(src);
        XSSFWorkbook wb = new XSSFWorkbook(fis);

        //Takes the first sheet from the excel file
        XSSFSheet sheet = wb.getSheetAt(0);
        return sheet.getLastRowNum() + 1;
    }

    public static int getAmountOfCodes() {
        return amountOfCodes;
    }
}

