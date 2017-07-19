package com.github.veselinazatchepina.testcustompreference.preference;

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

import com.github.veselinazatchepina.testcustompreference.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IconPickerPreference extends ListPreference {

    private class CustomListPreferenceAdapter extends ArrayAdapter<IconItem> {

        private Context context;
        private List<IconItem> icons;
        private int resource;

        public CustomListPreferenceAdapter(Context context, int resource,
                                           List<IconItem> objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
            this.icons = objects;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, parent, false);

                holder = new ViewHolder();
                holder.iconName = (TextView) convertView
                        .findViewById(R.id.iconName);
                holder.iconImage = (ImageView) convertView
                        .findViewById(R.id.iconImage);
                holder.radioButton = (RadioButton) convertView
                        .findViewById(R.id.iconRadio);
                holder.position = position;

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.iconName.setText(icons.get(position).name);

            int identifier = context.getResources().getIdentifier(
                    icons.get(position).file, "drawable",
                    context.getPackageName());
            holder.iconImage.setImageResource(identifier);

            holder.radioButton.setChecked(icons.get(position).isChecked);

            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ViewHolder holder = (ViewHolder) v.getTag();
                    for (int i = 0; i < icons.size(); i++) {
                        if (i == position)
                            icons.get(i).isChecked = true;
                        else
                            icons.get(i).isChecked = false;
                    }
                    getDialog().dismiss();
                }
            });

            return convertView;
        }

    }

    private static class IconItem {

        private String file;
        private boolean isChecked;
        private String name;

        public IconItem(CharSequence name, CharSequence file, boolean isChecked) {
            this(name.toString(), file.toString(), isChecked);
        }

        public IconItem(String name, String file, boolean isChecked) {
            this.name = name;
            this.file = file;
            this.isChecked = isChecked;
        }

    }

    private static class ViewHolder {
        protected ImageView iconImage;
        protected TextView iconName;
        protected int position;
        protected RadioButton radioButton;
    }

    private Context context;
    private ImageView icon;

    private CharSequence[] iconFile;
    private CharSequence[] iconName;
    private List<IconItem> icons;
    private SharedPreferences preferences;
    private Resources resources;
    private String selectedIconFile, defaultIconFile;
    private TextView summary;

    public IconPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        resources = context.getResources();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.attrs_icon, 0, 0);

        try {
            defaultIconFile = a.getString(R.styleable.attrs_icon_iconFile);
        } finally {
            a.recycle();
        }
    }

    private String getEntry(String value) {
        String[] entries = resources.getStringArray(R.array.iconName);
        String[] values = resources.getStringArray(R.array.iconFile);
        int index = Arrays.asList(values).indexOf(value);
        return entries[index];
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        selectedIconFile = preferences.getString(
                resources.getString(R.string.custom_theme_key), defaultIconFile);

        icon = (ImageView) view.findViewById(R.id.iconSelected);
        updateIcon();

        summary = (TextView) view.findViewById(R.id.summary);
        summary.setText(getEntry(selectedIconFile));

    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (icons != null) {
            for (int i = 0; i < iconName.length; i++) {
                IconItem item = icons.get(i);
                if (item.isChecked) {

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(
                            resources.getString(R.string.custom_theme_key),
                            item.file);
                    editor.apply();

                    selectedIconFile = item.file;
                    updateIcon();

                    summary.setText(item.name);

                    break;
                }
            }
        }

    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {

        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton(null, null);

        iconName = getEntries();
        iconFile = getEntryValues();

        if (iconName == null || iconFile == null
                || iconName.length != iconFile.length) {
            throw new IllegalStateException(
                    "ListPreference requires an entries array "
                            + "and an entryValues array which are both the same length");
        }

        String selectedIcon = preferences.getString(
                resources.getString(R.string.custom_theme_key),
                resources.getString(R.string.theme_default));

        icons = new ArrayList<IconItem>();

        for (int i = 0; i < iconName.length; i++) {
            boolean isSelected = selectedIcon.equals(iconFile[i]) ? true : false;
            IconItem item = new IconItem(iconName[i], iconFile[i], isSelected);
            icons.add(item);
        }

        CustomListPreferenceAdapter customListPreferenceAdapter = new CustomListPreferenceAdapter(
                context, R.layout.theme_item, icons);
        builder.setAdapter(customListPreferenceAdapter, null);

    }

    private void updateIcon() {
        int identifier = resources.getIdentifier(selectedIconFile, "drawable",
                context.getPackageName());

        icon.setImageResource(identifier);
        icon.setTag(selectedIconFile);
    }

}

