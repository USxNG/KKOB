import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ReplaceValues {

    private static String filePath; // Путь к выбранному файлу
   private static final Logger LOGGER = LogManager.getLogger(ReplaceValues.class);

    public static void main(String[] args) {

        // Создание массива строк
        System.setProperty("log4j.configurationFile", "log4j2.xml");

        String currentDirectory = System.getProperty("user.dir");
        System.out.println("Current working directory: " + currentDirectory);
        String inputString = "ККОМ-Цв-19х2х2,5-ЭмЭаН-В-КЧ-УТГ-1О-У1";
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
        // Пример использования метода
        if (filePath != null) {
            ArrayList<String> data = ExcelMy.getArrayListFromXLS(filePath,0);

            boolean successETMIKAB = ExcelMy.setArrayListToXLS(filePath, getArrayListNewCableETMIKAB(data), 1);
            boolean successAPOLAX = ExcelMy.setArrayListToXLS(filePath, getArrayListNewCableAPOLAX(data), 2);

            if (successETMIKAB) {
                System.out.println("Данные ЭТМИКАБ успешно записаны в файл.");
            } else {
                System.out.println("Произошел сбой при записи в ЭТМИКАБ в файл.");
            }

            if (successAPOLAX) {
                System.out.println("Данные АПОЛАКС успешно записаны в файл.");
            } else {
                System.out.println("Произошел сбой при записи в АПОЛАКС в файл.");
            }

        }
    }

    private static void createAndShowGUI() {
        // Создаем главное окно
        JFrame frame = new JFrame("Вуду РН");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

// Создаем кнопку для выбора файла
        JButton chooseFileButton = new JButton("Выбрать файл");
        JLabel selectedFileNameLabel = new JLabel("Выбранный файл: Нет");

// Создаем кнопку "ЭТМИКАБ"
        JButton executeETMIKABButton = new JButton("ЭТМИКАБ");
        executeETMIKABButton.setEnabled(false); // По умолчанию неактивна до выбора файла

// Создаем кнопку "АПОЛАКС"
        JButton executeAPOLAXButton = new JButton("АПОЛАКС");
        executeAPOLAXButton.setEnabled(false); // По умолчанию неактивна до выбора файла

// Создаем кнопку "Расшифровка"
        JButton decipherButton = new JButton("Расшифровка");
        decipherButton.setEnabled(false); // По умолчанию неактивна


        // Добавляем слушатель для кнопки выбора файла
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Открываем диалог выбора файла
                JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
                int result = fileChooser.showOpenDialog(null);

                // Если файл выбран, сохраняем путь к нему и активируем кнопки "ЭТМИКАБ" и "АПОЛАКС"
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    filePath = selectedFile.getAbsolutePath();
                    selectedFileNameLabel.setText("Выбранный файл: " + selectedFile.getName());
                    executeETMIKABButton.setEnabled(true);
                    executeAPOLAXButton.setEnabled(true);
                }
            }
        });

