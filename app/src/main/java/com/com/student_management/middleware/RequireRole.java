package com.com.student_management.middleware;

import android.content.Context;
import android.widget.Toast;

import com.com.student_management.constants.Roles;

public class RequireRole {
    public static boolean checkRole(String role, Context context) {
        if (role.equals(Roles.EMPLOYEE.toString())) {
            Toast.makeText(context, "You don't have permission to access this page", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public static boolean checkManagerRole(String role, Context context) {
        if (role.equals(Roles.MANAGER.toString())) {
            Toast.makeText(context, "You don't have permission to access this page", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
