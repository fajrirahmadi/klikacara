package co.id.klikacara.base.utils.stringhelper;

import android.support.annotation.NonNull;
import android.util.Patterns;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

/**
 * Created by user on 5/30/2017.
 */

public class StringHelper {

    public static String getStringBuilderToString(String... items) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : items) {
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }

    public static String getStringBuilderToStringFromList(List<String> list) {
        return getStringBuilderToStringFromList(list, ", ");
    }

    public static String getStringBuilderToStringFromList(char[] list) {
        return getStringBuilderToStringFromList(list, ", ");
    }

    @NonNull
    public static String getStringBuilderToStringFromList(List<String> list, String divider) {
        StringBuilder stringBuilder = new StringBuilder();
        int i = list.size() - 1;
        for (int x = 0; x < list.size(); x++) {
            stringBuilder.append(list.get(x));
            if (x != i) {
                stringBuilder.append(divider);
            }
        }
        return stringBuilder.toString();
    }

    public static String getStringBuilderToStringFromList(char[] list, String divider) {
        StringBuilder stringBuilder = new StringBuilder();
        int i = list.length - 1;
        for (int x = 0; x < list.length; x++) {
            stringBuilder.append(list[x]);
            if (x != i) {
                stringBuilder.append(divider);
            }
        }
        return stringBuilder.toString();
    }

    public static String getSimplePriceFormatter(Double price) {
        return String.format("%,.0f", price).replace(",", ".");
    }

    public static String getSimplePriceFormatter(BigDecimal price) {
        return String.format("%,.0f", price.doubleValue()).replace(",", ".");
    }

    public static String getSimplePriceFormatterInRp(Double price) {
        return getStringBuilderToString("Rp. ", getSimplePriceFormatter(price));
    }

    public static String getPriceInRp(Double price) {
        return price != null ?
                getStringBuilderToString("Rp ", getSimplePriceFormatter(price)) : "";
    }

    public static String getPriceInRp(BigDecimal price) {
        return price != null ?
                getStringBuilderToString("Rp ", getSimplePriceFormatter(price.doubleValue())) : "";
    }

    public static String getPriceInRp(Long price) {
        return price != null ?
                getStringBuilderToString("Rp ", getSimplePriceFormatter(price.doubleValue())) : "";
    }

    public static String getPriceInRp(String price) {
        return StringUtils.isNotBlank(price) ?
                price.contains("Rp") ? price : getStringBuilderToString("Rp ",
                        getSimplePriceFormatter(Double.valueOf(removeDotFromFormatedValue(price)))) : "";
    }

    public static String removeDotFromFormatedValue(String price) {
        return price.replace(".", "");
    }

    public static String removeRpFromValue(String price) {
        return removeDotFromFormatedValue(price).replace("Rp ", "");
    }


    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    public static String[] splitString(String divider, String value) {
        return value.split(divider);
    }

    public static boolean isUrl(String url) {
        return StringUtils.isNotBlank(url) && (url.contains("http://") || url.contains("https://"));
    }

    public static boolean isEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPhone(String phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }

    public static String getFormatterToLongDigitValue(String price) {
        DecimalFormat formatter = new DecimalFormat("#,###,###,###");
        return formatter.format(new BigDecimal(price)).replace(",", "")
                .replace(".", "");
    }

    public static String getFormatterToLongDigitValue(BigDecimal price) {
        DecimalFormat formatter = new DecimalFormat("#,###,###,###");
        return price != null ? formatter.format(price).replace(",", "")
                .replace(".", "") : null;
    }

    public static String removeTrailingZeros(Double value) {
        if (value != null) {
            DecimalFormat df = new DecimalFormat("###.#");
            return df.format(value);
        } else {
            return "";
        }
    }

    public static String removeTrailingZerosWithTwoDigitsDecimalAndRoundingFloor(Double value) {
        if (value != null) {
            DecimalFormat df = new DecimalFormat("###.##");
            value = BigDecimal.valueOf(value).setScale(2, RoundingMode.FLOOR).doubleValue();
            return convertCommaToDot(df.format(value));
        } else {
            return "";
        }
    }

    public static String removeTrailingZerosWithTwoDigitsDecimalAndRoundingFloorAndCommaDecimalSeparator(Double value) {
        if (value != null) {
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
            otherSymbols.setDecimalSeparator(',');
            otherSymbols.setGroupingSeparator('.');
            DecimalFormat df = new DecimalFormat("###.##", otherSymbols);
            value = BigDecimal.valueOf(value).setScale(2, RoundingMode.FLOOR).doubleValue();
            return df.format(value);
        } else {
            return "";
        }
    }

    public static String getDecimalFormatter(String price) {
        DecimalFormat formatter = new DecimalFormat("#,###,###,###");
        return formatter.format(new BigDecimal(price))
                .replace(",", ".");
    }

    public static String getDecimalWithCommasFormatter(String price) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#.00");
        return formatter.format(new BigDecimal(price));
    }

    public static String convertCommaToDot(String value) {
        return value.replace(",", ".");
    }

    public static String getContactFormated(String kontak) {
        String newKontak = kontak.replaceAll("[^\\d+]", "");
        return newKontak.contains("+62") ? newKontak :
                (newKontak.substring(0, 1).equals("0") ?
                        newKontak.replaceFirst(newKontak.substring(0, 1), "+62") :
                        getStringBuilderToString("+62", newKontak));
    }

    public static String getContactSent(String kontak) {
        return kontak.replace("+62", "0");
    }
}