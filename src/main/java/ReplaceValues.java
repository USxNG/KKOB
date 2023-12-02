import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
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
        JButton executeETMIKABButton = new JButton("РН->ЭТМИКАБ");
        executeETMIKABButton.setEnabled(false); // По умолчанию неактивна до выбора файла

// Создаем кнопку "АПОЛАКС"
        JButton executeAPOLAXButton = new JButton("РН->АПОЛАКС");
        executeAPOLAXButton.setEnabled(false); // По умолчанию неактивна до выбора файла

        // Добавляем кнопку "Расшифровать ЭТМИКАБ"
        JButton decipherETMIKABButton = new JButton("Расшифровать ЭТМИКАБ");
        decipherETMIKABButton.setEnabled(false); // По умолчанию неактивна


        // Добавляем кнопку "Расшифровать АПОЛАКС"
        JButton decipherAPOLAXButton = new JButton("Расшифровать АПОЛАКС");
        decipherAPOLAXButton.setEnabled(false); // По умолчанию неактивна

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
                    decipherAPOLAXButton.setEnabled(true);
                    decipherETMIKABButton.setEnabled(true);
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
                    boolean success = ExcelMy.setArrayListToXLS(filePath, getArrayListNewCableAPOLAX(data),1);

                    if (success) {
                        JOptionPane.showMessageDialog(frame, "Данные АПОЛАКС успешно записаны в файл.");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Произошел сбой при записи АПОЛАКС в файл.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Добавляем слушатель для кнопки "Расшифровать АПОЛАКС"
        decipherAPOLAXButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var target = "АПОЛАКС";
                int maxColumn = 10;
                if (filePath != null) {
                    try {
                        int N = ExcelMy.getColumnNumberWithTarget(filePath, target, maxColumn);
                        ArrayList<String> dataAPOLAX = ExcelMy.getArrayListWithTarget(filePath, target, maxColumn);
                        // Assuming setArrayListToXLS method also might throw exceptions
//                        System.out.println(processAndDecryptAPOLAXData(dataAPOLAX));
                        ExcelMy.setArrayListToXLS(filePath, processAndDecryptAPOLAXData(dataAPOLAX), N + 1);
                    } catch (Exception mye) {
                        mye.printStackTrace();
                        JOptionPane.showMessageDialog(null, "An error occurred while processing the data.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
;
;
        // Создаем панель для компонентов
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(executeETMIKABButton);
        buttonPanel.add(decipherETMIKABButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(executeAPOLAXButton);
        buttonPanel.add(decipherAPOLAXButton);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(chooseFileButton);
        panel.add(selectedFileNameLabel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(buttonPanel);

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
            NumberCoreSechenie = NumberCoreSechenie.toLowerCase().replaceAll("[xXХ]", "х");
            System.out.println(NumberCoreSechenie);
            String[] arrayNumberCoreSechenie = splitArray(NumberCoreSechenie, "х");
            var arrayListNumberCoreSechenie = new ArrayList<>(Arrays.asList(arrayNumberCoreSechenie));
            if (arrayNumberCoreSechenie.length <= 2) arrayListNumberCoreSechenie.add(1, "1");
            var numbercorepare = arrayListNumberCoreSechenie.get(0);
            var typedoubletrple = arrayListNumberCoreSechenie.get(1);
            var sechenie = arrayListNumberCoreSechenie.get(2);

            System.out.println(numbercorepare);
            System.out.println(typedoubletrple);
            System.out.println(sechenie);
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
            if(typedoubletrple.equals("1")) NumberCoreSechenie = numbercorepare + "х" + sechenie;
            if(typedoubletrple.equals("2")) NumberCoreSechenie = numbercorepare + "х2х" + sechenie;
            if(typedoubletrple.equals("3")) NumberCoreSechenie = numbercorepare + "х3х" + sechenie;
            if(typedoubletrple.equals("4")) NumberCoreSechenie = numbercorepare + "х4х" + sechenie;
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

            var stringAPOLAX = "Кабель АПОЛАКС-КМ";

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
            if(typedoubletrple.equals("1")) NumberCoreSechenie = numbercorepare + "х" + sechenie;
            if(typedoubletrple.equals("2")) NumberCoreSechenie = numbercorepare + "х2х" + sechenie;
            if(typedoubletrple.equals("3")) NumberCoreSechenie = numbercorepare + "х3х" + sechenie;
            if(typedoubletrple.equals("4")) NumberCoreSechenie = numbercorepare + "х4х" + sechenie;
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


    private static ArrayList<String> processAndDecryptAPOLAXData(ArrayList<String> arrayListAPOLAX) {
        ArrayList<String> processedData = new ArrayList<>();

        for (String encryptedValue : arrayListAPOLAX) {
            try {
                // Perform decryption or other processing on the encryptedValue
                String decryptedValue = decrypt(encryptedValue); // Replace with your decryption logic
                // Add the processed value to the result ArrayList
                processedData.add(decryptedValue);
            } catch (Exception e) {
                // Handle decryption or processing errors
                e.printStackTrace();
                // You might want to log the error or handle it in a way appropriate for your application
            }
        }

        return processedData;
    }

    // Example decryption method (replace with your actual decryption logic)
    private static String decrypt(String encryptedValue) {
        String result ="";
        var targetAPOLAX = "АПОЛАКС";
        String isolyazia ="c изоляцией из ПВХ пластиката, ";
        String screenObchiy ="без общего экрана,  ";
        String screenIndividual ="без парного экрана,  ";
        String bronya ="без брони, ";
        String obolochka ="с оболочкой из ПВХ пластиката, ";
        String zapolnenie ="с заполнением межжильных пространств не гигроскопичным материалом методом экструзии по ГОСТ IEC 60079-14, ";
        String gila ="токоведущие жилы медные многопроволочные ";
        String gilaLugenaya ="нелуженые ";
        String skrutka ="токоведущие жилы скручены в пучок";
        String vodoBlock ="";
        String pogIspoln ="";
        String temperatura ="температура эксплуатации от -50 градусов Цельсия до +80 градусов Цельсия, температура монтажа без подогрева -15 градусов Цельсия, ";
        String UF ="";
        String maslo ="";
        String chemic ="";
        String cvetObolochki ="цвет внешней оболочки черный (по умолчанию)";
        String srok ="cрок службы не менее 35 лет. Гарантийный срок эксплуатации 3 года. ";


        int position = encryptedValue.indexOf(targetAPOLAX) + targetAPOLAX.length(); //АПОЛАКС = "-"
        //
        var tempD = String.valueOf(encryptedValue.charAt(position+1));
        tempD= String.valueOf(encryptedValue.charAt(position+2)); //КМ
        tempD= String.valueOf(encryptedValue.charAt(position+3)); //КМ
        //изоляция
        if (tempD.equals("П")) isolyazia ="c изоляцией из ПВХ пластиката, ";
        if (tempD.equals("Т")) isolyazia ="c изоляцией из термопластичных эластомеров, ";
        String s = tempD + encryptedValue.charAt(position + 4);
        if (s.equals("Пс")) isolyazia ="c изоляцией из сшитого полиолефина, ";
        if (s.equals("Рв")) isolyazia ="c изоляцией из этиленпропиленовой резины, ";
        if (s.equals("Рк")) isolyazia ="c изоляцией из кремнийорганической резины, ";

        //общий экран
        tempD = String.valueOf(encryptedValue.charAt(position + 4));  //ПЭ
        if (tempD.equals("Э")) screenObchiy = "с общим экраном из медных проволок, ";
        s = tempD + String.valueOf(encryptedValue.charAt(position + 5)); //ПЭа
        if (s.equals("Эа")) screenObchiy = "с общим экраном из алюмополимерной ленты (алюмофлекс) с контактным луженым проводником под ним, ";
        if (s.equals("Эл")) screenObchiy = "с общим экраном из из медных луженых проволок, ";
        if (s.equals("Эк")) screenObchiy = "с общим комбинированным экраном, состоящем из алюмополимерной ленты и медных луженых проволок, ";
        if (s.equals("Эм")) screenObchiy = "с общим экраном из меднополимерной ленты (медьфлекс), ";

        tempD = String.valueOf(encryptedValue.charAt(position + 5)); //ПвЭ
        if (tempD.equals("Э")) screenObchiy = "с общим экраном из медных проволок, ";
        s = tempD + String.valueOf(encryptedValue.charAt(position + 6)); //ПвЭа
        if (s.equals("Эа")) screenObchiy = "с общим экраном из алюмополимерной ленты (алюмофлекс), ";
        if (s.equals("Эл")) screenObchiy = "с общим экраном из медных луженых проволок, ";
        if (s.equals("Эк")) screenObchiy = "с общим комбинированным экраном, состоящем из алюмополимерной ленты и медных луженых проволок, ";
        if (s.equals("Эм")) screenObchiy = "с общим экраном из меднополимерной ленты (медьфлекс), ";

        //индивидуальный экран
        if (encryptedValue.contains(")Э"))  screenIndividual = "с парным экраном из медных проволок, ";
        if (encryptedValue.contains(")Эа")) screenIndividual = "с парным экраном из алюмополимерной ленты (алюмофлекс) с контактным луженым проводником под ним, ";
        if (encryptedValue.contains(")Эл")) screenIndividual = "с парным экраном из медных луженых проволок, ";
        if (encryptedValue.contains(")Эк")) screenIndividual = "с парным комбинированным экраном, состоящем из алюмополимерной ленты и медных луженых проволок, ";
        if (encryptedValue.contains(")Эм")) screenIndividual = "с парным экраном из меднополимерной ленты (медьфлекс) с контактным луженым проводником под ним, ";

        //броня
        if (encryptedValue.contains("КП") || encryptedValue.contains("КВ") || encryptedValue.contains("КТ")) bronya = "с броней из стальной оцинкованной проволоки, ";
        if (encryptedValue.contains("БП") || encryptedValue.contains("БВ") || encryptedValue.contains("БТ")) bronya = "с броней из стальных оцинкованных лент, ";

        //оболочка
        if (encryptedValue.contains("Пнг") || encryptedValue.contains("Пвнг") ) obolochka = "с оболочкой из полимерных композиций, ";
        if (encryptedValue.contains("ХЛ")  )                                    obolochka = "с оболочкой из полимерных композиций, ";
        if (encryptedValue.contains("Тнг") || encryptedValue.contains("Твнг") ) obolochka = "с оболочкой из темопластичного эластомера, ";


        //цвет оболочки
        if (encryptedValue.contains("син")) cvetObolochki = "цвет внешней оболочки синий, ";
        if (encryptedValue.contains("оранж")) cvetObolochki = "цвет внешней оболочки оранжевый, ";
        if (encryptedValue.contains("желт")) cvetObolochki = "цвет внешней оболочки желтый, ";
        if (encryptedValue.contains("желтый с зеле")) cvetObolochki = "цвет внешней оболочки желтый с зеленой полосой, ";
        if (encryptedValue.contains("желто-зел")) cvetObolochki = "цвет внешней оболочки желтый с зеленой полосой, ";
        if (encryptedValue.contains("серый")) cvetObolochki = "цвет внешней оболочки серый, ";
        if (encryptedValue.contains("синий с красн")) cvetObolochki = "цвет внешней оболочки синий с красной полосой, ";
        if (encryptedValue.contains("черный с красн")) cvetObolochki = "цвет внешней оболочки черный с красной полосой, ";


        //жила однопроволочная
        if (encryptedValue.contains("ок")  ) gila = "токоведущие жилы медные однопроволочные класс 1 по ГОСТ 22483 ";

        //жила луженая
        if (encryptedValue.contains("0л") || encryptedValue.contains("5л") ) gilaLugenaya = "луженые, ";

        //скрутка
        if (encryptedValue.contains("х2х") || encryptedValue.contains("х(2х") ) skrutka = "токоведущие жилы скручены в пары, ";
        if (encryptedValue.contains("х3х") || encryptedValue.contains("х(3х") ) skrutka = "токоведущие жилы скручены в тройки, ";
        if (encryptedValue.contains("х4х") || encryptedValue.contains("х(4х") ) skrutka = "токоведущие жилы скручены в четверки, ";


        //водоблок
        if (encryptedValue.contains("внг")) vodoBlock = "с водоблокирующими элементами, ";


        //без заполнения
        if (encryptedValue.contains("без запол") || encryptedValue.contains("5л") ) zapolnenie = "без заполнения межжилных пространств методом экструзии, ";

        //пожарное исполнение
        if (encryptedValue.contains("нг(А)-LS")) pogIspoln = "исполнение нг(А)-LS по ГОСТ 31565, ";
        if (encryptedValue.contains("нг(А)-FR")) pogIspoln = "исполнение нг(А)-FR по ГОСТ 31565, ";
        if (encryptedValue.contains("нг(А)-FRLS")) pogIspoln = "исполнение нг(А)-FRLS по ГОСТ 31565, ";
        if (encryptedValue.contains("нг(А)-FRHF")) pogIspoln = "исполнение нг(А)-FRHF по ГОСТ 31565, ";
        if (encryptedValue.contains("нг(А)-HF")) pogIspoln = "исполнение нг(А)-HF по ГОСТ 31565, ";

        //ХЛ ЭХЛ
        if (encryptedValue.contains("-ХЛ")) temperatura = "в хладостойком исполнении, температура эксплуатации " +
                "от -65 градусов Цельсия до +80 градусов Цельсия, температура монтажа без подогрева -35 градусов Цельсия, ";
        if (encryptedValue.contains("-ЭХЛ")) temperatura = "в экстремальнохолодном исполнении, температура эксплуатации " +
                "от -70 градусов Цельсия до +80 градусов Цельсия, температура монтажа без подогрева -40 градусов Цельсия, ";
        //УФ М

        String subString = encryptedValue.substring((encryptedValue.indexOf("нг(А)")+5));  //обрезать до нг
        subString = subString.substring((subString.indexOf(" ")+1));  //обрезать до пробела
        String subString2 = subString.substring(0,(subString.indexOf(" ")+1)).replaceAll("[^0-9х,]", "");;  //обрезать до вторго пробела
        if (subString.contains("УФ")) UF = "стойкий к ультрафиолетовому излучению, ";
        if (subString.contains("М")) maslo = "стойкий к маслу и углеродным соединениям, ";
        if (subString.contains("Х")) chemic = "стойкий к химическим соединениям, ";

        //сроки
        if (encryptedValue.contains("HF")) srok = "cрок службы не менее 40 лет. Гарантийный срок эксплуатации 3 года. ";


        result = "Универсальный монтажный кабель, " +
                " на номинальное напряжение 660В переменного тока частотой до 1МГц или 1000В постоянного тока, " +
                gila +
                gilaLugenaya +
                skrutka +
                isolyazia +
                vodoBlock+
                zapolnenie +
                screenIndividual +
                screenObchiy +
                bronya +
                obolochka +
                cvetObolochki+

                pogIspoln +
                maslo+
                chemic+
                UF+
                temperatura +
                srok+
                subString2;


        // Replace this with your decryption algorithm
        // For example, you might use AES decryption, Base64 decoding, etc.
        // Here, I'm just returning the original value as a placeholder
        return result;
    }


}



