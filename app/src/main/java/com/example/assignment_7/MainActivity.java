package com.example.assignment_7;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;

import android.view.View;
import android.view.View.OnClickListener;

import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.AdapterView.OnItemSelectedListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;




public class MainActivity extends AppCompatActivity implements OnItemSelectedListener {


    EditText FirstNameEditText = null, LastNameEditText, PhoneNumberEditText, EducationEditText, HobbiesEditText;
    TextView summaryTextView = null, searchTextView = null;
    LayoutParams viewLayoutParams = null;
    Button saveInternalButton, saveUIButton, saveExternalButton, loadInternalButton, loadExternalButton, AddButton;
    Spinner colorPicker;
    // Here we create the layout
    LinearLayout linearLayout;
    AutoCompleteTextView SearchField;
    ArrayList<Contact> phoneCatalog = new ArrayList<Contact>();
    ArrayList<String> data = new ArrayList<String>();
    ArrayList<String> colors = new ArrayList<String>();
    ArrayAdapter<String> colorAdapter;

    ArrayAdapter<String> arrayAdapter;

    private String selectedColor;
    private int textSize;
    private String prefsFileName;
    private SeekBar seekBar;
    private SharedPreferences sharedPreferences;
    private static final String FONT_SIZE = "font_size";
    private static final String TEXT_COLOR = "text_color";

    //THIS IS FOR FILE SAVING

    private static final int BLOCK_SIZE = 128;
    //Here we define file path on the internal memory
    String path;
    //Here we define full file name with its path
    String FileName = "phoneData.txt";
    File file;

    String sdCardPath, destPath;
    String dirName="Assignment_7_data";
    // Here we define full file name with its path

    File sdCard, destPathFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Initial something for writting to file bruhhhhhhhhhhhhhhhhhhhh

        path = getFilesDir().getAbsolutePath() + "/";

        sdCard= Environment.getExternalStorageDirectory();
        sdCardPath=sdCard.getAbsolutePath();

//        destPathFile=   new File(getApplicationContext().getExternalFilesDir(
//                Environment.DIRECTORY_DOCUMENTS), dirName);
        destPathFile=   getApplicationContext().getExternalFilesDir(dirName);

        if(!destPathFile.exists()) {
            //Here we create the directory on the SD card
            destPathFile.mkdirs();
        }
        Toast.makeText(getApplicationContext(),
                destPathFile.getAbsolutePath() + " exist? " + destPathFile.exists() , Toast.LENGTH_LONG)
                .show();

        // initial Some data

        Contact newContact1 = new Contact("Nguyen", "Bui", "0469540115", "Bachelor", "dálkdhasfkljsaf");
        Contact newContact2 = new Contact("Hyeok", "Lee", "2013151620", "HighSchool", "Win Worlds");
        Contact newContact3 = new Contact("abc", "def", "0123456789", "Master", "abcdefghijklmnobq");
        Contact newContact4 = new Contact("Luan", "Hoang", "20132020", "Bachelor", "Pelu");

        phoneCatalog.add(newContact1);
        phoneCatalog.add(newContact2);
        phoneCatalog.add(newContact3);
        phoneCatalog.add(newContact4);

        // Here we define parameters for views
        viewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        viewLayoutParams.leftMargin = 40;
        viewLayoutParams.rightMargin = 40;
        viewLayoutParams.topMargin = 10;
        viewLayoutParams.bottomMargin = 10;
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        //Initial Colors

        colors.add("#000000");
        colors.add("#EB3434"); //RED
        colors.add("#EB8934"); //ORANGE
        colors.add("#34EBC9"); //
        colors.add("#7134EB"); //Purple
        colors.add("#FFEE00"); //Yellow


        //Initial Spinner

        TextView TextColorTextView = new TextView(this);
        TextColorTextView.setText("Text Color:");
        TextColorTextView.setLayoutParams(viewLayoutParams);
        linearLayout.addView(TextColorTextView);

        colorPicker = new Spinner(this);
        colorPicker.setLayoutParams(viewLayoutParams);
        linearLayout.addView(colorPicker);
        // Spinner click listener
        colorPicker.setOnItemSelectedListener(this);
        // Creating adapter for spinner
        colorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colors);

        // Drop down layout style - list view with radio button
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        colorPicker.setAdapter(colorAdapter);


