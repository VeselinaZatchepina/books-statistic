package com.github.veselinazatchepina.bookstatistics.preference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.github.veselinazatchepina.bookstatistics.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThemePreference extends ListPreference {
    private Context context;
    private ImageView themeImageView;
    private CharSequence[] themeImageFileCharArray;
    private CharSequence[] themeImageNameCharArray;
    private List<ThemeItem> ThemeIcons;
    private SharedPreferences preferences;
    private Resources resources;
    private String selectedImageFileName, defaultImageFileName;
    private TextView summaryTextView;

    public ThemePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        resources = context.getResources();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        getCustomAttributes(attrs);
    }

    private void getCustomAttributes(AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.attrs_icon, 0, 0);
        try {
            defaultImageFileName = a.getString(R.styleable.attrs_icon_iconFile);
        } finally {
            //The recycle() causes the allocated memory to be returned to the available pool immediately
            // and will not stay until garbage collection
            a.recycle();
        }
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        selectedImageFileName = preferences.getString(
                resources.getString(R.string.custom_theme_key), defaultImageFileName);
        themeImageView = ButterKnife.findById(view, R.id.iconSelected);
        updateIcon();
        summaryTextView = ButterKnife.findById(view, R.id.summary);
        summaryTextView.setText(getEntry(selectedImageFileName));
    }

    private void updateIcon() {
        int identifier = resources.getIdentifier(selectedImageFileName, "drawable",
                context.getPackageName());
        themeImageView.setImageResource(identifier);
        themeImageView.setTag(selectedImageFileName);
    }

    private String getEntry(String value) {
        String[] entries = resources.getStringArray(R.array.iconName);
        String[] values = resources.getStringArray(R.array.iconFile);
        int index = Arrays.asList(values).indexOf(value);
        return entries[index];
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        defineDialogButton(builder);
        getEntriesAndEntryValuesOfPreference();
        String selectedIcon = preferences.getString(
                resources.getString(R.string.custom_theme_key),
                resources.getString(R.string.theme_default));
        createListOfAllThemes(selectedIcon);
        defineAdapter(builder);
    }

    private void defineDialogButton(AlertDialog.Builder builder) {
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton(null, null);
    }

    private void getEntriesAndEntryValuesOfPreference() {
        themeImageNameCharArray = getEntries();
        themeImageFileCharArray = getEntryValues();
        if (themeImageNameCharArray == null || themeImageFileCharArray == null
                || themeImageNameCharArray.length != themeImageFileCharArray.length) {
            throw new IllegalStateException(
                    "ListPreference requires an entries array "
                            + "and an entryValues array which are both the same length");
        }
    }

    private void createListOfAllThemes(String selectedIcon) {
        ThemeIcons = new ArrayList<ThemeItem>();
        for (int i = 0; i < themeImageNameCharArray.length; i++) {
            boolean isSelected = selectedIcon.equals(themeImageFileCharArray[i]);
            ThemeItem item = new ThemeItem(themeImageNameCharArray[i], themeImageFileCharArray[i], isSelected);
            ThemeIcons.add(item);
        }
    }

    private void defineAdapter(AlertDialog.Builder builder) {
        CustomListPreferenceAdapter customListPreferenceAdapter = new CustomListPreferenceAdapter(
                context, R.layout.theme_item, ThemeIcons);
        builder.setAdapter(customListPreferenceAdapter, null);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (ThemeIcons != null) {
            for (int i = 0; i < themeImageNameCharArray.length; i++) {
                ThemeItem item = ThemeIcons.get(i);
                if (item.isChecked) {
                    saveChangedDataInPreference(item);
                    updateViews(item);
                    break;
                }
            }
        }
    }

    private void saveChangedDataInPreference(ThemeItem item) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(
                resources.getString(R.string.custom_theme_key),
                item.file);
        editor.apply();
    }

    private void updateViews(ThemeItem item) {
        selectedImageFileName = item.file;
        updateIcon();
        summaryTextView.setText(item.name);
    }


    /**
     * Adapter for inflate themeImageView item
     */
    private class CustomListPreferenceAdapter extends ArrayAdapter<ThemeItem> {
        private Context context;
        private List<ThemeItem> themeItems;
        private int resource;

        public CustomListPreferenceAdapter(Context context, int resource,
                                           List<ThemeItem> objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
            this.themeItems = objects;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflateConvertView(convertView, parent, resource);
                holder = new ViewHolder(convertView);
                holder.position = position;
                //Tags are essentially an extra piece of information that can be associated with a view
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            defineDialogViews(holder, themeItems, position);
            setListenerToConvertView(convertView, themeItems, position);
            return convertView;
        }
    }

    private View inflateConvertView(View convertView, ViewGroup parent, int resource) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(resource, parent, false);
        return convertView;
    }

    private void defineDialogViews(ViewHolder holder, List<ThemeItem> themeItems, int position) {
        holder.themeName.setText(themeItems.get(position).name);
        int identifier = context.getResources().getIdentifier(
                themeItems.get(position).file, "drawable",
                context.getPackageName());
        holder.themeImage.setImageResource(identifier);
        holder.radioButton.setChecked(themeItems.get(position).isChecked);
    }

    private void setListenerToConvertView(View convertView, final List<ThemeItem> themeItems, final int position) {
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < themeItems.size(); i++) {
                    themeItems.get(i).isChecked = (i == position);
                }
                getDialog().dismiss();
            }
        });
    }

    private static class ThemeItem {
        private String file;
        private boolean isChecked;
        private String name;

        public ThemeItem(CharSequence name, CharSequence file, boolean isChecked) {
            this(name.toString(), file.toString(), isChecked);
        }

        public ThemeItem(String name, String file, boolean isChecked) {
            this.name = name;
            this.file = file;
            this.isChecked = isChecked;
        }
    }

    public static class ViewHolder {
        int position;
        @BindView(R.id.iconImage)
        ImageView themeImage;
        @BindView(R.id.iconName)
        TextView themeName;
        @BindView(R.id.iconRadio)
        RadioButton radioButton;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