// Добавляем слушатель для кнопки "ЭТМИКАБ"
        executeETMIKABButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Выполняем программу и выводим сообщение об успешной записи или ошибке
                if (filePath != null) {
                    ArrayList<String> data = ExcelMy.getArrayListFromXLS(filePath, 0);
                    boolean success = ExcelMy.setArrayListToXLS(filePath, getArrayListNewCableETMIKAB(data),1);

                    if (success) {
                        JOptionPane.showMessageDialog(frame, "Данные ЭТМИКАБ успешно записаны в файл.");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Произошел сбой при записи ЭТМИКАБ в файл.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Добавляем слушатель для кнопки "АПОЛАКС"
        executeAPOLAXButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Выполняем программу и выводим сообщение об успешной записи или ошибке
                if (filePath != null) {
                    ArrayList<String> data = ExcelMy.getArrayListFromXLS(filePath, 0);
                    boolean success = ExcelMy.setArrayListToXLS(filePath, getArrayListNewCableAPOLAX(data),2);

                    if (success) {
                        JOptionPane.showMessageDialog(frame, "Данные АПОЛАКС успешно записаны в файл.");
                        decipherButton.setEnabled(true);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Произошел сбой при записи АПОЛАКС в файл.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        // Добавляем слушатель для кнопки "расшифровать"
        decipherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Добавьте здесь код для обработки нажатия кнопки "Расшифровка"
            }
        });


        // Создаем панель для компонентов
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(chooseFileButton);
        panel.add(selectedFileNameLabel); // Используем новый JLabel для отображения имени файла
        panel.add(Box.createVerticalStrut(10)); // Добавляем вертикальный отступ
        panel.add(executeETMIKABButton);
        panel.add(executeAPOLAXButton);
        panel.add(decipherButton);

// Создаем панель для подвала
        JPanel footerPanel = new JPanel();
        JLabel customTextLabel = new JLabel("msk.us@mail.ru");
        footerPanel.add(customTextLabel);

// Создаем основную панель, содержащую панель с компонентами и панель для подвала
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

// Устанавливаем компоненты на фрейм
        frame.getContentPane().add(mainPanel);

// Устанавливаем размеры и отображаем окно
        frame.setSize(300, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static String[] splitArray(String inputString, String сhOne) {
        String[] parts = inputString.split(сhOne);
        return parts;
    }


    public static String returnETMIKAB(String inputString,String chOne) {

        try {
            String[] s = splitArray(inputString, chOne);

            if (s.length < 9) {
                // Handle the case where the array does not have enough elements
                throw new IllegalArgumentException("Invalid array length");
            }



        String PogIspoln = s[7];
        PogIspoln = PogIspoln.replaceAll("[0-9Н]", "");

        String KiPv = s[0];

        String Markirovka = s[1];

        String NumberCoreSechenie = s[2];
        NumberCoreSechenie = NumberCoreSechenie.toLowerCase().replaceAll("[x]", "х");
        String[] arrayNumberCoreSechenie = splitArray(NumberCoreSechenie, "х");
        var arrayListNumberCoreSechenie = new ArrayList<>(Arrays.asList(arrayNumberCoreSechenie));
        if (arrayNumberCoreSechenie.length <= 2) arrayListNumberCoreSechenie.add(1, "1");
        var numbercorepare = arrayListNumberCoreSechenie.get(0);
        var typedoubletrple = arrayListNumberCoreSechenie.get(1);
        var sechenie = arrayListNumberCoreSechenie.get(2);
        if (sechenie.equals("1")) sechenie = sechenie + ",0";
        if (sechenie.equals("0,64")) sechenie = "0,5";
        if (sechenie.equals("0,6")) sechenie = "0,5";
        if (sechenie.equals("0,78")) sechenie = "0,5";


        String ParnObchiyBrony = s[3];
        String Parniy = "";
        String Obchiy = "";
        String Bronya = "";

        int N = 0;
        Parniy = String.valueOf(ParnObchiyBrony.charAt(N));
        if (!Parniy.equals("Н")) Parniy = Parniy + String.valueOf(ParnObchiyBrony.charAt(++N));

        Obchiy = String.valueOf(ParnObchiyBrony.charAt(++N));
        if (!Obchiy.equals("Н")) Obchiy = Obchiy + String.valueOf(ParnObchiyBrony.charAt(++N));

        Bronya = String.valueOf(ParnObchiyBrony.charAt(++N));
        if (!Bronya.equals("Н")) Bronya = Bronya + String.valueOf(ParnObchiyBrony.charAt(++N));



        String Vodoblok = "в";
        if (s[4].equals("Н")) Vodoblok = "";

        String ObolochkaCvet = s[5];
        String Cvet = String.valueOf(ObolochkaCvet.charAt(1));

        String SpetsIspoln = s[6];
        String Uf = String.valueOf(SpetsIspoln.charAt(0));
        String EXL = String.valueOf(SpetsIspoln.charAt(1));
        String Zapolnen = String.valueOf(SpetsIspoln.charAt(2));

        String UXL = s[8];

        // Создание отображения (HashMap) для сопоставления значений
        Map<String, String> valueKiPv = new HashMap<>();
        valueKiPv.put("КИ1ОМ", "Пв");
        valueKiPv.put("КИ2ОМ", "Пв");
        valueKiPv.put("КИ3ОМ", "Пв");
        valueKiPv.put("КИ1ОБ", "Пв");
        valueKiPv.put("КИ2ОБ", "Пв");
        valueKiPv.put("КИ3ОБ", "Пв");
        valueKiPv.put("КИ1ОА", "Пв");
        valueKiPv.put("КИ2ОА", "Пв");
        valueKiPv.put("КИ3ОА", "Пв");
        valueKiPv.put("КИ1ММ", "Пв");
        valueKiPv.put("КИ2ММ", "Пв");
        valueKiPv.put("КИ3ММ", "Пв");
        valueKiPv.put("КИ1МБ", "Пв");
        valueKiPv.put("КИ2МБ", "Пв");
        valueKiPv.put("КИ3МБ", "Пв");
        valueKiPv.put("КИ1МА", "Пв");
        valueKiPv.put("КИ2МА", "Пв");
        valueKiPv.put("КИ3МА", "Пв");

        // Создание отображения (HashMap) для сопоставления значений
        Map<String, String> valueOMOBMM = new HashMap<>();
            valueOMOBMM.put("ККОМ", "л ок");
            valueOMOBMM.put("ККмОМ", "л ок");
            valueOMOBMM.put("КИ1ОМ", "л ок");
            valueOMOBMM.put("КИ2ОМ", "л ок");
            valueOMOBMM.put("КИ3ОМ", "л ок");
            valueOMOBMM.put("ККОБ", "ок");
            valueOMOBMM.put("ККмОБ", "ок");
            valueOMOBMM.put("КИ1ОБ", "ок");
            valueOMOBMM.put("КИ2ОБ", "ок");
            valueOMOBMM.put("КИ3ОБ", "ок");
            valueOMOBMM.put("ККОА", "ок");
            valueOMOBMM.put("ККмОА", "ок");
            valueOMOBMM.put("КИ1ОА", "ок");
            valueOMOBMM.put("КИ2ОА", "ок");
            valueOMOBMM.put("КИ3ОА", "ок");
            valueOMOBMM.put("ККММ", "л");
            valueOMOBMM.put("ККмММ", "л");
            valueOMOBMM.put("КИ1ММ", "л");
            valueOMOBMM.put("КИ2ММ", "л");
            valueOMOBMM.put("КИ3ММ", "л");

        // Создание отображения (HashMap) для сопоставления значений
        Map<String, String> valueIndividualEaEmEp = new HashMap<>();
        valueIndividualEaEmEp.put("Эа", "Эф");
        valueIndividualEaEmEp.put("Эм", "Э");
        valueIndividualEaEmEp.put("Эп", "Э");

        // Создание отображения (HashMap) для сопоставления значений
        Map<String, String> valueObchiyEaEmEp = new HashMap<>();
        valueObchiyEaEmEp.put("Эа", "Эф");
        valueObchiyEaEmEp.put("Эм", "Э");
        valueObchiyEaEmEp.put("Эп", "Э");
        valueObchiyEaEmEp.put("Эк", "Эк");

        // Создание отображения (HashMap) для сопоставления значений
        Map<String, String> valueBronyaBK = new HashMap<>();
        valueBronyaBK.put("Бс", "Б");
        valueBronyaBK.put("Бц", "К");

        // Создание отображения (HashMap) для сопоставления значений
        Map<String, String> valueVodoblokB = new HashMap<>();
        valueVodoblokB.put("В", "в");

        // Создание отображения (HashMap) для сопоставления значений
        Map<String, String> valueObolochkaVPsPT = new HashMap<>();
        valueObchiyEaEmEp.put("Э", "Пв");
        valueObchiyEaEmEp.put("В", "В");
        valueObchiyEaEmEp.put("К", "П");
        valueObchiyEaEmEp.put("Т", "Т");

        // Создание отображения (HashMap) для сопоставления значений
        Map<String, String> valueCvet = new HashMap<>();
        valueCvet.put("С", "cиняя оболочка");
        valueCvet.put("О", "оранжевая оболочка");
        valueCvet.put("И", "оранжевая с синей полосой оболочка");
        valueCvet.put("Ф", "фиолетовая оболочка");
        valueCvet.put("Ч", "");
        valueCvet.put("У", "");

        // Создание отображения (HashMap) для сопоставления значений
        Map<String, String> valueUF = new HashMap<>();
        valueUF.put("У", "УФ");

        // Создание отображения (HashMap) для сопоставления значений
        Map<String, String> valueSpetsIspoln = new HashMap<>();

        valueSpetsIspoln.put("Х", "-ЭХЛ");
        valueSpetsIspoln.put("HL", "-ХЛ");

        Map<String, String> valueZapolnenie = new HashMap<>();
        valueZapolnenie.put("Н", "(без заполнения)");


        // Создание отображения (HashMap) для сопоставления значений
        Map<String, String> valuePogIspoln = new HashMap<>();
        valuePogIspoln.put("Н", "нг(А)-LS");
        valuePogIspoln.put("А", "нг(А)-LS");
        valuePogIspoln.put("Д", "нг(А)-LS");
        valuePogIspoln.put("Г", "нг(А)-HF");
        valuePogIspoln.put("О", "нг(А)-FRLS");
        valuePogIspoln.put("П", "нг(А)-FRHF");

        // Создание отображения (HashMap) для сопоставления значений
        Map<String, String> valuetempIzolazia = new HashMap<>();
        valuetempIzolazia.put("Н", "В");
        valuetempIzolazia.put("А", "В");
        valuetempIzolazia.put("Д", "В");
        valuetempIzolazia.put("Г", "П");
        valuetempIzolazia.put("О", "В");
        valuetempIzolazia.put("П", "П");
        valuetempIzolazia.put("Т", "Т");

        var stringETMiKAB = "Кабель ЭТМИКАБ МК";

        var isolayazia = valuetempIzolazia.get(PogIspoln);
        if (EXL.equals("Т")) isolayazia = "Т";

        if (valueKiPv.containsKey(KiPv)) isolayazia = valueKiPv.get(KiPv);

        if (!Obchiy.equals("Н")) {Obchiy = valueObchiyEaEmEp.get(Obchiy);} else {Obchiy="";}
        if (!Bronya.equals("Н")) {Bronya = valueBronyaBK.get(Bronya);} else {Bronya="";}

        var Obolochka = valuetempIzolazia.get(PogIspoln);
        if (EXL.equals("Т")) Obolochka = "Т";

        var PogIsplnenieNG = valuePogIspoln.get(PogIspoln);
        if (EXL.equals("Т")) PogIsplnenieNG = StringUtils.left(PogIsplnenieNG, 5);

        var OkLug = valueOMOBMM.get(KiPv);
        if (OkLug != null) sechenie = sechenie + OkLug;


        NumberCoreSechenie = "";
        NumberCoreSechenie = NumberCoreSechenie + numbercorepare + "х" + sechenie;
        if (!Parniy.equals("Н")) {
            if (!typedoubletrple.equals("1")){
            Parniy = valueIndividualEaEmEp.get(Parniy);
            NumberCoreSechenie = "";
            NumberCoreSechenie = NumberCoreSechenie + numbercorepare + "х(" + typedoubletrple + "х" + sechenie + ")" + Parniy;

        }
    }

        if ( Uf.equals("Н") || Uf==null) Uf = "";
        if ( UXL.contains("1")) Uf = "У";
        if (EXL.equals("Н") || EXL.equals("Т")) {
            if (UXL.contains("У")) {EXL = "HL";}
            else {EXL = "";}

        }
        if (EXL != null) EXL = valueSpetsIspoln.get(EXL);
        if (EXL == null) EXL = "";

        if (Uf != null) Uf = valueUF.get(Uf);
        if (Uf == null) Uf = "";

        if (Zapolnen.equals("Н")) {
            Zapolnen = valueZapolnenie.get("Н");
        } else {
            Zapolnen = "";
        }

        if (Cvet != null) {
            Cvet = valueCvet.get(Cvet);
        }

        var result =
                stringETMiKAB +
                        isolayazia +
                        Obchiy +
                        Bronya +
                        Obolochka +
                        Vodoblok +
                        PogIsplnenieNG +
                        EXL +
                        " " + NumberCoreSechenie +
                        " " + Uf +
                        " " + Zapolnen +
                        " " + Cvet;



        return result;
        } catch (Exception e) {
            // Log other exceptions and display a generic error message
            LOGGER.error("Error processing inputString: " + inputString, e);
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(null,
                            "Ошибка при обработке строки: " + inputString,
                            "Ошибка", JOptionPane.ERROR_MESSAGE)
            );
            return "Error";
        }
    }

    public static String returnAPOLAX(String inputString,String chOne) {

        try {
            String[] s = splitArray(inputString, chOne);

            if (s.length < 9) {
                // Handle the case where the array does not have enough elements
                throw new IllegalArgumentException("Invalid array length");
            }



            String PogIspoln = s[7];
            PogIspoln = PogIspoln.replaceAll("[0-9Н]", "");

            String KiPv = s[0];

            String Markirovka = s[1];

            String NumberCoreSechenie = s[2];
            NumberCoreSechenie = NumberCoreSechenie.toLowerCase().replaceAll("[x]", "х");
            String[] arrayNumberCoreSechenie = splitArray(NumberCoreSechenie, "х");
            var arrayListNumberCoreSechenie = new ArrayList<>(Arrays.asList(arrayNumberCoreSechenie));
            if (arrayNumberCoreSechenie.length <= 2) arrayListNumberCoreSechenie.add(1, "1");
            var numbercorepare = arrayListNumberCoreSechenie.get(0);
            var typedoubletrple = arrayListNumberCoreSechenie.get(1);
            var sechenie = arrayListNumberCoreSechenie.get(2);
            if (sechenie.equals("1")) sechenie = sechenie + ",0";
            if (sechenie.equals("0,64")) sechenie = "0,5";
            if (sechenie.equals("0,6")) sechenie = "0,5";
            if (sechenie.equals("0,78")) sechenie = "0,5";


            String ParnObchiyBrony = s[3];
            String Parniy = "";
            String Obchiy = "";
            String Bronya = "";

            int N = 0;
            Parniy = String.valueOf(ParnObchiyBrony.charAt(N));
            if (!Parniy.equals("Н")) Parniy = Parniy + String.valueOf(ParnObchiyBrony.charAt(++N));

            Obchiy = String.valueOf(ParnObchiyBrony.charAt(++N));
            if (!Obchiy.equals("Н")) Obchiy = Obchiy + String.valueOf(ParnObchiyBrony.charAt(++N));

            Bronya = String.valueOf(ParnObchiyBrony.charAt(++N));
            if (!Bronya.equals("Н")) Bronya = Bronya + String.valueOf(ParnObchiyBrony.charAt(++N));



            String Vodoblok = "в";
            if (s[4].equals("Н")) Vodoblok = "";

            String ObolochkaCvet = s[5];
            String Cvet = String.valueOf(ObolochkaCvet.charAt(1));

            String SpetsIspoln = s[6];
            String Uf = String.valueOf(SpetsIspoln.charAt(0));
            String EXL = String.valueOf(SpetsIspoln.charAt(1));
            String Zapolnen = String.valueOf(SpetsIspoln.charAt(2));

            String UXL = s[8];

            // Создание отображения (HashMap) для сопоставления значений
            Map<String, String> valueKiPv = new HashMap<>();
            valueKiPv.put("КИ1ОМ", "Пс");
            valueKiPv.put("КИ2ОМ", "Пс");
            valueKiPv.put("КИ3ОМ", "Пс");
            valueKiPv.put("КИ1ОБ", "Пс");
            valueKiPv.put("КИ2ОБ", "Пс");
            valueKiPv.put("КИ3ОБ", "Пс");
            valueKiPv.put("КИ1ОА", "Пс");
            valueKiPv.put("КИ2ОА", "Пс");
            valueKiPv.put("КИ3ОА", "Пс");
            valueKiPv.put("КИ1ММ", "Пс");
            valueKiPv.put("КИ2ММ", "Пс");
            valueKiPv.put("КИ3ММ", "Пс");
            valueKiPv.put("КИ1МБ", "Пс");
            valueKiPv.put("КИ2МБ", "Пс");
            valueKiPv.put("КИ3МБ", "Пс");
            valueKiPv.put("КИ1МА", "Пс");
            valueKiPv.put("КИ2МА", "Пс");
            valueKiPv.put("КИ3МА", "Пс");

            // Создание отображения (HashMap) для сопоставления значений
            Map<String, String> valueOMOBMM = new HashMap<>();
            valueOMOBMM.put("ККОМ", "л ок");
            valueOMOBMM.put("ККмОМ", "л ок");
            valueOMOBMM.put("КИ1ОМ", "л ок");
            valueOMOBMM.put("КИ2ОМ", "л ок");
            valueOMOBMM.put("КИ3ОМ", "л ок");
            valueOMOBMM.put("ККОБ", "ок");
            valueOMOBMM.put("ККмОБ", "ок");
            valueOMOBMM.put("КИ1ОБ", "ок");
            valueOMOBMM.put("КИ2ОБ", "ок");
            valueOMOBMM.put("КИ3ОБ", "ок");
            valueOMOBMM.put("ККОА", "ок");
            valueOMOBMM.put("ККмОА", "ок");
            valueOMOBMM.put("КИ1ОА", "ок");
            valueOMOBMM.put("КИ2ОА", "ок");
            valueOMOBMM.put("КИ3ОА", "ок");
            valueOMOBMM.put("ККММ", "л");
            valueOMOBMM.put("ККмММ", "л");
            valueOMOBMM.put("КИ1ММ", "л");
            valueOMOBMM.put("КИ2ММ", "л");
            valueOMOBMM.put("КИ3ММ", "л");

            // Создание отображения (HashMap) для сопоставления значений
            Map<String, String> valueIndividualEaEmEp = new HashMap<>();
            valueIndividualEaEmEp.put("Эа", "Эа");
            valueIndividualEaEmEp.put("Эм", "Э");
            valueIndividualEaEmEp.put("Эп", "Э");

            // Создание отображения (HashMap) для сопоставления значений
            Map<String, String> valueObchiyEaEmEp = new HashMap<>();
            valueObchiyEaEmEp.put("Эа", "Эа");
            valueObchiyEaEmEp.put("Эм", "Э");
            valueObchiyEaEmEp.put("Эп", "Э");
            valueObchiyEaEmEp.put("Эк", "Эк");

            // Создание отображения (HashMap) для сопоставления значений
            Map<String, String> valueBronyaBK = new HashMap<>();
            valueBronyaBK.put("Бс", "Б");
            valueBronyaBK.put("Бц", "К");

            // Создание отображения (HashMap) для сопоставления значений
            Map<String, String> valueVodoblokB = new HashMap<>();
            valueVodoblokB.put("В", "в");

            // Создание отображения (HashMap) для сопоставления значений
            Map<String, String> valueObolochkaVPsPT = new HashMap<>();
            valueObchiyEaEmEp.put("Э", "Пв");
            valueObchiyEaEmEp.put("В", "В");
            valueObchiyEaEmEp.put("К", "П");
            valueObchiyEaEmEp.put("Т", "Т");

            // Создание отображения (HashMap) для сопоставления значений
            Map<String, String> valueCvet = new HashMap<>();
            valueCvet.put("С", "cиняя оболочка");
            valueCvet.put("О", "оранжевая оболочка");
            valueCvet.put("И", "оранжевая с синей полосой оболочка");
            valueCvet.put("Ф", "фиолетовая оболочка");
            valueCvet.put("Ч", "");
            valueCvet.put("У", "");

            // Создание отображения (HashMap) для сопоставления значений
            Map<String, String> valueUF = new HashMap<>();
            valueUF.put("У", "УФ");

            // Создание отображения (HashMap) для сопоставления значений
            Map<String, String> valueSpetsIspoln = new HashMap<>();

            valueSpetsIspoln.put("Х", "-ЭХЛ");
            valueSpetsIspoln.put("HL", "-ХЛ");

            Map<String, String> valueZapolnenie = new HashMap<>();
            valueZapolnenie.put("Н", "(без заполнения)");


            // Создание отображения (HashMap) для сопоставления значений
            Map<String, String> valuePogIspoln = new HashMap<>();
            valuePogIspoln.put("Н", "нг(А)-LS");
            valuePogIspoln.put("А", "нг(А)-LS");
            valuePogIspoln.put("Д", "нг(А)-LS");
            valuePogIspoln.put("Г", "нг(А)-HF");
            valuePogIspoln.put("О", "нг(А)-FRLS");
            valuePogIspoln.put("П", "нг(А)-FRHF");

            // Создание отображения (HashMap) для сопоставления значений
            Map<String, String> valuetempIzolazia = new HashMap<>();
            valuetempIzolazia.put("Н", "В");
            valuetempIzolazia.put("А", "В");
            valuetempIzolazia.put("Д", "В");
            valuetempIzolazia.put("Г", "П");
            valuetempIzolazia.put("О", "В");
            valuetempIzolazia.put("П", "П");
            valuetempIzolazia.put("Т", "Т");

            var stringAPOLAX = "Кабель АПОЛАКС КМ";

            var isolayazia = valuetempIzolazia.get(PogIspoln);
            if (EXL.equals("Т")) isolayazia = "Т";

            if (valueKiPv.containsKey(KiPv)) isolayazia = valueKiPv.get(KiPv);

            if (!Obchiy.equals("Н")) {Obchiy = valueObchiyEaEmEp.get(Obchiy);} else {Obchiy="";}
            if (!Bronya.equals("Н")) {Bronya = valueBronyaBK.get(Bronya);} else {Bronya="";}

            var Obolochka = valuetempIzolazia.get(PogIspoln);
            if (EXL.equals("Т")) Obolochka = "Т";

            var PogIsplnenieNG = valuePogIspoln.get(PogIspoln);
            if (EXL.equals("Т")) PogIsplnenieNG = StringUtils.left(PogIsplnenieNG, 5);
            if (PogIspoln.equals("О") || PogIspoln.equals("П")) PogIsplnenieNG = "нг(А)-FR";

            var OkLug = valueOMOBMM.get(KiPv);
            if (OkLug != null) sechenie = sechenie + OkLug;


            NumberCoreSechenie = "";
            NumberCoreSechenie = NumberCoreSechenie + numbercorepare + "х" + sechenie;
            if (!Parniy.equals("Н")) {
                if (!typedoubletrple.equals("1")){
                    Parniy = valueIndividualEaEmEp.get(Parniy);
                    NumberCoreSechenie = "";
                    NumberCoreSechenie = NumberCoreSechenie + numbercorepare + "х(" + typedoubletrple + "х" + sechenie + ")" + Parniy;

                }
            }

            if ( Uf.equals("Н") || Uf==null) Uf = "";
            if ( UXL.contains("1")) Uf = "У";
            if (EXL.equals("Н") || EXL.equals("Т")) {
                if (UXL.contains("У")) {EXL = "HL";}
                else {EXL = "";}

            }
            if (EXL != null) EXL = valueSpetsIspoln.get(EXL);
            if (EXL == null) EXL = "";

            if (Uf != null) Uf = valueUF.get(Uf);
            if (Uf == null) Uf = "";

            if (Zapolnen.equals("Н")) {
                Zapolnen = valueZapolnenie.get("Н");
            } else {
                Zapolnen = "";
            }

            if (Cvet != null) {
                Cvet = valueCvet.get(Cvet);
            }

            var result =
                    stringAPOLAX +
                            isolayazia +
                            Obchiy +
                            Bronya +
                            Obolochka +
                            Vodoblok +
                            PogIsplnenieNG +
                            EXL +
                            " " + NumberCoreSechenie +
                            " " + Uf +
                            " " + Zapolnen +
                            " " + Cvet;





            return result;
        } catch (Exception e) {
            // Log other exceptions and display a generic error message
            LOGGER.error("Error processing inputString: " + inputString, e);
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(null,
                            "Ошибка при обработке строки: " + inputString,
                            "Ошибка", JOptionPane.ERROR_MESSAGE)
            );
            return "Error";
        }
    }



    public static ArrayList<String> getArrayListNewCableETMIKAB(ArrayList<String> arrayList) {
        ArrayList<String> newArrayListNewCable = new ArrayList<>();
        for (String s : arrayList) {
            newArrayListNewCable.add(returnETMIKAB(s,"-"));

        }

       return newArrayListNewCable;

    }

    public static ArrayList<String> getArrayListNewCableAPOLAX(ArrayList<String> arrayList) {
        ArrayList<String> newArrayListNewCable = new ArrayList<>();
        for (String s : arrayList) {
            newArrayListNewCable.add(returnAPOLAX(s,"-"));

        }

        return newArrayListNewCable;

    }


}



