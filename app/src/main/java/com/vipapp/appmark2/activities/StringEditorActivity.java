package com.vipapp.appmark2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.vipapp.appmark2.R;
import com.vipapp.appmark2.items.FileLocale;
import com.vipapp.appmark2.items.Item;
import com.vipapp.appmark2.manager.res.StringsManager;
import com.vipapp.appmark2.picker.StringChooser;
import com.vipapp.appmark2.picker.string.LocalePicker;
import com.vipapp.appmark2.project.Project;
import com.vipapp.appmark2.utils.ProjectUtils;
import com.vipapp.appmark2.utils.wrapper.Str;
import com.vipapp.appmark2.widget.RecyclerView;
import com.vipapp.appmark2.widget.TextView;
import com.vipapp.appmark2.xml.XMLObject;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import static com.vipapp.appmark2.utils.Const.ADD_LOCALE;

public class StringEditorActivity extends BaseActivity {
    ImageView change_locale;
    TextView error;
    File file;
    ProgressBar loading;
    ArrayList<FileLocale> locales;
    Project project;
    StringsManager strings;
    ArrayList<XMLObject> strings_list;
    RecyclerView strings_recycler;
    TextView title;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_string_editor);
        findViews();
        initVars();
        setCallbacks();
        setupViews();
        setupAnimation();
    }

    protected void onPause() {
        super.onPause();
        this.strings.save();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void findViews() {
        this.error = findViewById(R.id.error);
        this.loading = findViewById(R.id.loading);
        this.title = findViewById(R.id.title);
        this.change_locale = findViewById(R.id.change_locale);
        this.strings_recycler = findViewById(R.id.strings_recycler);
    }

    public void initVars() {
        this.file = (File) getIntent().getSerializableExtra("file");
        this.project = (Project) getIntent().getSerializableExtra("project");
        this.strings = new StringsManager(this.file);
    }

    public void setCallbacks() {
        this.strings.exec(strings_list -> {
            this.strings_list = strings_list;
            onStringsLoaded();
        });
        this.project.exec(none -> {
            this.locales = strings.getLocales(project);
            this.change_locale.setOnClickListener(view -> showLocaleChooser());
        });
    }

    private void showLocaleChooser() {
        StringChooser chooser = new StringChooser(result -> {
            if(result.getType() == ADD_LOCALE){
                addLocale();
            } else {
                String locale = ((result.getInstance()).equals("default") ? "" : result.getInstance());
                gotoLocaledFile(locale);
            }
        });
        chooser.setArray(toList(this.locales));
        chooser.setTitle(R.string.select_locale);
        chooser.show();
    }

    private void addLocale(){
        // Pick name
        LocalePicker picker = new LocalePicker(this::gotoLocaledFile);
        picker.show();
    }
    private void gotoLocaledFile(String locale){
        Intent i = new Intent(this, StringEditorActivity.class);
        i.putExtra("file", strings.getLocaled(project, locale).getFile());
        i.putExtra("project", this.project);
        i.putExtra("recreate", true);
        startActivity(i);
        finish();
    }
    private ArrayList<Item<String>> toList(ArrayList<FileLocale> locales) {
        ArrayList<Item<String>> returnable = new ArrayList<>();
        for (int i = 0; i < locales.size(); i++) {
            returnable.add(new Item<>(i, locales.get(i).getLocaleName()));
        }
        Collections.sort(returnable, (stringItem, stringItem2) -> 0);
        returnable.add(new Item<>(ADD_LOCALE, Str.get(R.string.add)));
        return returnable;
    }

    public void checkErrors() {
        File file = this.file;
        if (file == null) {
            setError(getString(R.string.file_not_found));
        } else if (file.exists()) {
            this.loading.setVisibility(View.VISIBLE);
        } else {
            setError(String.format(getString(R.string.xml_error), this.file.getAbsolutePath()));
        }
    }

    public void setupViews() {
        checkErrors();
        this.title.setText(ProjectUtils.getFileLocale(this.file));
        this.strings_recycler.pushValue(5, this.strings);
    }

    public void setupAnimation() {
        if (getIntent().getBooleanExtra("recreate", false)) {
            overridePendingTransition(0, 0);
        }
    }

    public void onStringsLoaded() {
        this.loading.setVisibility(View.GONE);
        this.strings_recycler.setVisibility(View.VISIBLE);
        if (this.strings_list == null) {
            setError(String.format(getString(R.string.xml_error), this.file.getAbsolutePath()));
        }
    }

    public void setError(String error) {
        this.error.setText(error);
    }
}