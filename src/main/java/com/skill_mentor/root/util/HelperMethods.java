package com.skill_mentor.root.util;

import com.skill_mentor.root.entity.UserEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class HelperMethods {

    public static UserEntity getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserEntity user) {
            return user;
        }
        throw new RuntimeException("Invalid user in security context");
    }

    public static Map<String, Object> extractDobAndGenderFromNic(String nic) {
        int[] dates = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        boolean isOld = true;

        int nicLength = nic.length();
        int days = (nicLength == 10) ? Integer.parseInt(nic.substring(2, 5))
                : Integer.parseInt(nic.substring(4, 7));

        boolean checkV = nicLength == 10 && (nic.toLowerCase().endsWith("v"));
        boolean checkPrefix = nicLength == 12 && (nic.startsWith("19") || nic.startsWith("20"));

        if (nicLength == 10 && checkV && (days <= 366 || (days >= 501 && days <= 866))) {
            isOld = true;
        } else if (nicLength == 12 && checkPrefix && (days <= 366 || (days >= 501 && days <= 866))) {
            isOld = false;
        } else {
            throw new IllegalArgumentException("Invalid NIC format");
        }

        int year = isOld ? 1900 + Integer.parseInt(nic.substring(0, 2))
                : Integer.parseInt(nic.substring(0, 4));

        String gender = "Male";
        if (days > 500) {
            days -= 500;
            gender = "Female";
        }

        int total = 0;
        int month = 0, day = 0;

        for (int i = 0; i < dates.length; i++) {
            total += dates[i];
            if (days <= total) {
                month = i + 1;
                day = days - (total - dates[i]);
                break;
            }
        }

        LocalDate dob = LocalDate.of(year, month, day);
        Map<String, Object> result = new HashMap<>();
        result.put("dob", dob);
        result.put("gender", gender);
        return result;
    }
}
