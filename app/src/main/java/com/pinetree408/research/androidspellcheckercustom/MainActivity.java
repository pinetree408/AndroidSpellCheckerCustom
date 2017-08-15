package com.pinetree408.research.androidspellcheckercustom;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements SpellCheckerSession.SpellCheckerSessionListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private SpellCheckerSession mScs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextServicesManager tsm = (TextServicesManager) getSystemService(
                Context.TEXT_SERVICES_MANAGER_SERVICE);
        mScs = tsm.newSpellCheckerSession(null, null, this, true);

        EditText mTextView = (EditText)findViewById(R.id.edit_text) ;
        mTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mScs.getSentenceSuggestions(new TextInfo[]{new TextInfo("lawi")}, 18);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private boolean isSentenceSpellCheckSupported() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    @Override
    public void onGetSuggestions(final SuggestionsInfo[] arg0) {
        Log.d(TAG, "onGetSuggestions");
    }

    @Override
    public void onGetSentenceSuggestions(final SentenceSuggestionsInfo[] arg0) {
        if (!isSentenceSpellCheckSupported()) {
            Log.e(TAG, "Sentence spell check is not supported on this platform, "
                    + "but accidentially called.");
            return;
        }
        Log.d(TAG, "onGetSentenceSuggestions");

        for (int i = 0; i < arg0.length; i++) {
            for (int j = 0; j < arg0[i].getSuggestionsCount(); j++) {
                SuggestionsInfo si = arg0[i].getSuggestionsInfoAt(j);
                String result = "";
                for (int k = 0; k < si.getSuggestionsCount(); k++) {
                    if (k != 0) {
                        result += ", ";
                    }
                    result += si.getSuggestionAt(k);
                }
                Log.d(TAG, result);
            }
        }
    }
}
