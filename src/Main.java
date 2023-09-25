import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String line = br.readLine().trim().replaceAll(" ", "");
            System.out.println(calc(line));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String calc(String input) throws Exception {

        if(input.length() < 3) {
            throw new Exception("Строка не является математической операцией");
        }

        String result;
        String[] operator;
        int x, y;
        String[] arg = input.replaceAll("[+*/()-]+"," ").split(" ");
        boolean RomanOut = false;

        if(isRoman(arg[0]) == isRoman(arg[1])) {

            if(isRoman(arg[0])) {
                operator = input.replaceAll("[A-z()]+","").split("");
                x = romanToArabic(arg[0]);
                y = romanToArabic(arg[1]);
                RomanOut = true;
            } else {
                operator = input.replaceAll("[0-9()]+","").split("");
                x = Integer.parseInt(arg[0]);
                y = Integer.parseInt(arg[1]);
            }
            
            if(arg.length > 2 || operator.length > 1) {
                throw new Exception("Формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
            }

            if(operator[0].equals("-") && x < y) {
                throw new Exception("В римской системе нет отрицательных чисел"); //т.к. в римской системе нет отрицательных чисел
            }

        } else {
            throw new Exception("Используются одновременно разные системы счисления");
        }

        if(x < 1 || x > 10 || y < 1 || y > 10) {
            throw new Exception("На вход принимаются только числа от 1 до 10, включительно.");
        }
        result = switch (operator[0]) {
            case "+" -> String.valueOf(x + y);
            case "-" -> String.valueOf(x - y);
            case "*" -> String.valueOf(x * y);
            case "/" -> String.valueOf(x / y);
            default -> throw new Exception("Ошибка выбора знака операции");
        };
        if(!RomanOut) {
            return result;
        } else {
            return arabicToRoman(Integer.parseInt(result));
        }
    }
    public static boolean isRoman(String str) {
        return str.contains("I") || str.contains("V") || str.contains("X") ||
                str.contains("L") || str.contains("C");
    }
    public static String arabicToRoman(int number) {

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;
        StringBuilder sb = new StringBuilder();

        while ((number > 0) && (i < romanNumerals.size())) {
            RomanNumeral currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }

        return sb.toString();
    }

    enum RomanNumeral {
        I(1), IV(4), V(5), IX(9), X(10),
        XL(40), L(50), XC(90), C(100);

        private final int value;

        RomanNumeral(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static List<RomanNumeral> getReverseSortedValues() {
            return Arrays.stream(values())
                    .sorted(Comparator.comparing((RomanNumeral e) -> e.value).reversed())
                    .collect(Collectors.toList());
        }
    }

    public static int romanToArabic(String input) {
        String romanNumeral = input.toUpperCase();
        int result = 0;

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;

        while ((!romanNumeral.isEmpty()) && (i < romanNumerals.size())) {
            RomanNumeral symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }
        return result;
    }
}