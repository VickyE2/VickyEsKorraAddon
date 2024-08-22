package com.vickyeka.vickyeskorraaddon.utilities;
import net.md_5.bungee.api.ChatColor;


public class Hexcolor {
    public static String getHexGradientPrefix(String text, String colorString) {
        String[] colorArray = colorString.split(",");
        StringBuilder gradientText = new StringBuilder();
        boolean inBrackets = false;
        int bracketCount = 0;

        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);

            if (currentChar == '[') {
                inBrackets = true;
                bracketCount++;
                gradientText.append(ChatColor.WHITE); // Change color to white
            } else if (currentChar == ']') {
                inBrackets = false;
                bracketCount--;
                gradientText.append(ChatColor.RESET); // Reset color
            } else {
                if (!inBrackets) {
                    String colorHex = colorArray[i % colorArray.length];
                    gradientText.append(ChatColor.of(colorHex));
                }
                gradientText.append(currentChar);
            }
        }

        // If there are unclosed brackets, reset to default color
        if (bracketCount > 0) {
            gradientText.append(ChatColor.RESET);
        }

        return gradientText.toString();
    }
}
