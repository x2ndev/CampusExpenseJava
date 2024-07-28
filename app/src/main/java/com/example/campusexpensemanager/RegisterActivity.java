package com.example.campusexpensemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {
    EditText reg_edt_fName, reg_edt_email, reg_edt_password, reg_edt_password2;
    TextView tv_user_data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reg_edt_fName = findViewById(R.id.reg_edt_fName);
        reg_edt_email = findViewById(R.id.reg_edt_email);
        reg_edt_password = findViewById(R.id.reg_edt_password);
        reg_edt_password2 = findViewById(R.id.reg_edt_password2);


        tv_user_data = findViewById(R.id.tv_user_data);

    }

    public void createAccountBtn(View view) {
        String name = reg_edt_fName.getText().toString();
        String email = reg_edt_email.getText().toString();
        String password = reg_edt_password.getText().toString();
        String password2 = reg_edt_password2.getText().toString();

        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !password2.isEmpty()) {
            if (!password.equals(password2)) {
                Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
                return;
            }
            createAccount(name, email, password);
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void createAccount(String name, String email, String password) {
        File file = new File(getFilesDir(), "userData.txt");

        if (isEmailExist(file, email)) {
            Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            // Tạo ID mới
            int newId = generateNewId(file);

            // Tạo chuỗi thông tin tài khoản theo định dạng
            String userData = newId + "|" + name + "|" + email + "|" + password;

            // Ghi thông tin tài khoản vào tệp
            bw.write(userData);
            bw.newLine();

            Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create account", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isEmailExist(File file, String email) {
        if (!file.exists()) {
            return false; // Nếu tệp không tồn tại, email không tồn tại
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split("\\|");
                if (userData.length > 2) {
                    String existingEmail = userData[2];
                    if (existingEmail.equals(email)) {
                        return true; // Email đã tồn tại
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false; // Email không tồn tại
    }

    private int generateNewId(File file) {
        int maxId = 0;

        if (!file.exists()) {
            return 1; // Nếu tệp không tồn tại, ID đầu tiên là 1
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split("\\|");
                if (userData.length > 0) {
                    int id = Integer.parseInt(userData[0]);
                    if (id > maxId) {
                        maxId = id;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return maxId + 1; // ID mới là ID lớn nhất + 1
    }





    public void showContentBtn(View view) {
        File file = new File(getFilesDir(), "userData.txt");

        if (!file.exists()) {
            tv_user_data.setText("No user data found");
            return;
        }

        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            tv_user_data.setText(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
            tv_user_data.setText("Failed to read user data");
        }
    }

    public void deleteDataBtn(View view) {
        File file = new File(getFilesDir(), "userData.txt");

        if (file.exists()) {
            if (file.delete()) {
                Toast.makeText(this, "User data deleted successfully", Toast.LENGTH_SHORT).show();
                tv_user_data.setText("No user data found");
            } else {
                Toast.makeText(this, "Failed to delete user data", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No user data found", Toast.LENGTH_SHORT).show();
        }
    }


}