//Here we initialize the SeekBar and EditText objects
        TextView TextSizeTextView = new TextView(this);
        TextSizeTextView.setText("Adjust The Bar To Change Text Size:");
        TextSizeTextView.setLayoutParams(viewLayoutParams);
        linearLayout.addView(TextSizeTextView);



        seekBar = new SeekBar(this);
        seekBar.setLayoutParams(viewLayoutParams);
        linearLayout.addView(seekBar);



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
// TODO Auto-generated method stub
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
// TODO Auto-generated method stub
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                textSize = progress;
                onTextSizeChange();
            }
        });

        //Submit Button

        saveUIButton = new Button(this);
        saveUIButton.setText("save UI Changes");
        saveUIButton.setLayoutParams(viewLayoutParams);
        saveUIButton.setOnClickListener(buttonSaveUIOnClick);
        linearLayout.addView(saveUIButton);


// Here we define a text view
        TextView FirstNameTextView = new TextView(this);
        FirstNameTextView.setText("First Name");
        FirstNameTextView.setLayoutParams(viewLayoutParams);
        linearLayout.addView(FirstNameTextView);
// Here we define the edit text
        FirstNameEditText = new EditText(this);
        FirstNameEditText.setLayoutParams(viewLayoutParams);


        linearLayout.addView(FirstNameEditText);
        // Here we define a text view
        TextView LastNameTextView = new TextView(this);
        LastNameTextView.setText("Last Name");
        LastNameTextView.setLayoutParams(viewLayoutParams);
        linearLayout.addView(LastNameTextView);
// Here we define the edit text
        LastNameEditText = new EditText(this);
        LastNameEditText.setLayoutParams(viewLayoutParams);


        linearLayout.addView(LastNameEditText);
        // Here we define a text view
        TextView PhoneNumberTextView = new TextView(this);
        PhoneNumberTextView.setText("Phone Number");
        PhoneNumberTextView.setLayoutParams(viewLayoutParams);
        linearLayout.addView(PhoneNumberTextView);
// Here we define the edit text
        PhoneNumberEditText = new EditText(this);
        PhoneNumberEditText.setInputType(InputType.TYPE_CLASS_PHONE);
        PhoneNumberEditText.setLayoutParams(viewLayoutParams);


        linearLayout.addView(PhoneNumberEditText);
        // Here we define a text view
        TextView EducationTextView = new TextView(this);
        EducationTextView.setText("Education Level");
        EducationTextView.setLayoutParams(viewLayoutParams);
        linearLayout.addView(EducationTextView);
// Here we define the edit text
        EducationEditText = new EditText(this);
        EducationEditText.setLayoutParams(viewLayoutParams);


        linearLayout.addView(EducationEditText);
        // Here we define a text view
        TextView HobbiesTextView = new TextView(this);
        HobbiesTextView.setText("Hobbies");
        HobbiesTextView.setLayoutParams(viewLayoutParams);
        linearLayout.addView(HobbiesTextView);
// Here we define the edit text
        HobbiesEditText = new EditText(this);
        HobbiesEditText.setLayoutParams(viewLayoutParams);


        linearLayout.addView(HobbiesEditText);

//Add new Data Button

        AddButton = new Button(this);
        AddButton.setText("Add new Data");
        AddButton.setLayoutParams(viewLayoutParams);
        AddButton.setOnClickListener(AddButtonOnClick);
        linearLayout.addView(AddButton);

//Save Internal Button

        saveInternalButton = new Button(this);
        saveInternalButton.setText("save Internal");
        saveInternalButton.setLayoutParams(viewLayoutParams);
        saveInternalButton.setOnClickListener(buttonSaveInternalOnClick);
        linearLayout.addView(saveInternalButton);

//Load Internal Button
        loadInternalButton = new Button(this);
        loadInternalButton.setText("load Internal");
        loadInternalButton.setLayoutParams(viewLayoutParams);
        loadInternalButton.setOnClickListener(buttonLoadInternalOnClick);
        linearLayout.addView(loadInternalButton);

//Save External Button

        saveExternalButton = new Button(this);
        saveExternalButton.setText("save External");
        saveExternalButton.setLayoutParams(viewLayoutParams);
        saveExternalButton.setOnClickListener(buttonSaveExternalOnClick);
        linearLayout.addView(saveExternalButton);

//Load External Button
        loadExternalButton = new Button(this);
        loadExternalButton.setText("load External");
        loadExternalButton.setLayoutParams(viewLayoutParams);
        loadExternalButton.setOnClickListener(buttonLoadExternalOnClick);
        linearLayout.addView(loadExternalButton);

