package com.gr.freshermanagement.utils;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.gr.freshermanagement.dto.request.employee.NewEmployeeRequest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ExcelUtility {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String SHEET = "student";
    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }
    public static List<NewEmployeeRequest> excelToFresherList(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<NewEmployeeRequest> fresherList = new ArrayList<>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                NewEmployeeRequest fresher = new NewEmployeeRequest();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 1:
                            fresher.setName(currentCell.getStringCellValue());
                            break;
                        case 2:
                            fresher.setDob(LocalDate.parse(currentCell.getStringCellValue()));
                            break;
                        case 3:
                            fresher.setAddress(currentCell.getStringCellValue());
                            break;
                        case 4:
                            fresher.setPhone(currentCell.getStringCellValue());
                            break;
                        case 5:
                            fresher.setGender(currentCell.getStringCellValue());
                            break;
                        case 6:
                            fresher.setEmail(String.valueOf(LocalDate.parse(currentCell.getStringCellValue())));
                            break;
                        case 7:
                            fresher.setDepartmentId(String.valueOf(Long.valueOf(currentCell.getStringCellValue())));
                            break;
                        case 8:
                            fresher.setPosition(currentCell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                fresherList.add(fresher);
            }
            workbook.close();
            return fresherList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}