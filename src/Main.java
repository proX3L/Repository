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

            if(isRoman(line)) {
                String[] arabic = line.replaceAll("[+*/()-]+"," ").split(" ");
                String[] operator = line.replaceAll("[A-z()]+","").split("");

                if(line.equals(arabic[0])) {
                    throw new Exception("Строка не является математической операцией");
                }

                if(arabic.length > 2 && operator.length > 1) {
                    throw new Exception("Формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
                }

                if(!isRoman(arabic[0]) || !isRoman(arabic[1])) {
                    throw new Exception("Используются одновременно разные системы счисления");
                }

                int result = Calculator(romanToArabic(arabic[0]), romanToArabic(arabic[1]), operator[0]);

                if(result <= 0) {
                    throw new Exception("В римской системе нет отрицательных чисел"); //т.к. в римской системе нет отрицательных чисел
                }

                System.out.println(arabicToRoman(result));
            } else {
                String[] operator = line.replaceAll("[0-9()]+","").split("");
                String[] arg = line.replaceAll("[+*/()-]+"," ").split(" ");

                if(line.equals(arg[0])) {
                    throw new Exception("Строка не является математической операцией");
                }

                if(arg.length > 2 && operator.length > 1) {
                    throw new Exception("Формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
                }

                if(line.matches("^[a-zA-Z]*$")) {
                    throw new Exception("Используются одновременно разные системы счисления");
                }

                System.out.println(Calculator(Integer.parseInt(arg[0]), Integer.parseInt(arg[1]), operator[0]));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
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

    public static int Calculator(int arg1, int arg2, String operator) throws Exception {

        if(arg1 < 1 || arg1 > 10 || arg2 < 1 || arg2 > 10) {
            throw new Exception("На вход принимаются только числа от 1 до 10, включительно.");
        }

        return switch (operator) {
            case "+" -> arg1 + arg2;
            case "-" -> arg1 - arg2;
            case "*" -> arg1 * arg2;
            case "/" -> arg1 / arg2;
            default -> 0;
        };
    }
}