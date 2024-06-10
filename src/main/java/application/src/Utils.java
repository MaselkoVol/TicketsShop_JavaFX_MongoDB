package application.src;

public class Utils {
    private Utils () {};
    public static boolean validatePhoneNumber(String phoneNumber) {
        // Умова 1: Починається з "+380"
        if (!phoneNumber.startsWith("+380")) {
            return false;
        }

        // Умова 2: Містить лише цифри окрім першого символа "+"
        for (int i = 1; i < phoneNumber.length(); i++) {
            if (!Character.isDigit(phoneNumber.charAt(i))) {
                return false;
            }
        }

        // Умова 3: Має довжину 13 символів
        if (phoneNumber.length() != 13) {
            return false;
        }

        // Якщо всі умови виконані, номер телефону є правильним
        return true;
    }


    public static boolean isValidPrice(String priceString) {
        if (priceString == null || priceString.isEmpty()) {
            return false;
        }

        // Перевіряємо, чи всі символи є цифрами або комою
        boolean hasComma = false;
        for (char c : priceString.toCharArray()) {
            if (Character.isDigit(c)) {
                continue;
            } else if (c == '.' && !hasComma) {
                hasComma = true;
            } else {
                return false;
            }
        }

        // Перевіряємо, чи є цифри перед та після коми та чи є дві цифри після коми
        if (hasComma) {
            int commaIndex = priceString.indexOf('.');
            if (commaIndex == 0 || commaIndex == priceString.length() - 1) {
                return false; // Кома не може бути першим або останнім символом
            }
            if (priceString.length() - commaIndex != 3) {
                return false;
            }
        }

        return true;
    }
}
