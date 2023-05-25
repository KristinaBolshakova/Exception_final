/*
Напишите приложение, которое будет запрашивать у пользователя следующие данные в произвольном порядке, разделенные пробелом:
Фамилия Имя Отчество датарождения номертелефона пол
Форматы данных:
фамилия, имя, отчество - строки
дата_рождения - строка формата dd.mm.yyyy
номер_телефона - целое беззнаковое число без форматирования
пол - символ латиницей f или m.
Приложение должно проверить введенные данные по количеству.
Если количество не совпадает с требуемым, вернуть код ошибки, обработать его и показать пользователю сообщение,
что он ввел меньше и больше данных, чем требуется.
Приложение должно попытаться распарсить полученные значения и выделить из них требуемые параметры.
Если форматы данных не совпадают, нужно бросить исключение, соответствующее типу проблемы.
Можно использовать встроенные типы java и создать свои.
Исключение должно быть корректно обработано, пользователю выведено сообщение с информацией, что именно неверно.
Если всё введено и обработано верно, должен создаться файл с названием, равным фамилии, в него в одну строку должны записаться полученные данные, вида
<Фамилия><Имя><Отчество><датарождения> <номертелефона><пол>
Однофамильцы должны записаться в один и тот же файл, в отдельные строки.
Не забудьте закрыть соединение с файлом.
При возникновении проблемы с чтением-записью в файл, исключение должно быть корректно обработано, пользователь должен увидеть стектрейс ошибки.
 */


import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите данные, разделяя пробелом:\nФамилию, Имя, Отчество, дату рождения, номер телефона и пол.");
        Scanner input = new Scanner(System.in);
        String[] data = input.nextLine().split(" ");

        if (data.length < 6) {
            throw new ArrayIndexOutOfBoundsException("Вы ввели меньше данных, чем требуется");
        } else if (data.length > 6) {
            throw new ArrayIndexOutOfBoundsException("Вы ввели больше данных, чем требуется");
        } else {

            StringBuilder dataString = new StringBuilder();


            String surname = data[0];
            String name = data[1];
            String patronymic = data[2];
            String birthday = data[3];
            String phone = data[4];
            String gender = data[5];

            Surname(surname, dataString);
            Name(name, dataString);
            Patronymic(patronymic, dataString);
            Birthday(birthday, dataString);
            Phone(phone, dataString);
            Gender(gender, dataString);
            WriteFile(surname, dataString);

        }
    }

    private static void Surname(String sname, StringBuilder ds) {
        if (sname.chars().allMatch(Character::isLetter)) {
            ds.append("<").append(sname).append(">");
        } else {
            throw new RuntimeException("Неверный ввод, введите Фамилию в буквенном формате");
        }
    }

    private static void Name(String n, StringBuilder ds) {
        if (n.chars().allMatch(Character::isLetter)) {
            ds.append("<").append(n).append(">");
        } else {
            throw new RuntimeException("Неверный ввод, введите Имя в буквенном формате");
        }
    }

    public static void Patronymic(String patr, StringBuilder ds) {
        if (patr.chars().allMatch(Character::isLetter)) {
            ds.append("<").append(patr).append(">");
        } else {
            throw new RuntimeException("Неверный ввод, введите Отчество в буквенном формате");
        }
    }

    public static void Birthday(String birth, StringBuilder ds) {
        if (birth.length() <= 10 && birth.length() > 1) {

            String[] birthDate = birth.split("\\.");

            try {
                int day = Integer.parseInt(birthDate[0]);
                int month = Integer.parseInt(birthDate[1]);
                int year = Integer.parseInt(birthDate[2]);

                if ((day > 0 && day <= 31)) {
                    ds.append("<").append(day).append(".");
                }
                if (month > 0 && month <= 12) {
                    ds.append(birthDate[1]).append(".");
                }
                if (year > 1900) {
                    ds.append(year).append(">");
                } else {
                    throw new RuntimeException("Неверный формат даты.");
                }

            } catch (NumberFormatException e) {
                throw new RuntimeException("Введены не целочисленные значения");
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new RuntimeException("Неверный формат даты. Необходимо ввести данные в формате dd.mm.yyyy");
            }


        }
    }

    public static void Phone(String ph, StringBuilder ds) {
        if (ph.length() <= 11) {
            try {
                long phoneNumber = Long.parseLong(ph);
                ds.append("<").append(phoneNumber).append(">");
            } catch (Exception e) {
                throw new RuntimeException("Введены не целочисленные значения.");
            }
        } else {
            throw new RuntimeException("Введено недостаточное количество элементов.");
        }
    }

    public static void Gender(String gend, StringBuilder ds) {
        if (gend.equals("f") || gend.equals("m")) {
            ds.append("<").append(gend).append(">").append("\n");
        } else {
            throw new RuntimeException("Неверный ввод, введите пол в формате f/m");
        }
    }

    public static void WriteFile(String sur, StringBuilder ds) {
        File file = new File(sur + ".txt");
        if (file.exists()) {
            System.out.println("Файл с такой фамилией существует.");
            try {
                PrintWriter pw = new PrintWriter(new FileWriter(file, true));
                pw.write(String.valueOf(ds));
                pw.close();
            } catch (Exception e) {
                throw new RuntimeException("Не удалось записать даные в файл.");

            }
        } else {
            try (FileWriter fw = new FileWriter(file)) {
                fw.write(String.valueOf(ds));
                fw.close();
                System.out.println("Файл с фамилией " + file + " создан");

            } catch (Exception e) {
                throw new RuntimeException("Файл не создан");
            }
        }
    }
}
