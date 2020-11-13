package com.example.calculatorapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText number1, number2;
    Button add, subtract, multiply, divide, calculate;
    TextView result, choice;
    float calculation;
    int input1, input2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (TextView)findViewById(R.id.result);
        choice = (TextView)findViewById(R.id.choice);
        number1 = (EditText)findViewById(R.id.number1);
        number2 = (EditText)findViewById(R.id.number2);

        add = (Button)findViewById(R.id.add);
        subtract = (Button)findViewById(R.id.subtract);
        multiply = (Button)findViewById(R.id.multiply);
        divide = (Button)findViewById(R.id.divide);
        calculate = (Button)findViewById(R.id.calculate);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (number1.getText().toString().isEmpty() || number2.getText().toString().isEmpty()) {
                    // Create the object of
                    // AlertDialog Builder class
                    AlertDialog.Builder builder
                            = new AlertDialog
                            .Builder(MainActivity.this);

                    // Set the message show for the Alert time
                    builder.setMessage("You must first input two values.");

                    // Set Alert Title
                    builder.setTitle("Alert!");

                    // Set Cancelable false
                    // for when the user clicks on the outside
                    // the Dialog Box then it will remain show
                    builder.setCancelable(false);

                    // Set the positive button with yes name
                    // OnClickListener method is use of
                    // DialogInterface interface.

                    builder
                            .setPositiveButton(
                                    "Close",
                                    new DialogInterface
                                            .OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {

                                            // When the user click yes button
                                            // then app will close
                                            dialog.cancel();
                                        }
                                    });

                    // Create the Alert dialog
                    AlertDialog alertDialog = builder.create();

                    // Show the Alert Dialog box
                    alertDialog.show();
                } else {
                    input1 = Integer.parseInt(number1.getText().toString());
                    input2 = Integer.parseInt(number2.getText().toString());
                    calculation = input1 + input2;
                    choice.setText(String.valueOf("You have selected to ADD the values"));
                }
            }
        });

        subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (number1.getText().toString().isEmpty() || number2.getText().toString().isEmpty()) {
                    // Create the object of
                    // AlertDialog Builder class
                    AlertDialog.Builder builder
                            = new AlertDialog
                            .Builder(MainActivity.this);

                    // Set the message show for the Alert time
                    builder.setMessage("You must first input two values.");

                    // Set Alert Title
                    builder.setTitle("Alert!");

                    // Set Cancelable false
                    // for when the user clicks on the outside
                    // the Dialog Box then it will remain show
                    builder.setCancelable(false);

                    // Set the positive button with yes name
                    // OnClickListener method is use of
                    // DialogInterface interface.

                    builder
                            .setPositiveButton(
                                    "Close",
                                    new DialogInterface
                                            .OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {

                                            // When the user click yes button
                                            // then app will close
                                            dialog.cancel();
                                        }
                                    });

                    // Create the Alert dialog
                    AlertDialog alertDialog = builder.create();

                    // Show the Alert Dialog box
                    alertDialog.show();
                } else {
                    input1 = Integer.parseInt(number1.getText().toString());
                    input2 = Integer.parseInt(number2.getText().toString());
                    calculation = input1 - input2;
                    choice.setText(String.valueOf("You have selected to SUBTRACT the values"));
                }
            }
        });

        multiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (number1.getText().toString().isEmpty() || number2.getText().toString().isEmpty()) {
                    // Create the object of
                    // AlertDialog Builder class
                    AlertDialog.Builder builder
                            = new AlertDialog
                            .Builder(MainActivity.this);

                    // Set the message show for the Alert time
                    builder.setMessage("You must first input two values.");

                    // Set Alert Title
                    builder.setTitle("Alert!");

                    // Set Cancelable false
                    // for when the user clicks on the outside
                    // the Dialog Box then it will remain show
                    builder.setCancelable(false);

                    // Set the positive button with yes name
                    // OnClickListener method is use of
                    // DialogInterface interface.

                    builder
                            .setPositiveButton(
                                    "Close",
                                    new DialogInterface
                                            .OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {

                                            // When the user click yes button
                                            // then app will close
                                            dialog.cancel();
                                        }
                                    });

                    // Create the Alert dialog
                    AlertDialog alertDialog = builder.create();

                    // Show the Alert Dialog box
                    alertDialog.show();
                } else {
                    input1 = Integer.parseInt(number1.getText().toString());
                    input2 = Integer.parseInt(number2.getText().toString());
                    calculation = input1 * input2;
                    choice.setText(String.valueOf("You have selected to MULTIPLY the values"));
                }
            }
        });

        divide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (number1.getText().toString().isEmpty() || number2.getText().toString().isEmpty()) {
                    // Create the object of
                    // AlertDialog Builder class
                    AlertDialog.Builder builder
                            = new AlertDialog
                            .Builder(MainActivity.this);

                    // Set the message show for the Alert time
                    builder.setMessage("You must first input two values.");

                    // Set Alert Title
                    builder.setTitle("Alert!");

                    // Set Cancelable false
                    // for when the user clicks on the outside
                    // the Dialog Box then it will remain show
                    builder.setCancelable(false);

                    // Set the positive button with yes name
                    // OnClickListener method is use of
                    // DialogInterface interface.

                    builder
                            .setPositiveButton(
                                    "Close",
                                    new DialogInterface
                                            .OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {

                                            // When the user click yes button
                                            // then app will close
                                            dialog.cancel();
                                        }
                                    });

                    // Create the Alert dialog
                    AlertDialog alertDialog = builder.create();

                    // Show the Alert Dialog box
                    alertDialog.show();
                } else {
                    input1 = Integer.parseInt(number1.getText().toString());
                    input2 = Integer.parseInt(number2.getText().toString());
                    calculation = input1 / input2;
                    choice.setText(String.valueOf("You have selected to DIVIDE the values"));
                }
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setText(String.valueOf(calculation));
            }
        });
    }
}