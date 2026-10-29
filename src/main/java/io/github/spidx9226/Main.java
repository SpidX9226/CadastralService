package io.github.spidx9226;

import java.util.List;

public class Main {
    public static List<String> _operations = List.of(
            "Граждане",
            "Заявки",
            "Поиск",
            "Фильтрация",
            "Статистика",
            "Экспорт данных",
            "Вывести таблицы базы данных",
            "Выход"
    );
    static void main() {
        printMenu(_operations);

        int op = -1;
        while (op != 0) {
            try {
                op = Integer.parseInt(IO.readln("\nВыберите действие: "));
            } catch (Exception _) {
                IO.println("Ошибка: введите номер желаемой операции целым числом.");
            }
        }
    }

    static void printMenu(List<String> operations) {
        IO.println("======================================== КАДАСТРОВАЯ СЛУЖБА ========================================");

        for (int i = 0; i <= operations.size(); ++i) {
            var operation = operations.get(i);

            if (operation.equalsIgnoreCase("выход")) {
                IO.println("0. Выход");
                break;
            }

            IO.println(String.format("%s. %s", i+1, operation));
        }
    }
}
