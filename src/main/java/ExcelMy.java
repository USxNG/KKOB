
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.swing.*;

public class ExcelMy extends JFrame {



    private static final Logger LOGGER = LogManager.getLogger(ExcelMy.class);

    public static ArrayList<String> getArrayListFromXLS(String filePath) {
        ArrayList<String> result = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             HSSFWorkbook workbook = new HSSFWorkbook(fis)) {

            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<org.apache.poi.ss.usermodel.Row> rowIterator = sheet.iterator();
            int rowNum = 0;

            while (rowIterator.hasNext()) {
                final int currentRowNum = rowNum;  // Declare a final variable to capture the current value of rowNum
                HSSFRow row = (HSSFRow) rowIterator.next();
                HSSFCell cell = row.getCell(0);

                if (cell != null) {
                    try {
                        String cellValue = cell.getStringCellValue();
                        result.add(cellValue);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        // В случае ошибки добавления значения в список, логируем ошибку
                        LOGGER.error("Ошибка в маркировке Роснефть на строке: " + (currentRowNum + 1) +
                                ", столбец: " + cell.getColumnIndex(), e);

                        // Выводим сообщение пользователю
                        SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(null,
                                        "Ошибка в маркировке Роснефть на строке: " + (currentRowNum + 1) +
                                                ", столбец: " + cell.getColumnIndex(),
                                        "Ошибка", JOptionPane.ERROR_MESSAGE)
                        );
                    } catch (Exception e) {
                        // Обработка других исключений
                        LOGGER.error("Необработанная ошибка на строке: " + (currentRowNum + 1), e);

                        // Выводим сообщение пользователю
                        SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(null,
                                        "Необработанная ошибка на строке: " + (currentRowNum + 1),
                                        "Ошибка", JOptionPane.ERROR_MESSAGE)
                        );
                    }
                }

                rowNum++;
            }
        } catch (IOException e) {
            LOGGER.error("Ошибка при чтении файла", e);
            // Выводим сообщение пользователю
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(null,
                            "Ошибка при чтении файла",
                            "Ошибка", JOptionPane.ERROR_MESSAGE)
            );
        }
        return result;
    }

    public static boolean setArrayListToXLS(String filePath, ArrayList<String> data) {
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             HSSFWorkbook workbook = new HSSFWorkbook(fis)) {

            // Открываем первый лист в книге
            HSSFSheet sheet = workbook.getSheetAt(0);

            // Итерируем по всем строкам в листе
            Iterator<org.apache.poi.ss.usermodel.Row> rowIterator = sheet.iterator();
            Iterator<String> dataIterator = data.iterator();

            while (rowIterator.hasNext() && dataIterator.hasNext()) {
                HSSFRow row = (HSSFRow) rowIterator.next();
                HSSFCell cell = row.createCell(1); // Создаем или получаем ячейку второго столбца

                // Получаем значение из ArrayList и записываем его в ячейку второго столбца
                String cellValue = dataIterator.next();
                cell.setCellValue(cellValue);
            }

            // Сохраняем изменения в файл
            try (FileOutputStream fos = new FileOutputStream(new File(filePath))) {
                workbook.write(fos);
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
