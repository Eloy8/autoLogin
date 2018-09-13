package excel;

import constants.PrivateData;
import data.Code;
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

public class ExcelManager {

    private static int AmountOfCodes = 3;
    private static int randomNumberPosition = 0;
    private static int[] uniqueRandomNumberArray = new int[AmountOfCodes];
    private static ArrayList<Code> codeList = new ArrayList<>();

    public static Code[] getUniqueCodeList() throws IOException {
        loadCodeList();
        uniqueRandomNumberArray = getRandomUniqueNumberArray();
        Code[] uniqueCodeList = new Code[AmountOfCodes];
        for (int i = 0; i < AmountOfCodes; i++) {
            uniqueCodeList[i] = codeList.get(uniqueRandomNumberArray[i]);
            System.out.println(uniqueCodeList[i]);
        }
        return uniqueCodeList;
    }

    private static void loadCodeList() throws IOException {
        File src = new File(PrivateData.codeSheetPathName);
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

    private static int[] getRandomUniqueNumberArray() throws IOException {
        uniqueRandomNumberArray = getRandomUniqueNumber();
        return uniqueRandomNumberArray;
    }

    private static int[] getRandomUniqueNumber() throws IOException {
        while (randomNumberPosition < AmountOfCodes) {
            Random random = new Random();
            int randomNumber = random.nextInt(getSheetSize() - 1);
            if (randomNumberPosition == 0) {
                uniqueRandomNumberArray[randomNumberPosition] = randomNumber;
                randomNumberPosition++;
            } else {
                uniquenessCheck(randomNumber);
            }
        }
        return uniqueRandomNumberArray;
    }

    private static void uniquenessCheck(int randomNumber) {
        boolean noUniqueNumber = true;
        for (int anUniqueRandomNumberArray : uniqueRandomNumberArray) {
            if (randomNumber == anUniqueRandomNumberArray) {
                noUniqueNumber = false;
                break;
            }
        }
        if (noUniqueNumber) {
            uniqueRandomNumberArray[randomNumberPosition] = randomNumber;
            randomNumberPosition++;
        }
    }



    private static int getSheetSize() throws IOException {
        File src = new File(PrivateData.codeSheetPathName);
        FileInputStream fis = new FileInputStream(src);
        XSSFWorkbook wb = new XSSFWorkbook(fis);

        //Takes the first sheet from the excel file
        XSSFSheet sheet = wb.getSheetAt(0);
        return sheet.getLastRowNum() + 1;
    }

    public static int getAmountOfCodes() {
        return AmountOfCodes;
    }
}

