package com.example.campusexpensemanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.edt_email);
        password = findViewById(R.id.edt_password);
    }





    private void createTestAccount(String id, String name, String email, String password) {
        File file = new File(getFilesDir(), "userData.txt");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            // Tạo chuỗi thông tin tài khoản theo định dạng
            String userData = id + "|" + name + "|" + email + "|" + password;

            // Ghi thông tin tài khoản vào tệp
            bw.write(userData);
            bw.newLine();

            Toast.makeText(this, "Test account created successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create test account", Toast.LENGTH_SHORT).show();
        }
    }

    public void Test(View view){
            // Tạo tài khoản thử nghiệm với thông tin cố định
            String id = "1";
            String name = "Nguyen Xuan Nam";
            String email = "nam@gmail.com";
            String password = "nam";

            createTestAccount(id, name, email, password);
    }

    public void Login(View view) {
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();
        if (emailStr.isEmpty() || passwordStr.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        } else {
            // Proceed with login logic
            CheckLogin(emailStr, passwordStr);
        }
    }

    private void CheckLogin(String getEmail, String getPassword) {
        File file = new File(getFilesDir(), "userData.txt");

        if (!file.exists()) {
            Toast.makeText(this, "No user data found", Toast.LENGTH_SHORT).show();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Dữ liệu định dạng: ID|Nguyen Van A|nguyenvana@gmail.com|nguyenvanapasssword
                String[] userData = line.split("\\|");
                if (userData.length == 4) {
                    String id = userData[0];
                    String name = userData[1];
                    String email = userData[2];
                    String password = userData[3];

                    if (email.equals(getEmail) && password.equals(getPassword)) {
                        Toast.makeText(this, "Login successful for user: " + name, Toast.LENGTH_SHORT).show();

                        // Lưu thông tin đăng nhập vào SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("USER_ID", id);
                        editor.putString("USER_NAME", name);
                        editor.putString("USER_MAIL", email);
                        editor.apply();

                        Intent intent = new Intent(this, HomeActivity.class);
                        intent.putExtra("USER_ID", id);
                        intent.putExtra("USER_NAME", name);
                        intent.putExtra("USER_MAIL", email);
                        startActivity(intent);
                        finish(); // Đóng Activity hiện tại

                        return;
                    }
                }
            }
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}