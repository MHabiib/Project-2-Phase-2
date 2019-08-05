package com.future.tcfm.service.impl;
import com.future.tcfm.model.User;
import com.future.tcfm.repository.UserRepository;
import com.future.tcfm.service.ExcelReaderService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelReaderServiceImpl implements ExcelReaderService {
    @Autowired
    UserRepository userRepository;

    private static String[] columns = {"Name", "Email", "Date Of Birth", "Salary"};

    @Override
    public void store(MultipartFile file) {
        try {
            List<User> userList = parseExcelFile(file.getInputStream());
            userRepository.saveAll(userList);
            System.out.println("Bulk Insert success!");
        } catch (IOException e) {
            throw new RuntimeException("Fail -> Message "+e.getMessage());
        }

    }
    @Override
    public ByteArrayInputStream loadFile() throws IOException {
        // Create a Workbook
        Workbook workbook = new XSSFWorkbook();     // new HSSFWorkbook() for generating `.xls` file

        /* CreationHelper helps us create instances for various things like DataFormat,
           Hyperlink, RichTextString etc in a format (HSSF, XSSF) independent way */
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("User");

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Creating cells
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
//        FileOutputStream fileOut = new FileOutputStream("poi-generated-file.xlsx");
        ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
        return new ByteArrayInputStream(fileOut.toByteArray());
    }

    private static List<User> parseExcelFile(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet("Customers");
            Iterator<Row> rows = sheet.iterator();
            List<User> userList = new ArrayList<>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip row (0)
                if(rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                User user = new User();
                int cellIndex = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    if(cellIndex==0) { // email
                        user.setEmail(currentCell.getStringCellValue());
                    } else if(cellIndex==1) { // password
                        user.setPassword(currentCell.getStringCellValue());
                    } else if(cellIndex==2) { // name
                        user.setName(currentCell.getStringCellValue());
                    } else if(cellIndex==3) { // phone number
                        user.setPhone(currentCell.getStringCellValue());
                    } else if(cellIndex==4) { // groupName
                        user.setGroupName(currentCell.getStringCellValue());
                    } else if(cellIndex==5) { // role
                        user.setRole(currentCell.getStringCellValue());
                    }
                    user.setTotalPeriodPayed(0);
                    user.setBalanceUsed(0.0);
                    user.setPeriodeTertinggal(-1);
                    user.setJoinDate(System.currentTimeMillis());
                    user.setActive(true);
                    user.setImagePath("");
                    user.setImageURL("");
                    cellIndex++;
                }
                userList.add(user);
            }
            // Close WorkBook
            workbook.close();
            return userList;
        } catch (IOException e) {
            throw new RuntimeException("FAIL! -> message = " + e.getMessage());
        }
    }


}
