package com.gr.freshermanagement.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class ExcelService<T> {

        public void importFile(File file) throws IOException {
        // open Excel file
        List<T> data = readExcelFile(file);

        processData(data);
    }

    // Read all sheet in Excel file
    protected List<T> readExcelFile(File file) throws IOException {
        List<T> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                Iterator<Row> rowIterator = sheet.iterator();

                // Skip the header row
                if (rowIterator.hasNext()) {
                    rowIterator.next();
                }

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    T item = mapRowToEntity(row);
                    data.add(item);
                }
            }
        }

        return data;
    }

    // mapping entity
    protected abstract T mapRowToEntity(Row row);

    // data process
    protected abstract void processData(List<T> data);
}

