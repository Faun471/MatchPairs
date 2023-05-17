package me.faun.matchpairs.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import me.faun.matchpairs.R;
import me.faun.matchpairs.utils.MediaPlayerUtils;
import me.faun.matchpairs.utils.SettingsUtils;
import org.jetbrains.annotations.NotNull;

public class Settings extends DialogFragment {

    private SeekBar musicVolumeSeekBar;
    private SeekBar soundEffectsVolumeSeekBar;
    private TextView musicVolumeTextView, soundEffectsVolumeTextView;

    public Settings() {
        // Required empty public constructor
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create the dialog with a custom style
        Dialog dialog = new Dialog(requireContext(), R.style.DialogFragmentTheme);

        // Set a transparent background
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Disable outside touches (optional)
        dialog.setCanceledOnTouchOutside(true);

        // Disable the back button (optional)
        dialog.setCancelable(false);

        // Inflate the dialog's content view
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_settings, null);

        dialog.setContentView(view);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        musicVolumeTextView = view.findViewById(R.id.music_volume_text);
        musicVolumeSeekBar = view.findViewById(R.id.music_volume);

        soundEffectsVolumeTextView = view.findViewById(R.id.sfx_volume_text);
        soundEffectsVolumeSeekBar = view.findViewById(R.id.sfx_volume);

        Button saveButton = view.findViewById(R.id.save);

        initSettings();

        musicVolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                musicVolumeTextView.setText(String.format("Music Volume: %s%%", progressChangedValue * 10));
                SettingsUtils.getInstance(getActivity()).setMusicVolume(getContext(), musicVolumeSeekBar.getProgress() / 10f);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        view.findViewById(R.id.add_volume_music).setOnClickListener(v -> {
            if (musicVolumeSeekBar.getProgress() < 10) {
                musicVolumeSeekBar.setProgress(musicVolumeSeekBar.getProgress() + 1);
                MediaPlayerUtils.playSoundEffect(getActivity(), R.raw.menu_click, SettingsUtils.getInstance(getActivity()).getSoundEffectsVolume());
                return;
            }

            MediaPlayerUtils.playSoundEffect(getActivity(), R.raw.card_click_wrong, SettingsUtils.getInstance(getActivity()).getSoundEffectsVolume());
        });

        view.findViewById(R.id.subtract_volume_music).setOnClickListener(v -> {
            if (musicVolumeSeekBar.getProgress() > 0) {
                musicVolumeSeekBar.setProgress(musicVolumeSeekBar.getProgress() - 1);
                MediaPlayerUtils.playSoundEffect(getActivity(), R.raw.menu_click, SettingsUtils.getInstance(getActivity()).getSoundEffectsVolume());
                return;
            }

            MediaPlayerUtils.playSoundEffect(getActivity(), R.raw.card_click_wrong, SettingsUtils.getInstance(getActivity()).getSoundEffectsVolume());
        });

        view.findViewById(R.id.add_volume_sfx).setOnClickListener(v -> {
            if (soundEffectsVolumeSeekBar.getProgress() < 10) {
                soundEffectsVolumeSeekBar.setProgress(soundEffectsVolumeSeekBar.getProgress() + 1);
                MediaPlayerUtils.playSoundEffect(getActivity(), R.raw.menu_click, SettingsUtils.getInstance(getActivity()).getSoundEffectsVolume());
                return;
            }

            MediaPlayerUtils.playSoundEffect(getActivity(), R.raw.card_click_wrong, SettingsUtils.getInstance(getActivity()).getSoundEffectsVolume());
        });

        view.findViewById(R.id.subtract_volume_sfx).setOnClickListener(v -> {
            if (soundEffectsVolumeSeekBar.getProgress() > 0) {
                soundEffectsVolumeSeekBar.setProgress(soundEffectsVolumeSeekBar.getProgress() - 1);
                MediaPlayerUtils.playSoundEffect(getActivity(), R.raw.menu_click, SettingsUtils.getInstance(getActivity()).getSoundEffectsVolume());
                return;
            }

            MediaPlayerUtils.playSoundEffect(getActivity(), R.raw.card_click_wrong, SettingsUtils.getInstance(getActivity()).getSoundEffectsVolume());
        });

        saveButton.setOnClickListener(v -> {
            MediaPlayerUtils.playSoundEffect(getActivity(), R.raw.menu_click, SettingsUtils.getInstance(getActivity()).getSoundEffectsVolume());
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });

        soundEffectsVolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                soundEffectsVolumeTextView.setText(String.format("Sound Effects Volume: %s%%", progressChangedValue * 10));
                SettingsUtils.getInstance(getActivity()).setSoundEffectsVolume(getContext(), soundEffectsVolumeSeekBar.getProgress() / 10f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return view;
    }

    private void initSettings() {
        musicVolumeTextView.setText("Music Volume: " + ((int) (SettingsUtils.getInstance(getActivity()).getMusicVolume() * 100)) + "%");
        musicVolumeSeekBar.setProgress((int) (SettingsUtils.getInstance(getContext()).getMusicVolume() * 10f));

        soundEffectsVolumeTextView.setText("Sound Effects Volume: " + ((int) (SettingsUtils.getInstance(getActivity()).getSoundEffectsVolume() * 100)) + "%");
        soundEffectsVolumeSeekBar.setProgress((int) (SettingsUtils.getInstance(getContext()).getSoundEffectsVolume() * 10f));
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        getView().post(() -> getView().requestLayout());

//        if (dialog != null) {
//            int width = ViewGroup.LayoutParams.MATCH_PARENT;
//            int height = ViewGroup.LayoutParams.MATCH_PARENT;
//
//            dialog.getWindow().setLayout(width, height);
//        }
    }
}