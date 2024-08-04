package org.example;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String prefix = "", resultPath = "";
        Boolean append = false;
        StatisticMode statisticMode = StatisticMode.NO;
        List<String> inputPaths = new ArrayList<>();

        try {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-p":
                        if (i + 1 < args.length) prefix = args[++i];
                        else throw new IllegalArgumentException("Параметр для опции -p не указан.");
                        break;
                    case "-o":
                        if (i + 1 < args.length) resultPath = args[++i] + "\\";
                        else throw new IllegalArgumentException("Параметр для опции -o не указан.");
                        break;
                    case "-a":
                        append = true;
                        break;
                    case "-s":
                        statisticMode = StatisticMode.BRIEF;
                        break;
                    case "-f":
                        statisticMode = StatisticMode.FULL;
                        break;
                    default:
                        inputPaths.add(args[i]);
                        break;
                }
            }

            if (inputPaths.isEmpty()) throw new IllegalArgumentException("Не указаны входные файлы.");
            FileProcessor fileProcessor = new FileProcessor();

            switch (statisticMode) {
                case NO:
                    fileProcessor.processFilesWithoutStatistics(inputPaths, resultPath, prefix, append);
                    break;
                case BRIEF:
                    fileProcessor.processFilesWithBriefStatistics(inputPaths, resultPath, prefix, append);
                    break;
                case FULL:
                    fileProcessor.processFilesWithFullStatistics(inputPaths, resultPath, prefix, append);
                    break;
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка аргументов командной строки: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла ошибка: " + e.getMessage());
        }
    }
}
