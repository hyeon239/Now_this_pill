package com.example.now_this_pill.Setting;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.now_this_pill.BuildConfig;
import com.example.now_this_pill.R;

public class VersionFragment extends Fragment {


    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_version, container, false);

        try {
            // 앱 버전 가져오기
            String versionName = BuildConfig.VERSION_NAME;

            // 버전 정보를 화면에 표시
            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView versionTextView = view.findViewById(R.id.version_text_view);
            versionTextView.setText("version : " + versionName);
        } catch (Exception e) {
            Log.e("VersionFragment", "Error getting app version", e);
            Toast.makeText(requireContext(), "앱 버전을 가져오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
}