// Here we define a text view
        summaryTextView = new TextView(this);
        summaryTextView.setLayoutParams(viewLayoutParams);
        linearLayout.addView(summaryTextView);

        TextView SearchTextView = new TextView(this);
        SearchTextView.setText("Search Data");
        SearchTextView.setLayoutParams(viewLayoutParams);
        linearLayout.addView(SearchTextView);

        //Here we define the AutoCompleteTextView object
        SearchField = new AutoCompleteTextView(this);
        SearchField.setLayoutParams(viewLayoutParams);
        linearLayout.addView(SearchField);
//Here we set the text color
        SearchField.setTextColor(Color.RED);
//Here we define the required number of letters to be typed in the AutoCompleteTextView
        SearchField.setThreshold(1);

        loadUISettings();
        InitializingArrayAdapter();


// Here we define a text view
        ScrollView scrollView = new ScrollView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        scrollView.setLayoutParams(layoutParams);


        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);

        scrollView.addView(linearLayout);
        this.addContentView(scrollView, layoutParams);
    }

    private void loadUISettings() {

        SharedPreferences loadedSharedPrefs = getSharedPreferences(prefsFileName, MODE_PRIVATE);
//Here we set the TextView font size to the previously saved values
        float fontSize = loadedSharedPrefs.getFloat(FONT_SIZE, 14.0f);
        selectedColor = loadedSharedPrefs.getString(TEXT_COLOR, "#000000");
        System.out.println(textSize);
        System.out.println(selectedColor);
        seekBar.setProgress((int) fontSize);
        int spinnerPosition = colorAdapter.getPosition(selectedColor);
        colorPicker.setSelection(spinnerPosition);
        onTextSizeChange();
        onTextColorChange();
    }

    private void onTextSizeChange() {
        FirstNameEditText.setTextSize(textSize);
        LastNameEditText.setTextSize(textSize);
        PhoneNumberEditText.setTextSize(textSize);
        EducationEditText.setTextSize(textSize);
        HobbiesEditText.setTextSize(textSize);
    }

    private void onTextColorChange() {
        FirstNameEditText.setTextColor(Color.parseColor(selectedColor));
        LastNameEditText.setTextColor(Color.parseColor(selectedColor));
        PhoneNumberEditText.setTextColor(Color.parseColor(selectedColor));
        EducationEditText.setTextColor(Color.parseColor(selectedColor));
        HobbiesEditText.setTextColor(Color.parseColor(selectedColor));
    }
    private void InitializingArrayAdapter() {
        for (Contact c : phoneCatalog) {
            data.add(c.firstName + " --- " + c.lastName + " --- " + c.phoneNumber + " --- " + c.educationLevel + " --- " +c.hobbies );
        };

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, data );
        SearchField.setAdapter(arrayAdapter);

    };

    private void updatingArrayAdapter(String returnData) {
        String[] dataSplit = returnData.split("\n");
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dataSplit );
        SearchField.setAdapter(arrayAdapter);
};

    private OnClickListener AddButtonOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean feedback = false;
            if (FirstNameEditText.getText().length() == 0) {
                FirstNameEditText.setBackgroundColor(Color.rgb(254, 150, 150));
                feedback = true;
            }
            if (LastNameEditText.getText().length() == 0) {
                LastNameEditText.setBackgroundColor(Color.rgb(254, 150, 150));
                feedback = true;
            }
            if (PhoneNumberEditText.getText().length() == 0) {
                PhoneNumberEditText.setBackgroundColor(Color.rgb(254, 150, 150));
                feedback = true;
            }
            if (EducationEditText.getText().length() == 0) {
                EducationEditText.setBackgroundColor(Color.rgb(254, 150, 150));
                feedback = true;
            }
            if (HobbiesEditText.getText().length() == 0) {
                HobbiesEditText.setBackgroundColor(Color.rgb(254, 150, 150));
                feedback = true;
            }
            if (feedback)
                Toast.makeText(getBaseContext(), "Missing data!", Toast.LENGTH_SHORT).show();
            else {
                Contact newContact = new Contact(FirstNameEditText.getText().toString(),
                        LastNameEditText.getText().toString(),
                        PhoneNumberEditText.getText().toString(),
                        EducationEditText.getText().toString(),
                        HobbiesEditText.getText().toString());

                phoneCatalog.add(newContact);
                String summaryString ="";

                for (Contact e : phoneCatalog) {
                summaryString += e.toString() + "\n";
            }

                summaryTextView.setText(summaryString);
        };
        };
    };

    private OnClickListener buttonSaveInternalOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
                String summaryString ="";

                for (Contact e : phoneCatalog) {
                    summaryString += e.toString() + "\n";
                }

                try {

                    file = new File(FileName);
//Here we make the file readable by other applications too.
                    file.setReadable(true, false);
                    FileOutputStream fileOutputStream = openFileOutput(FileName, MODE_PRIVATE);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
//Here we write the text to the file
                    outputStreamWriter.write(summaryString);
                    outputStreamWriter.close();
                    Toast.makeText(getApplicationContext(), getString(R.string.file_save_fb), Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.file_not_found_excp), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.io_excp), Toast.LENGTH_LONG).show();
                }
            };
    };

    private OnClickListener buttonLoadInternalOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            FileInputStream fileInputStream;
            InputStreamReader inputStreamReader;
            try {
                fileInputStream = openFileInput(FileName);
                inputStreamReader = new InputStreamReader(fileInputStream);
                char[] inputBuffer = new char[BLOCK_SIZE];
                String fileContent="";
                int charRead;
                while((charRead = inputStreamReader.read(inputBuffer))>0) {
//Here we convert chars to string
                    String readString =String.copyValueOf(inputBuffer, 0, charRead);
                    fileContent+=readString;
//Here we re-initialize the inputBuffer array to remove its content
                    inputBuffer = new char[BLOCK_SIZE];
                }
//Here we set the text of the commentEditText to the one, which has been read
                updatingArrayAdapter(fileContent);
                summaryTextView.setText(fileContent);
                Toast.makeText(getApplicationContext(), getString(R.string.file_load_fb), Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), getString(R.string.file_not_found_excp), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), getString(R.string.io_excp), Toast.LENGTH_LONG).show();
            }
        };
    };

    private OnClickListener buttonSaveExternalOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
                String summaryString ="";

                for (Contact e : phoneCatalog) {
                    summaryString += e.toString() + "\n";
                }

                try {

                    //Here we initialize the File object, which refers to
                    //the comment file on the SD card under the specified directory
                    File file = new File(destPathFile, FileName);
                    //Here we overwrite the previous file content with the new content
                    FileOutputStream fileOutputStream = new FileOutputStream(file, false);
                    //Here we initialize the write stream
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                    //Here we write the text to the file
                    outputStreamWriter.write(summaryString);
                    //Here we close the write stream
                    outputStreamWriter.close();
                    Toast.makeText(getApplicationContext(),
                            "Data was written to :" + file.getAbsolutePath(), Toast.LENGTH_LONG)
                            .show();
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            e.getMessage(), Toast.LENGTH_LONG)
                            .show();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),
                            e.getMessage(), Toast.LENGTH_LONG)
                            .show();
                }
            };
    };

    private OnClickListener buttonLoadExternalOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            FileInputStream fileInputStream;
            InputStreamReader inputStreamReader;
            try {

                file = new File(destPathFile, FileName);
                fileInputStream = new FileInputStream(file);
                inputStreamReader = new InputStreamReader(fileInputStream);
                char[] inputBuffer = new char[BLOCK_SIZE];
                String fileContent = "";
                int charRead;
                while ((charRead = inputStreamReader.read(inputBuffer)) > 0) {
                    // Here we convert chars to string
                    String readString = String.copyValueOf(inputBuffer, 0,
                            charRead);
                    fileContent += readString;
                    // Here we re-initialize the inputBuffer array to remove
                    // its content
                    inputBuffer = new char[BLOCK_SIZE];
                }
                // Here we set the text of the commentEditText to the one,
                // which has been read
                updatingArrayAdapter(fileContent);
                summaryTextView.setText(fileContent);
                Toast.makeText(getApplicationContext(),
                        getString(R.string.file_load_fb), Toast.LENGTH_LONG)
                        .show();
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.file_not_found_excp), Toast.LENGTH_LONG)
                        .show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.io_excp), Toast.LENGTH_LONG)
                        .show();
            }
        };
    };


    private OnClickListener buttonSaveUIOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //Here we initialize the SharedPreferences object
            sharedPreferences =getSharedPreferences(prefsFileName, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

//Here we save the values in the EditText view to preferences
            editor.putFloat(FONT_SIZE, FirstNameEditText.getTextSize());
            editor.putString(TEXT_COLOR, selectedColor);

//Here we save the values
            editor.commit();
            Toast.makeText(getApplicationContext(), getString(R.string.save_fb), Toast.LENGTH_LONG).show();
        };
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        selectedColor = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + selectedColor, Toast.LENGTH_LONG).show();

        onTextColorChange();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}