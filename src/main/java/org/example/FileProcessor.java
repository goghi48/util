package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileProcessor {

    public void processFilesWithoutStatistics(List<String> inputPaths, String resultPath, String prefix, boolean append) {
        List<String> integerLines = new ArrayList<>();
        List<String> floatLines = new ArrayList<>();
        List<String> stringLines = new ArrayList<>();

        for (String inputPath : inputPaths) {
            try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.matches("-?\\d+")) {
                        integerLines.add(line);
                    } else if (line.matches("-?\\d*\\.\\d+([eE][-+]?\\d+)?")) {
                        floatLines.add(line);
                    } else {
                        stringLines.add(line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Ошибка чтения файла " + inputPath + ": " + e.getMessage());
            }
        }

        if (!integerLines.isEmpty()) writeFile(resultPath + prefix + "integers.txt", integerLines, append);
        if (!floatLines.isEmpty()) writeFile(resultPath + prefix + "floats.txt", floatLines, append);
        if (!stringLines.isEmpty()) writeFile(resultPath + prefix + "strings.txt", stringLines, append);
    }

    public void processFilesWithBriefStatistics(List<String> inputPaths, String resultPath, String prefix, boolean append) {
        List<String> integerLines = new ArrayList<>();
        List<String> floatLines = new ArrayList<>();
        List<String> stringLines = new ArrayList<>();

        int integerCount = 0, floatCount = 0, stringCount = 0;

        for (String inputPath : inputPaths) {
            try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.matches("-?\\d+")) {
                        integerLines.add(line);
                        integerCount++;
                    } else if (line.matches("-?\\d*\\.\\d+([eE][-+]?\\d+)?")) {
                        floatLines.add(line);
                        floatCount++;
                    } else {
                        stringLines.add(line);
                        stringCount++;
                    }
                }
            } catch (IOException e) {
                System.err.println("Ошибка чтения файла " + inputPath + ": " + e.getMessage());
            }
        }

        System.out.println("Краткая статистика:");
        System.out.println("Количество целых чисел: " + integerCount);
        System.out.println("Количество вещественных чисел: " + floatCount);
        System.out.println("Количество строк: " + stringCount);

        if (!integerLines.isEmpty()) writeFile(resultPath + prefix + "integers.txt", integerLines, append);
        if (!floatLines.isEmpty()) writeFile(resultPath + prefix + "floats.txt", floatLines, append);
        if (!stringLines.isEmpty()) writeFile(resultPath + prefix + "strings.txt", stringLines, append);
    }

    public void processFilesWithFullStatistics(List<String> inputPaths, String resultPath, String prefix, boolean append) {
        List<String> integerLines = new ArrayList<>();
        List<String> floatLines = new ArrayList<>();
        List<String> stringLines = new ArrayList<>();

        int integerCount = 0, floatCount = 0, stringCount = 0;
        long minInt = Long.MAX_VALUE, maxInt = Long.MIN_VALUE, sumInt = 0;
        double minFloat = Double.MAX_VALUE, maxFloat = Double.MIN_VALUE, sumFloat = 0.0;
        int minStringLength = Integer.MAX_VALUE, maxStringLength = 0;

        for (String inputPath : inputPaths) {
            try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.matches("-?\\d+")) {
                        integerLines.add(line);
                        long value = Long.parseLong(line);
                        integerCount++;
                        sumInt += value;
                        if (value < minInt) minInt = value;
                        if (value > maxInt) maxInt = value;
                    } else if (line.matches("-?\\d*\\.\\d+([eE][-+]?\\d+)?")) {
                        floatLines.add(line);
                        double value = Double.parseDouble(line);
                        floatCount++;
                        sumFloat += value;
                        if (value < minFloat) minFloat = value;
                        if (value > maxFloat) maxFloat = value;
                    } else {
                        stringLines.add(line);
                        int length = line.length();
                        stringCount++;
                        if (length < minStringLength) minStringLength = length;
                        if (length > maxStringLength) maxStringLength = length;
                    }
                }
            } catch (IOException e) {
                System.err.println("Ошибка чтения файла " + inputPath + ": " + e.getMessage());
            }
        }

        System.out.println("Полная статистика:");
        System.out.println("Количество целых чисел: " + integerCount);
        System.out.println("Количество вещественных чисел: " + floatCount);
        System.out.println("Количество строк: " + stringCount);
        if (integerCount > 0) {
            double avgInt = (double) sumInt / integerCount;
            System.out.println("Целые числа: минимум = " + minInt + ", максимум = " + maxInt + ", сумма = " + sumInt + ", среднее = " + avgInt);
        }
        if (floatCount > 0) {
            double avgFloat = sumFloat / floatCount;
            System.out.println("Вещественные числа: минимум = " + minFloat + ", максимум = " + maxFloat + ", сумма = " + sumFloat + ", среднее = " + avgFloat);
        }
        if (stringCount > 0) {
            System.out.println("Строки: длина самой короткой строки = " + minStringLength + ", длина самой длинной строки = " + maxStringLength);
        }

        if (!integerLines.isEmpty()) writeFile(resultPath + prefix + "integers.txt", integerLines, append);
        if (!floatLines.isEmpty()) writeFile(resultPath + prefix + "floats.txt", floatLines, append);
        if (!stringLines.isEmpty()) writeFile(resultPath + prefix + "strings.txt", stringLines, append);
    }

    private void writeFile(String path, List<String> lines, boolean append) {
        try {
            Path filePath = Paths.get(path);
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            try (FileWriter writer = new FileWriter(path, append)) {
                for (String line : lines) {
                    writer.write(line + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка записи файла " + path + ": " + e.getMessage());
        }
    }
}
