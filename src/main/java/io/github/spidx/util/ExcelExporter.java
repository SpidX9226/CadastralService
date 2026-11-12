package io.github.spidx.util;

import io.github.spidx.model.Citizen;
import io.github.spidx.model.Request;
import io.github.spidx.service.CitizenService;
import io.github.spidx.service.RequestService;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ExcelExporter {

    private ExcelExporter() {
    }

    public static void Export(Path path) throws IOException {
        var citizens = CitizenService.GetAll();
        var requests = RequestService.GetAll();

        try (Workbook workbook = new XSSFWorkbook()) {

            ExportCitizens(workbook, citizens);
            ExportRequests(workbook, requests);

            try (OutputStream output = Files.newOutputStream(path)) {
                workbook.write(output);
            }
        }
    }

    private static void ExportCitizens(
            Workbook workbook,
            java.util.List<Citizen> citizens) {

        Sheet sheet = workbook.createSheet("Citizens");

        String[] headers = {
                "ID",
                "Full Name",
                "Date of Birth",
                "Passport Series",
                "Passport Number"
        };

        CreateHeader(sheet, headers);

        int rowIndex = 1;

        for (Citizen citizen : citizens) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(
                    citizen.GetId().toString());

            row.createCell(1).setCellValue(
                    citizen.GetFullName());

            row.createCell(2).setCellValue(
                    citizen.getDateOfBirth().toString());

            row.createCell(3).setCellValue(
                    citizen.GetPassportSeries());

            row.createCell(4).setCellValue(
                    citizen.GetPassportNumber());
        }

        AdjustColumns(sheet, headers.length);
    }

    private static void ExportRequests(
            Workbook workbook,
            java.util.List<Request> requests) {

        Sheet sheet = workbook.createSheet("Requests");

        String[] headers = {
                "ID",
                "Citizen ID",
                "Status",
                "Opened At",
                "Closed At"
        };

        CreateHeader(sheet, headers);

        int rowIndex = 1;

        for (Request request : requests) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(
                    request.GetId().toString());

            row.createCell(1).setCellValue(
                    request.GetCitizenId().toString());

            row.createCell(2).setCellValue(
                    request.GetStatus().name());

            row.createCell(3).setCellValue(
                    request.GetOpenAt().toString());

            Cell closedAt = row.createCell(4);

            if (request.GetClosedAt() != null) {
                closedAt.setCellValue(
                        request.GetClosedAt().toString());
            }
        }

        AdjustColumns(sheet, headers.length);
    }

    private static void CreateHeader(
            Sheet sheet,
            String[] headers) {

        Workbook workbook = sheet.getWorkbook();

        CellStyle style = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setBold(true);

        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        Row header = sheet.createRow(0);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = header.createCell(i);

            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }

        sheet.createFreezePane(0, 1);
    }

    private static void AdjustColumns(
            Sheet sheet,
            int columnCount) {

        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
